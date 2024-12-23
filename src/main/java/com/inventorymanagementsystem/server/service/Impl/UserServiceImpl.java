package com.inventorymanagementsystem.server.service.Impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.inventorymanagementsystem.server.dto.request.AuthenticationRequest;
import com.inventorymanagementsystem.server.dto.response.UserDTO;
import com.inventorymanagementsystem.server.entities.TempUser;
import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.helper.EmailTemplate;
import com.inventorymanagementsystem.server.helper.GenerateOtp;
import com.inventorymanagementsystem.server.helper.PasswordBcrypt;
import com.inventorymanagementsystem.server.helper.ResourceNotFoundException;
import com.inventorymanagementsystem.server.helper.UserIdGenerator;
import com.inventorymanagementsystem.server.repositories.UserRepo;
import com.inventorymanagementsystem.server.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final ConcurrentHashMap<String, TempUser> tempUserStore = new ConcurrentHashMap<>();

    @Override
    public User saveUser(User user) {
        String userId;
        do {
            userId = UserIdGenerator.generateProductId();
        } while (userRepo.existsById(userId));
        user.setId(userId);

        String hashPassword = PasswordBcrypt.hashPassword(user.getPassword());
        user.setPassword(hashPassword);
        user.setRepeat_password(hashPassword);
        user.setRole("SALESPERSON");

        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User existingUser = userRepo.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashPassword = PasswordBcrypt.hashPassword(user.getPassword());
            existingUser.setPassword(hashPassword);
            existingUser.setRepeat_password(hashPassword);
        }

        existingUser.setFirst_name(user.getFirst_name());
        existingUser.setLast_name(user.getLast_name());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());

        if (!existingUser.isEmailVerified()) {
            existingUser.setEmailVerified(user.isEmailVerified());
        }

        // Save the updated user
        User saveUser = userRepo.save(existingUser);

        return Optional.ofNullable(saveUser);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not Found"));
        userRepo.delete(user);
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    public boolean isUserExistByEmailAndPassword(String email, String password) {
        User user = userRepo.findByEmailAndPassword(email, password).orElse(null);
        return user != null;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User register(User user) {
        User existingUserByEmail = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (existingUserByEmail != null && existingUserByEmail.isEmailVerified()) {
            throw new RuntimeException("User already exist and verified");
        }

        User existingByUsername = userRepo.findByUsername(user.getUsername()).orElse(null);
        if (existingByUsername != null) {
            throw new RuntimeException("Username already exist");
        }

        TempUser tempUser = new TempUser(
                UUID.randomUUID().toString(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getRepeat_password(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getPhone(),
                user.getAddress(),
                GenerateOtp.generateOtp(),
                LocalDateTime.now()
        );

        // Store the temporary user object in the map
        tempUserStore.put(tempUser.getEmail(), tempUser);

        sendVerificationEmail(tempUser.getFirst_name(), tempUser.getEmail(), tempUser.getOtp());

        logger.info("User registered successfully, please verify your email");

        return User.builder()
                .email(tempUser.getEmail())
                .username(tempUser.getUsername())
                .first_name(tempUser.getFirst_name())
                .last_name(tempUser.getLast_name())
                .phone(tempUser.getPhone())
                .address(tempUser.getAddress())
                .build();
    }

    @Override
    public void verify(String email, String otp) {
        TempUser tempUser = tempUserStore.get(email);

        if (tempUser == null) {
            logger.warn("User Not Found");
            throw new RuntimeException("User Not Found");
        } else if (!otp.equals(tempUser.getOtp())) {
            logger.warn("Invalid Otp");
            throw new RuntimeException("Invalid Otp");
        } else if (tempUser.getOtpGeneratedTime().plusMinutes(2).isBefore(LocalDateTime.now())) {
            logger.warn("OTP expired");
            throw new RuntimeException("OTP expired");
        } else {
            String hashPassword = PasswordBcrypt.hashPassword(tempUser.getPassword());
            String userId;
            do {
                userId = UserIdGenerator.generateProductId();
            } while (userRepo.existsById(userId));

            User user = User.builder()
                    .id(userId)
                    .email(tempUser.getEmail())
                    .username(tempUser.getUsername().toLowerCase())
                    .password(hashPassword)
                    .repeat_password(hashPassword)
                    .first_name(tempUser.getFirst_name())
                    .last_name(tempUser.getLast_name())
                    .phone(tempUser.getPhone())
                    .address(tempUser.getAddress())
                    .otp(tempUser.getOtp())
                    .isEmailVerified(true)
                    .role("SALESPERSON")
                    .localDateTime(LocalDateTime.now())
                    .build();

            logger.info("User verified successfully");

            userRepo.save(user);

            tempUserStore.remove(email);  // remove temporary user
        }
    }

    @Override
    public User login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            if (PasswordBcrypt.checkPassword(password, existingUser.getPassword())) {
                logger.info("User logged in successfully");
                return existingUser;
            } else {
                logger.warn("Invalid Password");
                throw new RuntimeException("Invalid Password");
            }
        } else {
            logger.warn("User Does not exist");
            throw new RuntimeException("User Does not exist");
        }
    }

    private void sendVerificationEmail(String firstname, String email, String otp) {
        String subject = "Email Verification: Invenquity!!!";
        String body = EmailTemplate.getEmailTemplateForVerifyUser(firstname, otp);
        emailService.sendEmail(email, subject, body);
    }

    @Override
    public User loginSecurity(AuthenticationRequest authenticationRequest) {
        // Check if the provided username is an email or regular username
        Optional<User> optionalUser = userRepo.findByUsername(authenticationRequest.getUsername())
                .or(() -> userRepo.findByEmail(authenticationRequest.getUsername()));

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            if (PasswordBcrypt.checkPassword(authenticationRequest.getPassword(), existingUser.getPassword())) {
                logger.info("User logged in successfully");
                return existingUser;
            } else {
                logger.warn("Invalid Password");
                throw new RuntimeException("Invalid Password");
            }
        } else {
            logger.warn("User does not exist");
            throw new RuntimeException("User does not exist");
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public Optional<User> updateByUsernameOrEmail(String identifier, User user) {
        Optional<User> userOptional = userRepo.findByUsernameOrEmail(identifier, identifier);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setEmail(user.getEmail());
            existingUser.setUsername(user.getUsername());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String hashPassword = PasswordBcrypt.hashPassword(user.getPassword());
                existingUser.setPassword(hashPassword);
                existingUser.setRepeat_password(hashPassword);
            }

            existingUser.setFirst_name(user.getFirst_name());
            existingUser.setLast_name(user.getLast_name());
            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());

            if (!existingUser.isEmailVerified()) {
                existingUser.setEmailVerified(user.isEmailVerified());
            }

            userRepo.save(existingUser);
            return Optional.of(existingUser);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        // return userRepo.findAll();
        List<User> users = userRepo.findAll();
        return users.stream()
                    .map(UserDTO::new)
                    .collect(Collectors.toList());
    }

    @Override
    public void deleteUserByUsernameorEmail(String identifier) {
        User user = userRepo.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found"));
        userRepo.delete(user);
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
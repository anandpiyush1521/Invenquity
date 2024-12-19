package com.inventorymanagementsystem.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.inventorymanagementsystem.server.dto.request.AuthenticationRequest;
import com.inventorymanagementsystem.server.dto.response.AuthenticationRespJwt;
import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.service.UserService;
import com.inventorymanagementsystem.server.service.Impl.CustomUserDetailsService;
import com.inventorymanagementsystem.server.util.JwtUtil;

@RequestMapping("/api/invenquity")
@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        if(userService.isUserExistByEmail(user.getEmail())){
            return ResponseEntity.badRequest().body("User with email already exists");
        }
        userService.register(user);
        return ResponseEntity.ok("User registered successfully, please verify your email");
    }


    //verify
    @PostMapping("/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> verifyUser(@RequestParam String email, @RequestParam String otp){
        try {
            userService.verify(email, otp);
            return ResponseEntity.ok("User verified successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //login
    // @PostMapping("/login")
    // public ResponseEntity<?> loginUser(@RequestBody User user){
    //     try{
    //         User loggedInUser = userService.login(user.getEmail(), user.getPassword());
    //         return ResponseEntity.ok(loggedInUser);
    //     }catch(Exception e){
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @PostMapping("/login")
    // public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
    //     authenticationManager.authenticate(
    //             new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
    //     );

    //     final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    //     final String jwt = jwtUtil.generateToken(userDetails.getUsername());

    //     return ResponseEntity.ok(new AuthenticationResponse(jwt));
    // }

    // @PostMapping("/login")
    // public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){
    //     try {
    //         User loggedInUser = userService.loginSecurity(authenticationRequest);
    //         final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loggedInUser.getUsername());
    //         final String jwt = jwtUtil.generateToken(userDetails.getUsername());

    //         AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt, loggedInUser.getUsername(), loggedInUser.getEmail(), loggedInUser.getFirst_name(), loggedInUser.getAddress());

    //         return ResponseEntity.ok(authenticationResponse);
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){
        try {
            User loggedInUser = userService.loginSecurity(authenticationRequest);
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loggedInUser.getUsername());
        
            final String jwt = jwtUtil.generateToken(userDetails.getUsername(), loggedInUser.getEmail(), loggedInUser.getRole(), loggedInUser.getFirst_name(), loggedInUser.getAddress());    

            AuthenticationRespJwt authenticationRespJwt = new AuthenticationRespJwt(jwt);

            return ResponseEntity.ok(authenticationRespJwt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String token) {
        try {
            // Extract the JWT token by removing the "Bearer " prefix
            String jwtToken = token.replace("Bearer ", "");

            // Invalidate the token (you need to implement the logic for token invalidation)
            jwtUtil.invalidateToken(jwtToken);

            return ResponseEntity.ok("User logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Logout failed: " + e.getMessage());
        }
    }
}

package com.inventorymanagementsystem.server.service.Impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.repositories.UserRepo;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if the username is in email format or a regular username
        User user = userRepo.findByUsername(username)
                .orElseGet(() -> userRepo.findByEmail(username).orElseThrow(() -> 
                    new UsernameNotFoundException("User not found with username or email: " + username)
                ));

        // convert role to granted authority
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
        
        // Returning the UserDetails using the Spring Security User object
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.singletonList(authority));
    }
}

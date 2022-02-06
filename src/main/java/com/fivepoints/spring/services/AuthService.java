package com.fivepoints.spring.services;

import com.fivepoints.spring.entities.ERole;
import com.fivepoints.spring.entities.Role;
import com.fivepoints.spring.entities.User;
import com.fivepoints.spring.exceptions.EmailAlreadyUsedException;
import com.fivepoints.spring.exceptions.ResourceNotFoundException;
import com.fivepoints.spring.payload.requests.LoginRequest;
import com.fivepoints.spring.payload.requests.RegisterRequest;
import com.fivepoints.spring.repositories.RoleRepository;
import com.fivepoints.spring.repositories.UserRepository;
import com.fivepoints.spring.security.jwt.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    // pour crypter le password (NB: il faut ajouter le bean BCryptPasswordEncoder dans l'application)
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    public String login(LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return this.jwtTokenUtils.generateToken(userDetails);
    }

    public String register(RegisterRequest registerRequest) throws EmailAlreadyUsedException {
        // test if email already used
        if (this.userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyUsedException("Error: Email is already in use!");
        }
        // Create new user account
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setSubscribed("false");
        user.setPermission("false");
        user.setDownloadsNumber(0);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role guestRole = this.roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
        roles.add(guestRole);

        // Affect User Roles
        user.setRoles(roles);
        this.userRepository.save(user);
        return "User registered successfully!";
    }
}

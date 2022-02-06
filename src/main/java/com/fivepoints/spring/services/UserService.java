package com.fivepoints.spring.services;

import com.fivepoints.spring.entities.Book;
import com.fivepoints.spring.entities.ERole;
import com.fivepoints.spring.entities.Role;
import com.fivepoints.spring.entities.User;
import com.fivepoints.spring.exceptions.ResourceNotFoundException;
import com.fivepoints.spring.repositories.BookRepository;
import com.fivepoints.spring.repositories.RoleRepository;
import com.fivepoints.spring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<User> getAllUsers()
    {

        return this.userRepository.findAll();
    }

    public User findUserByID(long id)
    {
        Optional<User> userData = this.userRepository.findById(id);
        // Return statement if user exist or throw exception
        return userData.orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    public String updateUserByID(long id, User user)
    {
        Optional<User> userData = this.userRepository.findById(id);
        if (userData.isPresent()) {
            User existingUser = userData.orElseThrow(() -> new ResourceNotFoundException("User not found"));
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            // Change password if exist
            if(!user.getPassword().isEmpty())
            {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            // save existingUser in the database
            this.userRepository.save(existingUser);
            // return statement
            return "User updated successfully!";
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public String deleteUserById(long id)
    {
        Optional<User> userData = this.userRepository.findById(id);
        if (userData.isPresent()) {
            this.userRepository.deleteById(id);
            return "User deleted successfully!";
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    public String bookDownload(User user,long bookId) {
        Optional<User> userData = this.userRepository.findById(user.getId());
        Book book = this.bookRepository.findByIdTwo(bookId);
        if (userData.isPresent()) {
            User user1 = userData.orElse(null);
            this.userRepository.save(user1);
        }
        return "Book downloaded successfully!";
    }

    public String demandSubscription(User user) {
        Optional<User> userData = this.userRepository.findById(user.getId());
        if (userData.isPresent()) {
            User user1 = userData.orElse(null);
            user.setPermission("true");
            this.userRepository.save(user1);
        }
        return "Demand saved successfully!";
    }

    public String approveSubscribe(long idUser) {
        Optional<User> userData = this.userRepository.findById(idUser);
        if (userData.isPresent()) {
            System.out.println(idUser);
            User user = userData.orElse(null);
            user.setSubscribed("true");
            this.userRepository.save(user);
        }
        return "Subscription saved successfully!";
    }

    // Affecter Role to user
    public String affectUserToRole(long idUser, long idRole) {
        Optional<User> userData = this.userRepository.findById(idUser);
        if (userData.isPresent()) {
            User existingUser = userData.orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Optional<Role> roleData = this.roleRepository.findById(idRole);
            if (roleData.isPresent()) {
                Role existingRole = roleData.orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                Set<Role> roles = existingUser.getRoles();
                roles.add(existingRole);
                existingUser.setRoles(roles);
                this.userRepository.save(existingUser);
                return "User affected to role successfully!";
            }else{
                return "User affected to role successfully!";
            }
        }
        return "User affected to role successfully!";
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        User user = null;
        try {
            user = userRepository.findByEmail(email);
        } catch (Exception e) {
            throw e;
        }
        return user;
    }

}

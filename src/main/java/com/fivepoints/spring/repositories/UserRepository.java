package com.fivepoints.spring.repositories;

import com.fivepoints.spring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // return True if email already exist
    boolean existsByEmail(String email);
    // find user by email address
    User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:name% ")
    List<User> filterByName(@Param("name") String name );
}

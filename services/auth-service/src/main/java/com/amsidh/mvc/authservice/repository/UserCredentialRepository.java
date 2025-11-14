package com.amsidh.mvc.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amsidh.mvc.authservice.entity.UserCredential;

/**
 * Repository interface for UserCredential entity.
 * 
 * Provides database access methods for user authentication and management.
 * Spring Data JPA automatically implements these methods.
 * 
 * @author Amsidh Mohammed
 */
@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {

    /**
     * Find a user by email address.
     * Email is the primary authentication identifier.
     * 
     * @param email the user's email address
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<UserCredential> findByEmail(String email);

    /**
     * Find a user by username.
     * Can be used for alternative login mechanism.
     * 
     * @param name the username
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<UserCredential> findByName(String name);

    /**
     * Check if a user with the given email already exists.
     * Used during registration to prevent duplicate accounts.
     * 
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

}

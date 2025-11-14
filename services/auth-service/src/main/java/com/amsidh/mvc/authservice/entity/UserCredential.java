package com.amsidh.mvc.authservice.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing user credentials for authentication.
 * 
 * This entity stores user information including encrypted passwords.
 * Passwords are encrypted using BCryptPasswordEncoder before storage.
 * 
 * Security Notes:
 * - Password field stores BCrypt hashed passwords (never plain text)
 * - Email and name fields must be unique to prevent duplicate accounts
 * - Timestamps track account creation and last update for audit purposes
 * 
 * @author Amsidh Mohammed
 */
@Entity
@Table(name = "user_credentials", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_name", columnList = "name")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {

    /**
     * Primary key - auto-generated user ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Unique username for the user
     * Used for login and identification
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /**
     * Unique email address for the user
     * Primary authentication identifier
     */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * BCrypt encrypted password
     * NEVER store plain text passwords!
     * Format: $2a$10$... (BCrypt hash)
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Timestamp when the user account was created
     * Automatically set on insert
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * Timestamp when the user account was last updated
     * Automatically updated on entity modification
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedDate;

}

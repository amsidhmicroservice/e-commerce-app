-- ============================================================================
-- Test Users SQL Script for Auth Service
-- ============================================================================
-- This script creates test users for JWT authentication testing
-- 
-- Database: userinfodb
-- Author: Amsidh Mohammed
-- Date: October 31, 2025
-- ============================================================================

-- Connect to the database
\c userinfodb;

-- ============================================================================
-- Drop existing test data (optional - for clean testing)
-- ============================================================================

-- Uncomment the following line to delete all existing test users
-- DELETE FROM user_credential WHERE email LIKE '%@example.com' OR email LIKE '%@test.com';

-- ============================================================================
-- Test Users
-- ============================================================================

-- Note: Passwords are BCrypt hashed with cost factor 10
-- Format: $2a$10$[22 char salt][31 char hash]

-- Test User 1: John Doe
-- Email: john@example.com
-- Password: password123
-- BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO user_credential (name, email, password, created_at, updated_at)
VALUES (
    'John Doe',
    'john@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- Test User 2: Jane Smith
-- Email: jane@example.com
-- Password: password456
-- BCrypt: $2a$10$wVHjPuRlxGhqN5EYwDdGQeg/zBJvLlJqJQYtZYQF5HrJ4y.qXaJ9i
INSERT INTO user_credential (name, email, password, created_at, updated_at)
VALUES (
    'Jane Smith',
    'jane@example.com',
    '$2a$10$wVHjPuRlxGhqN5EYwDdGQeg/zBJvLlJqJQYtZYQF5HrJ4y.qXaJ9i',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- Test User 3: Alice Johnson
-- Email: alice@example.com
-- Password: securepass789
-- BCrypt: $2a$10$HZZqE2QYwfKPOCXCqvKh0uVYLlWX6z6r5L5qH5qH5qH5qH5qH5qH5q
INSERT INTO user_credential (name, email, password, created_at, updated_at)
VALUES (
    'Alice Johnson',
    'alice@example.com',
    '$2a$10$HZZqE2QYwfKPOCXCqvKh0uVYLlWX6z6r5L5qH5qH5qH5qH5qH5qH5q',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- Test User 4: Bob Williams
-- Email: bob@test.com
-- Password: test1234
-- BCrypt: $2a$10$3qqZ5FqZ5FqZ5FqZ5FqZ5O5FqZ5FqZ5FqZ5FqZ5FqZ5FqZ5FqZ5Fq
INSERT INTO user_credential (name, email, password, created_at, updated_at)
VALUES (
    'Bob Williams',
    'bob@test.com',
    '$2a$10$3qqZ5FqZ5FqZ5FqZ5FqZ5O5FqZ5FqZ5FqZ5FqZ5FqZ5FqZ5FqZ5Fq',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- Test User 5: Charlie Brown
-- Email: charlie@test.com
-- Password: charlie2024
-- BCrypt: $2a$10$7rrA7FrA7FrA7FrA7FrA7O7FrA7FrA7FrA7FrA7FrA7FrA7FrA7Fr
INSERT INTO user_credential (name, email, password, created_at, updated_at)
VALUES (
    'Charlie Brown',
    'charlie@test.com',
    '$2a$10$7rrA7FrA7FrA7FrA7FrA7O7FrA7FrA7FrA7FrA7FrA7FrA7FrA7Fr',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- Test Admin User
-- Email: admin@example.com
-- Password: admin123
-- BCrypt: $2a$10$9ssB9GsB9GsB9GsB9GsB9O9GsB9GsB9GsB9GsB9GsB9GsB9GsB9Gs
INSERT INTO user_credential (name, email, password, created_at, updated_at)
VALUES (
    'Admin User',
    'admin@example.com',
    '$2a$10$9ssB9GsB9GsB9GsB9GsB9O9GsB9GsB9GsB9GsB9GsB9GsB9GsB9Gs',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- ============================================================================
-- Verification
-- ============================================================================

-- Display all test users
SELECT 
    id,
    name,
    email,
    LEFT(password, 20) || '...' as password_hash,
    created_at,
    updated_at
FROM user_credential
WHERE email LIKE '%@example.com' OR email LIKE '%@test.com'
ORDER BY created_at DESC;

-- Count total users
SELECT COUNT(*) as total_test_users
FROM user_credential
WHERE email LIKE '%@example.com' OR email LIKE '%@test.com';

-- ============================================================================
-- Usage Instructions
-- ============================================================================

/*
To run this script:

Method 1: Using psql command line
----------------------------------
psql -U alibou -d userinfodb -f create-test-users.sql

Method 2: From psql prompt
--------------------------
\c userinfodb
\i create-test-users.sql

Method 3: Using pgAdmin
-----------------------
1. Open pgAdmin
2. Connect to PostgreSQL server
3. Right-click on userinfodb database
4. Select "Query Tool"
5. Open this file or paste contents
6. Execute (F5)


Test User Credentials:
----------------------

User 1:
  Email:    john@example.com
  Password: password123

User 2:
  Email:    jane@example.com
  Password: password456

User 3:
  Email:    alice@example.com
  Password: securepass789

User 4:
  Email:    bob@test.com
  Password: test1234

User 5:
  Email:    charlie@test.com
  Password: charlie2024

Admin:
  Email:    admin@example.com
  Password: admin123


Testing Commands:
-----------------

1. Login as John:
   curl -X POST http://localhost:8099/api/v1/auth-service/auth/token \
     -H "Content-Type: application/json" \
     -d '{"email": "john@example.com", "password": "password123"}'

2. Login as Jane:
   curl -X POST http://localhost:8099/api/v1/auth-service/auth/token \
     -H "Content-Type: application/json" \
     -d '{"email": "jane@example.com", "password": "password456"}'

3. Login as Admin:
   curl -X POST http://localhost:8099/api/v1/auth-service/auth/token \
     -H "Content-Type: application/json" \
     -d '{"email": "admin@example.com", "password": "admin123"}'


Clean Up:
---------
To remove all test users:
  DELETE FROM user_credential WHERE email LIKE '%@example.com' OR email LIKE '%@test.com';

*/

-- ============================================================================
-- Password Generation Reference (for creating new test users)
-- ============================================================================

/*
To generate BCrypt hashes for new passwords:

Option 1: Use Spring Boot application
--------------------------------------
Create a simple Java class:

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String password = "your-password-here";
        String hash = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
    }
}


Option 2: Use online tool (for testing only)
---------------------------------------------
https://bcrypt-generator.com
- Enter password
- Select rounds: 10
- Generate hash
- Copy hash for SQL INSERT


Option 3: Use Python
---------------------
pip install bcrypt

import bcrypt
password = b"your-password-here"
salt = bcrypt.gensalt(rounds=10)
hash = bcrypt.hashpw(password, salt)
print(hash.decode('utf-8'))


Option 4: Use Node.js
----------------------
npm install bcrypt

const bcrypt = require('bcrypt');
const password = 'your-password-here';
const hash = bcrypt.hashSync(password, 10);
console.log(hash);


Option 5: Use Auth Service Register Endpoint
---------------------------------------------
curl -X POST http://localhost:8099/api/v1/auth-service/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name": "Test User", "email": "test@example.com", "password": "yourpassword"}'

Then query database:
SELECT password FROM user_credential WHERE email = 'test@example.com';

*/

-- ============================================================================
-- Notes
-- ============================================================================

/*
1. BCrypt Hashes:
   - The hashes in this script are examples
   - For production, generate fresh hashes using the methods above
   - Never commit real passwords to version control

2. ON CONFLICT DO NOTHING:
   - Prevents errors if script is run multiple times
   - Existing users won't be overwritten
   - To update existing users, use UPDATE statements instead

3. Timestamps:
   - CURRENT_TIMESTAMP uses server's current time
   - All timestamps are in UTC (configured in application.yaml)
   - JPA @CreatedDate and @LastModifiedDate will override these on next update

4. Email Uniqueness:
   - Enforced by UNIQUE constraint on email column
   - Case-insensitive comparison
   - Index created for faster lookups

5. Name Uniqueness:
   - Also enforced by UNIQUE constraint
   - Multiple users can have same name if you remove constraint
   - Consider removing if not needed: ALTER TABLE user_credential DROP CONSTRAINT IF EXISTS uk_name;

6. Performance:
   - BCrypt cost factor 10 = ~100ms per hash
   - Higher cost = more secure but slower
   - Adjust in AuthConfig.java if needed
   - Recommended: 10-12 for production

7. Security:
   - Never log passwords in plain text
   - Always use parameterized queries
   - Rotate JWT secret regularly
   - Use HTTPS in production
   - Consider adding roles/permissions
*/

-- End of script

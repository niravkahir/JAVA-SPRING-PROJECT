-- Insert admin user (password: Admin@123)
-- BCrypt hash for "Admin@123"
INSERT IGNORE INTO users (username, name, email, password, role) VALUES
('admin', 'Administrator', 'admin@example.com','$2a$10$YourHashedPasswordHere', 'ADMIN');

-- Note: You need to generate BCrypt hash for your admin password
-- Use online BCrypt generator or run this in Java:
-- new BCryptPasswordEncoder().encode("Admin@123")
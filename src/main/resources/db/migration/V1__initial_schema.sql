-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Create expenses table
CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DOUBLE NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(500),
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- Create budgets table
CREATE TABLE IF NOT EXISTS budgets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    monthly_limit DOUBLE NOT NULL,
    month VARCHAR(7) NOT NULL,
    user_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
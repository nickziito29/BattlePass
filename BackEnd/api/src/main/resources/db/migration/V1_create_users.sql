CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       nickname VARCHAR(255),
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       birth_date DATE,
                       gender VARCHAR(20),
                       profile_picture_url VARCHAR(512),
                       bio TEXT,
                       pronoun VARCHAR(50),
                       custom_gender VARCHAR(50),
                       hometown VARCHAR(100),
                       current_city VARCHAR(100),
                       status VARCHAR(30) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
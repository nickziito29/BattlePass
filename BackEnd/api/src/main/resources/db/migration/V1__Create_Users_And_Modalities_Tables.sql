CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       nickname VARCHAR(255),
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       birth_date DATE,
                       gender VARCHAR(50),
                       profile_picture_url VARCHAR(255),
                       bio TEXT,
                       pronoun VARCHAR(50),
                       custom_gender VARCHAR(255),
                       hometown VARCHAR(255),
                       current_city VARCHAR(255),
                       status VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       is_new_user BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE modalities (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            description VARCHAR(500)
);
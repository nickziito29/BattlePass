CREATE TABLE verification_token (
                                    id UUID PRIMARY KEY,
                                    token VARCHAR(255) NOT NULL,
                                    expiry_date TIMESTAMP NOT NULL,
                                    user_id UUID NOT NULL,
                                    CONSTRAINT fk_user_verification_token FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);
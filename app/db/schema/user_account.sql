-- User account (used for login and recipe ownership)
CREATE TABLE user_account
(
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    password_hash VARCHAR(60)         NOT NULL,
    email_address VARCHAR(100) UNIQUE NOT NULL
);

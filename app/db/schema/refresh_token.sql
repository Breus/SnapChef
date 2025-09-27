-- Refresh token for users to retrieve a fresh JWT
CREATE TABLE refresh_token
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES user_account (id) ON DELETE CASCADE,
    token      VARCHAR(255) UNIQUE NOT NULL,
    issued_at  TIMESTAMP           NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP           NOT NULL,
    valid      BOOLEAN             NOT NULL DEFAULT TRUE
);
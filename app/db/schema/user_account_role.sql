CREATE TABLE user_account_role (
    user_account_id BIGSERIAL NOT NULL REFERENCES user_account(id),
    role_id BIGINT NOT NULL REFERENCES role(id),
    PRIMARY KEY (user_account_id, role_id)
);
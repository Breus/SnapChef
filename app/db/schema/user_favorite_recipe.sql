-- User's favorite recipes (linking table)
CREATE TABLE user_favorite_recipe
(
    user_account_id BIGINT NOT NULL REFERENCES user_account (id),
    recipe_id       BIGINT NOT NULL REFERENCES recipe (id),
    PRIMARY KEY (user_account_id, recipe_id)
);
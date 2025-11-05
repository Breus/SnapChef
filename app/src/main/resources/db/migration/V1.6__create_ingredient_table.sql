-- Ingredient for a specific recipe
CREATE TABLE ingredient
(
    id          BIGSERIAL PRIMARY KEY,
    recipe_id   BIGINT       NOT NULL REFERENCES recipe (id) ON DELETE CASCADE,
    description VARCHAR(255) NOT NULL,          -- e.g., 1/2 cup of flour
    position    INTEGER      NOT NULL DEFAULT 0 -- to preserve order
);
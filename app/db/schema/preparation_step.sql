-- Preparation step for a specific recipe
CREATE TABLE preparation_step (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
    description TEXT NOT NULL,
    position INTEGER NOT NULL DEFAULT 0 -- step order
);
-- Preparation step (per recipe)
CREATE TABLE preparation_step (
    id BIGINT PRIMARY KEY,
    recipe_id BIGINT REFERENCES recipe(id) ON DELETE CASCADE,
    description TEXT NOT NULL,
    position INTEGER NOT NULL DEFAULT 0 -- step order
);
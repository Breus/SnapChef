-- Preparation step (per recipe)
CREATE TABLE step (
    id BIGSERIAL PRIMARY KEY,
    recipe_id INTEGER REFERENCES recipe(id) ON DELETE CASCADE,
    description TEXT NOT NULL,
    position INTEGER NOT NULL DEFAULT 0 -- step order
);
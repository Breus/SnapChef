-- Preparation step (per recipe)
CREATE TABLE steps (
    id SERIAL PRIMARY KEY,
    recipe_id INTEGER REFERENCES recipe(id) ON DELETE CASCADE,
    description TEXT NOT NULL,
    position INTEGER NOT NULL DEFAULT 0 -- step order
);
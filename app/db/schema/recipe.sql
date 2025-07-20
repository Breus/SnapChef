-- Recipe
CREATE TABLE recipe (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    author_id BIGINT REFERENCES account(id) ON DELETE SET NULL
);
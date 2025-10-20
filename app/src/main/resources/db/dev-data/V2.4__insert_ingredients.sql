-- Insert demo ingredients
INSERT INTO ingredient (name, quantity, position, recipe_id)
VALUES ('Spaghetti', '200g', 1, 1),
       ('Ground beef', '300g', 2, 1),
       ('Tomato sauce', '400ml', 3, 1),
       ('Chicken thighs', '500g', 1, 2),
       ('Curry powder', '2 tbsp', 2, 2),
       ('Coconut milk', '400ml', 3, 2),
       ('Romaine lettuce', '1 head', 1, 3),
       ('Croutons', '100g', 2, 3),
       ('Parmesan cheese', '50g', 3, 3),
       ('Flour', '250g', 1, 4),
       ('Chocolate chips', '200g', 2, 4),
       ('Butter', '150g', 3, 4)
ON CONFLICT DO NOTHING;


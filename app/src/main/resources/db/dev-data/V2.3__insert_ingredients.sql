-- Insert demo ingredients
INSERT INTO ingredient (name, quantity, position, recipe_id)
SELECT name, quantity, position, r.id
FROM (VALUES ('Spaghetti', '200g', 1, 'Spaghetti Bolognese'),
             ('Ground beef', '300g', 2, 'Spaghetti Bolognese'),
             ('Tomato sauce', '400ml', 3, 'Spaghetti Bolognese'),
             ('Chicken thighs', '500g', 1, 'Chicken Curry'),
             ('Curry powder', '2 tbsp', 2, 'Chicken Curry'),
             ('Coconut milk', '400ml', 3, 'Chicken Curry'),
             ('Romaine lettuce', '1 head', 1, 'Caesar Salad'),
             ('Croutons', '100g', 2, 'Caesar Salad'),
             ('Parmesan cheese', '50g', 3, 'Caesar Salad'),
             ('Flour', '250g', 1, 'Chocolate Chip Cookies'),
             ('Chocolate chips', '200g', 2, 'Chocolate Chip Cookies'),
             ('Butter', '150g', 3, 'Chocolate Chip Cookies')) AS ingredients(name, quantity, position, recipe_title)
         JOIN recipe r ON r.title = ingredients.recipe_title

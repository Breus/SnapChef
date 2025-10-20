-- Insert demo recipes
INSERT INTO recipe (title, description, num_servings, preparation_time, author_id)
VALUES ('Spaghetti Bolognese', 'Classic Italian pasta dish.', 4, 'MIN_0_15', 1),
       ('Chicken Curry', 'Spicy Indian-style curry with tender chicken.', 4, 'MIN_0_15', 2),
       ('Caesar Salad', 'Classic romaine lettuce salad with creamy dressing.', 4, 'MIN_15_30', 3),
       ('Chocolate Chip Cookies', 'Soft and chewy homemade cookies.', 4, 'MIN_15_30', 4),
       ('Mushroom Risotto', 'Creamy Italian rice dish with mushrooms.', 4, 'MIN_30_45', 2),
       ('Fish Tacos', 'Mexican-style fish tacos with fresh salsa.', 4, 'MIN_30_45', 3),
       ('Greek Salad', 'Traditional Mediterranean salad with feta cheese.', 4, 'HOUR_PLUS', 4),
       ('Beef Stir Fry', 'Quick and easy Asian-style beef dish.', 4, 'HOUR_PLUS', 1),
       ('Banana Bread', 'Moist and delicious homemade bread.', 4, 'MIN_15_30', NULL),
       ('Vegetable Soup', 'Healthy and warming soup with mixed vegetables.', 4, 'MIN_15_30', NULL)
ON CONFLICT DO NOTHING;


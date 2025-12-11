-- Insert demo recipes
INSERT INTO recipe (title, description, image_name, num_servings, preparation_time, author_id)
SELECT title, description, image_name, num_servings, preparation_time, ua.id
FROM (VALUES ('Spaghetti Bolognese', 'Classic Italian pasta dish.', 'spaghett.jpg', 4, 'MIN_0_15', 'Robert'),
             ('Chicken Curry', 'Spicy Indian-style curry with tender chicken.', null, 4, 'MIN_0_15', 'Breus'),
             ('Caesar Salad', 'Classic romaine lettuce salad with creamy dressing.', null, 4, 'MIN_15_30', 'ChefMaster'),
             ('Chocolate Chip Cookies', 'Soft and chewy homemade cookies.', null, 4, 'MIN_15_30', 'FoodLover'),
             ('Mushroom Risotto', 'Creamy Italian rice dish with mushrooms.', null, 4, 'MIN_30_45', 'Breus'),
             ('Fish Tacos', 'Mexican-style fish tacos with fresh salsa.', null, 4, 'MIN_30_45', 'ChefMaster'),
             ('Greek Salad', 'Traditional Mediterranean salad with feta cheese.', null, 4, 'HOUR_PLUS', 'FoodLover'),
             ('Beef Stir Fry', 'Quick and easy Asian-style beef dish.', null, 4, 'HOUR_PLUS', 'Robert'),
             ('Banana Bread', 'Moist and delicious homemade bread.', null, 4, 'MIN_15_30', NULL),
             ('Vegetable Soup', 'Healthy and warming soup with mixed vegetables.', null, 4, 'MIN_15_30',
              NULL)) AS recipes(title, description, image_name, num_servings, preparation_time, username)
         LEFT JOIN user_account ua ON ua.username = recipes.username;

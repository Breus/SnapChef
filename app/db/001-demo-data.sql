INSERT INTO user_account (id, username, password_hash, email_address) VALUES
  (1, 'Robert', 'demo-hash', 'robert@example.com'),
  (2, 'Breus', 'demo-hash', 'breus@example.com'),
  (3, 'ChefMaster', 'demo-hash', 'chef@example.com'),
  (4, 'FoodLover', 'demo-hash', 'food@example.com'),
  (5, 'KitchenPro', 'demo-hash', 'kitchen@example.com')
ON CONFLICT (id) DO NOTHING;

INSERT INTO role (id, name) VALUES
  (1, 'admin'),
  (2, 'user')
ON CONFLICT (id) DO NOTHING;

-- Map users to roles
INSERT INTO user_account_role (user_account_id, role_id) VALUES
  (1, 1), -- Robert: admin
  (1, 2), -- Robert: user
  (2, 1), -- Breus: admin
  (2, 2), -- Breus: user
  (3, 2), -- ChefMaster: user
  (4, 2), -- FoodLover: user
  (5, 2)  -- KitchenPro: user
ON CONFLICT (user_account_id, role_id) DO NOTHING;

INSERT INTO recipe (id, title, description, author_id) VALUES
  (1, 'Spaghetti Bolognese', 'Classic Italian pasta dish.', 1),
  (2, 'Chicken Curry', 'Spicy Indian-style curry with tender chicken.', 2),
  (3, 'Caesar Salad', 'Classic romaine lettuce salad with creamy dressing.', 3),
  (4, 'Chocolate Chip Cookies', 'Soft and chewy homemade cookies.', 4),
  (5, 'Mushroom Risotto', 'Creamy Italian rice dish with mushrooms.', 2),
  (6, 'Fish Tacos', 'Mexican-style fish tacos with fresh salsa.', 3),
  (7, 'Greek Salad', 'Traditional Mediterranean salad with feta cheese.', 4),
  (8, 'Beef Stir Fry', 'Quick and easy Asian-style beef dish.', 1),
  (9, 'Banana Bread', 'Moist and delicious homemade bread.', 2),
  (10, 'Vegetable Soup', 'Healthy and warming soup with mixed vegetables.', 3)
ON CONFLICT (id) DO NOTHING;

INSERT INTO ingredient (id, name, quantity, position, recipe_id) VALUES
  (1, 'Spaghetti', '200g', 1, 1),
  (2, 'Ground beef', '300g', 2, 1),
  (3, 'Tomato sauce', '400ml', 3, 1),
  (4, 'Chicken thighs', '500g', 1, 2),
  (5, 'Curry powder', '2 tbsp', 2, 2),
  (6, 'Coconut milk', '400ml', 3, 2),
  (7, 'Romaine lettuce', '1 head', 1, 3),
  (8, 'Croutons', '100g', 2, 3),
  (9, 'Parmesan cheese', '50g', 3, 3),
  (10, 'Flour', '250g', 1, 4),
  (11, 'Chocolate chips', '200g', 2, 4),
  (12, 'Butter', '150g', 3, 4),
  (13, 'Arborio rice', '300g', 1, 5),
  (14, 'Mushrooms', '200g', 2, 5),
  (15, 'Vegetable stock', '1L', 3, 5),
  (16, 'White fish fillets', '400g', 1, 6),
  (17, 'Corn tortillas', '8 pieces', 2, 6),
  (18, 'Fresh salsa', '200g', 3, 6),
  (19, 'Cucumber', '1 piece', 1, 7),
  (20, 'Feta cheese', '200g', 2, 7),
  (21, 'Kalamata olives', '100g', 3, 7),
  (22, 'Beef strips', '400g', 1, 8),
  (23, 'Mixed vegetables', '300g', 2, 8),
  (24, 'Soy sauce', '50ml', 3, 8),
  (25, 'Ripe bananas', '3 pieces', 1, 9),
  (26, 'Sugar', '150g', 2, 9),
  (27, 'Baking powder', '1 tsp', 3, 9),
  (28, 'Mixed vegetables', '400g', 1, 10),
  (29, 'Stock cubes', '2 pieces', 2, 10),
  (30, 'Fresh herbs', '1 bunch', 3, 10)
ON CONFLICT (id) DO NOTHING;

INSERT INTO preparation_step (id, description, position, recipe_id) VALUES
  (1, 'Boil spaghetti according to package instructions.', 1, 1),
  (2, 'Cook ground beef until browned.', 2, 1),
  (3, 'Add tomato sauce to beef and simmer for 10 minutes.', 3, 1),
  (4, 'Cut chicken into bite-sized pieces.', 1, 2),
  (5, 'Cook chicken with curry powder until golden.', 2, 2),
  (6, 'Add coconut milk and simmer for 20 minutes.', 3, 2),
  (7, 'Wash and chop the romaine lettuce.', 1, 3),
  (8, 'Toss with croutons and cheese.', 2, 3),
  (9, 'Add Caesar dressing and mix well.', 3, 3),
  (10, 'Mix flour, butter, and chocolate chips.', 1, 4),
  (11, 'Form into cookies and place on baking sheet.', 2, 4),
  (12, 'Bake at 180°C for 12 minutes.', 3, 4),
  (13, 'Sauté mushrooms until golden brown.', 1, 5),
  (14, 'Add rice and gradually add hot stock.', 2, 5),
  (15, 'Stir continuously until rice is creamy and cooked.', 3, 5),
  (16, 'Season and cook fish until flaky.', 1, 6),
  (17, 'Warm tortillas and prepare toppings.', 2, 6),
  (18, 'Assemble tacos with fish and salsa.', 3, 6),
  (19, 'Chop all vegetables into chunks.', 1, 7),
  (20, 'Add olives and crumbled feta.', 2, 7),
  (21, 'Drizzle with olive oil and oregano.', 3, 7),
  (22, 'Marinate beef strips in soy sauce.', 1, 8),
  (23, 'Stir-fry vegetables until crisp-tender.', 2, 8),
  (24, 'Add beef and cook until done.', 3, 8),
  (25, 'Mash bananas and mix with wet ingredients.', 1, 9),
  (26, 'Combine with dry ingredients.', 2, 9),
  (27, 'Bake in loaf pan for 1 hour.', 3, 9),
  (28, 'Chop all vegetables into small pieces.', 1, 10),
  (29, 'Simmer in stock until tender.', 2, 10),
  (30, 'Season and add fresh herbs.', 3, 10)
ON CONFLICT (id) DO NOTHING;

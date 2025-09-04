-- The password_hash for all these users is 'password123' hashed using bcrypt
INSERT INTO user_account (username, password_hash, email_address) VALUES
  ('Robert', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'robert@example.com'),
  ('Breus', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'breus@example.com'),
  ('ChefMaster', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'chef@example.com'),
  ('FoodLover', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'food@example.com'),
  ('KitchenPro', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'kitchen@example.com');

INSERT INTO role (id, name) VALUES
  (1, 'admin'),
  (2, 'user');

-- Map users to roles
INSERT INTO user_account_role (user_account_id, role_id) VALUES
  (1, 1), -- Robert: admin
  (1, 2), -- Robert: user
  (2, 1), -- Breus: admin
  (2, 2), -- Breus: user
  (3, 2), -- ChefMaster: user
  (4, 2), -- FoodLover: user
  (5, 2);  -- KitchenPro: user

INSERT INTO recipe (title, description, author_id) VALUES
  ('Spaghetti Bolognese', 'Classic Italian pasta dish.', 1),
  ('Chicken Curry', 'Spicy Indian-style curry with tender chicken.', 2),
  ('Caesar Salad', 'Classic romaine lettuce salad with creamy dressing.', 3),
  ('Chocolate Chip Cookies', 'Soft and chewy homemade cookies.', 4),
  ('Mushroom Risotto', 'Creamy Italian rice dish with mushrooms.', 2),
  ('Fish Tacos', 'Mexican-style fish tacos with fresh salsa.', 3),
  ('Greek Salad', 'Traditional Mediterranean salad with feta cheese.', 4),
  ('Beef Stir Fry', 'Quick and easy Asian-style beef dish.', 1),
  ('Banana Bread', 'Moist and delicious homemade bread.', 2),
  ( 'Vegetable Soup', 'Healthy and warming soup with mixed vegetables.', 3)
ON CONFLICT DO NOTHING;

INSERT INTO ingredient (name, quantity, position, recipe_id) VALUES
  ('Spaghetti', '200g', 1, 1),
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
  ('Butter', '150g', 3, 4),
  ('Arborio rice', '300g', 1, 5),
  ('Mushrooms', '200g', 2, 5),
  ('Vegetable stock', '1L', 3, 5),
  ('White fish fillets', '400g', 1, 6),
  ('Corn tortillas', '8 pieces', 2, 6),
  ('Fresh salsa', '200g', 3, 6),
  ('Cucumber', '1 piece', 1, 7),
  ('Feta cheese', '200g', 2, 7),
  ('Kalamata olives', '100g', 3, 7),
  ('Beef strips', '400g', 1, 8),
  ('Mixed vegetables', '300g', 2, 8),
  ('Soy sauce', '50ml', 3, 8),
  ('Ripe bananas', '3 pieces', 1, 9),
  ('Sugar', '150g', 2, 9),
  ('Baking powder', '1 tsp', 3, 9),
  ('Mixed vegetables', '400g', 1, 10),
  ('Stock cubes', '2 pieces', 2, 10),
  ('Fresh herbs', '1 bunch', 3, 10)
ON CONFLICT (id) DO NOTHING;

INSERT INTO preparation_step (description, position, recipe_id) VALUES
  ('Boil spaghetti according to package instructions.', 1, 1),
  ('Cook ground beef until browned.', 2, 1),
  ('Add tomato sauce to beef and simmer for 10 minutes.', 3, 1),
  ('Cut chicken into bite-sized pieces.', 1, 2),
  ('Cook chicken with curry powder until golden.', 2, 2),
  ('Add coconut milk and simmer for 20 minutes.', 3, 2),
  ('Wash and chop the romaine lettuce.', 1, 3),
  ('Toss with croutons and cheese.', 2, 3),
  ('Add Caesar dressing and mix well.', 3, 3),
  ('Mix flour, butter, and chocolate chips.', 1, 4),
  ('Form into cookies and place on baking sheet.', 2, 4),
  ('Bake at 180°C for 12 minutes.', 3, 4),
  ('Sauté mushrooms until golden brown.', 1, 5),
  ('Add rice and gradually add hot stock.', 2, 5),
  ('Stir continuously until rice is creamy and cooked.', 3, 5),
  ('Season and cook fish until flaky.', 1, 6),
  ('Warm tortillas and prepare toppings.', 2, 6),
  ('Assemble tacos with fish and salsa.', 3, 6),
  ('Chop all vegetables into chunks.', 1, 7),
  ('Add olives and crumbled feta.', 2, 7),
  ('Drizzle with olive oil and oregano.', 3, 7),
  ('Marinate beef strips in soy sauce.', 1, 8),
  ('Stir-fry vegetables until crisp-tender.', 2, 8),
  ('Add beef and cook until done.', 3, 8),
  ('Mash bananas and mix with wet ingredients.', 1, 9),
  ('Combine with dry ingredients.', 2, 9),
  ('Bake in loaf pan for 1 hour.', 3, 9),
  ('Chop all vegetables into small pieces.', 1, 10),
  ('Simmer in stock until tender.', 2, 10),
  ('Season and add fresh herbs.', 3, 10);

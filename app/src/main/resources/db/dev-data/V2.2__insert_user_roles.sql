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


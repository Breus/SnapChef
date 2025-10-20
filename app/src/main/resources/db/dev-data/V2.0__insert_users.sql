-- Demo data file that will only be executed in development profile
-- The password_hash for all these users is 'password123' hashed using bcrypt
INSERT INTO user_account (username, password_hash, email_address)
VALUES ('Robert', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'robert@example.com'),
       ('Breus', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'breus@example.com'),
       ('ChefMaster', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'chef@example.com'),
       ('FoodLover', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'food@example.com'),
       ('KitchenPro', '$2a$10$JPQ/pnZqC8efUOi3M9ZqReeNDR7IkA1Ry973r.IK020zHSuP4P.KC', 'kitchen@example.com')
ON CONFLICT (id) DO NOTHING;


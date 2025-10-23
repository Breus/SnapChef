INSERT INTO user_account_role (user_account_id, role_id)
SELECT ua.id, r.id
FROM (VALUES ('Robert', 'admin'),
             ('Robert', 'user'),
             ('Breus', 'admin'),
             ('Breus', 'user'),
             ('ChefMaster', 'user'),
             ('FoodLover', 'user'),
             ('KitchenPro', 'user')) AS mappings(username, role_name)
         JOIN user_account ua ON ua.username = mappings.username
         JOIN role r ON r.name = mappings.role_name
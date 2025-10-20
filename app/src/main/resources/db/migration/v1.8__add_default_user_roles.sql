INSERT INTO role (id, name)
VALUES (1, 'admin'),
       (2, 'user')
ON CONFLICT (id) DO NOTHING;
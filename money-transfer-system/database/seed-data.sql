-- Passwords are 'password'
INSERT INTO users (username, password) VALUES
('alice_101', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG'),
('bob_102', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG'),
('charlie_103', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG');

INSERT INTO accounts (user_id, holder_name, balance, status) VALUES
  (101, 'Alice', 10000.00, 'ACTIVE'),
  (102, 'Bob', 5000.00, 'ACTIVE'),
  (103, 'Charlie', 2500.00, 'LOCKED');


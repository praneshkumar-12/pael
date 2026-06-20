-- Passwords are 'password'
INSERT INTO users (username, password) VALUES
('alice', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG'),
('bob', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG'),
('charlie', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG');

INSERT INTO accounts (user_id, holder_name, balance, status) VALUES
  (1, 'Alice', 10000.00, 'ACTIVE'),
  (2, 'Bob', 5000.00, 'ACTIVE'),
  (3, 'Charlie', 2500.00, 'LOCKED');


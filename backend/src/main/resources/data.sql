INSERT INTO accounts (holder_name, balance, status, version) VALUES
('John Doe', 10000.00, 'ACTIVE', 0),
('Jane Smith', 5000.00, 'ACTIVE', 0),
('Bob Johnson', 7500.00, 'ACTIVE', 0)
ON DUPLICATE KEY UPDATE holder_name = VALUES(holder_name);

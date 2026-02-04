CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    holder_name VARCHAR(255) NOT NULL,
    balance DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    version INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transaction_logs (
    id VARCHAR(36) PRIMARY KEY,
    from_account BIGINT NOT NULL,
    to_account BIGINT NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    failure_reason VARCHAR(255),
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_account) REFERENCES accounts(id),
    FOREIGN KEY (to_account) REFERENCES accounts(id)
);

CREATE INDEX idx_transaction_logs_from_account ON transaction_logs(from_account);
CREATE INDEX idx_transaction_logs_to_account ON transaction_logs(to_account);
CREATE INDEX idx_transaction_logs_idempotency_key ON transaction_logs(idempotency_key);

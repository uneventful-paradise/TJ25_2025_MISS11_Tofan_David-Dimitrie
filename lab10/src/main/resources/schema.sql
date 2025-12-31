-- The Write Model: Stores every event
CREATE TABLE IF NOT EXISTS event_store (
   id SERIAL PRIMARY KEY,
   aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    event_data TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL
    );

-- Optimization: Stores state at specific versions
CREATE TABLE IF NOT EXISTS snapshots (
    id SERIAL PRIMARY KEY,
    aggregate_id VARCHAR(255) NOT NULL,
    state_data TEXT NOT NULL,
    version INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- The Read Model: Queryable view of accounts
CREATE TABLE IF NOT EXISTS account_view (
    account_id VARCHAR(255) PRIMARY KEY,
    owner VARCHAR(255),
    balance NUMERIC(10, 2)
    );
-- store events
CREATE TABLE IF NOT EXISTS event_store (
   id SERIAL PRIMARY KEY,
   aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    event_data TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL
    );

-- state at specific version
CREATE TABLE IF NOT EXISTS snapshots (
    id SERIAL PRIMARY KEY,
    aggregate_id VARCHAR(255) NOT NULL,
    state_data TEXT NOT NULL,
    version INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- read model
CREATE TABLE IF NOT EXISTS account_view (
    account_id VARCHAR(255) PRIMARY KEY,
    owner VARCHAR(255),
    balance NUMERIC(10, 2)
    );
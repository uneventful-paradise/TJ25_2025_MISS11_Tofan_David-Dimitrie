-- This is the H2 equivalent of PostgreSQL's ON CONFLICT
MERGE INTO app_config (config_key, config_value)
    KEY(config_key)
    VALUES ('environment.name', 'Default');
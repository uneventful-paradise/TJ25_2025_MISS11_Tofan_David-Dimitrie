INSERT INTO app_config (config_key, config_value) VALUES ('environment.name', 'Default')
    ON CONFLICT (config_key) DO UPDATE SET config_value = 'Default';
package com.example.lab02HW.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("devDB")
                .addScript("classpath:schema-dev.sql")
                .addScript("classpath:data-dev.sql")
                .build();
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource(DataSourceProperties dataSourceProperties) {
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s",
                dataSourceProperties.getHost(),
                dataSourceProperties.getPort(),
                dataSourceProperties.getDatabaseName());
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
    }
}

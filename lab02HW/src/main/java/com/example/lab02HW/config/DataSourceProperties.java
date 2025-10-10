package com.example.lab02HW.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "db")
@Validated
public class DataSourceProperties {
    @NotBlank
    private String host;
    @NotNull
    @Positive
    private Integer port;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String databaseName;
    public String getHost() {return host;}
    public void setHost(String host) {this.host = host;}
    public Integer getPort() {return port;}
    public void setPort(Integer port) {this.port = port;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getDatabaseName() {return databaseName;}
    public void setDatabaseName(String databaseName) {this.databaseName = databaseName;}
}

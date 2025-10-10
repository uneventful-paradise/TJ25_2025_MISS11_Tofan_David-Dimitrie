package com.example.lab02HW.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseInfoContributor implements InfoContributor {

    @Autowired
    private DataSource dataSource;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> dbDetails = new HashMap<>();
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            dbDetails.put("url", metaData.getURL());
            dbDetails.put("userName", metaData.getUserName());
            dbDetails.put("driverName", metaData.getDriverName());
            dbDetails.put("databaseVersion", metaData.getDatabaseProductVersion());
        } catch (SQLException e) {
            dbDetails.put("error", e.getMessage());
        }
        builder.withDetail("databaseConnection", dbDetails);
    }
}

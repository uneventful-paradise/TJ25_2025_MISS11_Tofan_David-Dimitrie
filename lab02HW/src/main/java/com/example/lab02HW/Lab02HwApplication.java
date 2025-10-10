package com.example.lab02HW;
import com.example.lab02HW.config.DataSourceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableConfigurationProperties(DataSourceProperties.class)
public class Lab02HwApplication {
	public static void main(String[] args) {
        SpringApplication.run(Lab02HwApplication.class, args);
	}

}

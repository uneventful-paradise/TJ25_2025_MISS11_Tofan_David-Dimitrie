package com.example.lab03;

import com.example.lab03.Compulsory.BaseClass;
import com.example.lab03.DomainModel.Customer;
import com.example.lab03.DomainModel.Order;
import com.example.lab03.Repository.InMemoryRepository;
import com.example.lab03.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class Lab03Application {
    private static final Logger log = LoggerFactory.getLogger(Lab03Application.class);

	public static void main(String[] args) {
        SpringApplication.run(Lab03Application.class, args);
	}

    @Bean
    public CommandLineRunner run(OrderService orderService, InMemoryRepository repository) {
        return args -> {

            log.info("\n\n\nSTARTING MAIN DEMO\n\n\n");
            //loyal customer
            repository.findById(1L).ifPresent(loyal_customer -> {
                Order loyal_order = new Order(101L, loyal_customer, new BigDecimal("1001"));
                orderService.processOrder(loyal_order);
            });

            repository.findById(2L).ifPresent(regular_customer -> {
                Order regular_order = new Order(102L, regular_customer, new BigDecimal("5000"));
                orderService.processOrder(regular_order);
            });

            //non existent customer
            try {
                Customer fake_customer = new Customer(103L, "tata", false);
                Order fake_order = new Order(103L, fake_customer, new BigDecimal("1001"));
                orderService.processOrder(fake_order);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            log.info("\n\n\nFINISHED MAIN DEMO\n\n\n");
        };
    }


//    @Bean
//    public CommandLineRunner run(BaseClass baseClass) {
//        return args -> {
//            baseClass.printDependencies();
//        };
//    }
}

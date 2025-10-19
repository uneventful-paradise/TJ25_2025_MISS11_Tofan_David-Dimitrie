package com.example.lab03.Repository;

import com.example.lab03.DomainModel.Customer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//what does repository do?
@Repository
public class InMemoryRepository {
    //why concurrent?
    private final Map<Long, Customer> customers = new ConcurrentHashMap();
    private final Logger log =  LoggerFactory.getLogger(InMemoryRepository.class);
    @PostConstruct
    public void init() {
        log.info("Initializing repo");
        customers.put(1L, new Customer(1L, "David", true));
        customers.put(2L, new Customer(2L, "Andrei", false));
        customers.put(3L, new Customer(3L, "Cosmin", true));
    }
    //what is Optional?
    public Customer getCustomerById(Long id) {
        return customers.get(id);
    }

    public Optional<Customer> findById(Long id) {
        log.info("Finding customer by id {}", id);
        // Wrap the result in an Optional. ofNullable handles cases where the customer might not exist (returns an empty Optional).
        return Optional.ofNullable(customers.get(id));
    }
}

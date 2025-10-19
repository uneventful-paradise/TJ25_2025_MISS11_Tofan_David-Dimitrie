package com.example.lab03.Discount;

import com.example.lab03.DomainModel.Order;
import com.example.lab03.Exception.DiscountException;
import com.example.lab03.Repository.InMemoryRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DiscountDecorator {
    private final InMemoryRepository repository;

    public  DiscountDecorator(InMemoryRepository repository){
        this.repository = repository;
    }

    @Pointcut("execution(* com.example.lab03.Discount.DiscountService.applyDiscount(..)) && args(order)")
    public void applyDiscountPointcut(Order order){
    }

    @Before("applyDiscountPointcut(order)")
    public void checkCustomerEligibility(Order order){
        Long customer_id = order.getCustomer().getId();
        repository.findById(customer_id).orElseThrow(()->new DiscountException("Customer with id " + customer_id + " not found"));
    }
}

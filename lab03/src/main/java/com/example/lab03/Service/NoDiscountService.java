package com.example.lab03.Service;

import com.example.lab03.Discount.DiscountService;
import com.example.lab03.DomainModel.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//@Service
public class NoDiscountService implements DiscountService {
    private static final Logger log = LoggerFactory.getLogger(NoDiscountService.class);
    @Override
    public BigDecimal applyDiscount(Order order){
        log.info("Method: applyDiscount (NoDiscount) | Customer: {} | id: {} | Discount: 0",
                order.getCustomer().getName(), order.getCustomer().getId());
        return BigDecimal.ZERO;
    }
}

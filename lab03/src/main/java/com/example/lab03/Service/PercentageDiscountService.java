package com.example.lab03.Service;

import com.example.lab03.Discount.DiscountService;
import com.example.lab03.DomainModel.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//@Service
public class PercentageDiscountService implements DiscountService {
    private static final Logger log = LoggerFactory.getLogger(PercentageDiscountService.class);
    private static final BigDecimal DISCOUNT_AMOUNT = new BigDecimal("0.10");
    @Override
    public BigDecimal applyDiscount(Order order){
        if (order.getCustomer().isLoyal()) {
            BigDecimal discount = order.getValue().multiply(DISCOUNT_AMOUNT);
            log.info("Method: applyDiscount (Percentage Discount) | Customer: {} | id: {} | Discount: {}",
                    order.getCustomer().getName(), order.getCustomer().getId(), discount);
            return discount;
        } else {
            log.warn("Method: applyDiscount(Percentage Discount) | Customer {} IS NOT LOYAL",  order.getCustomer().getName());
            return BigDecimal.ZERO;
        }
    }
}

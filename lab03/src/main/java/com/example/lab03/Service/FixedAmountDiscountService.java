package com.example.lab03.Service;

import com.example.lab03.Discount.DiscountService;
import com.example.lab03.DomainModel.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//@Service
public class FixedAmountDiscountService implements DiscountService {
    private static final Logger log = LoggerFactory.getLogger(FixedAmountDiscountService.class);
    private static final BigDecimal DISCOUNT_AMOUNT = new BigDecimal("0.40");
    private static final BigDecimal VALUE_THRESHOLD = new BigDecimal("100.00");
    @Override
    public BigDecimal applyDiscount(Order order){
        if (order.getValue().compareTo(VALUE_THRESHOLD) >= 0) {
            log.info("Method: applyDiscount (Fixed Discount) | Customer: {} | id: {} | Discount: {}",
                    order.getCustomer().getName(), order.getCustomer().getId(), DISCOUNT_AMOUNT);
            return DISCOUNT_AMOUNT;
        } else {
            log.warn("Method: applyDiscount (Fixed Discount) | order id {} IS NOT HIGH ENOUGH {} < {}",
                    order.getId(), order.getValue(), VALUE_THRESHOLD);
            return BigDecimal.ZERO;
        }
    }
}

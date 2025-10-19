package com.example.lab03.Service;

import com.example.lab03.Discount.DiscountService;
import com.example.lab03.DomainModel.Order;
import com.example.lab03.Event.HighValueDiscountEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final BigDecimal HIGH_DISCOUNT = new BigDecimal("100.00");

    private final DiscountService discountService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderService(DiscountService discountService,  ApplicationEventPublisher applicationEventPublisher) {
        this.discountService = discountService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public BigDecimal processOrder(Order order) {
        log.info("Processing order {} for customer {}", order.getId(), order.getCustomer().getName());
        BigDecimal discount = discountService.applyDiscount(order);

        if (discount.compareTo(HIGH_DISCOUNT) > 0) {
            //publish the event
            applicationEventPublisher.publishEvent(new HighValueDiscountEvent(this, order, discount));
            log.info("Publishing event!");
        }

        BigDecimal final_price = order.getValue().subtract(discount);
        log.info("Original price was {}. Final price is {}", order.getValue(), final_price);
        return final_price;
    }
}

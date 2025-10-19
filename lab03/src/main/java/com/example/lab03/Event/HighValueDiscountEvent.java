package com.example.lab03.Event;

import com.example.lab03.Discount.DiscountService;
import com.example.lab03.DomainModel.Order;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

public class HighValueDiscountEvent extends ApplicationEvent{
    private final Order order;
    private final BigDecimal discount;

    public HighValueDiscountEvent(Object source, Order order, BigDecimal discount) {
        super(source);
        this.order = order;
        this.discount = discount;
    }

    public Order getOrder() {
        return order;
    }
    public BigDecimal getDiscount() {
        return discount;
    }
}

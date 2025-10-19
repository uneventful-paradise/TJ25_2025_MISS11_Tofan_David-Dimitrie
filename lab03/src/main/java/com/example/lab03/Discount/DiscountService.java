package com.example.lab03.Discount;

import com.example.lab03.DomainModel.Order;

import java.math.BigDecimal;

public interface DiscountService {
    BigDecimal applyDiscount(Order order);
}

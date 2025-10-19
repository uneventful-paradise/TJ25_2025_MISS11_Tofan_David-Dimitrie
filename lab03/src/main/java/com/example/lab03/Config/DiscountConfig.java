package com.example.lab03.Config;

import com.example.lab03.Discount.DiscountService;
import com.example.lab03.Service.FixedAmountDiscountService;
import com.example.lab03.Service.NoDiscountService;
import com.example.lab03.Service.PercentageDiscountService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscountConfig {

    @Bean
    @ConditionalOnProperty(name = "discount.strategy", havingValue = "percentage")
    public DiscountService percentageDiscountService() {

        return new PercentageDiscountService();
    }

    @Bean
    @ConditionalOnProperty(name = "discount.strategy", havingValue = "fixed")
    public DiscountService fixedDiscountService() {
        return new FixedAmountDiscountService();
    }

    @Bean
    @ConditionalOnProperty(name = "discount.strategy", havingValue = "none", matchIfMissing = true)
    public DiscountService noDiscountService() {
        return new NoDiscountService();
    }
}

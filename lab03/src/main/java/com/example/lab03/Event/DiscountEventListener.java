package com.example.lab03.Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DiscountEventListener {
    private static final Logger log = LoggerFactory.getLogger(DiscountEventListener.class.getName());

    @EventListener
    public void handleHighValueDiscount(HighValueDiscountEvent event) {
        log.warn("High value discount occurred! Customer {}, order {}, discount {}",
                event.getOrder().getCustomer().getName(), event.getOrder().getId(), event.getDiscount());
    }
}

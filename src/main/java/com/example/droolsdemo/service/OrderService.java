package com.example.droolsdemo.service;

import com.example.droolsdemo.model.Order;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final KieSession kieSession;

    @Autowired
    public OrderService(KieSession kieSession) {
        this.kieSession = kieSession;
    }

    public Order processOrder(Order order) {
        kieSession.insert(order);
        kieSession.fireAllRules();
        kieSession.dispose();
        return order;
    }
}
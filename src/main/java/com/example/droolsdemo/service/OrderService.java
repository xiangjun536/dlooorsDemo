package com.example.droolsdemo.service;

import com.example.droolsdemo.model.Order;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final KieContainer kieContainer;

    @Autowired
    public OrderService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public Order processOrder(Order order) {
        // 为每次操作创建新的KieSession实例
        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.insert(order);
            kieSession.fireAllRules();
            return order;
        } finally {
            // 在finally块中确保session被释放
            kieSession.dispose();
        }
    }
}
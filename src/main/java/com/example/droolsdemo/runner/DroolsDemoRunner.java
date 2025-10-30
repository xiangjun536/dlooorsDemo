package com.example.droolsdemo.runner;

import com.example.droolsdemo.model.Order;
import com.example.droolsdemo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DroolsDemoRunner implements ApplicationRunner {

    private final OrderService orderService;

    @Autowired
    public DroolsDemoRunner(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("===== Drools Rule Engine Demo =====");
        
        // Test VIP customer with large order
        Order vipOrder = new Order("VIP", 1500.0);
        System.out.println("Original VIP order: " + vipOrder);
        Order processedVipOrder = orderService.processOrder(vipOrder);
        System.out.println("Processed VIP order: " + processedVipOrder);
        System.out.println();
        
        // Test regular customer with medium order
        Order regularOrder = new Order("Regular", 800.0);
        System.out.println("Original regular order: " + regularOrder);
        Order processedRegularOrder = orderService.processOrder(regularOrder);
        System.out.println("Processed regular order: " + processedRegularOrder);
        System.out.println();
        
        // Test small order
        Order smallOrder = new Order("Regular", 300.0);
        System.out.println("Original small order: " + smallOrder);
        Order processedSmallOrder = orderService.processOrder(smallOrder);
        System.out.println("Processed small order: " + processedSmallOrder);
        
        System.out.println("===== Demo Completed =====");
    }
}
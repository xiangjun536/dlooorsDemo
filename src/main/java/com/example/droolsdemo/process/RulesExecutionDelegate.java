package com.example.droolsdemo.process;

import com.example.droolsdemo.model.Order;
import com.example.droolsdemo.service.OrderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Camunda服务任务处理器，用于在流程中执行Drools规则
 */
@Component("rulesExecutionDelegate")
public class RulesExecutionDelegate implements JavaDelegate {

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // 从流程变量中获取订单信息
        String orderId = (String) execution.getVariable("orderId");
        Double orderAmount = (Double) execution.getVariable("orderAmount");
        Boolean isVip = (Boolean) execution.getVariable("isVip");
        
        System.out.println("执行规则 - 订单ID: " + orderId + ", 订单金额: " + orderAmount + ", 是否VIP: " + isVip);
        
        // 创建订单对象（使用现有的Order类结构）
        String customerType = isVip ? "VIP" : "Regular";
        Order order = new Order(customerType, orderAmount);
        
        // 执行规则
        Order processedOrder = orderService.processOrder(order);
        
        // 将计算后的折扣设置回流程变量
        execution.setVariable("discount", processedOrder.getDiscount());
        System.out.println("规则执行完成 - 折扣: " + processedOrder.getDiscount());
    }
}
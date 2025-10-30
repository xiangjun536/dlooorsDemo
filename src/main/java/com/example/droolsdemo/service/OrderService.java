package com.example.droolsdemo.service;

import com.example.droolsdemo.model.Order;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    
    /**
     * 根据指定的规则名称处理订单
     * @param order 订单对象
     * @param ruleNames 要执行的规则名称集合
     * @return 处理后的订单
     */
    public Order processOrderWithRules(Order order, Set<String> ruleNames) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.insert(order);
            // 创建规则过滤器，只执行指定的规则
            AgendaFilter filter = new AgendaFilter() {
                @Override
                public boolean accept(Match match) {
                    return ruleNames.contains(match.getRule().getName());
                }
            };
            kieSession.fireAllRules(filter);
            return order;
        } finally {
            kieSession.dispose();
        }
    }
    
    /**
     * 根据指定的规则名称处理订单（字符串数组版本）
     * @param order 订单对象
     * @param ruleNames 要执行的规则名称数组
     * @return 处理后的订单
     */
    public Order processOrderWithRules(Order order, String... ruleNames) {
        Set<String> ruleNameSet = Arrays.stream(ruleNames)
                .collect(Collectors.toCollection(HashSet::new));
        return processOrderWithRules(order, ruleNameSet);
    }
}
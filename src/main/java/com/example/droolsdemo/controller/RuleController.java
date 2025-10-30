package com.example.droolsdemo.controller;

import com.example.droolsdemo.config.DroolsConfig;
import com.example.droolsdemo.model.Order;
import com.example.droolsdemo.service.OrderService;
import com.example.droolsdemo.service.RuleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    private final RuleManagementService ruleManagementService;
    private final OrderService orderService;

    @Autowired
    public RuleController(RuleManagementService ruleManagementService, OrderService orderService) {
        this.ruleManagementService = ruleManagementService;
        this.orderService = orderService;
    }

    /**
     * 获取所有规则名称
     */
    @GetMapping
    public Set<String> getAllRules() {
        return ruleManagementService.getAllRuleNames();
    }

    /**
     * 获取指定规则内容
     */
    @GetMapping("/{ruleName}")
    public String getRule(@PathVariable String ruleName) {
        return ruleManagementService.getRuleContent(ruleName);
    }

    /**
     * 添加或更新规则
     */
    @PostMapping
    public String addOrUpdateRule(@RequestParam String ruleName, @RequestBody String ruleContent) {
        ruleManagementService.addOrUpdateRule(ruleName, ruleContent);
        return "Rule added or updated successfully: " + ruleName;
    }

    /**
     * 删除规则
     */
    @DeleteMapping("/{ruleName}")
    public String deleteRule(@PathVariable String ruleName) {
        boolean deleted = ruleManagementService.deleteRule(ruleName);
        return deleted ? "Rule deleted successfully: " + ruleName : "Rule not found: " + ruleName;
    }

    /**
     * 重新加载规则
     */
    @PostMapping("/reload")
    public String reloadRules() {
        ruleManagementService.reloadRules();
        return "Rules reloaded successfully";
    }

    /**
     * 执行指定规则处理订单
     */
    @PostMapping("/execute")
    public Order executeRules(@RequestBody Order order, @RequestParam(required = false) Set<String> ruleNames) {
        if (ruleNames != null && !ruleNames.isEmpty()) {
            return orderService.processOrderWithRules(order, ruleNames);
        } else {
            return orderService.processOrder(order);
        }
    }

    /**
     * 处理订单（使用所有规则）
     */
    @PostMapping("/process-order")
    public Order processOrder(@RequestBody Order order) {
        return orderService.processOrder(order);
    }

    /**
     * 初始化默认规则（直接定义）
     */
    @PostMapping("/init-default")
    public String initDefaultRules() {
        // 直接定义并添加默认规则
        String vipRule = "package com.example.rules\n" +
                        "import com.example.droolsdemo.model.Order;\n\n" +
                        "rule \"VIP Customer Discount\"\n" +
                        "    when\n" +
                        "        $order: Order(customerType == \"VIP\", amount >= 1000)\n" +
                        "    then\n" +
                        "        $order.setDiscount($order.getAmount() * 0.2);\n" +
                        "        System.out.println(\"Applied VIP discount: \" + $order.getDiscount());\n" +
                        "end";
        
        String regularRule = "package com.example.rules\n" +
                           "import com.example.droolsdemo.model.Order;\n\n" +
                           "rule \"Regular Customer Discount\"\n" +
                           "    when\n" +
                           "        $order: Order(customerType == \"Regular\", amount >= 1000)\n" +
                           "    then\n" +
                           "        $order.setDiscount($order.getAmount() * 0.1);\n" +
                           "        System.out.println(\"Applied regular discount: \" + $order.getDiscount());\n" +
                           "end";
        
        String smallOrderRule = "package com.example.rules\n" +
                               "import com.example.droolsdemo.model.Order;\n\n" +
                               "rule \"Small Order No Discount\"\n" +
                               "    when\n" +
                               "        $order: Order(amount < 1000)\n" +
                               "    then\n" +
                               "        $order.setDiscount(0);\n" +
                               "        System.out.println(\"No discount for small order\");\n" +
                               "end";
        
        ruleManagementService.addOrUpdateRule("VIP Customer Discount", vipRule);
        ruleManagementService.addOrUpdateRule("Regular Customer Discount", regularRule);
        ruleManagementService.addOrUpdateRule("Small Order No Discount", smallOrderRule);
        
        return "Default rules initialized successfully";
    }
}
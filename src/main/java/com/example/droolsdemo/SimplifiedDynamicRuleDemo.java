package com.example.droolsdemo;

import com.example.droolsdemo.model.Order;
import com.example.droolsdemo.service.OrderService;
import com.example.droolsdemo.service.RuleManagementService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
/**
 * 简化的动态规则演示运行器，用于直接测试动态规则管理功能
 */
public class SimplifiedDynamicRuleDemo {

    public static void main(String[] args) {
        // 启动Spring Boot应用程序获取上下文
        ApplicationContext context = SpringApplication.run(CamundaDroolsDemoApplication.class, "--spring.main.web-application-type=none");
        
        // 获取必要的服务
        OrderService orderService = context.getBean(OrderService.class);
        RuleManagementService ruleManagementService = context.getBean(RuleManagementService.class);
        
        System.out.println("=== 简化版动态规则管理演示 ===");
        
        // 初始化默认规则
        initializeDefaultRules(ruleManagementService);
        
        // 测试规则执行
        testRuleExecution(orderService);
        
        // 测试动态添加规则
        addNewRule(ruleManagementService);
        
        // 测试动态修改规则
        updateExistingRule(ruleManagementService);
        
        // 测试按名称执行特定规则
        testExecuteSpecificRules(orderService);
        
        // 关闭应用程序
        SpringApplication.exit(context);
    }
    
    private static void initializeDefaultRules(RuleManagementService ruleManagementService) {
        System.out.println("\n1. 初始化默认规则...");
        
        // VIP客户折扣规则（使用唯一规则名称）
        String vipRule = "package com.example.rules\n" +
                "import com.example.droolsdemo.model.Order\n" +
                "\n" +
                "rule \"VIP Customer Discount Demo\"\n" +
                "    when\n" +
                "        $order: Order(customerType == \"VIP\")\n" +
                "    then\n" +
                "        $order.setDiscount(0.15);\n" +
                "        System.out.println(\"应用VIP客户15%折扣\");\n" +
                "end";
        
        // 普通客户折扣规则（使用唯一规则名称）
        String normalRule = "package com.example.rules\n" +
                "import com.example.droolsdemo.model.Order\n" +
                "\n" +
                "rule \"Normal Customer Discount Demo\"\n" +
                "    when\n" +
                "        $order: Order(customerType == \"NORMAL\" && amount > 1000)\n" +
                "    then\n" +
                "        $order.setDiscount(0.05);\n" +
                "        System.out.println(\"应用普通客户5%折扣\");\n" +
                "end";
        
        // 小额订单折扣规则（使用唯一规则名称）
        String smallOrderRule = "package com.example.rules\n" +
                "import com.example.droolsdemo.model.Order\n" +
                "\n" +
                "rule \"Small Order Discount Demo\"\n" +
                "    when\n" +
                "        $order: Order(amount < 500)\n" +
                "    then\n" +
                "        $order.setDiscount(0.02);\n" +
                "        System.out.println(\"应用小额订单2%折扣\");\n" +
                "end";
        
        ruleManagementService.addOrUpdateRule("VIP_Customer_Discount.drl", vipRule);
        ruleManagementService.addOrUpdateRule("Normal_Customer_Discount.drl", normalRule);
        ruleManagementService.addOrUpdateRule("Small_Order_Discount.drl", smallOrderRule);
        
        // 重新加载规则
        ruleManagementService.reloadRules();
        
        System.out.println("已添加3条默认规则:");
        ruleManagementService.getAllRuleNames().forEach(System.out::println);
    }
    
    private static void testRuleExecution(OrderService orderService) {
        System.out.println("\n2. 测试规则执行...");
        
        // 测试VIP客户订单
        Order vipOrder = new Order();
        vipOrder.setCustomerType("VIP");
        vipOrder.setAmount(2000.0);
        orderService.processOrder(vipOrder);
        System.out.println("VIP订单处理结果 - 折扣率: " + vipOrder.getDiscount());
        
        // 测试普通客户大额订单
        Order normalLargeOrder = new Order();
        normalLargeOrder.setCustomerType("NORMAL");
        normalLargeOrder.setAmount(1500.0);
        orderService.processOrder(normalLargeOrder);
        System.out.println("普通大额订单处理结果 - 折扣率: " + normalLargeOrder.getDiscount());
        
        // 测试小额订单
        Order smallOrder = new Order();
        smallOrder.setCustomerType("NORMAL");
        smallOrder.setAmount(300.0);
        orderService.processOrder(smallOrder);
        System.out.println("小额订单处理结果 - 折扣率: " + smallOrder.getDiscount());
    }
    
    private static void addNewRule(RuleManagementService ruleManagementService) {
        System.out.println("\n3. 动态添加新规则...");
        
        // 添加特别促销规则（使用唯一规则名称）
        String promotionRule = "package com.example.rules\n" +
                "import com.example.droolsdemo.model.Order\n" +
                "\n" +
                "rule \"Special Promotion Discount Demo\"\n" +
                "    when\n" +
                "        $order: Order(amount > 3000)\n" +
                "    then\n" +
                "        $order.setDiscount(0.20);\n" +
                "        System.out.println(\"应用特别促销20%折扣\");\n" +
                "end";
        
        ruleManagementService.addOrUpdateRule("Special_Promotion_Discount.drl", promotionRule);
        ruleManagementService.reloadRules();
        
        System.out.println("添加新规则后，所有规则:");
        ruleManagementService.getAllRuleNames().forEach(System.out::println);
    }
    
    private static void updateExistingRule(RuleManagementService ruleManagementService) {
        System.out.println("\n4. 动态修改现有规则...");
        
        // 修改VIP客户折扣规则，提高折扣率
        String updatedVipRule = "package com.example.rules\n" +
                "import com.example.droolsdemo.model.Order\n" +
                "\n" +
                "rule \"VIP Customer Discount Demo\"\n" +
                "    when\n" +
                "        $order: Order(customerType == \"VIP\")\n" +
                "    then\n" +
                "        $order.setDiscount(0.25);\n" +
                "        System.out.println(\"应用VIP客户25%折扣(已更新)\");\n" +
                "end";
        
        ruleManagementService.addOrUpdateRule("VIP_Customer_Discount.drl", updatedVipRule);
        ruleManagementService.reloadRules();
        
        System.out.println("已更新VIP客户折扣规则");
    }
    
    private static void testExecuteSpecificRules(OrderService orderService) {
        System.out.println("\n5. 测试按名称执行特定规则...");
        
        // 测试只执行VIP规则
        Order vipOrder = new Order();
        vipOrder.setCustomerType("VIP");
        vipOrder.setAmount(2000.0);
        orderService.processOrderWithRules(vipOrder, "VIP_Customer_Discount.drl");
        System.out.println("只执行VIP规则 - 折扣率: " + vipOrder.getDiscount());
        
        // 测试只执行小额订单规则
        Order smallOrder = new Order();
        smallOrder.setCustomerType("NORMAL");
        smallOrder.setAmount(300.0);
        orderService.processOrderWithRules(smallOrder, "Small_Order_Discount.drl");
        System.out.println("只执行小额订单规则 - 折扣率: " + smallOrder.getDiscount());
        
        // 测试执行多个规则
        Order largeOrder = new Order();
        largeOrder.setCustomerType("VIP");
        largeOrder.setAmount(4000.0);
        orderService.processOrderWithRules(largeOrder, new String[]{
            "VIP_Customer_Discount.drl", 
            "Special_Promotion_Discount.drl"
        });
        System.out.println("执行多个规则 - 折扣率: " + largeOrder.getDiscount());
    }
}
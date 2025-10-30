package com.example.droolsdemo.runner;

import com.example.droolsdemo.config.DroolsConfig;
import com.example.droolsdemo.model.Order;
import com.example.droolsdemo.service.OrderService;
import com.example.droolsdemo.service.RuleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DynamicRuleDemoRunner implements ApplicationRunner {

    private final RuleManagementService ruleManagementService;
    private final OrderService orderService;

    @Autowired
    public DynamicRuleDemoRunner(RuleManagementService ruleManagementService, OrderService orderService) {
        this.ruleManagementService = ruleManagementService;
        this.orderService = orderService;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("\n===== Dynamic Rule Management Demo =====");
        
        // 1. 初始化默认规则
        initializeDefaultRules();
        
        // 2. 演示基本规则执行
        System.out.println("\n--- Testing Default Rules ---");
        Order testOrder = new Order("VIP", 1200.0);
        System.out.println("Original order: " + testOrder);
        Order processedOrder = orderService.processOrder(testOrder);
        System.out.println("After processing with all rules: " + processedOrder);
        
        // 3. 演示选择特定规则执行
        System.out.println("\n--- Testing Specific Rule Execution ---");
        Order specificRuleOrder = new Order("Regular", 600.0);
        System.out.println("Original order: " + specificRuleOrder);
        
        // 只执行常规客户折扣规则
        Set<String> specificRules = new HashSet<>();
        specificRules.add("Regular Customer Discount");
        Order processedSpecificOrder = orderService.processOrderWithRules(specificRuleOrder, specificRules);
        System.out.println("After processing with specific rule: " + processedSpecificOrder);
        
        // 4. 添加新规则
        System.out.println("\n--- Adding New Rule ---");
        String newRuleContent = "package com.example.rules\n" +
                "import com.example.droolsdemo.model.Order;\n" +
                "\n" +
                "rule \"Special Promotion Discount\"\n" +
                "    when\n" +
                "        $order: Order(amount >= 2000)\n" +
                "    then\n" +
                "        $order.setDiscount($order.getAmount() * 0.3); // 30% discount for orders >= 2000\n" +
                "        System.out.println(\"Applied special promotion discount: \" + $order.getDiscount());\n" +
                "end";
        ruleManagementService.addOrUpdateRule("Special Promotion Discount", newRuleContent);
        
        // 测试新规则
        Order largeOrder = new Order("Regular", 2500.0);
        System.out.println("Large order: " + largeOrder);
        Order processedLargeOrder = orderService.processOrder(largeOrder);
        System.out.println("After processing with new rule: " + processedLargeOrder);
        
        // 5. 修改现有规则
        System.out.println("\n--- Modifying Existing Rule ---");
        String modifiedRuleContent = "package com.example.rules\n" +
                "import com.example.droolsdemo.model.Order;\n" +
                "\n" +
                "rule \"VIP Customer Discount\"\n" +
                "    when\n" +
                "        $order: Order(customerType == \"VIP\", amount >= 1000)\n" +
                "    then\n" +
                "        $order.setDiscount($order.getAmount() * 0.25); // Increased to 25% discount for VIPs\n" +
                "        System.out.println(\"Applied enhanced VIP discount: \" + $order.getDiscount());\n" +
                "end";
        ruleManagementService.addOrUpdateRule("VIP Customer Discount", modifiedRuleContent);
        
        // 测试修改后的规则
        Order vipOrder = new Order("VIP", 1500.0);
        System.out.println("VIP order: " + vipOrder);
        Order processedVipOrder = orderService.processOrder(vipOrder);
        System.out.println("After processing with modified VIP rule: " + processedVipOrder);
        
        // 6. 测试根据参数选择规则执行
        System.out.println("\n--- Testing Rule Selection by Parameter ---");
        Order parameterTestOrder = new Order("VIP", 2500.0);
        System.out.println("Original VIP large order: " + parameterTestOrder);
        
        // 只执行VIP规则
        Set<String> vipRules = new HashSet<>();
        vipRules.add("VIP Customer Discount");
        Order vipOnlyOrder = orderService.processOrderWithRules(parameterTestOrder, vipRules);
        System.out.println("After processing with only VIP rule: " + vipOnlyOrder);
        
        // 只执行特殊促销规则
        Order promotionTestOrder = new Order("VIP", 2500.0);
        Set<String> promotionRules = new HashSet<>();
        promotionRules.add("Special Promotion Discount");
        Order promotionOnlyOrder = orderService.processOrderWithRules(promotionTestOrder, promotionRules);
        System.out.println("After processing with only promotion rule: " + promotionOnlyOrder);
        
        System.out.println("\n===== Dynamic Rule Management Demo Completed =====");
    }

    private void initializeDefaultRules() {
        try {
            // 直接定义默认规则内容
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
            
            System.out.println("Default rules initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize default rules: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String extractRule(String fullContent, String ruleName) {
        String ruleStart = "rule \"" + ruleName + "\"";
        String ruleEnd = "end";
        
        int startIndex = fullContent.indexOf(ruleStart);
        if (startIndex == -1) return null;
        
        int endIndex = fullContent.indexOf(ruleEnd, startIndex) + ruleEnd.length();
        if (endIndex == -1) return null;
        
        // 添加必要的package和import语句
        String packageLine = "package com.example.rules\n" +
                            "import com.example.droolsdemo.model.Order;\n\n";
        
        return packageLine + fullContent.substring(startIndex, endIndex);
    }
}
package com.example.droolsdemo;

import com.example.droolsdemo.process.ProcessService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 集成Camunda和Drools的演示应用类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.droolsdemo"})
public class CamundaDroolsDemoApplication {

    public static void main(String[] args) {
        // 启动Spring应用
        ConfigurableApplicationContext context = SpringApplication.run(CamundaDroolsDemoApplication.class, args);
        
        // 获取ProcessService实例
        ProcessService processService = context.getBean(ProcessService.class);
        
        System.out.println("\n===== Camunda与Drools集成演示 =====\n");
        System.out.println("注意: 由于自动部署被禁用，流程未自动部署。请手动部署流程后再使用。");
        System.out.println("您可以通过以下方式继续使用：");
        System.out.println("1. 访问 http://localhost:8080/camunda 进入Camunda管理控制台");
        System.out.println("2. 使用REST API管理流程和规则");
        
        /* 暂时注释掉流程启动代码，因为自动部署被禁用
        try {
            // 测试VIP客户订单处理流程
            System.out.println("1. 测试VIP客户订单处理流程（订单金额：1000元）");
            String vipProcessInstanceId = processService.startOrderProcess(1000.0, true);
            
            // 完成第一个任务（检查订单信息）
            System.out.println("\n2. 完成检查订单信息任务");
            Task vipTask1 = processService.getTaskByProcessInstanceId(vipProcessInstanceId);
            if (vipTask1 != null) {
                System.out.println("   - 当前任务: " + vipTask1.getName());
                processService.completeTask(vipTask1.getId(), new HashMap<>());
            }
            
            // 等待规则执行完成并检查下一个任务
            Thread.sleep(1000);
            Task vipTask2 = processService.getTaskByProcessInstanceId(vipProcessInstanceId);
            if (vipTask2 != null) {
                System.out.println("\n3. 规则执行完成，进入支付处理阶段");
                System.out.println("   - 当前任务: " + vipTask2.getName());
                
                // 完成支付处理任务
                System.out.println("\n4. 完成支付处理任务");
                Map<String, Object> paymentVariables = new HashMap<>();
                paymentVariables.put("paymentSuccessful", true);
                processService.completeTask(vipTask2.getId(), paymentVariables);
                
                // 等待进入下一阶段
                Thread.sleep(1000);
                Task vipTask3 = processService.getTaskByProcessInstanceId(vipProcessInstanceId);
                if (vipTask3 != null) {
                    System.out.println("\n5. 进入发货处理阶段");
                    System.out.println("   - 当前任务: " + vipTask3.getName());
                    
                    // 完成发货处理任务，结束流程
                    System.out.println("\n6. 完成发货处理任务，流程结束");
                    processService.completeTask(vipTask3.getId(), new HashMap<>());
                }
            }
            
            // 测试普通客户订单处理流程
            System.out.println("\n\n===== 测试普通客户订单处理流程 =====\n");
            System.out.println("1. 测试普通客户订单处理流程（订单金额：1200元）");
            String regularProcessInstanceId = processService.startOrderProcess(1200.0, false);
            
            // 完成第一个任务（检查订单信息）
            System.out.println("\n2. 完成检查订单信息任务");
            Task regularTask1 = processService.getTaskByProcessInstanceId(regularProcessInstanceId);
            if (regularTask1 != null) {
                System.out.println("   - 当前任务: " + regularTask1.getName());
                processService.completeTask(regularTask1.getId(), new HashMap<>());
            }
            
            // 等待规则执行完成并检查下一个任务
            Thread.sleep(1000);
            Task regularTask2 = processService.getTaskByProcessInstanceId(regularProcessInstanceId);
            if (regularTask2 != null) {
                System.out.println("\n3. 规则执行完成，进入支付处理阶段");
                System.out.println("   - 当前任务: " + regularTask2.getName());
            }
            
            System.out.println("\n\n===== 演示完成 =====");
            System.out.println("Camunda与Drools集成已成功！您可以通过以下方式继续使用：");
            System.out.println("1. 访问 http://localhost:8080/camunda 进入Camunda管理控制台");
            System.out.println("2. 使用REST API管理流程和规则");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("演示过程中出现错误: " + e.getMessage());
        }
        */
    }
}
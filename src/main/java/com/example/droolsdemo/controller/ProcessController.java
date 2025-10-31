package com.example.droolsdemo.controller;

import com.example.droolsdemo.process.ProcessService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程控制器，提供REST API来管理Camunda流程
 */
@RestController
@RequestMapping("/api/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    /**
     * 启动新的订单处理流程
     */
    @PostMapping("/order/start")
    public ResponseEntity<Map<String, String>> startOrderProcess(
            @RequestParam double orderAmount,
            @RequestParam boolean isVip) {
        
        String processInstanceId = processService.startOrderProcess(orderAmount, isVip);
        
        Map<String, String> response = new HashMap<>();
        response.put("processInstanceId", processInstanceId);
        response.put("status", "success");
        response.put("message", "订单处理流程已启动");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取所有待处理任务
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = processService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * 完成任务
     */
    @PostMapping("/task/{taskId}/complete")
    public ResponseEntity<Map<String, String>> completeTask(
            @PathVariable String taskId,
            @RequestBody(required = false) Map<String, Object> variables) {
        
        if (variables == null) {
            variables = new HashMap<>();
        }
        
        processService.completeTask(taskId, variables);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "任务已完成");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 根据流程实例ID获取任务
     */
    @GetMapping("/task/process/{processInstanceId}")
    public ResponseEntity<Task> getTaskByProcessInstanceId(@PathVariable String processInstanceId) {
        Task task = processService.getTaskByProcessInstanceId(processInstanceId);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 测试流程和规则集成
     */
    @PostMapping("/test/integration")
    public ResponseEntity<Map<String, Object>> testIntegration() {
        // 启动一个测试流程
        String processInstanceId = processService.startOrderProcess(1000.0, true); // VIP客户，订单金额1000元
        
        // 获取第一个任务（检查订单信息）
        Task task = processService.getTaskByProcessInstanceId(processInstanceId);
        
        if (task != null) {
            // 完成第一个任务
            processService.completeTask(task.getId(), new HashMap<>());
            
            // 获取下一个任务（应该是处理支付）
            Task nextTask = processService.getTaskByProcessInstanceId(processInstanceId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("processInstanceId", processInstanceId);
            response.put("firstTask", task.getName());
            response.put("nextTask", nextTask != null ? nextTask.getName() : "流程已结束");
            response.put("status", "success");
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
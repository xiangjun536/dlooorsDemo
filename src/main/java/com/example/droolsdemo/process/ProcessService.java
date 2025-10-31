package com.example.droolsdemo.process;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 流程服务类，用于管理Camunda流程
 */
@Service
public class ProcessService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 启动订单处理流程
     */
    public String startOrderProcess(double orderAmount, boolean isVip) {
        // 生成唯一订单ID
        String orderId = "ORDER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // 设置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", orderId);
        variables.put("orderAmount", orderAmount);
        variables.put("isVip", isVip);
        
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("OrderProcess", variables);
        
        System.out.println("流程已启动 - 流程实例ID: " + processInstance.getId() + ", 订单ID: " + orderId);
        return processInstance.getId();
    }

    /**
     * 完成用户任务
     */
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
        System.out.println("任务已完成 - 任务ID: " + taskId);
    }

    /**
     * 获取所有待处理任务
     */
    public List<Task> getAllTasks() {
        return taskService.createTaskQuery().list();
    }

    /**
     * 根据流程实例ID获取任务
     */
    public Task getTaskByProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    }

    /**
     * 根据流程变量查询任务
     */
    public List<Task> getTasksByVariable(String variableName, Object variableValue) {
        return taskService.createTaskQuery().processVariableValueEquals(variableName, variableValue).list();
    }
}
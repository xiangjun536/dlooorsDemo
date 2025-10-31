package com.example.droolsdemo.service;

import org.camunda.bpm.engine.delegate.JavaDelegate;
import java.util.Map;
import java.util.Set;

public interface RuleManagementService extends JavaDelegate {
    /**
     * 添加或更新规则
     * @param ruleName 规则名称
     * @param ruleContent 规则内容
     */
    void addOrUpdateRule(String ruleName, String ruleContent);
    
    /**
     * 删除规则
     * @param ruleName 规则名称
     * @return 是否删除成功
     */
    boolean deleteRule(String ruleName);
    
    /**
     * 获取所有规则名称
     * @return 规则名称集合
     */
    Set<String> getAllRuleNames();
    
    /**
     * 获取特定规则内容
     * @param ruleName 规则名称
     * @return 规则内容，如果规则不存在则返回null
     */
    String getRuleContent(String ruleName);
    
    /**
     * 重新加载规则
     */
    void reloadRules();
}
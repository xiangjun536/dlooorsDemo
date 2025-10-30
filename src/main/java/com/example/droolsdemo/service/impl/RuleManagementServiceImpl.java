package com.example.droolsdemo.service.impl;

import com.example.droolsdemo.service.RuleManagementService;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class RuleManagementServiceImpl implements RuleManagementService {

    private static final Logger logger = LoggerFactory.getLogger(RuleManagementServiceImpl.class);
    private final KieServices kieServices;
    private final KieContainer kieContainer;
    private final Map<String, String> ruleStore = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    public RuleManagementServiceImpl(KieContainer kieContainer) {
        this.kieServices = KieServices.Factory.get();
        this.kieContainer = kieContainer;
    }
    
    @PostConstruct
    public void init() {
        // 初始化时不加载现有规则文件，避免与动态添加的规则冲突
        // 规则将在应用程序启动后通过API或演示类动态添加
        logger.info("Rule management service initialized");
    }

    @Override
    public void addOrUpdateRule(String ruleName, String ruleContent) {
        logger.info("Adding or updating rule: {}", ruleName);
        lock.writeLock().lock();
        try {
            ruleStore.put(ruleName, ruleContent);
            reloadRules();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deleteRule(String ruleName) {
        logger.info("Deleting rule: {}", ruleName);
        lock.writeLock().lock();
        try {
            boolean removed = ruleStore.remove(ruleName) != null;
            if (removed) {
                reloadRules();
            }
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Set<String> getAllRuleNames() {
        lock.readLock().lock();
        try {
            return new HashSet<>(ruleStore.keySet());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String getRuleContent(String ruleName) {
        lock.readLock().lock();
        try {
            return ruleStore.get(ruleName);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void reloadRules() {
        logger.info("Reloading rules, total rules: {}", ruleStore.size());
        lock.writeLock().lock();
        try {
            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
            ReleaseId releaseId = kieServices.newReleaseId("com.example", "drools-demo", "1.0.0-SNAPSHOT");
            kieFileSystem.generateAndWritePomXML(releaseId);
            
            // 写入所有规则
            int ruleIndex = 0;
            for (Map.Entry<String, String> entry : ruleStore.entrySet()) {
                String rulePath = "src/main/resources/rules/dynamic_rule_" + ruleIndex + ".drl";
                kieFileSystem.write(rulePath, entry.getValue());
                ruleIndex++;
            }
            
            // 构建规则
            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
            kieBuilder.buildAll();
            
            Results results = kieBuilder.getResults();
            if (results.hasMessages(Message.Level.ERROR)) {
                List<Message> errors = results.getMessages(Message.Level.ERROR);
                StringBuilder errorMessage = new StringBuilder();
                for (Message error : errors) {
                    errorMessage.append("Error: " + error.getText() + "\n");
                }
                throw new RuntimeException("Rule compilation errors: " + errorMessage.toString());
            }
            
            // 更新容器
            KieModule kieModule = kieBuilder.getKieModule();
            kieContainer.updateToVersion(releaseId);
            
            logger.info("Rules reloaded successfully. Total rules: {}", ruleStore.size());
        } catch (Exception e) {
            logger.error("Failed to reload rules: {}", e.getMessage());
            throw new RuntimeException("Failed to reload rules", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
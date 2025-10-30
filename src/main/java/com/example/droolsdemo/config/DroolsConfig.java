package com.example.droolsdemo.config;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private final KieServices kieServices = KieServices.Factory.get();

    @Bean
    public KieContainer kieContainer() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        try {
            // 不生成pom.xml，直接构建
            
            // 不加载现有的规则文件，避免与动态添加的规则冲突
            // 规则将通过RuleManagementService动态添加
            
            KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
            kb.buildAll();
            
            if (kb.getResults().hasMessages(Message.Level.ERROR)) {
                throw new RuntimeException("Build errors: " + kb.getResults().toString());
            }
            
            KieModule kieModule = kb.getKieModule();
            KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
            return kieContainer;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build KieContainer", e);
        }
    }

    @Bean
    public KieBase kieBase(KieContainer kieContainer) {
        return kieContainer.getKieBase();
    }
    
    // 动态规则将通过RuleManagementService管理

}
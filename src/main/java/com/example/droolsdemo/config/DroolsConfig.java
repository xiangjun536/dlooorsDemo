package com.example.droolsdemo.config;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private final KieServices kieServices = KieServices.Factory.get();

    @Bean
    public KieContainer kieContainer() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        try {
            kieFileSystem.write(ResourceFactory.newClassPathResource("rules/sample.drl"));
            KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
            kb.buildAll();
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


}
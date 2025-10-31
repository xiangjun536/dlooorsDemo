package com.example.droolsdemo.config;

import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultProcessEngineConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Camunda流程引擎配置类
 */
@Configuration
public class CamundaConfig extends DefaultProcessEngineConfiguration {

    @Override
    public void preInit(SpringProcessEngineConfiguration configuration) {
        super.preInit(configuration);
        // 可以在这里自定义Camunda的配置
        configuration.setJobExecutorActivate(true);
        configuration.setHistory("FULL"); // 完整历史记录
    }
}
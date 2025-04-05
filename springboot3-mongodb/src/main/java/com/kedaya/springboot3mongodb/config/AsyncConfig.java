package com.kedaya.springboot3mongodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author：CHENWEI
 * @Package：com.kedaya.springboot3mongodb.config
 * @Project：springboot3-db
 * @name：AsyncConfig
 * @Date：2025-04-05 15:43
 * @Filename：AsyncConfig
 */
@Configuration
public class AsyncConfig implements AsyncConfigurer, WebMvcConfigurer {

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 设置异步请求超时时间，对大文件下载很重要
        configurer.setDefaultTimeout(3600000); // 1小时

        // 配置适合文件传输的任务执行器
        configurer.setTaskExecutor(mvcTaskExecutor());
    }

    @Bean
    public ThreadPoolTaskExecutor mvcTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("mvc-file-executor-");
        return executor;
    }
}

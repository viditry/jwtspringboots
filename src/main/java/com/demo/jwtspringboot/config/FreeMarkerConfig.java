package com.demo.jwtspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Properties;

@Configuration
public class FreeMarkerConfig {

    // Configuration for FreeMarker View Resolver
    @Bean
    public FreeMarkerConfigurer freeMarkerResolver() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:/templates/");
        configurer.setDefaultEncoding("UTF-8");
        Properties settings = new Properties();
        settings.setProperty("default_encoding", "utf-8");
        settings.setProperty("number_format", "0.##");
        configurer.setFreemarkerSettings(settings);
        return configurer;
    }
    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".html");
        viewResolver.setCache(false);   //Set to true during production
        viewResolver.setContentType("text/html;charset=UTF-8");
        return viewResolver;
    }
}

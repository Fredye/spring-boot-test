package com.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.framework.registry.ServiceRegistry;
import com.demo.framework.registry.ServiceRegistryImpl;


@Configuration
@ConfigurationProperties(prefix = "registry")
public class RegistryConfig {
    private String servers;

    @Bean
    public ServiceRegistry serviceRegistry() {
        return new ServiceRegistryImpl(servers);
    }


    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }
}

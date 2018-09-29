package com.demo.framework.registry;

public interface ServiceRegistry {

    /**
     * 注册服务信息
     * @param serviceName 服务名称
     * @param serviceAddress 注册服务的地址
     */
    void register(String serviceName,String serviceAddress);
}
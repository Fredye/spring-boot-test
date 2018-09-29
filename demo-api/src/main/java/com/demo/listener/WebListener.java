package com.demo.listener;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import com.demo.framework.registry.ServiceRegistry;

/**
 * 监听服务启动事件,注册服务到zookeeper
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 17/3/1
 * Time: 下午3:59
 */


@Component
	public class WebListener implements ServletContextListener{
	    private static Logger logger = LoggerFactory
	            .getLogger(WebListener.class);
	    @Value("${server.address}")
	    private String serverAddress;
	    @Value("${server.port}")
	    private int serverPort;
	    @Value("${server.name}")
	    private String serverName;

	    @Autowired
	    private ServiceRegistry serviceRegistry;
	    @Override
	    public void contextDestroyed(ServletContextEvent event) {
	        // TODO Auto-generated method stub

	    }

	    @Override
	    public void contextInitialized(ServletContextEvent event) {
	        //获取请求映射
	        ServletContext servletContext=event.getServletContext();
	        ApplicationContext applicationContext=WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	        RequestMappingHandlerMapping mapping=applicationContext.getBean(RequestMappingHandlerMapping.class);
	        Map<RequestMappingInfo,HandlerMethod> infoMap=mapping.getHandlerMethods();
	        for(RequestMappingInfo info:infoMap.keySet()){
	            String serviceName=info.getName();
	            logger.debug("-----------"+serviceName);
	            if(null!=serviceName){
	                serviceRegistry.register(serviceName,String.format("%s:%d/%s", serverAddress,serverPort,serverName) );
	            }
	        }
	    }

	}


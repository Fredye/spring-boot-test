package com.demo.framework.registry;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.springframework.stereotype.Component;
import com.demo.framework.registry.ServiceRegistry;
import java.util.concurrent.CountDownLatch;

@Component
public class ServiceRegistryImpl implements ServiceRegistry, Watcher {

	private static final int SESSION_TIMEOUT = 5000;
	
    private static final String REGISTRY_PATH = "/registry";

    private CountDownLatch latch = new CountDownLatch(1);
    
    private Logger logger = Logger.getLogger(ServiceRegistryImpl.class);

    private ZooKeeper zk;

    public ServiceRegistryImpl() {
        logger.debug("初始化类");
    }

    public ServiceRegistryImpl(String zkServers) {
        try {
            zk = new ZooKeeper(zkServers, SESSION_TIMEOUT, this);
            latch.await();
            logger.debug("connected to zookeeper");

        } catch (Exception e) {
            logger.error("create zookeeper client failuer", e);
        }
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        String registryPath = REGISTRY_PATH;
        try {
            logger.debug("-zk---------"+zk);
            //创建根节点：持久节点
            if (zk.exists(registryPath, false) == null) {
                zk.create(registryPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                logger.debug("create registry node:{}");
            }
            //创建服务节点：持久节点
            String servicePath=registryPath+"/"+serviceName;
            if(zk.exists(servicePath, false)==null){
                zk.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.debug("create service node :{}"+servicePath);
            }
            //创建地址节点：临时顺序节点
            String addressPath=servicePath+"/address-";
            String addressNode=zk.create(addressPath, serviceAddress.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("create node address:{}=>{}"+addressNode);
        } catch (Exception e) {
            logger.error("create node failure",e);
        }

    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }

    }

}
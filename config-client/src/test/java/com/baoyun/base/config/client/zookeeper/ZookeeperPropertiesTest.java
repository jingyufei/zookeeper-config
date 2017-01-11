package com.baoyun.base.config.client.zookeeper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import com.baoyun.base.config.client.annotation.ZookeeperValue;
import com.baoyun.base.config.client.bean.NodeBean;
import com.baoyun.base.config.client.bean.NodeDataBean;
import com.baoyun.base.config.client.bean.PojoTestBean;
import com.baoyun.base.config.client.bean.RealtimeBean;
import com.baoyun.base.config.client.util.LocalZookeeperServer;
import com.baoyun.base.config.client.util.ZookeeperClient;
import com.baoyun.base.config.client.util.ZookeeperResource;
import com.baoyun.base.config.client.util.ZookeeperServerStart;

@Slf4j
public class ZookeeperPropertiesTest extends TestCase {

    ZookeeperProperties<String> zookeeperProperties = new ZookeeperProperties<String>();
    String zookeeperList = "127.0.0.1:" + ZookeeperServerStart.serverPort;
    String root = "/linker/prop";

    String keyA = "t.a";
    String valueA = "DataA数据";

    @Override
    public void setUp() throws IOException, InterruptedException {

        LocalZookeeperServer.start();

        ZookeeperClient.waitStartUp(zookeeperList);

        // String zookeeperList = "127.0.0.1:" + ZookeeperServerStart.serverPort;

        zookeeperProperties.setZookeeperAddress(zookeeperList);
        zookeeperProperties.setPropertiesPath(root);
        try {
            Thread.sleep(1000);
            final CountDownLatch latch1 = new CountDownLatch(1);
            ZooKeeper zookeeper = new ZooKeeper(zookeeperList, 1000, zookeeperProperties.geneWatcher(latch1));
            for (Field field : PojoTestBean.class.getDeclaredFields()) {
                if (field.isAnnotationPresent(ZookeeperValue.class)) {
                    ZookeeperValue zookeeperValue = field.getAnnotation(ZookeeperValue.class);
                    NodeDataBean nodeData = new NodeDataBean("data" + new Random().nextInt(10000), "描述信息");
                    NodeBean nodeBean = new NodeBean(zookeeperValue.key(), nodeData);
                    ZookeeperResource.writeDataToZookeeper(zookeeper, root, null, nodeBean);
                }
            }
            zookeeper.close();
            zookeeperProperties.afterPropertiesSet();

        } catch (Exception e) {
//            log.error(" exception:{}", e);
        }

    }

    @Override
    public void tearDown() throws IOException, InterruptedException {
        // main.shutdown();
        LocalZookeeperServer.stop();
    }

    public void testGet() {
        PojoTestBean pojo = new PojoTestBean();
        for (Field field : pojo.getClass().getDeclaredFields()) {
            RealtimeBean realtimeBean = new RealtimeBean();
            realtimeBean.setBean(pojo);
            realtimeBean.setBeanName("pojoBean");
            realtimeBean.setField(field);
            if (field.isAnnotationPresent(ZookeeperValue.class)) {
                ZookeeperValue zooValue = field.getAnnotation(ZookeeperValue.class);
                realtimeBean.setKey(zooValue.key());
                zookeeperProperties.registerRealtimeBean(realtimeBean);
            }

        }

        String addrValue = zookeeperProperties.get("test.addr");
        System.out.println(" \t addrValue=" + addrValue);
        TestCase.assertTrue(addrValue.startsWith("data"));
    }

    public void testWatcher() {

        WatchedEvent watchedEvent = new WatchedEvent(EventType.NodeDataChanged, KeeperState.SyncConnected, root + "/" + "test.addr");
        zookeeperProperties.getWatcher().process(watchedEvent);
    }
}

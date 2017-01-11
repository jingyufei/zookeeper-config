package com.baoyun.base.config.client.zookeeper.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

import com.baoyun.base.config.client.bean.NodeBean;
import com.baoyun.base.config.client.bean.NodeDataBean;
import com.baoyun.base.config.client.util.ReflectUtil;
import com.baoyun.base.config.client.util.ZookeeperResource;
import com.baoyun.base.config.client.zookeeper.ZookeeperProperties;
import com.baoyun.base.config.client.zookeeper.ZookeeperValueBeanProcessor;

@Slf4j
public class WorkflowMain {

    static String zookeeperAddress = "127.0.0.1:2181";
    static String propertiesPath = "/mytest/mae/prop";

    static List<NodeBean> valueDataBeanList = new ArrayList<NodeBean>();

    static ZooKeeper zookeeper;

    static ZookeeperValueBeanProcessor zookeeperValueBeanProcessor = new ZookeeperValueBeanProcessor();

    static ZookeeperProperties<String> zookeeperProperties;

    private static void connectZookeeper() throws IOException {
        final CountDownLatch latch = new CountDownLatch(1);
        if (zookeeper == null) {
            zookeeper = new ZooKeeper(zookeeperAddress, 1000 * 3, new Watcher() {

                @Override
                public void process(WatchedEvent event) {
                    boolean connectSuccess = KeeperState.SyncConnected.equals(event.getState());
                    if (connectSuccess) {
                        latch.countDown();
                    }

                }
            });

            System.out.println(" \t zookeeperSessionId=" + zookeeper.getSessionId());
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
//            log.error("connectZookeeper exception:{} ", e);
        }

    }

    public static void setUp() {
        try {
            connectZookeeper();
            System.out.println(" \t zookeeperSessionId=" + zookeeper.getSessionId());
            if (zookeeper != null) {
                Stat rootStat = zookeeper.exists(propertiesPath, false);
                if (rootStat == null) {
                    rootStat = ZookeeperResource.createFullPath(zookeeper, propertiesPath, null);
                    System.out.println(" \t 创建 " + propertiesPath);
                }

                // 写入数据
                NodeDataBean dataInfo = new NodeDataBean(
                        "jdbc:mysql://192.168.3.223/mae_data_activeorder_tmd_temp?useUnicode=true&characterEncoding=utf-8&socketTimeout=50000",
                        "测试信息,使用jdbc url");
                NodeBean nodeInfo = new NodeBean("test.info", dataInfo);

                NodeDataBean dataAddr = new NodeDataBean("测试地址", "测试地址");
                NodeBean nodeAddr = new NodeBean("test.addr", dataAddr);

                NodeDataBean dataTel = new NodeDataBean("13601009999", "手机号码");
                NodeBean nodeTel = new NodeBean("test.tel.phoneno", dataTel);

                NodeDataBean dataWebUrl = new NodeDataBean("http://weburl.com", "网页链接");
                NodeBean nodeWeburl = new NodeBean("test.weburl", dataWebUrl);

                NodeDataBean dataBlogUrl = new NodeDataBean("blog.url.com", "个人主页链接");
                NodeBean nodeBlogUrl = new NodeBean("test.blogurl", dataBlogUrl);

                NodeDataBean dataBool1 = new NodeDataBean("on", "成功标识1");
                NodeBean nodeBool1 = new NodeBean("test.isSuccess1", dataBool1);
                NodeDataBean dataBool2 = new NodeDataBean("true", "成功标识2");
                NodeBean nodeBool2 = new NodeBean("test.isSuccess2", dataBool2);
                NodeDataBean dataBool3 = new NodeDataBean("abc", "成功标识3");
                NodeBean nodeBool3 = new NodeBean("test.isSuccess3", dataBool3);

                NodeDataBean dataLong = new NodeDataBean("1234567890", "长整型数值");
                NodeBean nodeLong = new NodeBean("test.countLong", dataLong);

                NodeDataBean dataInt = new NodeDataBean("1234567890", "整数");
                NodeBean nodeInt = new NodeBean("test.countInt", dataInt);

                valueDataBeanList.add(nodeInfo);
                valueDataBeanList.add(nodeAddr);
                valueDataBeanList.add(nodeTel);
                valueDataBeanList.add(nodeWeburl);
                valueDataBeanList.add(nodeBlogUrl);
                valueDataBeanList.add(nodeBool1);
                valueDataBeanList.add(nodeBool2);
                valueDataBeanList.add(nodeBool3);
                valueDataBeanList.add(nodeLong);
                valueDataBeanList.add(nodeInt);

                ZookeeperResource.writeDataToZookeeper(zookeeper, propertiesPath, null, valueDataBeanList);

                System.out.println(" \t 测试数据准备完毕  zookeeperList:" + zookeeperAddress + "  root:" + propertiesPath);

                zookeeperProperties = new ZookeeperProperties<String>();
                zookeeperProperties.setZookeeperAddress(zookeeperAddress);
                zookeeperProperties.setPropertiesPath(propertiesPath);
                zookeeperProperties.afterPropertiesSet();

                // ZookeeperReadService zookeeperReadService = new ZookeeperReadService();
                ReflectUtil.setFieldValue(zookeeperValueBeanProcessor, "zookeeperProperties", zookeeperProperties);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 初始化数据
        setUp();

        Thread springThread = new SpringMockThread(zookeeperValueBeanProcessor);
        Thread infoThread = new InfoThread();
        Thread setDataThread = new SetDataMockThread(zookeeper, propertiesPath, zookeeperProperties);

        springThread.start();
        infoThread.start();
        setDataThread.start();

        Thread.sleep(1000 * 3600);

    }

}

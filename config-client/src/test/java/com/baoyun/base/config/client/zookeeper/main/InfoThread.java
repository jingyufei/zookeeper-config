package com.baoyun.base.config.client.zookeeper.main;

import java.lang.ref.SoftReference;
import java.util.List;

import com.baoyun.base.config.client.bean.RealtimeBean;
import com.baoyun.base.config.client.util.BeanToString;
import com.baoyun.base.config.client.zookeeper.ZookeeperProperties;

/**
 * 信息打印线程
 *
 */
public class InfoThread extends Thread {

    private String key = "test.countInt";

    public InfoThread() {
        this.setName("InfoThread");
    }

    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(1000 * 30);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ZookeeperProperties<?> zookeeperProperties = new ZookeeperProperties<String>();
            System.out.println(" info " + zookeeperProperties.getZookeeperAddress());
            System.out.println(" info key" + zookeeperProperties.get(key));

            List<SoftReference<RealtimeBean>> realtimeBeanList = zookeeperProperties.getRealtimeBeanList(key);
            System.out.println(" info list" + realtimeBeanList);

            String listToString = "";
            String beanListToString = "";
            if (realtimeBeanList != null) {
                for (SoftReference<RealtimeBean> softRealtime : realtimeBeanList) {
                    RealtimeBean realtime = softRealtime.get();
                    listToString += BeanToString.string(realtime);
                    beanListToString += BeanToString.string(realtime.getBean());
                }
            }
            System.out.println(" info list(toString)" + listToString);
            System.out.println(" info beanList(toString)" + beanListToString);
            System.out.println(" \t 刷新完毕 \n");

        }
    }

}

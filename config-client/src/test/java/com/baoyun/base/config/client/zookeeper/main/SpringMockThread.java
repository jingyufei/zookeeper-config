package com.baoyun.base.config.client.zookeeper.main;

import com.baoyun.base.config.client.bean.PojoTestBean;
import com.baoyun.base.config.client.zookeeper.ZookeeperValueBeanProcessor;

/**
 * 模拟Spring 线程
 *
 */
public class SpringMockThread extends Thread {

    boolean init = false;

    ZookeeperValueBeanProcessor zookeeperValueBeanProcessor;

    public SpringMockThread(ZookeeperValueBeanProcessor zookeeperValueBeanProcessor) {
        this.setName("springMockThread");
        this.zookeeperValueBeanProcessor = zookeeperValueBeanProcessor;
    }

    @Override
    public void run() {

        while (true) {
            if (!init) {
                zookeeperValueBeanProcessor.postProcessAfterInitialization(new PojoTestBean(), "pojoTestBean");
                init = true;
            } else {
                System.out.println(" \t springThread init finish \n ");
                try {
                    Thread.sleep(1000 * 60 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}

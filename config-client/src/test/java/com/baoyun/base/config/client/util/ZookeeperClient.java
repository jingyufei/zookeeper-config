package com.baoyun.base.config.client.util;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;

public class ZookeeperClient {

    static volatile boolean waitSuccess = false;

    public static boolean waitStartUp(String host) {

        while (!waitSuccess) {
            System.out.println(" \t waitSuccess= " + waitSuccess);
            try {
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                ZooKeeper zookeeper = new ZooKeeper(host, 3000, getWatcher());
                System.out.println(" \t zookeeper= " + zookeeper);
                if (zookeeper != null) {
                    States states = zookeeper.getState();
                    if (states.isAlive() && states.isConnected()) {
                        System.out.println(" \t states.isAlive()= " + states.isAlive() + "   states.isConnected()" + states.isConnected());

                        zookeeper.close();

                    } else {
                        zookeeper.close();
                    }
                    waitSuccess = true;
                    break;

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return waitSuccess;
    }

    public static Watcher getWatcher() {
        return new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                System.out.println(" \t event" + event.toString());

            }

        };
    }
}

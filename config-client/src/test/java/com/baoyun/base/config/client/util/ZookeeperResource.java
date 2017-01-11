package com.baoyun.base.config.client.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.baoyun.base.config.client.bean.NodeBean;

public class ZookeeperResource {
    /**
     * 创建完整路径
     * 
     * @param zookeeper
     * @param path
     * @param watcher
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    public static Stat createFullPath(ZooKeeper zookeeper, String path, Watcher watcher) throws KeeperException, InterruptedException,
            UnsupportedEncodingException {
        Stat existsPath = zookeeper.exists(path, false);
        if (existsPath == null) {
            String[] subPathArray = path.split("/");
            String createPath = "";
            for (String subPath : subPathArray) {
                if (subPath != null && !subPath.trim().isEmpty()) {
                    createPath = createPath + "/" + subPath;
                    Stat subExistsStat = zookeeper.exists(createPath, false);
                    if (subExistsStat == null) {
                        String createResult = zookeeper.create(createPath, UTF8StringUtil.stringToByte(createPath), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                        System.out.println(" \t createResult=" + createResult);
                    }
                }
            }
            existsPath = zookeeper.exists(path, false);
        } else {

        }
        return existsPath;
    }

    /**
     * 往zookeeper写入数据列表
     * 
     * @param zookeeper
     * @param root
     * @param watcher
     * @param nodeBeanList
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void writeDataToZookeeper(ZooKeeper zookeeper, String root, Watcher watcher, List<NodeBean> nodeBeanList)
            throws UnsupportedEncodingException, KeeperException, InterruptedException {

        createFullPath(zookeeper, root, watcher);
        for (NodeBean nodeBean : nodeBeanList) {
            String path = root + "/" + nodeBean.getPath();
            String value = JSONObject.fromObject(nodeBean.getValueData()).toString();
            System.out.println(" \t value: " + value + "  path:" + path);

            Stat existsPathStat = zookeeper.exists(path, false);
            if (existsPathStat == null) {
                String createValue = zookeeper.create(path, UTF8StringUtil.stringToByte(value), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                System.out.println(" \t createValue=" + createValue);
            } else {
                Stat setDataStat = zookeeper.setData(path, UTF8StringUtil.stringToByte(value), -1);
                System.out.println(" \t setDataStat=" + setDataStat);
            }
        }
    }

    /**
     * 往zookeeper写入(单节点)数据
     * 
     * @param zookeeper
     * @param root
     * @param watcher
     * @param nodeBeanList
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void writeDataToZookeeper(ZooKeeper zookeeper, String root, Watcher watcher, NodeBean nodeBean) throws UnsupportedEncodingException,
            KeeperException, InterruptedException {

        createFullPath(zookeeper, root, watcher);
        String path = root + "/" + nodeBean.getPath();
        String value = JSONObject.fromObject(nodeBean.getValueData()).toString();
        System.out.println(" \t value: " + value + "  path:" + path);

        Stat existsPathStat = zookeeper.exists(path, false);
        if (existsPathStat == null) {
            String createValue = zookeeper.create(path, UTF8StringUtil.stringToByte(value), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(" \t createValue=" + createValue);
        } else {
            Stat setDataStat = zookeeper.setData(path, UTF8StringUtil.stringToByte(value), -1);
            System.out.println(" \t setDataStat=" + setDataStat);
        }

        
    }
}

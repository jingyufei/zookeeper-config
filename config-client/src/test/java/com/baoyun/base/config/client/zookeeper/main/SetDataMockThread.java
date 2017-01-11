package com.baoyun.base.config.client.zookeeper.main;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baoyun.base.config.client.bean.NodeBean;
import com.baoyun.base.config.client.bean.NodeDataBean;
import com.baoyun.base.config.client.bean.PojoTestBean;
import com.baoyun.base.config.client.bean.RealtimeBean;
import com.baoyun.base.config.client.util.ReflectUtil;
import com.baoyun.base.config.client.util.ZookeeperResource;
import com.baoyun.base.config.client.zookeeper.ZookeeperProperties;

public class SetDataMockThread extends Thread {

    static final Logger log = LoggerFactory.getLogger(SetDataMockThread.class);

    ZooKeeper zookeeper;

    String root;

    ZookeeperProperties zookeeperProperties;

    public SetDataMockThread(ZooKeeper zookeeper, String root, ZookeeperProperties zookeeperProperties) {
        this.setName("SetDataMockTread");
        this.zookeeper = zookeeper;
        this.root = root;
        this.zookeeperProperties = zookeeperProperties;
    }

    private void writeData(NodeBean nodeBean) throws UnsupportedEncodingException, KeeperException, InterruptedException {
        ZookeeperResource.writeDataToZookeeper(zookeeper, root, null, nodeBean);
        // 等待数据刷新时间
        Thread.sleep(100);
    }

    @Override
    public void run() {
        try {
            while (true) {

                if (zookeeper != null) {
                    System.out.println(" \t zookeeperSessionId=" + zookeeper.getSessionId());
                    Stat rootStat = zookeeper.exists(root, false);
                    if (rootStat == null) {
                        rootStat = ZookeeperResource.createFullPath(zookeeper, root, null);
                        System.out.println(" \t 创建 " + root);
                    }

                    // test.info
                    String info = UUID.randomUUID().toString().substring(0, 4) + "=信息info";
                    String infoKey = "test.info";
                    NodeDataBean dataInfo = new NodeDataBean(info, "测试信息");
                    NodeBean nodeInfo = new NodeBean(infoKey, dataInfo);
                    this.writeData(nodeInfo);
                    String currentInfoValue = zookeeperProperties.get(infoKey);
                    CompareDiffException.compare(info, currentInfoValue);
                    List<SoftReference<RealtimeBean>> softRealtimeBeanList = zookeeperProperties.getRealtimeBeanList(infoKey);
                    if (!softRealtimeBeanList.isEmpty()) {
                        throw new CompareDiffException(0, 0, "test.info结果应该为空");
                    }

                    // test.addr
                    String addr = UUID.randomUUID().toString().substring(0, 5) + "地址addr";
                    String addrKey = "test.addr";
                    NodeDataBean dataAddr = new NodeDataBean(addr, "测试地址");
                    NodeBean nodeAddr = new NodeBean(addrKey, dataAddr);
                    this.writeData(nodeAddr);
                    String currentAddr = zookeeperProperties.get(addrKey);
                    CompareDiffException.compare(addr, currentAddr);
                    List<SoftReference<RealtimeBean>> softRealtimeAddrList = zookeeperProperties.getRealtimeBeanList(addrKey);
                    for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeAddrList) {
                        Object object = softRealtimeBean.get().getBean();
                        if (object instanceof PojoTestBean) {
                            PojoTestBean pojo = (PojoTestBean) object;
                            CompareDiffException.compare(addr, pojo.getAddr());
                        }
                    }

                    // test.tel.phoneno
                    String telPhoneNo = "136" + (new Random()).nextInt(99999999);
                    String telPhoneNoKey = "test.tel.phoneno";
                    NodeDataBean dataTel = new NodeDataBean(telPhoneNo, "手机号码");
                    NodeBean nodeTel = new NodeBean(telPhoneNoKey, dataTel);
                    this.writeData(nodeTel);
                    String currentTelPhoneNo = zookeeperProperties.get(telPhoneNoKey);
                    CompareDiffException.compare(telPhoneNo, currentTelPhoneNo);
                    List<SoftReference<RealtimeBean>> softRealtimeTelList = zookeeperProperties.getRealtimeBeanList(telPhoneNoKey);
                    for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeTelList) {
                        Object object = softRealtimeBean.get().getBean();
                        if (object instanceof PojoTestBean) {
                            PojoTestBean pojo = (PojoTestBean) object;
                            CompareDiffException.compare(telPhoneNo, pojo.getTelPhoneNo());
                        }
                    }

                    // test.weburl
                    String webUrl = "http://www." + UUID.randomUUID().toString().substring(0, 4) + ".com";
                    String webUrlKey = "test.weburl";
                    NodeDataBean dataWebUrl = new NodeDataBean(webUrl, "网页链接");
                    NodeBean nodeWebUrl = new NodeBean(webUrlKey, dataWebUrl);
                    this.writeData(nodeWebUrl);
                    String currentWebUrl = zookeeperProperties.get(webUrlKey);
                    CompareDiffException.compare(webUrl, currentWebUrl);
                    List<SoftReference<RealtimeBean>> softRealtimeWebUrl = zookeeperProperties.getRealtimeBeanList(webUrlKey);
                    for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeWebUrl) {
                        Object object = softRealtimeBean.get().getBean();
                        String webUrlField = (String) ReflectUtil.getFieldValue(object, "webUrl");
                        CompareDiffException.compare(webUrl, webUrlField);
                    }

                    // test.blogurl
                    String blogUrl = "http://blog." + UUID.randomUUID().toString().substring(0, 2) + ".me";
                    String blogUrlKey = "test.blogurl";
                    NodeDataBean dataBlogUrl = new NodeDataBean(blogUrl, "个人主页链接");
                    NodeBean nodeBlogUrl = new NodeBean(blogUrlKey, dataBlogUrl);
                    this.writeData(nodeBlogUrl);
                    String currentBlogUrl = zookeeperProperties.get(blogUrlKey);
                    CompareDiffException.compare(blogUrl, currentBlogUrl);
                    List<SoftReference<RealtimeBean>> softRealtimeBlogUrl = zookeeperProperties.getRealtimeBeanList(webUrlKey);
                    for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeBlogUrl) {
                        Object object = softRealtimeBean.get().getBean();
                        String blogUrlField = (String) ReflectUtil.getFieldValue(object, "blogUrl");
                        CompareDiffException.compare(blogUrl, blogUrlField);
                    }

                    String[] booleanArray = new String[] { "on", "true", "off", "false", "abcd", "On" };

                    // test.isSuccess1
                    String bool1 = booleanArray[(new Random()).nextInt(booleanArray.length - 1)];

                    String bool1Key = "test.isSuccess1";
                    NodeDataBean dataBoolean1 = new NodeDataBean(bool1, "成功标识1");
                    NodeBean nodeBoolean1 = new NodeBean(bool1Key, dataBoolean1);
                    this.writeData(nodeBoolean1);
                    String currentBool1 = zookeeperProperties.get(bool1Key);
                    CompareDiffException.compare(bool1, currentBool1);
                    List<SoftReference<RealtimeBean>> softRealtimeBool1List = zookeeperProperties.getRealtimeBeanList(bool1Key);
                    for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeBool1List) {
                        Object object = softRealtimeBean.get().getBean();
                        boolean bool1Field = (Boolean) ReflectUtil.getFieldValue(object, "isSuccess1");
                        boolean bool1Value = bool1.equalsIgnoreCase("on") || bool1.equals("true");
                        CompareDiffException.compare(bool1Value, bool1Field);
                    }

                    // test.isSuccess2
                    String bool2 = booleanArray[(new Random()).nextInt(booleanArray.length - 1)];
                    String bool2Key = "test.isSuccess2";
                    NodeDataBean dataBoolean2 = new NodeDataBean(bool2, "成功标识2");
                    NodeBean nodeBoolean2 = new NodeBean(bool2Key, dataBoolean2);
                    this.writeData(nodeBoolean2);
                    String currentBool2 = zookeeperProperties.get(bool2Key);
                    CompareDiffException.compare(bool2, currentBool2);
                    List<SoftReference<RealtimeBean>> softRealtimeBeanBool2List = zookeeperProperties.getRealtimeBeanList(bool2Key);
                    for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeBeanBool2List) {
                        Object object = softRealtimeBean.get().getBean();
                        boolean bool2Field = (Boolean) ReflectUtil.getFieldValue(object, "isSuccess2");
                        boolean bool2Value = bool2.equalsIgnoreCase("on") || bool2.equalsIgnoreCase("true");
                        CompareDiffException.compare(bool2Value, bool2Field);
                    }

                    // test.isSuccess3
                    String bool3 = booleanArray[(new Random()).nextInt(booleanArray.length - 1)];
                    String bool3Key = "test.isSuccess3";
                    NodeDataBean dataBool3 = new NodeDataBean(bool3, "成功标识3");
                    NodeBean nodeBool3 = new NodeBean(bool3Key, dataBool3);
                    this.writeData(nodeBool3);
                    String currentBool3 = zookeeperProperties.get(bool3Key);
                    CompareDiffException.compare(bool3, currentBool3);
                    List<SoftReference<RealtimeBean>> softRealtimeBeanBool3List = zookeeperProperties.getRealtimeBeanList(bool3Key);
                    for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeBeanBool3List) {
                        Object object = softRealtimeBean.get().getBean();
                        boolean bool3Field = (Boolean) ReflectUtil.getFieldValue(object, "isSuccess3");
                        boolean bool3Value = bool3.equalsIgnoreCase("on") || bool3.equalsIgnoreCase("true");
                        CompareDiffException.compare(bool3Value, bool3Field);
                    }

                    // test.countLong
                    String longValue = String.valueOf(new Random().nextLong());
                    String longValueKey = "test.countLong";
                    NodeDataBean dataLong = new NodeDataBean(longValue, "长整型数值");
                    NodeBean nodeLong = new NodeBean(longValueKey, dataLong);
                    this.writeData(nodeLong);
                    String currentLong = zookeeperProperties.get(longValueKey);
                    CompareDiffException.compare(longValue, currentLong);
                    List<SoftReference<RealtimeBean>> softRealtimeBeanLongList = zookeeperProperties.getRealtimeBeanList(longValueKey);
                    for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeBeanLongList) {
                        Object object = softRealtimeBean.get().getBean();
                        long longField = (Long) ReflectUtil.getFieldValue(object, "countLong");
                        CompareDiffException.compare(Long.valueOf(longValue), longField);
                    }

                    // test.countInt
                    String intValue = String.valueOf((new Random()).nextInt(Integer.MAX_VALUE));
                    String intValueKey = "test.countInt";
                    NodeDataBean dataInt = new NodeDataBean(intValue, "整数");
                    NodeBean nodeInt = new NodeBean(intValueKey, dataInt);
                    this.writeData(nodeInt);
                    String currentInt = zookeeperProperties.get(intValueKey);
                    CompareDiffException.compare(intValue, currentInt);
                    List<SoftReference<RealtimeBean>> softRealtimeBeanIntList = zookeeperProperties.getRealtimeBeanList(intValueKey);
                    for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeBeanIntList) {
                        Object object = softRealtimeBean.get().getBean();
                        int intField = (Integer) ReflectUtil.getFieldValue(object, "countInt");
                        CompareDiffException.compare(Integer.valueOf(intValue), intField);
                    }

                    System.out.println(" \t 放置数据 并对比结束");

                    Thread.sleep(1000 * 20);

                }

            }

        } catch (Exception e) {
            log.error(" thread:{} exception:{} ", this.getName(), e);
        }
    }
}

package com.baoyun.base.config.client.zookeeper;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertySource;

import com.baoyun.base.config.client.bean.RealtimeBean;
import com.baoyun.base.config.client.util.ReflectUtil;
import com.baoyun.base.config.client.util.UTF8StringUtil;

/**
 * Zookeeper属性配置
 *
 */
public class ZookeeperProperties<T> extends PropertySource<T> {
	
	public ZookeeperProperties() {
    	super("zookeeperProperties");
	}

	public ZookeeperProperties(String name) {
		super(name);
	}

	static final Logger log = LoggerFactory.getLogger(ZookeeperProperties.class);

    public static final String JSON_VALUE = "value";

    /**
     * Zookeeper地址列表
     */
    @Getter
    @Setter
    private String zookeeperAddress;

    /**
     * Zookeeper目录
     */
    @Getter
    @Setter
    private String propertiesPath;

    /**
     * 超时时间 (毫秒)
     */
    @Getter
    @Setter
    private int sessionTimeout = 3000;

    /**
     * zookeeper 连接信息
     */
    @Getter
    private ZooKeeper zookeeper;

    @Getter
    private Watcher watcher;

    /**
     * zooKeeper中 key-value值
     */
    private ConcurrentHashMap<String, String> zookeeperValueMap = new ConcurrentHashMap<String, String>();

    /**
     * 根据 key 将要刷新的bean/field 缓存, 如果一个bean被初始化多次, 应该多次存放, 不然会导致部分字段值没有被刷新
     */
    private ConcurrentHashMap<String, CopyOnWriteArrayList<SoftReference<RealtimeBean>>> keySoftRealtimeBeanMap = new ConcurrentHashMap<String, CopyOnWriteArrayList<SoftReference<RealtimeBean>>>();

    public void afterPropertiesSet() {
        this.freshZookeeperValue();
    }

    /**
     * 获取属性值
     * 
     * @param key
     * @return
     */
    public String get(String key) {
        String value = null;
        if (key != null) {
            value = zookeeperValueMap.get(key);
        } else {
            log.warn(" get key:{} is null ", key);
        }
        log.debug(" get key:{} value:{} ", key, value);
        return value;
    }

    /**
     * 根据key 更新Zookeeper映射 (相同值则不刷新)
     * 
     * @param key
     * @param value
     */
    private void setZookeeperValue(String key, String value) {
        try {

            String oldValue = zookeeperValueMap.get(key.trim());
            if (oldValue == null || !oldValue.equals(value)) {
                String putResult = zookeeperValueMap.put(key, value.trim());
                log.debug(" setZookeeperValue key:{} value:{} oldValue:{} putResult:{} ", key, value, oldValue, putResult);

                CopyOnWriteArrayList<SoftReference<RealtimeBean>> softRealtimeList = getRealtimeBeanList(key);
                CopyOnWriteArrayList<SoftReference<RealtimeBean>> invalidRealtimeList = new CopyOnWriteArrayList<SoftReference<RealtimeBean>>();
                for (SoftReference<RealtimeBean> softRealtimeBean : softRealtimeList) {
                    RealtimeBean realtimeBean = softRealtimeBean.get();
                    if (realtimeBean != null && realtimeBean.getBean() != null) {
                        Object targetObject = realtimeBean.getBean();
                        Field targetField = realtimeBean.getField();
                        log.info(" setZookeeperValue key:{} value:{} realtime:{} ", key, value, realtimeBean);
                        ReflectUtil.setFieldValue(targetObject, targetField, value);
                    } else {
                        log.info("setZookeeperValue key:{} value:{} softRealtimeBean:{} include null bean realtimeBean:{} ", key, value, softRealtimeBean,
                                realtimeBean);
                        invalidRealtimeList.add(softRealtimeBean);
                    }
                }
                softRealtimeList.removeAll(invalidRealtimeList);
            } else {
                log.debug(" setZookeeperValue the same value, need not to fresh. ");
            }

        } catch (Exception e) {
            log.error(" setZookeeperValue-exception exception:{} ", e);
        }
    }

    /**
     * 刷新 zookeeperValue
     */
    public void freshZookeeperValue() {
        try {
        	freshZookeeperConnection();
        	if(zookeeper == null){
        		log.error("cannot connected to zookeeper:{}",zookeeperAddress);
        		return;
        	}
        	
            boolean isSetZookeeperList = false;
            boolean isSetRoot = false;
            if (zookeeperAddress == null || zookeeperAddress.trim().isEmpty()) {
                log.error(" get need to set zookeeperList. ");
            } else {
                isSetZookeeperList = true;
            }
            if (propertiesPath == null || propertiesPath.trim().isEmpty()) {
                log.error(" get need to set root. ");
            } else {
                isSetRoot = true;
            }

            if (isSetZookeeperList && isSetRoot) {
                this.readProp();
                log.info(" get fresh zookeeperList:{}  root:{} values freshMap:{} ", zookeeperAddress, propertiesPath, zookeeperValueMap);
            }
        } catch (Exception e) {
            log.error(" freshZookeeperValue exception:{} ", e);
        }
    }
    
    private void freshZookeeperConnection() throws InterruptedException, IOException {
        if (zookeeper != null) {
            States state = zookeeper.getState();
            if (state.isAlive() && state.isConnected()) {
                log.info(" zookeeper connection is still connect. ");
            } else {
                zookeeper.close();
                this.connectZookeeper();
            }
        } else {
            this.connectZookeeper();
        }
    }
    
    private void connectZookeeper() throws IOException {
        final CountDownLatch latch = new CountDownLatch(1);
        this.watcher = geneWatcher(latch);
        if (zookeeper == null) {
            zookeeper = new ZooKeeper(zookeeperAddress, this.sessionTimeout, this.watcher);
        }
        try {
            boolean result = latch.await(10,TimeUnit.SECONDS);//最多等待10秒
            if(!result){
            	try {
    				zookeeper.close();
    			} catch (InterruptedException e1) {
    				log.error("connectZookeeper fail,close it", e1);
    			}
            	zookeeper = null;
            }
        } catch (InterruptedException e) {
            log.error("connectZookeeper exception:{} ", e);
        }

    }

    /**
     * 放置 RealtimeBean
     * 
     * @param key
     * @param realtimeBean
     * @return
     */
    public boolean registerRealtimeBean(RealtimeBean realtimeBean) {
        boolean operation = false;
        try {
            if (realtimeBean == null) {
                log.error(" setRealtimeBean realtimeBeam:{} is null. ", realtimeBean);
            } else {

                if (keySoftRealtimeBeanMap == null) {
                    keySoftRealtimeBeanMap = new ConcurrentHashMap<String, CopyOnWriteArrayList<SoftReference<RealtimeBean>>>();
                }
                String key = realtimeBean.getKey();
                log.debug(" setRealtimeBean before set key:{} list:{} ", key, keySoftRealtimeBeanMap.get(key));
                if (keySoftRealtimeBeanMap.get(key) == null) {
                    keySoftRealtimeBeanMap.put(key, new CopyOnWriteArrayList<SoftReference<RealtimeBean>>());
                }
                SoftReference<RealtimeBean> softRealtimeBean = new SoftReference<RealtimeBean>(realtimeBean);
                keySoftRealtimeBeanMap.get(key).add(softRealtimeBean);
                log.debug(" setRealtimeBean after set key:{} list:{} ", key, keySoftRealtimeBeanMap.get(key));
                operation = true;
            }
        } catch (Exception e) {
            log.error("setRealtimeBean-exception realtimeBean:{} exception:{} ", realtimeBean, e);
        }
        return operation;
    }

    /**
     * 读取 ZooKeeper属性
     * 
     * 
     * @return
     */
    private void readProp() {
        log.info(" readProp zookeeperList:{} root:{} ", this.zookeeperAddress, this.propertiesPath);

        // watcher = this.getWatcher();
        try {
            Stat rootStat = zookeeper.exists(propertiesPath, null);
            if (rootStat == null) {
                log.error(" readProp-root path:{} zookeeperList:{} is empty in zookeeper", propertiesPath, zookeeperAddress);
            } else {
                List<String> childrenList = zookeeper.getChildren(propertiesPath, watcher);
                if (childrenList == null || childrenList.isEmpty()) {
                    log.error(" readProp-root doesn't exists children node. root:{} zookeeperList:{} ", propertiesPath, zookeeperAddress);
                } else {
                    for (String childName : childrenList) {
                        String childPath = propertiesPath + "/" + childName;
                        Stat childStat = zookeeper.exists(childPath, watcher);
                        if (childStat != null) {
                            byte[] childData = zookeeper.getData(childPath, watcher, childStat);

                            String propValue = childDataToPropValue(childData);
                            this.setZookeeperValue(childName, propValue);

                        } else {
                            log.warn(" readProp-child Stat is null. zookeeperList:{} root:{} childName:{} childStat:{} ", zookeeperAddress, propertiesPath, childName,
                                    childStat);
                        }
                    }

                }
            }

        } catch (KeeperException e) {
            log.error(" readProp-KeeperException zookeeperList:{} root:{} exception:{} ", zookeeperAddress, propertiesPath, e);
        } catch (InterruptedException e) {
            log.error(" readProp-InterruptedException zookeeperList:{} root:{} exception:{} ", zookeeperAddress, propertiesPath, e);
        } catch (Exception e) {
            log.error(" readProp-Exception zookeeperList:{} root:{} exception:{} ", zookeeperAddress, propertiesPath, e);
        }

    }

    /**
     * 子节点数据转为 配置值, 需要解析 json
     * 
     * @param data
     * @return
     */
    private String childDataToPropValue(byte[] data) {
        String propValue = "";
        try {
            String childValue = UTF8StringUtil.stringFromByte(data);

            JSONObject jsonObject = JSONObject.fromObject(childValue);
            if (!jsonObject.isEmpty()) {
                propValue = jsonObject.getString(JSON_VALUE);
            }
        } catch (Exception e) {
            log.error(" childDataToPropValue exception:{} ", e);
        }
        return propValue;
    }

    

    


    /**
     * 生成一个Watcher
     * 
     * @return
     */
    public Watcher geneWatcher(final CountDownLatch latch) {
        return new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info(" Watcher-process event:{} ", event);
                KeeperState eventStat = event.getState();
                String eventPath = event.getPath();
                EventType eventType = event.getType();
                boolean connectSuccess = KeeperState.SyncConnected.equals(eventStat);
                boolean dataChange = EventType.NodeDataChanged.equals(eventType);
                if (connectSuccess) {
                    latch.countDown();
                }
                // 仅处理连接成功 并且是 数据改变的事件
                if (connectSuccess && dataChange) {
                    // 以 root开始 并且 不以root结束的才算是 配置列表
                    boolean isKeyNode = eventPath.startsWith(propertiesPath) && !eventPath.endsWith(propertiesPath);
                    if (zookeeper != null && isKeyNode) {
                        try {
                            Stat keyNodeStat = zookeeper.exists(eventPath, watcher);
                            byte[] keyData = zookeeper.getData(eventPath, watcher, keyNodeStat);
                            String value = childDataToPropValue(keyData);
                            String key = eventPath.replaceFirst(propertiesPath, "").replaceFirst("/", "");
                            setZookeeperValue(key, value);
                            
                        } catch (KeeperException e) {
                            log.error("  Watcher-process-KeeperException event:{} exception:{} ", event, e);
                        } catch (InterruptedException e) {
                            log.error("  Watcher-process-InterruptedException event:{} exception:{} ", event, e);
                        }
                    }
                }

            }

        };
    }

    /**
     * 获取 realtimeBean列表
     * 
     * @param key
     * @return
     */
    public CopyOnWriteArrayList<SoftReference<RealtimeBean>> getRealtimeBeanList(String key) {
        CopyOnWriteArrayList<SoftReference<RealtimeBean>> realtimeBeanList = new CopyOnWriteArrayList<SoftReference<RealtimeBean>>();

        if (key == null || key.trim().isEmpty()) {
            log.error(" getRealtimeBeanList key:{} is null or empty. ", key);
        } else {
            if (keySoftRealtimeBeanMap.get(key.trim()) == null) {
                log.error(" getRealtimeBeanList key:{} don't exists list.", key);
            } else {
                realtimeBeanList = keySoftRealtimeBeanMap.get(key);
            }
        }

        return realtimeBeanList;
    }

	@Override
	public Object getProperty(String name) {
		return get(name);
	}
	
	public String getZookeeperAddress() {
		return zookeeperAddress;
	}

	public void setZookeeperAddress(String zookeeperAddress) {
		this.zookeeperAddress = zookeeperAddress;
	}

	public String getPropertiesPath() {
		return propertiesPath;
	}

	public void setPropertiesPath(String propertiesPath) {
		this.propertiesPath = propertiesPath;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

    
	public ZooKeeper getZookeeper() {
		return zookeeper;
	}

	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}

	public Watcher getWatcher() {
		return watcher;
	}

	public void setWatcher(Watcher watcher) {
		this.watcher = watcher;
	}

	public ConcurrentHashMap<String, String> getZookeeperValueMap() {
		return zookeeperValueMap;
	}

	public void setZookeeperValueMap(
			ConcurrentHashMap<String, String> zookeeperValueMap) {
		this.zookeeperValueMap = zookeeperValueMap;
	}

	public ConcurrentHashMap<String, CopyOnWriteArrayList<SoftReference<RealtimeBean>>> getKeySoftRealtimeBeanMap() {
		return keySoftRealtimeBeanMap;
	}

	public void setKeySoftRealtimeBeanMap(
			ConcurrentHashMap<String, CopyOnWriteArrayList<SoftReference<RealtimeBean>>> keySoftRealtimeBeanMap) {
		this.keySoftRealtimeBeanMap = keySoftRealtimeBeanMap;
	}
}

package com.baoyun.base.config.server.util;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;

public class ZkClient {
    private volatile static ConcurrentHashMap<String, ZooKeeper> map = new ConcurrentHashMap<String, ZooKeeper>();
    
    private static Logger log = Logger.getLogger(ZkClient.class);
    
    public static ZooKeeper getZooKeeper(String zookeeperList)throws IOException{
    	if(map.containsKey(zookeeperList)){
    		ZooKeeper keeper = map.get(zookeeperList);
    		States state = keeper.getState();
    		if(state.isAlive() && state.isConnected()){
    			return keeper;
    		}else{
    			try{
    				keeper.close(); 
    			}catch(InterruptedException ie){
    				log.error("close zookeeper {"+zookeeperList+"} error",ie);
    			}
    			map.remove(keeper);
    		}
    	}
    	ZooKeeper keeper = connectZookeeper(zookeeperList);
    	if(keeper == null)
    		throw new IOException("no available zookeeper{"+zookeeperList+"}");
    	
    	States state = keeper.getState();
    	if(state.isAlive() && state.isConnected()){
    		map.put(zookeeperList, keeper);
			return keeper;
		}
    	
    	throw new IOException("no available zookeeper{"+zookeeperList+"}");
    }

	private static ZooKeeper connectZookeeper(final String zookeeperList) throws IOException {
		final CountDownLatch latch = new CountDownLatch(1);
		
		ZooKeeper zookeeper = new ZooKeeper(zookeeperList, 1000 * 60, new Watcher(){
			@Override
			public void process(WatchedEvent event) {
				KeeperState state = event.getState();
				if(state == KeeperState.SyncConnected){
					//已连接
					log.info("zookeeper {} connected Ok.");
					latch.countDown();
				}else if(state == KeeperState.Disconnected){
					//已关闭
					log.warn("zookeeper {} disconnected");
				}else if(state == KeeperState.Expired){
					//已过期
					log.info("zookeeper {} expired.");
				}
			}
			
		});
    	
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
    	return zookeeper;
	}
}

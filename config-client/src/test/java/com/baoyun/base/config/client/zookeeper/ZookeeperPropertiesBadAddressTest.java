package com.baoyun.base.config.client.zookeeper;

import java.io.IOException;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import com.baoyun.base.config.client.util.LocalZookeeperServer;

@Slf4j
public class ZookeeperPropertiesBadAddressTest extends TestCase {
	ZookeeperProperties zp;
    @Override
    public void setUp() throws IOException, InterruptedException {
    	zp = new ZookeeperProperties();
    }
    
    public void testUnconnected(){
    	zp.setZookeeperAddress("127.0.0.1:2182");
    	zp.setPropertiesPath("/linker/config");
    	
    	zp.freshZookeeperValue();
    }

    @Override
    public void tearDown() throws IOException, InterruptedException {
        // main.shutdown();
        LocalZookeeperServer.stop();
    }
}

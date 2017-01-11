package com.baoyun.base.config.client.zookeeper;


public class ZookeeperSpringApplication {
	public static ZookeeperProperties<?> zookeeperProperties;

	public static ZookeeperProperties<?> getZookeeperProperties() {
		return zookeeperProperties;
	}

	public static void setZookeeperProperties(
			ZookeeperProperties<?> zookeeperProperties) {
		ZookeeperSpringApplication.zookeeperProperties = zookeeperProperties;
	}
	
}

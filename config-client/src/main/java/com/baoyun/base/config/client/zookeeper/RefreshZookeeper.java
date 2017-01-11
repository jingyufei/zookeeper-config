package com.baoyun.base.config.client.zookeeper;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("z")
public class RefreshZookeeper {
	private static Logger log = Logger.getLogger(RefreshZookeeper.class);
	@Scheduled(cron = "0 * * * * ?")
    public void autoFreshProp() {
        log.info(" autoFreshProp start ");
        try {
        	if(ZookeeperSpringApplication.zookeeperProperties != null){
	            ZookeeperSpringApplication.zookeeperProperties.freshZookeeperValue();
        	}
        } catch (Exception e) {
            log.error(" autoFreshProp exception:{} ", e);
        }

    }
}

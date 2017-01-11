package com.baoyun.base.config.server;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.baoyun.base.config.client.zookeeper.ZookeeperSpringApplication;


/**
 * Created by Tangmd on 2014/12/16.
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@ComponentScan("com.baoyun.base")
public class ConfigServerMain 
{
    public static void main( String[] args )
    {
    	SpringApplication application = new SpringApplication(ConfigServerMain.class);
//    	ZookeeperSpringApplication application = new ZookeeperSpringApplication(ConfigServerMain.class);
        Set<Object> sourcesSet = new HashSet<Object>();
        sourcesSet.add("classpath:applicationContext.xml");
        application.setSources(sourcesSet);

        application.run(args);
    }
}

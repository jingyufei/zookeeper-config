package com.baoyun.base.config.client.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解, 获取 zookeeper配置的值
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZookeeperValue {

    /**
     * 属性主键
     * 
     * @return
     */
    String key();

    /**
     * 是否实施刷新
     */
    boolean realtime() default true;

}

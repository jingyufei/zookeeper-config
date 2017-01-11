package com.baoyun.base.config.client.zookeeper;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.baoyun.base.config.client.annotation.ZookeeperValue;
import com.baoyun.base.config.client.bean.RealtimeBean;
import com.baoyun.base.config.client.util.ReflectUtil;

/**
 * 处理注解 @ZookeeperValue
 *
 */
@Component
public class ZookeeperValueBeanProcessor implements BeanPostProcessor, BeanFactoryAware {
	private static Logger log = Logger.getLogger(ZookeeperValueBeanProcessor.class);
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 缓存 注解
        if (bean != null) {

            // 处理 bean 的 ZookeeperValue注解
            Field[] beanFieldArray = bean.getClass().getDeclaredFields();
            if (beanFieldArray == null || beanFieldArray.length == 0) {
                log.warn(" postProcessBeforeInitialization field array is null or empty. beanName:{} bean:{} ");
            } else {
                for (Field beanField : beanFieldArray) {
                    processorField(bean, beanName, beanField);
                }
            }

            // 处理父类
            Class<?> supperClass = bean.getClass().getSuperclass();
            while (!supperClass.equals(Object.class)) {
                Field[] beanSupperFieldArray = supperClass.getDeclaredFields();
                if (beanSupperFieldArray == null || beanSupperFieldArray.length == 0) {
                    log.warn(" postProcessBeforeInitialization supper class field array is null or empty. beanName:{} bean:{} ");
                } else {
                    for (Field supperBeanField : beanSupperFieldArray) {
                        processorField(bean, beanName, supperBeanField);
                    }
                }
                supperClass = supperClass.getSuperclass();
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    /**
     * 处理一个 bean的字段的 ZookeeperValue 注解
     * 
     * @param bean
     * @param beanName
     * @param beanField
     * @return
     */
    private boolean processorField(Object bean, String beanName, Field beanField) {
        boolean operation = false;
        try {

            if (beanField.isAnnotationPresent(ZookeeperValue.class)) {
                ZookeeperValue zookeeperValue = beanField.getAnnotation(ZookeeperValue.class);
                boolean realtime = zookeeperValue.realtime();
                String key = zookeeperValue.key();

                // 拼接 RealtimeBean
                if (realtime) {
                    RealtimeBean realtimeBean = new RealtimeBean();
                    realtimeBean.setBean(bean);
                    realtimeBean.setBeanName(beanName);
                    realtimeBean.setField(beanField);
                    realtimeBean.setKey(key);

                    boolean success = ZookeeperSpringApplication.zookeeperProperties.registerRealtimeBean(realtimeBean);
                    if (!success) {
                        log.error(" postProcessAfterInitialization-error beanName:{} bean:{} realtimeBean:{} success:{} ");
                    }
                    String fieldValue = ZookeeperSpringApplication.zookeeperProperties.get(key);
                    operation = ReflectUtil.setFieldValue(bean, beanField, fieldValue);

                    log.info(" processorField bean:{} beanName:{} beanField:{} success:{} ");

                }
            }

        } catch (Exception e) {
            log.error("processField-exception exception:{} ", e);
        }
        return operation;
    }

}

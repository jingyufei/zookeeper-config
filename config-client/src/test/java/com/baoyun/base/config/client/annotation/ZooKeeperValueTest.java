package com.baoyun.base.config.client.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import junit.framework.TestCase;

import com.baoyun.base.config.client.bean.JDBCInfoBean;

/**
 * 读取注解 测试
 *
 */
public class ZooKeeperValueTest extends TestCase {

    /**
     * 测试读取注解
     */
    public void testReadAnnotation() {

        Field[] fieldArray = JDBCInfoBean.class.getDeclaredFields();
        TestCase.assertNotNull(fieldArray);
//        TestCase.assertEquals(10, fieldArray.length);
        for (Field field : fieldArray) {
            if (field.isAnnotationPresent(ZookeeperValue.class)) {
                System.out.println(" \t " + field.getName() + " 使用注解 ");
            }
            System.out.println(" \t isAccessiable()=" + field.isAccessible() + "  isAnnotationPresent()=" + field.isAnnotationPresent(ZookeeperValue.class));

            Annotation[] annotationArray = field.getAnnotations();
            TestCase.assertNotNull(annotationArray);
            if ("info".equals(field.getName())) {
                TestCase.assertEquals(2, annotationArray.length);
            } else {
                TestCase.assertEquals(1, annotationArray.length);
            }
            for (Annotation annotation : annotationArray) {
                System.out.println(annotation);
                if (annotation instanceof ZookeeperValue) {
                    ZookeeperValue zookeeperValue = (ZookeeperValue) annotation;
                    boolean realtime = zookeeperValue.realtime();
                    String key = zookeeperValue.key();
                    System.out.println(" \t " + field.getName() + " realtime=" + realtime + "  key=" + key);
                }
            }
        }

    }

}

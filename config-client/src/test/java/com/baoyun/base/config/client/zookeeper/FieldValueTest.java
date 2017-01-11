package com.baoyun.base.config.client.zookeeper;

import java.lang.reflect.Field;

import com.baoyun.base.config.client.bean.EmptyBean;
import com.baoyun.base.config.client.bean.NotSupportBean;
import com.baoyun.base.config.client.bean.SupportBean;
import com.baoyun.base.config.client.util.BeanToString;
import com.baoyun.base.config.client.util.ReflectUtil;

import junit.framework.TestCase;

public class FieldValueTest extends TestCase {

    SupportBean supportBean = new SupportBean();

    String[] fieldNameArray = new String[] { "boolLittle", "boolBig", "intLittle", "intBig", "longLittle", "longBig", "string" };

    @Override
    public void setUp() {

    }

    public void testSetFieldValueByField() {

        SupportBean supportBean = new SupportBean();
        System.out.println(" before supportBean=" + BeanToString.string(supportBean));
        Field[] fieldArray = supportBean.getClass().getDeclaredFields();
        // 校验 Field name
        TestCase.assertNotNull(fieldArray);
        TestCase.assertEquals(7, fieldArray.length);
        for (int fieldCount = 0; fieldCount < fieldArray.length; fieldCount++) {
            String targetFieldName = fieldNameArray[fieldCount];
            Field field = fieldArray[fieldCount];
            TestCase.assertEquals(targetFieldName, field.getName());
        }

        String[] testValueArray1 = new String[] { "true", "On", "123", "234", "999999", "888888", "测试字符串" };
        for (int testCount = 0; testCount < fieldArray.length; testCount++) {
            Field field = fieldArray[testCount];
            ReflectUtil.setFieldValue(supportBean, field, testValueArray1[testCount]);

            Object fieldValueByField = ReflectUtil.getFieldValue(supportBean, field);
            Object fieldValueByName = ReflectUtil.getFieldValue(supportBean, fieldNameArray[testCount]);
            TestCase.assertEquals(fieldValueByField, fieldValueByName);
        }
        System.out.println(" after valueArray1=" + BeanToString.string(supportBean));

        String[] testValueArray2 = new String[] { "abcd", "ON", "abcd", "234", "abcd", "888888", "" };
        for (int testCount = 0; testCount < fieldArray.length; testCount++) {
            Field field = fieldArray[testCount];
            ReflectUtil.setFieldValue(supportBean, field, testValueArray2[testCount]);

        }
        System.out.println(" after valueArray2=" + BeanToString.string(supportBean));

    }

    public void testSetFieldValueByFieldEmpty() {

        Field field = null;
        ReflectUtil.setFieldValue(new EmptyBean(), SupportBean.class.getDeclaredFields()[0], null);
        ReflectUtil.setFieldValue(new EmptyBean(), SupportBean.class.getDeclaredFields()[0], "abc");

        ReflectUtil.setFieldValue(null, SupportBean.class.getDeclaredFields()[0], "abcd");
        ReflectUtil.setFieldValue(new SupportBean(), field, "abcd");
        ReflectUtil.setFieldValue(new SupportBean(), SupportBean.class.getDeclaredFields()[0], null);

        ReflectUtil.setFieldValue(new NotSupportBean(), NotSupportBean.class.getDeclaredFields()[2], "abcd");

    }

    public void testSetFieldValueByName() {

        SupportBean supportBean = new SupportBean();
        System.out.println(" byName before supportBean:" + BeanToString.string(supportBean));

        Object[] testValueArray1 = new Object[] { true, false, 1234, 1256, 99912345, 88812345L, "测试字符串" };
        for (int testCount = 0; testCount < fieldNameArray.length; testCount++) {
            ReflectUtil.setFieldValue(supportBean, fieldNameArray[testCount], testValueArray1[testCount]);

            Object fieldValueByName = ReflectUtil.getFieldValue(supportBean, fieldNameArray[testCount]);
            TestCase.assertEquals(String.valueOf(testValueArray1[testCount]), String.valueOf(fieldValueByName));
        }

        System.out.println(" byName after supportBean:" + BeanToString.string(supportBean));

    }

    public void testSetFieldValueByNameEmpty() {
        ReflectUtil.setFieldValue(null, "test", 123);
        ReflectUtil.setFieldValue(new SupportBean(), null, 123);
        ReflectUtil.setFieldValue(new SupportBean(), "test", null);

        ReflectUtil.setFieldValue(new EmptyBean(), "test", "abcd");

    }

    public void testGetFieldValueByFieldEmpty() {
        Field field = null;
        ReflectUtil.getFieldValue(null, field);
        ReflectUtil.getFieldValue(new SupportBean(), field);
    }

    public void testGetFieldValueByNameEmpty() {
        ReflectUtil.getFieldValue(null, "test");
        String fieldName = null;
        ReflectUtil.getFieldValue(new SupportBean(), fieldName);
        ReflectUtil.getFieldValue(new EmptyBean(), "test");

        ReflectUtil.getFieldValue(new NotSupportBean(), NotSupportBean.class.getDeclaredFields()[2]);
    }

}

package com.baoyun.base.config.client.zookeeper;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.baoyun.base.config.client.bean.EmptyBean;
import com.baoyun.base.config.client.bean.TypeBean;
import com.baoyun.base.config.client.util.BeanToString;

public class BeanToStringTest extends TestCase {

    public void testToStringFull() {
        TypeBean typeBean = new TypeBean();
        typeBean.setBoolLittle(false);
        typeBean.setBoolBig(Boolean.TRUE);
        typeBean.setCharLittle('C');
        typeBean.setCharBig('A');
        typeBean.setDoubleLittle(12.345);
        typeBean.setDoubleBig(11.2345);
        typeBean.setFloatLittle(34.12f);
        typeBean.setFloatBig(34.678F);
        typeBean.setIntLittle(123);
        typeBean.setIntBig(234);
        typeBean.setLongLittle(7891012);
        typeBean.setLongBig(7891111221l);
        typeBean.setByteLittle((byte) 123);
        typeBean.setByteBig(Byte.valueOf("11"));
        typeBean.setShortLittle((short) 1122);
        typeBean.setShortBig(Short.valueOf("109"));
        typeBean.setString("stringTest");
        typeBean.setByteArrayLittle(new byte[] { 121, 123, 121, 98, 97 });
        typeBean.setCharArrayLittle(new char[] { 'A', 'D', 'M', '1', '好' });
        List<String> stringList = new ArrayList<String>();
        stringList.add("新的测试");
        stringList.add("测试字符串");
        typeBean.setStringList(stringList);

        String string = BeanToString.string(typeBean);
        System.out.println(" \t string=" + string);

        TestCase.assertTrue(string.contains("boolLittle="));
        TestCase.assertTrue(string.contains("boolBig="));
        TestCase.assertTrue(string.contains("charLittle="));
        TestCase.assertTrue(string.contains("charBig="));
        TestCase.assertTrue(string.contains("doubleLittle="));
        TestCase.assertTrue(string.contains("doubleBig="));
        TestCase.assertTrue(string.contains("floatLittle="));
        TestCase.assertTrue(string.contains("floatBig="));
        TestCase.assertTrue(string.contains("intLittle="));
        TestCase.assertTrue(string.contains("intBig="));
        TestCase.assertTrue(string.contains("longLittle="));
        TestCase.assertTrue(string.contains("longBig="));
        TestCase.assertTrue(string.contains("byteLittle="));
        TestCase.assertTrue(string.contains("byteBig="));
        TestCase.assertTrue(string.contains("shortLittle="));
        TestCase.assertTrue(string.contains("shortBig="));
        TestCase.assertTrue(string.contains("string="));
        TestCase.assertTrue(string.contains("byteArrayLittle="));
        TestCase.assertTrue(string.contains("charArrayLittle="));
        TestCase.assertTrue(string.contains("stringList="));

    }

    public void testToStringEmpty() {
        String emptyInput = BeanToString.string(null);
        System.out.println(" \t emptyInput=" + emptyInput);
        TestCase.assertEquals("", emptyInput);

        String emptyField = BeanToString.string(new EmptyBean());
        System.out.println(" \t emptyField=" + emptyField);
        TestCase.assertEquals("", emptyField);

    }

}

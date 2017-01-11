package com.baoyun.base.config.client.zookeeper;

import java.io.UnsupportedEncodingException;

import com.baoyun.base.config.client.util.BeanToString;
import com.baoyun.base.config.client.util.UTF8StringUtil;

import junit.framework.TestCase;

public class StringValueTest extends TestCase {

    public void testStringToByte() throws UnsupportedEncodingException {
        String value1 = "这是测试字符串";

        byte[] value1ByteArray = UTF8StringUtil.stringToByte(value1);
        System.out.println(" \t byteArray Hex: " + printHexString(value1ByteArray));
        String value1Revert = UTF8StringUtil.stringFromByte(value1ByteArray);
        TestCase.assertEquals(value1, value1Revert);

        byte[] value2ByteArray = UTF8StringUtil.stringToByte("");
        System.out.println(" \t byteArray2 Hex: " + printHexString(value2ByteArray));

        byte[] value3ByteArray = UTF8StringUtil.stringToByte("   ");
        System.out.println(" \t byteArray3 Hex: " + printHexString(value3ByteArray));

        byte[] value4ByteArray = UTF8StringUtil.stringToByte(null);
        System.out.println(" \t byteArray4 Hex: " + printHexString(value4ByteArray));
        TestCase.assertNull(value4ByteArray);

    }

    public void testStringFromByte() throws UnsupportedEncodingException {
        String value1 = UTF8StringUtil.stringFromByte(new byte[] {});
        System.out.println(" value1=" + value1);
        TestCase.assertEquals("", value1);

        String value2 = UTF8StringUtil.stringFromByte(null);
        System.out.println(" value2=" + value2);
        TestCase.assertNull(value2);
    }

    public void testStringToBoolean() {
        // true 测试
        String[] trueArray = new String[] { "TRUE", "true", "ON", "on", "True" };
        for (String value : trueArray) {
            boolean boolValue = UTF8StringUtil.stringToBoolean(value);
            TestCase.assertTrue(boolValue);
        }

        String[] falseArray = new String[] { "", "  ", "abcd", "false", "F", "123", "0", "1" };
        for (String value : falseArray) {
            boolean boolValue = UTF8StringUtil.stringToBoolean(value);
            TestCase.assertFalse(boolValue);
        }

        boolean boolNull = UTF8StringUtil.stringToBoolean(null);
        TestCase.assertFalse(boolNull);

    }

    public void testOther() {
        UTF8StringUtil stringValue = new UTF8StringUtil();
        System.out.println(" \t stringValue=" + BeanToString.string(stringValue));

    }

    public static String printHexString(byte[] b) {
        StringBuffer hex = new StringBuffer();
        if (b != null) {
            for (int i = 0; i < b.length; i++) {
                hex.append(Integer.toHexString(b[i] & 0xFF));

                // System.out.print(hex.toUpperCase() );
            }
        } else {
            hex.append("null");
        }
        return hex.toString();

    }

}

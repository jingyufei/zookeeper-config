package com.baoyun.base.config.client.util;

import java.io.UnsupportedEncodingException;

/**
 * String和byte[]使用 UTF-8字符集互相转换
 *
 */
public class UTF8StringUtil {

    public static final String charsetName = "UTF-8";

    /**
     * byte[] 转 String
     * 
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String stringFromByte(byte[] data) throws UnsupportedEncodingException {
        String value = null;
        if (data != null) {
            value = new String(data, charsetName);
        }
        return value;
    }

    /**
     * String 转 byte[]
     * 
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] stringToByte(String value) throws UnsupportedEncodingException {
        byte[] data = null;
        if (value != null) {
            data = value.getBytes(charsetName);
        }
        return data;
    }

    /**
     * String 转 boolean
     * 
     * @param value
     * @return
     */
    public static boolean stringToBoolean(String value) {
        boolean boolValue = false;
        if (value != null && ("on".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value))) {
            boolValue = true;
        }
        return boolValue;
    }

}

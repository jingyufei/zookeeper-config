package com.baoyun.base.config.client.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanToString {

    static final Logger log = LoggerFactory.getLogger(BeanToString.class);

    /**
     * 将 Object 基本类型 打印为String
     * 
     * @param object
     * @return
     */
    public static String string(Object object) {

        StringBuffer stringBuffer = new StringBuffer();

        if (object != null) {

            Field[] fieldArray = object.getClass().getDeclaredFields();
            if (fieldArray != null && fieldArray.length > 0) {
                stringBuffer.append("(");
                for (int fieldSize = 0; fieldSize < fieldArray.length; fieldSize++) {
                    Field field = fieldArray[fieldSize];
                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();
                    stringBuffer.append(fieldName);
                    stringBuffer.append("=");

                    try {
                        boolean access = field.isAccessible();
                        field.setAccessible(true);

                        Object fieldValue = field.get(object);

                        if (fieldValue == null) {
                            stringBuffer.append("null");
                        } else {
                            if (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
                                stringBuffer.append((boolean) fieldValue);
                            } else if (Character.class.equals(fieldType) || char.class.equals(fieldType)) {
                                stringBuffer.append((char) fieldValue);
                            } else if (Double.class.equals(fieldType) || double.class.equals(fieldType)) {
                                stringBuffer.append((double) fieldValue);
                            } else if (Float.class.equals(fieldType) || float.class.equals(fieldType)) {
                                stringBuffer.append((float) fieldValue);
                            } else if (Integer.class.equals(fieldType) || int.class.equals(fieldType)) {
                                stringBuffer.append((int) fieldValue);
                            } else if (Long.class.equals(fieldType) || long.class.equals(fieldType)) {
                                stringBuffer.append((long) fieldValue);
                            } else if (Byte.class.equals(fieldType) || byte.class.equals(fieldType)) {
                                stringBuffer.append((byte) fieldValue);
                            } else if (Short.class.equals(fieldType) || short.class.equals(fieldType)) {
                                stringBuffer.append((short) fieldValue);
                            } else if (String.class.equals(fieldType)) {
                                stringBuffer.append((String) fieldValue);
                            }
                        }

                        field.setAccessible(access);

                    } catch (Exception e) {
                        log.error(" string-Exception object:{} exception:{} ", object, e);
                    }

                    if (fieldSize < (fieldArray.length - 1)) {
                        stringBuffer.append(", ");
                    }

                }
                stringBuffer.append(")");
            }
        }

        return stringBuffer.toString();
    }

}

package com.baoyun.base.config.client.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectUtil {

    static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 更新 bean 的beanField字段的值 为fieldValue
     * <p>
     * 目前支持Long, Integer, Boolean, String
     * </p>
     * <p>
     * 其中 输入为 "on" 或 "true" 表示 true
     * </p>
     * 
     * @param bean
     * @param beanField
     * @param fieldValue
     * @return
     */
    public static boolean setFieldValue(Object bean, Field beanField, String fieldValue) {
        boolean operation = false;

        boolean changeAccess = false;
        try {
            if (bean != null && beanField != null && fieldValue != null) {
                if (!beanField.isAccessible()) {
                    beanField.setAccessible(true);
                    changeAccess = true;
                }
                Class<?> fieldType = beanField.getType();

                if (long.class.equals(fieldType) || Long.class.equals(fieldType)) {
                    beanField.set(bean, Long.valueOf(fieldValue));
                    operation = true;
                } else if (int.class.equals(fieldType) || Integer.class.equals(fieldType)) {
                    beanField.set(bean, Integer.valueOf(fieldValue));
                    operation = true;
                } else if (boolean.class.equals(fieldType) || Boolean.class.equals(fieldType)) {
                    boolean boolValue = UTF8StringUtil.stringToBoolean(fieldValue);
                    beanField.set(bean, boolValue);
                    operation = true;
                } else if (String.class.equals(fieldType)) {
                    beanField.set(bean, fieldValue);
                    operation = true;
                } else {
                    log.error(" setFieldValue-notSupportType bean:{} beanField:{} fieldValue:{} type:{} ", bean, beanField, fieldValue, beanField.getClass());
                }

            } else {
                log.error(" setFieldValue-null bean:{} beanField:{} fieldValue:{} must all not null.", bean, beanField, fieldValue);
            }
        } catch (NumberFormatException e) {
            log.error(" setFieldValue-NumberFormatException bean:{} beanField:{} fieldValue:{} ", bean, beanField, fieldValue);
        } catch (IllegalArgumentException e) {
            log.error(" setFieldValue-IllegalArgumentException bean:{} beanField:{} fieldValue:{} ", bean, beanField, fieldValue);
        } catch (IllegalAccessException e) {
            log.error(" setFieldValue-IllegalAccessException bean:{} beanField:{} fieldValue:{} ", bean, beanField, fieldValue);
        } catch (Exception e) {
            log.error(" setFieldValue-Exception bean:{} beanField:{} fieldValue:{} ", bean, beanField, fieldValue);
        } finally {
            // 还原访问权限
            if (changeAccess) {
                beanField.setAccessible(false);
            }
        }

        return operation;
    }

    /**
     * 根据 字段名称 修改对象字段值
     * 
     * @param target
     * @param fieldName
     * @param value
     * @return
     */
    public static boolean setFieldValue(Object targetObject, String fieldName, Object fieldValue) {
        boolean operation = false;
        boolean changeAccess = false;
        Field fieldChange = null;
        try {
            if (targetObject != null && fieldName != null) {
                Field[] fieldArray = targetObject.getClass().getDeclaredFields();
                if (fieldArray != null && fieldArray.length > 0) {
                    for (Field field : fieldArray) {
                        if (fieldName.equals(field.getName())) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                                changeAccess = true;
                                fieldChange = field;
                            }
                            field.set(targetObject, fieldValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(" setFieldValue targetObject:{} fieldName:{} fieldValue:{} exception:{} ", targetObject, fieldName, fieldValue, e);
        } finally {
            if (changeAccess && fieldChange != null) {
                fieldChange.setAccessible(false);
            }
        }

        return operation;
    }

    /**
     * 获取 bean 的beanField字段的值 为fieldValue
     * <p>
     * 目前支持Long, Integer, Boolean, String
     * </p>
     * <p>
     * 其中 输入为 "on" 或 "true" 表示 true
     * </p>
     * 
     * @param bean
     * @param beanField
     * @param fieldValue
     * @return
     */
    public static Object getFieldValue(Object bean, Field beanField) {
        Object value = null;
        boolean changeAccess = false;
        try {
            if (bean != null && beanField != null) {
                if (!beanField.isAccessible()) {
                    beanField.setAccessible(true);
                    changeAccess = false;
                }

                value = beanField.get(bean);

            } else {
                log.error(" getFieldValue-null bean:{} beanField:{} must all not null.", bean, beanField);
            }
        } catch (Exception e) {
            log.error(" getFieldValue-Exception bean:{} beanField:{} ", bean, beanField);
        } finally {
            if (changeAccess) {
                beanField.setAccessible(false);
            }
        }

        return value;
    }

    /**
     * 根据 字段名称 获取对象字段值
     * 
     * @param target
     * @param fieldName
     * @param value
     * @return
     */
    public static Object getFieldValue(Object targetObject, String fieldName) {
        Object value = null;
        try {
            if (targetObject != null && fieldName != null) {
                Field[] fieldArray = targetObject.getClass().getDeclaredFields();
                if (fieldArray != null && fieldArray.length > 0) {
                    for (Field field : fieldArray) {
                        if (fieldName.equals(field.getName())) {
                            value = getFieldValue(targetObject, field);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(" getFieldValue targetObject:{} fieldName:{} exception:{} ", targetObject, fieldName, e);
        }

        return value;
    }
}

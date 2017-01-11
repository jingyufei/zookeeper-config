package com.baoyun.base.config.client.bean;

import java.lang.reflect.Field;

/**
 * 实施刷新bean
 *
 */
public class RealtimeBean {

    /**
     * 需要刷新的Bean
     */
    private Object bean;

    /**
     * 需要刷新的Bean名称
     */
    private String beanName;

    /**
     * 字段
     */
    private Field field;

    /**
     * 需要刷新的 键值
     */
    private String key;

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "RealtimeBean [bean=" + bean + ", beanName=" + beanName
				+ ", field=" + field + ", key=" + key + "]";
	}

	public RealtimeBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RealtimeBean(Object bean, String beanName, Field field, String key) {
		super();
		this.bean = bean;
		this.beanName = beanName;
		this.field = field;
		this.key = key;
	}
}

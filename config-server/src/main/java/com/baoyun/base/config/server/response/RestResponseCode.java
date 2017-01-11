package com.baoyun.base.config.server.response;

/**
 * Created by Pasenger on 2014/12/18.
 */
public interface RestResponseCode {
    /**
     * 请求成功
     */
    public static final int SUCCESS = 1;
    public static final String SUCCESS_DESC = "success";
    
    public static final int NO_THE_GROUP = 1000;
    public static final String No_THE_GROUP_DESC = "未找到该组别";
    
    public static final int NO_THE_PROPERTY = 1001;
    public static final String No_THE_PROPERTY_DESC = "未找到该配置项";
    
    public static final int REPEATED_GROPU = 1002;
    public static final String REPEATED_GROPU_DESC = "重复的组别名";
    
    public static final int REPEATED_GROPU_PROPERTY = 1003;
    public static final String REPEATED_GROPU_PROPERTY_DESC = "组别名和配置项名均重复";
    
    public static final int REPEATED_PROPERTY = 1004;
    public static final String REPEATED_PROPERTY_DESC = "配置项已存在";
    
    /**
     * 操作失败
     */
    public static final int FAILURE = 1005;
    public static final String FAILURE_DESC = "操作失败,请确认后重试!";
    
    /**
     * 参数错误
     */
    public static final int PARAMETER_ERROR = 499;
    public static final String PARAMETER_ERROR_DESC = "参数异常,请重试!";
    
    /**
     * 内部错误
     */
    public static final int INTERNAL_ERROR = 500;
    public static final String INTERNAL_ERROR_DESC = "内部发生错误,请联系管理员!";
    
   
}

package com.baoyun.base.config.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    public static final String FULL_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DAY_FORMAT = "yyyy-MM-dd";
    public static final String DAY_FORMAT_FIGURE = "yyyyMMdd";
    public static final String YEAR_MONTH = "yyyyMM";
    
    public static final String REST_EXPIRE_TIME = "yyyyMMddHHmmss";
    public static final String REST_TIMESTAMP = "yyyyMMddHHmmssSSS";

    /**
     * 返回完整的 {@link #FULL_FORMAT} = {@value #FULL_FORMAT}
     * 
     * @param date
     * @return
     */
    public static String formatDateByFull(long date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(FULL_FORMAT);
        return simpleFormat.format(new Date(date));
    }

    /**
     * 返回完整的 {@link #FULL_FORMAT} = {@value #FULL_FORMAT}
     * 
     * @param date
     * @return
     */
    public static String formatDateByFull(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(FULL_FORMAT);
        return simpleFormat.format(date);
    }

    /**
     * 格式化 Date 为 {@link #YEAR_MONTH} = {@value #YEAR_MONTH}
     * 
     * @param date
     * @return
     */
    public static String formatYearAndMonth(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(YEAR_MONTH);
        return simpleFormat.format(date);
    }

    /**
     * 格式化 Date 为 {@link #DATE_TIME_FORMAT} = {@value #DATE_TIME_FORMAT}
     * 
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        return simpleFormat.format(date);
    }

    /**
     * 格式化 long型日期 为 {@link #DATE_TIME_FORMAT} = {@value #DATE_TIME_FORMAT}
     * 
     * @param date
     * @return
     */
    public static String formatDateTime(long longDate) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        return simpleFormat.format(new Date(longDate));
    }
    
    /**
     * 格式化Date 为{@link #DAY_FORMAT} = {@value #DAY_FORMAT}
     * 
     * @param date
     * @return
     */
    public static String formatDay(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(DAY_FORMAT);
        return simpleFormat.format(date);
    }

    /**
     * 格式化long型日期为 {@link #DAY_FORMAT} = {@value #DAY_FORMAT}
     * 
     * @param longDate
     * @return
     */
    public static String formatDay(long longDate) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(DAY_FORMAT);
        return simpleFormat.format(new Date(longDate));
    }
    
    /**
     * 格式化 Date为 接入层 过期时间
     * 
     * @param date
     * @return
     */
    public static String formatExpireTime(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(REST_EXPIRE_TIME);
        return simpleFormat.format(date);
    }

    /**
     * 格式化 Date 为接入层 时间戳
     * 
     * @param date
     * @return
     */
    public static String formatTimestamp(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(REST_TIMESTAMP);
        return simpleFormat.format(date);
    }

}

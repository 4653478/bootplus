package com.age.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 *
 * @author age
 * @Email 4653478@qq.com
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化所有格式
     */
    private final static SimpleDateFormat[] SIMPLE_DATE_FORMATS = new SimpleDateFormat[6];

    static {
        // 静态初始化
        SIMPLE_DATE_FORMATS[0] = new SimpleDateFormat("yyyy-MM-dd");
        SIMPLE_DATE_FORMATS[1] = new SimpleDateFormat("yyyy年MM月dd日");
        SIMPLE_DATE_FORMATS[2] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SIMPLE_DATE_FORMATS[3] = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        SIMPLE_DATE_FORMATS[4] = new SimpleDateFormat("yyyy/MM/dd");
        SIMPLE_DATE_FORMATS[5] = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
    }

    /**
     * 转换日期为String使用默认的格式(yyyy-MM-dd)
     *
     * @param date Date
     * @return String
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 安全用法
     */
    private static final ThreadLocal<DateFormat> TL_DATE_FORMATTER =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_TIME_PATTERN));

    /**
     * 转换日期为String
     *
     * @param date    Date
     * @param pattern 转换的格式
     * @return String
     */
    public static String format(Date date, String pattern) {
        try {
            if (date != null) {
                if (null != pattern) {
                    TL_DATE_FORMATTER.set(new SimpleDateFormat(pattern));
                }
                return TL_DATE_FORMATTER.get().format(date);
            }
            return null;
        } finally {
            TL_DATE_FORMATTER.remove();
        }
    }

    /**
     * 将字符串转为日期
     *
     * @param date 字符串
     * @return Date
     * @throws Exception
     */
    public static Date parse(String date) throws Exception {
        if (date != null) {
            for (SimpleDateFormat df : SIMPLE_DATE_FORMATS) {
                try {
                    return df.parse(date);
                } catch (ParseException e) {
                    continue;
                }
            }

        }
        throw new Exception("转换失败");
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳
     */
    public static Long getCurrentUnixTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 判断日期是否是周末
     *
     * @param d 日期
     * @return 是否是周末
     */
    public static boolean isWeekend(Date d) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(d);

        int i = myCalendar.get(Calendar.DAY_OF_WEEK);
        // 星期日i==1，星期六i==7
        if (i == Calendar.SUNDAY || i == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }

    /**
     * 判断日期是否是周末
     *
     * @param d yyyy-MM-dd
     * @return 是否是周末
     * @throws Exception
     */
    public static boolean isWeekend(String d) throws Exception {
        return DateUtils.isWeekend(DateUtils.parse(d));
    }

    /**
     * 判断日期是否是周末
     *
     * @param timestamp 时间戳
     * @return 是否是周末
     * @throws Exception
     */
    public static boolean isWeekendByTimestamp(String timestamp) throws Exception {
        return isWeekend(AgeUtil.stampToDate(timestamp));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(betweenDays));
    }

}

package com.jinzht.pro1.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具
 */
public class DateUtils {

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期逻辑
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static String timeLogic(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        Date date = strToDate(dateStr);
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long time = (now - past) / 1000;

        StringBuffer sb = new StringBuffer();
        if (time > 0 && time < 60) { // 1小时内
            return sb.append(time + "秒前").toString();
        } else if (time > 60 && time < 3600) {
            return sb.append(time / 60 + "分钟前").toString();
        } else if (time >= 3600 && time < 3600 * 24) {
            return sb.append(time / 3600 + "小时前").toString();
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            return sb.append("昨天").toString();
        } else if (time >= 3600 * 48 && time < 3600 * 72) {
            return sb.append("前天").toString();
        } else if (time >= 3600 * 72) {
            return dateToString(dateStr);
        }
        return dateToString(dateStr);
    }

    /**
     * 日期字符串转换为Date
     *
     * @param dateStr
     * @return
     */
    public static Date strToDate(String dateStr) {
        Date date = null;
        if (!TextUtils.isEmpty(dateStr)) {
            try {
                date = DATE_FORMAT.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 日期转换为字符串
     *
     * @param timeStr
     * @return
     */
    public static String dateToString(String timeStr) {
        // 判断是否是今年
        Date date = strToDate(timeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 如果是今年的话，才去“xx月xx日”日期格式
//        if (calendar.get(Calendar.YEAR) == getYear()) {
//            return DATE_FORMAT_TILL_DAY_CURRENT_YEAR.format(date);
//        }
        return DATE_FORMAT.format(date);
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    // 获取去年的年份
    public static int getLastYear() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        return c.get(Calendar.YEAR);
    }

    // 获取下一年年份
    public static int getNextYear() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 1);
        return c.get(Calendar.YEAR);
    }

    // 获取上一个月的月份
    public static int getLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        return c.get(Calendar.MONTH) + 1;
    }

    // 获取下一个月的月份
    public static int getNextMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        return c.get(Calendar.MONTH) + 1;
    }
}

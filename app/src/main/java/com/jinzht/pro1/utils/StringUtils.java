package com.jinzht.pro1.utils;

import java.math.BigDecimal;

/**
 * 字符串工具类
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    // 没有输入任何值
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    // 输入了空格，不是有效的值
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    // 相等
    public static boolean isEquals(String actual, String expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    // 长度
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    // 校验电话号码是否合法
    public static boolean isTel(String tel) {
        String telRegex = "^((1[3,5,8][0-9])|(14[5,7])|(17[,0,6,7,8]))\\d{8}$";
        return tel.matches(telRegex);
    }

    // byte转换成KB或者MB
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }
}

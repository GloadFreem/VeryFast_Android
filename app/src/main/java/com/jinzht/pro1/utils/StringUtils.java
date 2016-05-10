package com.jinzht.pro1.utils;

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
}

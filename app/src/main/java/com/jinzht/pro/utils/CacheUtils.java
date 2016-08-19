package com.jinzht.pro.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 缓存工具类
 */
public class CacheUtils {

    /**
     * 保存数据
     *
     * @param ser  流
     * @param name 文件名
     * @return
     */
    public static boolean saveObject(Serializable ser, String name) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = UiUtils.getContext().openFileOutput(name, 0);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取数据
     *
     * @param name 文件名
     * @return
     */
    public static Serializable readObject(String name) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = UiUtils.getContext().openFileInput(name);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

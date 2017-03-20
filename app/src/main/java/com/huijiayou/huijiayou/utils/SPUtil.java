package com.huijiayou.huijiayou.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.huijiayou.huijiayou.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtil {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "HuiJiaYou";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static <T>void put(String key, T object) {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object == null) return;
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    public static <T> T get(String key, T defaultObject) {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Object o = null;
        if (defaultObject instanceof String) {
            o = sp.getString(key, defaultObject.toString());
        } else if (defaultObject instanceof Boolean) {
            o = sp.getBoolean(key, ((Boolean) defaultObject).booleanValue());
        } else if (defaultObject instanceof Integer) {
            o = sp.getInt(key, ((Integer) defaultObject).intValue());
        } else if (defaultObject instanceof Float) {
            o = sp.getFloat(key, ((Float) defaultObject).floatValue());
        } else if (defaultObject instanceof Long) {
            o = sp.getLong(key, ((Long) defaultObject).longValue());
        }
        T t = (T) o;
        return t;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 保存对象
     * @param classData
     * @param key
     */
    public static void saveClassData(Object classData, String key){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(classData);
            String base64Str = new String(Base64.encode(baos.toByteArray(),1));
            oos.close();
            baos.close();

            SharedPreferences sp = MyApplication.getContext().getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key,base64Str);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object getClassData(String key){
        Object object = null;
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        String base64Str = sp.getString(key, "");

        byte[] base64 = Base64.decode(base64Str.getBytes(), 1);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            object = ois.readObject();
            ois.close();
            bais.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

}
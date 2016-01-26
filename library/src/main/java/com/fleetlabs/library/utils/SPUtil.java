package com.fleetlabs.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.androidannotations.api.sharedpreferences.SharedPreferencesCompat;

/**
 * Created by Aaron.Wu on 2015/9/10.
 */
public class SPUtil {

    /**
     * @param context
     * @param key
     * @param data
     */
    public static void set(Context context, String spName, String key, Object data) {

        String type = data.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }

    /**
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object get(Context context, String spName, String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);

        if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sp.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defValue);
        }
        return null;
    }

    /**
     * @param context
     * @param key
     */
    public static void remove(Context context, String spName, String key) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String spName, String key) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.contains(key);
    }
}
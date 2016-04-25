package com.creditease.checkcars.data.sp.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreferences 操作
 *
 * @author 子龍
 * @date 2015年1月9日
 * @company 宜信
 */
public class SPHelper
{

    public static boolean getBoolean(Context context, String spFileName, String key, boolean defaultB)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(key, defaultB);
    }

    public static int getInt(Context context, String spFileName, String key)
    {
        return getInt(context, spFileName, key, -1);
    }

    public static int getInt(Context context, String spFileName, String key, int defaultValue)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        return sharedPrefs.getInt(key, defaultValue);
    }

    public static long getLong(Context context, String spFileName, String key)
    {
        return getLong(context, spFileName, key, -1L);
    }

    public static long getLong(Context context, String spFileName, String key, long defaultValue)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        return sharedPrefs.getLong(key, defaultValue);
    }

    public static String getString(Context context, String spFileName, String key)
    {
        return getString(context, spFileName, key, "");
    }

    public static String getString(Context context, String spFileName, String key, String defaultStr)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        return sharedPrefs.getString(key, defaultStr);
    }

    public static void putBoolean(Context context, String spFileName, String key, boolean content)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        sharedPrefs.edit().putBoolean(key, content).commit();
    }

    public static void putInt(Context context, String spFileName, String key, int content)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        sharedPrefs.edit().putInt(key, content).commit();
    }

    public static void putLong(Context context, String spFileName, String key, long content)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        sharedPrefs.edit().putLong(key, content).commit();
    }

    public static void putString(Context context, String spFileName, String key, String content)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(key, content).commit();
    }

    private SPHelper()
    {
    }

}

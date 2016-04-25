package com.creditease.checkcars.data.sp.base;

import android.content.Context;

/**
 * @author 子龍
 * @date 2015年1月9日
 * @company 宜信
 */
public abstract class SP
{
    protected static SP sp;
    protected Context context;

    protected SP(Context context)
    {
        this.context = context;
    }

    public boolean getBoolean(String key, boolean defaultB)
    {
        return SPHelper.getBoolean(context, getSPFileName(), key, defaultB);
    }

    public int getInt(String key)
    {
        return SPHelper.getInt(context, getSPFileName(), key);
    }

    public int getInt(String key, int defaultValue)
    {
        return SPHelper.getInt(context, getSPFileName(), key, defaultValue);
    }

    public long getLong(String key)
    {
        return SPHelper.getLong(context, getSPFileName(), key);
    }

    public long getLong(String key, long defaultValue)
    {
        return SPHelper.getLong(context, getSPFileName(), key, defaultValue);
    }

    public abstract String getSPFileName();

    public String getString(String key)
    {
        return SPHelper.getString(context, getSPFileName(), key);
    }

    public String getString(String key, String defaultStr)
    {
        return SPHelper.getString(context, getSPFileName(), key, defaultStr);
    }

    public void putBoolean(String key, boolean content)
    {
        SPHelper.putBoolean(context, getSPFileName(), key, content);
    }

    public void putInt(String key, int content)
    {
        SPHelper.putInt(context, getSPFileName(), key, content);
    }

    public void putLong(String key, long content)
    {
        SPHelper.putLong(context, getSPFileName(), key, content);
    }

    public void putString(String key, String content)
    {
        SPHelper.putString(context, getSPFileName(), key, content);
    }

}

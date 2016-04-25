/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * @author noah.zheng
 */
public class NetWorkUtils
{
    public enum NetWorkState
    {
        WIFI, MOBILE, NONE;
    }

    private Context mContext;
    public State wifiState = null;

    public State mobileState = null;

    public NetWorkUtils(Context context)
    {
        mContext = context;
    }

    /**
     * 获取当前的网络状态
     *
     * @return
     */
    public NetWorkState getConnectState()
    {
        ConnectivityManager manager =
                ( ConnectivityManager ) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        manager.getActiveNetworkInfo();
        wifiState = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ( info != null )
        {
            mobileState = info.getState();
        }
        if ( (wifiState != null) && (mobileState != null) && (State.CONNECTED != wifiState)
                && (State.CONNECTED == mobileState) )
        {
            return NetWorkState.MOBILE;
        } else if ( (wifiState != null) && (mobileState != null) && (State.CONNECTED != wifiState)
                && (State.CONNECTED != mobileState) )
        {
            return NetWorkState.NONE;
        } else if ( (wifiState != null) && (State.CONNECTED == wifiState) )
        {
            return NetWorkState.WIFI;
        }
        return NetWorkState.NONE;
    }

}

package com.creditease.checkcars.ui.act.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.creditease.checkcars.tools.LocalUtils;
import com.creditease.checkcars.tools.NetWorkUtils;
import com.creditease.checkcars.tools.NetWorkUtils.NetWorkState;

@SuppressLint( "HandlerLeak" )
public class UpLoadPosService extends Service
{
    private static final int INTERVAL = 30 * 60 * 1000;

    private Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            startLocation();
        }

    };

    private LocalBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    public class LocalBinder extends Binder
    {
        UpLoadPosService getService()
        {
            return UpLoadPosService.this;
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        startLocation();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private void startLocation()
    {
        NetWorkUtils netUtils = new NetWorkUtils(getBaseContext());
        if ( netUtils != null && netUtils.getConnectState() != NetWorkState.NONE )
        {
            LocalUtils.getUtils(getApplicationContext()).startLocation();
        }
        mHandler.sendEmptyMessageDelayed(0, INTERVAL);
    }
}

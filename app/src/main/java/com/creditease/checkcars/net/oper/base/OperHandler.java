package com.creditease.checkcars.net.oper.base;

import android.os.Handler;
import android.os.Looper;

public class OperHandler extends Handler
{
    private static OperHandler handler;

    public static OperHandler getHandel()
    {
        if ( handler == null )
        {
            handler = new OperHandler();
        }
        return handler;
    }

    public static void initOperHandler()
    {
        if ( handler == null )
        {
            handler = new OperHandler();
        }
    }

    private OperHandler()
    {
        super(Looper.myLooper());
    }

}

package com.creditease.checkcars.ui.act.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.creditease.checkcars.ui.act.service.UpLoadPosService;

public class BootReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent i = new Intent(context, UpLoadPosService.class);
        context.startService(i);
    }

}

package com.creditease.checkcars.ui.act;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.creditease.checkcars.R;
import com.creditease.checkcars.msgpush.bean.NotiObj;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.widget.NotifyMsgItemLayout;

/**
 * @author zgb
 */
public class NotifyMsgActivity extends BaseActivity
{
    private LinearLayout mMainLayout;
    private List< NotiObj > mList;

    @Override
    protected void initViews()
    {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.notify_msg);
        mMainLayout = ( LinearLayout ) findViewById(R.id.notify_msg_layout);
        mList = new ArrayList< NotiObj >();
    }

    @Override
    protected void initData()
    {
    }

    @Override
    protected void initEvents()
    {

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getData(getIntent());
    }


    private void addMsgItem()
    {
        if ( mList.size() > 0 )
        {
            mMainLayout.removeAllViews();
            for ( NotiObj obj : mList )
            {
                NotifyMsgItemLayout item = new NotifyMsgItemLayout(this, obj);
                mMainLayout.addView(item);
            }
            mList.clear();
        } else
        {
            closeOneAct(NotifyMsgActivity.class);
        }
    }


    /**
     * 启动模式为SingleInstance，因此需要重写onNewIntent方法
     */
    @SuppressWarnings( "deprecation" )
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        getData(intent);
        PowerManager pm = ( PowerManager ) this.getSystemService(Context.POWER_SERVICE);
        if ( !pm.isScreenOn() )
        {
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();
        }
    }

    private void getData(Intent intent)
    {
        ArrayList< NotiObj > objs = getIntent().getParcelableArrayListExtra("msglist");
        if ( objs != null && objs.size() > 0 )
        {
            mList.addAll(objs);
            addMsgItem();
        }
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }

}

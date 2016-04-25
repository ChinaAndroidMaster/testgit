package com.creditease.checkcars.ui.act.main.saler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;

import com.creditease.checkcars.R;
import com.creditease.checkcars.msgpush.MsgPushManager;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.net.uploadimg.ImageUploadManager;
import com.creditease.checkcars.net.ver.VersionUpdateManager;
import com.creditease.checkcars.ui.act.MineSettingActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.main.FragmentOrders;
import com.creditease.checkcars.ui.act.service.UpLoadPosService;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年7月8日
 * @company CREDITEASE
 */
@SuppressLint( "InflateParams" )
public class MainSalerActivity extends BaseActivity implements OnTabChangeListener, OnClickListener
{

    private FragmentTabHost mTabHost;
    private View tabDetection, tabAllcover;
    private ImageView detectionImgV, allcoverImgV;
    private LayoutInflater inflater;
    private int[] drawableIds = {R.drawable.tab_detection_unselect,
            R.drawable.tab_detection_selected, R.drawable.tab_allcover_unselect,
            R.drawable.tab_allcover_selected};
    private ImageView settingImgV;

    private Drawable[][] drawables = new Drawable[2][2];

    private long firstTime = 0;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.main_tabs_saler);
        inflater = LayoutInflater.from(this);
        mTabHost = ( FragmentTabHost ) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        tabDetection = inflater.inflate(R.layout.include_tab_topview_saler_detection, null);
        tabAllcover = inflater.inflate(R.layout.include_tab_topview_saler_allcover, null);
        detectionImgV = ( ImageView ) tabDetection.findViewById(R.id.saler_topview_detection_imgv);
        allcoverImgV = ( ImageView ) tabAllcover.findViewById(R.id.saler_topview_allcover_imgv);
        settingImgV = ( ImageView ) findViewById(R.id.main_tabs_saler_setting);
        for ( int i = 0, len = drawableIds.length; i < len; i++ )
        {
            int r = i / 2;
            if ( drawables[r] == null )
            {
                drawables[r] = new Drawable[2];
            }
            drawables[r][i % 2] = getResources().getDrawable(drawableIds[i]);
        }
        mTabHost.addTab(mTabHost.newTabSpec(FragmentDeteciton.TAG).setIndicator(tabDetection),
                FragmentDeteciton.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(FragmentAllcover.TAG).setIndicator(tabAllcover),
                FragmentAllcover.class, null);

        mTabHost.getTabWidget().setDividerDrawable(null);
    }

    @Override
    protected void initData()
    {
        // 启动service,每十分钟记录一次师傅位置信息
        Intent intent = new Intent(this, UpLoadPosService.class);
        startService(intent);
        // 检测版本
        VersionUpdateManager.getInstance().checkVersionWS(true, getContext());
        // 更新报告字典
        OperationFactManager.getManager().updateBaseReport(getApplicationContext(), null);
    }

    @Override
    protected void initEvents()
    {
        mTabHost.setOnTabChangedListener(this);
        settingImgV.setOnClickListener(this);
    }


    public void loadOrders()
    {
        if ( FragmentOrders.getFragment() != null )
        {
            FragmentOrders.getFragment().loadOrdersWS();
        }
    }

    @Override
    public void onBackPressed()
    {
        long secondTime = System.currentTimeMillis();
        if ( (secondTime - firstTime) < 1000 )
        {// 如果两次按键时间间隔大于1000毫秒，则不退出
            super.onBackPressed();
            MobclickAgent.onKillProcess(getContext());
            ImageUploadManager.getManager(getApplicationContext()).stopService();
            finishAllActivity();
            // appExit();
        } else
        {
            showToast("再按一次返回键退出程序", 800);
            firstTime = secondTime;// 更新firstTime
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MsgPushManager.getManager(this).ableUmengPush(this);
        updateMsgPushData();
    }

    @Override
    public void onTabChanged(String tabId)
    {
        if ( tabId.equals(FragmentDeteciton.TAG) )
        {
            detectionImgV.setImageDrawable(drawables[0][1]);
            allcoverImgV.setImageDrawable(drawables[1][0]);
        } else if ( tabId.equals(FragmentAllcover.TAG) )
        {
            detectionImgV.setImageDrawable(drawables[0][0]);
            allcoverImgV.setImageDrawable(drawables[1][1]);
        }
    }


    @Override
    public BaseActivity getContext()
    {
        return this;
    }

    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.main_tabs_saler_setting:
                BaseActivity.launch(getContext(), MineSettingActivity.class, null);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;

            default:
                break;
        }
    }
}

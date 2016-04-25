package com.creditease.checkcars.ui.act.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.msgpush.MsgPushManager;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.net.uploadimg.ImageUploadManager;
import com.creditease.checkcars.net.ver.VersionUpdateManager;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.service.UpLoadPosService;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年7月8日
 * @company CREDITEASE
 */
@SuppressLint( "InflateParams" )
public class MainActivity extends BaseActivity implements OnTabChangeListener
{

    private FragmentTabHost mTabHost;
    private View bottomViewOrders, bottomViewActivities, bottomViewMine;
    private TextView tabOrdersTV, tabActivitiesTV, tabMineTV;
    private LayoutInflater inflater;
    private int[] drawableIds = {R.drawable.icon_orders_unselected, R.drawable.icon_orders_selected,
            R.drawable.icon_activities_unselected, R.drawable.icon_activities_selected,
            R.drawable.icon_mine_unselected, R.drawable.icon_mine_selected,
            R.drawable.icon_qa_unselected, R.drawable.icon_qa_selected};

    private Drawable[][] drawables = new Drawable[4][2];
    private int textSelectedColor;
    private int textUnselectedColor;
    private ImageView guide;

    private long firstTime = 0;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.main_tabs);
        inflater = LayoutInflater.from(this);
        textSelectedColor = getResources().getColor(R.color.color_text_selected);
        textUnselectedColor = getResources().getColor(R.color.color_text_black_gray);
        mTabHost = ( FragmentTabHost ) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        bottomViewOrders = inflater.inflate(R.layout.include_tab_bottomview, null);
        bottomViewActivities = inflater.inflate(R.layout.include_tab_bottomview, null);
        bottomViewMine = inflater.inflate(R.layout.include_tab_bottomview, null);
        // bottomViewQA = inflater
        // .inflate(R.layout.include_tab_bottomview, null);

        tabOrdersTV = ( TextView ) bottomViewOrders.findViewById(R.id.tab_bottomview_tv);
        tabActivitiesTV = ( TextView ) bottomViewActivities.findViewById(R.id.tab_bottomview_tv);
        tabMineTV = ( TextView ) bottomViewMine.findViewById(R.id.tab_bottomview_tv);
        // tabQATV = (TextView) bottomViewQA
        // .findViewById(R.id.tab_bottomview_tv);

        tabOrdersTV.setText(R.string.tab_orders);
        tabActivitiesTV.setText(R.string.tab_activities);
        tabMineTV.setText(R.string.tab_mine);
        // tabQATV.setText(R.string.tab_qa);

        tabOrdersTV.setTextColor(textSelectedColor);
        tabActivitiesTV.setTextColor(textUnselectedColor);
        tabMineTV.setTextColor(textUnselectedColor);
        // tabQATV.setTextColor(textUnselectedColor);

        for ( int i = 0, len = drawableIds.length; i < len; i++ )
        {
            int r = i / 2;
            if ( drawables[r] == null )
            {
                drawables[r] = new Drawable[2];
            }
            drawables[r][i % 2] = getResources().getDrawable(drawableIds[i]);
        }
        tabOrdersTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0][1], null, null);
        tabActivitiesTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[1][0], null, null);
        tabMineTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[2][0], null, null);
        // tabQATV.setCompoundDrawablesWithIntrinsicBounds(null,
        // drawables[3][0], null, null);

        mTabHost.addTab(mTabHost.newTabSpec(FragmentOrders.TAG).setIndicator(bottomViewOrders),
                FragmentOrders.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(FragmentMine.TAG).setIndicator(bottomViewMine),
                FragmentMine.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(FragmentActivities.TAG).setIndicator(bottomViewActivities),
                FragmentActivities.class, null);
        // mTabHost.addTab(mTabHost.newTabSpec(FragmentQuesAnswer.TAG)
        // .setIndicator(bottomViewQA), FragmentQuesAnswer.class,
        // null);

        mTabHost.getTabWidget().setDividerDrawable(null);
        guide = ( ImageView ) findViewById(R.id.guide_imgv);
        guide.setVisibility(View.GONE);
        // String currentVer = Util.getAppVersionName(this);
        // String ver = SharePrefenceManager.getAppVersion(this);
        // if ("".equals(ver) || !currentVer.equals(ver)) {
        // // SharePrefenceManager.setAppVersion(this, currentVer);
        // guide.setVisibility(View.VISIBLE);
        // guide.setImageResource(R.drawable.guide_01);
        // guide.setTag(1);
        // guide.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // int tag = (Integer) guide.getTag();
        // if (tag == 2) {
        // // SharePrefenceManager.showGuide(getContext());
        // guide.setVisibility(View.GONE);
        // }
        // guide.setImageResource(R.drawable.guide_02);
        // guide.setTag(tag + 1);
        // }
        // });
        // } else {
        // guide.setVisibility(View.GONE);
        // }
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
        if ( tabId.equals(FragmentOrders.TAG) )
        {
            tabOrdersTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0][1], null, null);
            tabActivitiesTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[1][0], null, null);
            tabMineTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[2][0], null, null);
            // tabQATV.setCompoundDrawablesWithIntrinsicBounds(null,
            // drawables[3][0], null, null);

            tabOrdersTV.setTextColor(textSelectedColor);
            tabActivitiesTV.setTextColor(textUnselectedColor);
            tabMineTV.setTextColor(textUnselectedColor);
            // tabQATV.setTextColor(textUnselectedColor);
        } else if ( tabId.equals(FragmentActivities.TAG) )
        {
            tabOrdersTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0][0], null, null);
            tabActivitiesTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[1][1], null, null);
            tabMineTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[2][0], null, null);
            // tabQATV.setCompoundDrawablesWithIntrinsicBounds(null,
            // drawables[3][0], null, null);

            tabOrdersTV.setTextColor(textUnselectedColor);
            tabActivitiesTV.setTextColor(textSelectedColor);
            tabMineTV.setTextColor(textUnselectedColor);
            // tabQATV.setTextColor(textUnselectedColor);
        } else if ( tabId.equals(FragmentMine.TAG) )
        {
            tabOrdersTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0][0], null, null);
            tabActivitiesTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[1][0], null, null);
            tabMineTV.setCompoundDrawablesWithIntrinsicBounds(null, drawables[2][1], null, null);
            // tabQATV.setCompoundDrawablesWithIntrinsicBounds(null,
            // drawables[3][0], null, null);

            tabOrdersTV.setTextColor(textUnselectedColor);
            tabActivitiesTV.setTextColor(textUnselectedColor);
            tabMineTV.setTextColor(textSelectedColor);
            // tabQATV.setTextColor(textUnselectedColor);
        }
        // else if (tabId.equals(FragmentQuesAnswer.TAG)) {
        // tabOrdersTV.setCompoundDrawablesWithIntrinsicBounds(null,
        // drawables[0][0], null, null);
        // tabActivitiesTV.setCompoundDrawablesWithIntrinsicBounds(null,
        // drawables[1][0], null, null);
        // tabMineTV.setCompoundDrawablesWithIntrinsicBounds(null,
        // drawables[2][0], null, null);
        // // tabQATV.setCompoundDrawablesWithIntrinsicBounds(null,
        // // drawables[3][1], null, null);
        //
        // tabOrdersTV.setTextColor(textUnselectedColor);
        // tabActivitiesTV.setTextColor(textUnselectedColor);
        // tabMineTV.setTextColor(textUnselectedColor);
        // // tabQATV.setTextColor(textSelectedColor);
        // }
    }


    @Override
    public BaseActivity getContext()
    {
        return this;
    }
}

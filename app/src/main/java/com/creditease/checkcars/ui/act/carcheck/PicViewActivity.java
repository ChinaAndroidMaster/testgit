/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.act.carcheck;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.adapter.ViewPagerAdapter;
import com.creditease.utilframe.BitmapUtils;
import com.creditease.utilframe.exception.HttpException;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年5月19日
 * @company CREDITEASE
 */
@SuppressLint( {"InflateParams", "ClickableViewAccessibility"} )
public class PicViewActivity extends BaseActivity
        implements OnClickListener, OnPageChangeListener, RequestListener
{
    public static final String PARAM_PICS = "_param_pics";
    public static final String PARAM_REPORT_ID = "_param_reportId";
    public static final String PARAM_ITEM_ID = "_param_attrid";
    public static final String PARAM_ITEM_POSITION = "_param_position";

    public static final String PARAM_OPER = "_oper";

    public static final int OPER_CANCEL = 0;
    public static final int OPER_DELETE = 1;

    private String checkItemId = "";
    private String reportId = "";
    private int position = 0;
    private ArrayList< String > picList;
    private ArrayList< View > viewList = new ArrayList< View >();
    private RelativeLayout parent;
    private ViewPager mViewPager;
    private Button deleteAllBtn;
    private TextView numTV;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_picview);
        parent = ( RelativeLayout ) findViewById(R.id.parent);
        mViewPager = ( ViewPager ) findViewById(R.id.act_picview_viewpager);
        deleteAllBtn = ( Button ) findViewById(R.id.act_picview_btn_deleteall);
        numTV = ( TextView ) findViewById(R.id.act_picview_tv);
    }

    @Override
    protected void initData()
    {
        LayoutInflater inflater = ( LayoutInflater ) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BitmapUtils utils = new BitmapUtils(getContext());
        utils.configDefaultLoadingImage(R.drawable.icon);
        Intent intent = getIntent();
        picList = intent.getStringArrayListExtra(PARAM_PICS);
        reportId = intent.getStringExtra(PARAM_REPORT_ID);
        checkItemId = intent.getStringExtra(PARAM_ITEM_ID);
        position = intent.getIntExtra(PARAM_ITEM_POSITION, 0);

        if ( picList != null )
        {
            viewList.clear();
            for ( String path : picList )
            {
                View view = inflater.inflate(R.layout.listitem_picview, null);
                view.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        closeOneAct(getContext());
                    }
                });
                ImageView imgV = ( ImageView ) view.findViewById(R.id.listitem_picview_imgv);
                utils.display(imgV, path);
                viewList.add(view);
            }

            ViewPagerAdapter adapter =
                    new ViewPagerAdapter(getContext(), viewList, new ArrayList< String >());
            mViewPager.setAdapter(adapter);
            mViewPager.setCurrentItem(position);
            numTV.setText((position + 1) + "/" + picList.size());
        }

    }


    @Override
    protected void initEvents()
    {
        deleteAllBtn.setOnClickListener(this);
        mViewPager.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
    }


    @Override
    public void onBackPressed()
    {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onResume()
    {
        MobclickAgent.onResume(getContext());
        super.onResume();
    }

    @Override
    public void onPause()
    {
        MobclickAgent.onPause(getContext());
        super.onPause();
    }


    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch ( id )
        {
            case R.id.act_picview_viewpager:
                closeOneAct(getContext());
                break;
            case R.id.act_picview_btn_deleteall:
                showPopupWindow();
                break;
        }
    }


    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {

    }

    @Override
    public void onPageScrollStateChanged(int arg0)
    {

    }

    @Override
    public void onPageSelected(int index)
    {
        numTV.setText((index + 1) + "/" + picList.size());
    }


    @Override
    public void onSuccess(Bundle bundle)
    {
        Intent intent = new Intent();
        intent.putExtra(PARAM_OPER, OPER_DELETE);
        setResult(RESULT_OK, intent);
        closeOneAct(getContext());
    }

    @Override
    public void onFailure(OperResponse response)
    {
        showToast(response != null ? response.respmsg : "删除失败");
        // closeOneAct(getContext());
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        showToast(errorMsg);
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        showToast(msg);
    }

    private void showPopupWindow()
    {

        // 一个自定义的布局，作为显示的内容
        View contentView =
                LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_clear_pics, null);
        // 设置按钮的点击事件
        Button sureBtn = ( Button ) contentView.findViewById(R.id.popupwindow_clear_pics_btn_sure);

        Button cancelBtn = ( Button ) contentView.findViewById(R.id.popupwindow_clear_pics_btn_cancel);

        final PopupWindow popupWindow =
                new PopupWindow(contentView, android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
        sureBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // 删除
                OperationFactManager.getManager().deleteReportAttrImageWS(getContext(), reportId,
                        checkItemId, PicViewActivity.this);
                popupWindow.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 设置好参数之后再show
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }

}

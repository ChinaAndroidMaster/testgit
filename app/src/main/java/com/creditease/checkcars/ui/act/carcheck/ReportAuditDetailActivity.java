package com.creditease.checkcars.ui.act.carcheck;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年8月13日
 * @company CREDITEASE
 */
public class ReportAuditDetailActivity extends BaseActivity implements OnClickListener
{

    public static final String Tag = ReportAuditDetailActivity.class.getSimpleName();
    public static final String PARAM_REASON = "_param_reason";

    private TextView titleTV;
    private ImageView backImgV;
    private TextView contTV;


    @Override
    protected void initData()
    {
        titleTV.setText("批注");
        String reason = getIntent().getStringExtra(PARAM_REASON);
        contTV.setText(reason);
    }

    @Override
    protected void initEvents()
    {
        backImgV.setOnClickListener(this);
    }

    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_reportauditdetail);
        titleTV = ( TextView ) findViewById(R.id.header_tv_title);
        backImgV = ( ImageView ) findViewById(R.id.header_imgv_back);
        contTV = ( TextView ) findViewById(R.id.act_reportauditdetail_tv_cont);
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
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v)
    {
        int vId = v.getId();
        switch ( vId )
        {
            case R.id.header_imgv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }
}

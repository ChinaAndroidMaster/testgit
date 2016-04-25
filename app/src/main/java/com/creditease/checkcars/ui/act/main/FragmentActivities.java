package com.creditease.checkcars.ui.act.main;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.ui.act.CarPreReportActivity;
import com.creditease.checkcars.ui.act.FeedBackActivity;
import com.creditease.checkcars.ui.act.MyQAActivity;
import com.creditease.checkcars.ui.act.ShowCarInfoActivity;
import com.creditease.checkcars.ui.act.WebViewActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;

/**
 * 活动版块
 *
 * @author 子龍
 * @function
 * @date 2015年7月8日
 * @company CREDITEASE
 */
public class FragmentActivities extends BaseFragment implements OnClickListener
{
    public static final String TAG = "activities";

    private TextView qaTV, schoolTV, activitiesTV, feedbackTV, assessCarTV, vinQeueryCarTV, carPreReportTV;
    private TextView titleTV;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_activities;
    }

    @Override
    protected void initView(View view)
    {
        titleTV = ( TextView ) view.findViewById(R.id.header_tv_title);
        titleTV.setText(R.string.str_activities_title);
        view.findViewById(R.id.header_imgv_back).setVisibility(View.GONE);
        qaTV = ( TextView ) view.findViewById(R.id.fragment_activities_qa_tv);
        schoolTV = ( TextView ) view.findViewById(R.id.fragment_activities_school_tv);
        activitiesTV = ( TextView ) view.findViewById(R.id.fragment_activities_activities_tv);
        feedbackTV = ( TextView ) view.findViewById(R.id.fragment_activities_feedback_tv);
        assessCarTV = ( TextView ) view.findViewById(R.id.fragment_activities_assess_car);
        vinQeueryCarTV = ( TextView ) view.findViewById(R.id.fragment_activities_vin_query);
        carPreReportTV = ( TextView ) view.findViewById(R.id.fragment_activities_pre_carreport);
    }

    @Override
    protected void initData()
    {
    }

    @Override
    protected void initEvents()
    {
        qaTV.setOnClickListener(this);
        schoolTV.setOnClickListener(this);
        activitiesTV.setOnClickListener(this);
        feedbackTV.setOnClickListener(this);
        assessCarTV.setOnClickListener(this);
        vinQeueryCarTV.setOnClickListener(this);
        carPreReportTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent();
        int id = v.getId();
        switch ( id )
        {
            case R.id.fragment_activities_qa_tv:
                intent.putExtra(WebViewActivity.PARAM_TITLE, qaTV.getText().toString());
                BaseActivity.launch(getContext(), MyQAActivity.class, intent);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.fragment_activities_school_tv:
                intent.putExtra(WebViewActivity.PARAM_TITLE, schoolTV.getText().toString());
                intent.putExtra(WebViewActivity.PARAM_URL, Config.URL_APPRAISER_SCHOOL);
                BaseActivity.launch(getContext(), WebViewActivity.class, intent);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.fragment_activities_activities_tv:
                intent.putExtra(WebViewActivity.PARAM_TITLE, activitiesTV.getText().toString());
                intent.putExtra(WebViewActivity.PARAM_URL, Config.URL_ACTIVITIES);
                BaseActivity.launch(getContext(), WebViewActivity.class, intent);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.fragment_activities_feedback_tv:
                BaseActivity.launch(getContext(), FeedBackActivity.class, intent);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.fragment_activities_assess_car:
                intent.putExtra(WebViewActivity.PARAM_TITLE, assessCarTV.getText().toString());
                intent.putExtra(WebViewActivity.PARAM_URL, Config.URL_OLDCAR_SOURCE);
                BaseActivity.launch(getContext(), WebViewActivity.class, intent);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.fragment_activities_vin_query:
                intent.putExtra("type", ShowCarInfoActivity.FROM_VIN_QUERY);
                BaseActivity.launch(getContext(), ShowCarInfoActivity.class, intent);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.fragment_activities_pre_carreport:
                BaseActivity.launch(getContext(), CarPreReportActivity.class, intent);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            default:
                break;
        }
    }
}

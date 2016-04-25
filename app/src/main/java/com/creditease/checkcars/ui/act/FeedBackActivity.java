/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.act;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.utilframe.exception.HttpException;
import com.umeng.analytics.MobclickAgent;

/**
 * 我要吐槽
 *
 * @author 子龍
 * @function
 * @date 2015年7月10日
 * @company CREDITEASE
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener, RequestListener
{
    /**
     * 标题
     */
    private TextView titleTV;

    private EditText inputET;
    ;
    /**
     * 返回图标
     */
    private ImageView backImgV;

    private Button commitBtn;

    private LoadingGifDialog loadDialog;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_feedback);
        loadDialog = new LoadingGifDialog(getContext());
        titleTV = (( TextView ) findViewById(R.id.header_tv_title));
        titleTV.setText(R.string.str_feedback);
        backImgV = (( ImageView ) findViewById(R.id.header_imgv_back));
        commitBtn = (( Button ) findViewById(R.id.act_feedback_btn));
        inputET = (( EditText ) findViewById(R.id.act_feedback_input_et));
    }

    @Override
    protected void initData()
    {
    }

    @Override
    protected void initEvents()
    {
        backImgV.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch ( id )
        {
            case R.id.header_imgv_back:
                onBackPressed();
                break;
            case R.id.act_feedback_btn:
                commit();
                break;
        }
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
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        ZLToast.getToast().showToast(getContext(), R.string.str_feedback_commit_success);
        loadDialog.dismiss();
    }

    @Override
    public void onFailure(OperResponse response)
    {
        loadDialog.dismiss();
        showToast(response.respmsg);
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        loadDialog.dismiss();
        showToast(errorMsg);
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        loadDialog.dismiss();
        showToast(msg);
    }

    private void commit()
    {

        String feedback = inputET.getText().toString();
        if ( TextUtils.isEmpty(feedback) )
        {
            ZLToast.getToast().showToast(getContext(), R.string.str_feedback_cont_null);
            return;
        }
        feedback = feedback.trim();
        String uuid = SharePrefenceManager.getAppraiserId(getContext());
        OperationFactManager.getManager().commitFeedbackWS(getContext(), uuid, feedback, this);
//    loadDialog.setText(R.string.text_report_add_loading);
//    TDialog.Builder.builderDialog(loadDialog).showDialog();
        loadDialog.showDialog();
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }
}

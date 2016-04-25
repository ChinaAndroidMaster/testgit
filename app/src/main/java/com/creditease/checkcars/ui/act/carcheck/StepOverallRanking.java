package com.creditease.checkcars.ui.act.carcheck;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.creditease.checkcars.R;
import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.ui.act.WebViewActivity;
import com.creditease.checkcars.ui.widget.CleanableEditText;

/**
 * @author 子龍
 * @function
 * @date 2015年11月6日
 * @company CREDITEASE
 */
public class StepOverallRanking extends BaseStep implements OnClickListener
{

    /**
     * 估价请求码
     */
    public static final int REQUEST_CODE_PRICE = 1;
    public static final String KEY_PRICE = "_price";
    /******************
     * 评论车
     *************************/
    public CleanableEditText commentET;
    public EditText valueET;
    private RadioGroup levelRG;
    private Button commitBtn, evaluateBtn;
    private RadioButton level1RB, level2RB, level3RB, level4RB;

    public StepOverallRanking(CarCheckActivity activity, View contentRootView, CarReport report)
    {
        super(activity, contentRootView, report);
        initViewData();
    }

    @Override
    public void initViews()
    {
        /****************** 评论车 *************************/
        commentET = ( CleanableEditText ) findViewById(R.id.carcomment_et_comment);
        valueET = ( EditText ) findViewById(R.id.carcomment_et_value);
        commitBtn = ( Button ) findViewById(R.id.carcomment_btn_commit);
        evaluateBtn = ( Button ) findViewById(R.id.carcomment_btn_evaluate);
        levelRG = ( RadioGroup ) findViewById(R.id.carcomment_rg);
        level1RB = ( RadioButton ) findViewById(R.id.carcomment_rb_level1);
        level2RB = ( RadioButton ) findViewById(R.id.carcomment_rb_level2);
        level3RB = ( RadioButton ) findViewById(R.id.carcomment_rb_level3);
        level4RB = ( RadioButton ) findViewById(R.id.carcomment_rb_level4);
    }

    protected void initViewData()
    {
        commentET.setText(TextUtils.isEmpty(report.remark) ? "" : report.remark);
        valueET.setText(TextUtils.isEmpty(report.amount) ? "" : report.amount);
    }

    @Override
    public void initEvents()
    {
        /****************** 评论车 *************************/
        commitBtn.setOnClickListener(this);
        evaluateBtn.setOnClickListener(this);
        levelRG.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1)
            {
                // 获取变更后的选中项的ID
                int id = arg0.getCheckedRadioButtonId();
                switch ( id )
                {
                    case R.id.carcomment_rb_level1:
                        report.level = CarReport.LEVEL_1;
                        break;
                    case R.id.carcomment_rb_level2:
                        report.level = CarReport.LEVEL_2;
                        break;
                    case R.id.carcomment_rb_level3:
                        report.level = CarReport.LEVEL_3;
                        break;
                    case R.id.carcomment_rb_level4:
                        report.level = CarReport.LEVEL_4;
                        break;
                    default:
                        break;
                }
            }
        });

        switch ( report.level )
        {
            case CarReport.LEVEL_1:
                level1RB.setChecked(true);
                break;
            case CarReport.LEVEL_2:
                level2RB.setChecked(true);
                break;
            case CarReport.LEVEL_3:
                level3RB.setChecked(true);
                break;
            case CarReport.LEVEL_4:
                level4RB.setChecked(true);
                break;
        }
        setTextWatcher();
        ableCommitBtn();
    }

    /**
     * 设置text监听
     * <p/>
     * 下午5:28:13
     */
    private void setTextWatcher()
    {
        commentET.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void afterTextChanged(Editable s)
            {
                ableCommitBtn();
                mContext.saveReport();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });
        valueET.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void afterTextChanged(Editable s)
            {
                ableCommitBtn();
                mContext.saveReport();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });
    }

    private void ableCommitBtn()
    {
        if ( TextUtils.isEmpty(commentET.getText().toString())
                || TextUtils.isEmpty(valueET.getText().toString()) )
        {
            commitBtn.setClickable(false);
            commitBtn.setBackgroundResource(R.color.color_bg_btn_next_unable);
        } else
        {
            commitBtn.setClickable(true);
            commitBtn.setBackgroundResource(R.color.color_bg_btn_next_able);
        }
    }


    @Override
    public boolean validate()
    {
        /************************* 综合评论 *******************************/
        if ( TextUtils.isEmpty(commentET.getText()) )
        {
            showToast(R.string.str_carcheck_comment_error);
            return false;
        }
        String amount = valueET.getText().toString();
        if ( TextUtils.isEmpty(amount) )
        {
            showToast(R.string.str_carcheck_carvalue_error);
            return false;
        }
        double a = Double.parseDouble(amount);
        if ( a < 0.01 )
        {
            showToast(R.string.valuations_reasonable);
            return false;
        }
        if ( a > 1000 )
        {
            showToast(R.string.valuations_reasonable);
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch ( id )
        {
            case R.id.carcomment_btn_commit:
                commitReport();
                break;
            case R.id.carcomment_btn_evaluate:
                Intent intent = new Intent();
                intent.putExtra(WebViewActivity.PARAM_TITLE, "我要估价");
                intent.putExtra(WebViewActivity.PARAM_URL, Config.URL_NATIVE_SOURCE);
                intent.setClass(mContext, WebViewActivity.class);
                startActivityForResult(intent, REQUEST_CODE_PRICE);
                // BaseActivity.launch(mContext, WebViewActivity.class, intent);
                mContext.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    /**
     * 提交报告
     * <p/>
     * 下午9:23:13
     */
    public void commitReport()
    {
        mContext.commitReport();
    }

    /**
     * 获取评价
     *
     * @return 下午1:01:08
     */
    public String getComment()
    {
        return commentET.getText().toString().trim();
    }

    /**
     * 获取车估价
     *
     * @return 下午1:01:51
     */
    public String getCarValue()
    {
        return valueET.getText().toString().trim();
    }

    public void setCarValue(String value)
    {
        valueET.setText(value);
    }

    @Override
    public void destroy()
    {
    }

}

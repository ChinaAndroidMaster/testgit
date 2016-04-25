/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.act.password;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.BaseApplication;
import com.creditease.checkcars.R;
import com.creditease.checkcars.tools.PhotoUtils;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.password.BaseStep.onNextActionListener;
import com.creditease.checkcars.ui.widget.FixedViewFlipper;

/**
 * @author 子龍
 * @function
 * @date 2015年7月31日
 * @company CREDITEASE
 */
public class PasswordResetActivity extends BaseActivity implements OnClickListener,
        onNextActionListener
{
    public static final String EXTRA_DATA_CHANGE_TYPE =
            "com.creditease.checkcars.extra_data_change_type";
    public static final String CHANGE_TYPE_PASSWORD = "password";
    public static final String CHANGE_TYPE_FORGOT_PASSWORD = "forgot_password";
    public boolean isAgreement = true;
    private FixedViewFlipper mVfFlipper;
    private Button mBtnNext;
    private ImageView mBtnBack;
    private TextView mTxtTitle;
    private ImageView imageViewStep1;
    private ImageView imageViewStep2;
    private ImageView imageViewStep3;
    private BaseStep mCurrentStep;// 当前的step
    private StepPhone mStepPhone;// 手机号&验证码 step
    private StepSetPassword mStepSetPassword;// 重置密码step
    private int mCurrentStepIndex = 1; // 当前的step index
    private String changeType;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_resetpwd);
        View root = getWindow().getDecorView();
        setupUI(root);
        mVfFlipper = ( FixedViewFlipper ) findViewById(R.id.act_resetpwd_viewflipper);
        mVfFlipper.setMeasureAllChildren(false);
        mVfFlipper.setDisplayedChild(0);
        mBtnNext = ( Button ) findViewById(R.id.act_resetpwd_btn_next);
        mBtnBack = ( ImageView ) findViewById(R.id.header_imgv_back);
        mTxtTitle = ( TextView ) findViewById(R.id.header_tv_title);
        imageViewStep1 = ( ImageView ) findViewById(R.id.act_resetpwd_imgv_step1);
        imageViewStep2 = ( ImageView ) findViewById(R.id.act_resetpwd_imgv_step2);
        imageViewStep3 = ( ImageView ) findViewById(R.id.act_resetpwd_imgv_step3);
    }

    @Override
    protected void initData()
    {
        Intent intent = getIntent();
        changeType = intent.getStringExtra(EXTRA_DATA_CHANGE_TYPE);
        if ( CHANGE_TYPE_PASSWORD.equals(changeType) )
        {
            mTxtTitle.setText("修改密码");
        } else if ( CHANGE_TYPE_FORGOT_PASSWORD.equals(changeType) )
        {
            mTxtTitle.setText("忘记密码");
        }
        mCurrentStep = initStep();
        mVfFlipper.refreshDrawableState();
        mVfFlipper.invalidate();
    }

    @Override
    protected void initEvents()
    {
        mCurrentStep.setOnNextActionListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
    }


    protected BaseApplication getBaseApplication()
    {
        return mApplication;
    }

    protected String getChangeType()
    {
        return changeType;
    }


    protected float getDensity()
    {
        return mDensity;
    }

    public String getPassword()
    {
        if ( mStepSetPassword != null )
        {
            return mStepSetPassword.getPassword();
        }

        return "";
    }

    protected String getPhoneNumber()
    {
        if ( mStepPhone != null )
        {
            return mStepPhone.getPhoneNumber();
        }
        return "";
    }

    protected int getScreenHeight()
    {
        return mScreenHeight;
    }

    protected int getScreenWidth()
    {
        return mScreenWidth;
    }


    private BaseStep initStep()
    {
        switch ( mCurrentStepIndex )
        {
            case 1:
                if ( CHANGE_TYPE_FORGOT_PASSWORD.equals(changeType)
                        || CHANGE_TYPE_PASSWORD.equals(changeType) )
                {
                    finishStep(1);
                    if ( mStepPhone == null )
                    {
                        mStepPhone = new StepPhone(this, mVfFlipper.getChildAt(0));
                    }
                    mStepPhone.setPhoneFocus();
                    mBtnNext.setText("获取验证码");
                    mBtnNext.setVisibility(View.VISIBLE);
                    return mStepPhone;
                }
            case 2:
                finishStep(3);
                if ( CHANGE_TYPE_PASSWORD.equals(changeType) )
                {
                    if ( mStepSetPassword == null )
                    {
                        mStepSetPassword = new StepSetPassword(this, mVfFlipper.getChildAt(1));
                    }
                    mStepSetPassword.setPwdFocus();
                    mBtnNext.setText("确认");
                    mBtnNext.setVisibility(View.VISIBLE);
                    return mStepSetPassword;
                } else if ( CHANGE_TYPE_FORGOT_PASSWORD.equals(changeType) )
                {
                    if ( mStepSetPassword == null )
                    {
                        mStepSetPassword = new StepSetPassword(this, mVfFlipper.getChildAt(1));
                    }
                    mStepSetPassword.setPwdFocus();
                    mBtnNext.setText("更改");
                    mBtnNext.setVisibility(View.VISIBLE);
                    return mStepSetPassword;
                }
        }
        return null;
    }


    @Override
    public void next()
    {
        mCurrentStepIndex++;
        mCurrentStep = initStep();
        mCurrentStep.setOnNextActionListener(this);
        mVfFlipper.setInAnimation(this, R.anim.push_left_in);
        mVfFlipper.setOutAnimation(this, R.anim.push_left_out);
        mVfFlipper.showNext();

    }

    @Override
    public void onBackPressed()
    {
        doPrevious();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch ( id )
        {
            case R.id.header_imgv_back:
                closeOneAct(this);
                break;
            case R.id.act_resetpwd_btn_next:
                doNext();
        }
    }

    public void doPrevious()
    {
        mCurrentStepIndex--;
        mCurrentStep = initStep();
        if ( mCurrentStep != null )
        {
            mCurrentStep.setOnNextActionListener(this);
            mVfFlipper.setInAnimation(this, R.anim.push_right_in);
            mVfFlipper.setOutAnimation(this, R.anim.push_right_out);
            mVfFlipper.showPrevious();
        } else
        {
            closeOneAct(this);
        }
    }

    private void doNext()
    {
        if ( mCurrentStep.validate() )
        {
            if ( mCurrentStep.isChange() )
            {
                mCurrentStep.doNext();
            } else
            {
                next();
            }
        }
    }

    public void finishStep(int step)
    {
        switch ( step )
        {
            case 1:
                imageViewStep1.setImageResource(R.drawable.ic_step_one_passed);
                break;
            case 2:
                imageViewStep1.setImageResource(R.drawable.ic_step_one_passed);
                imageViewStep2.setImageResource(R.drawable.ic_step_two_passed);
                break;
            case 3:
                imageViewStep1.setImageResource(R.drawable.ic_step_one_passed);
                imageViewStep2.setImageResource(R.drawable.ic_step_two_passed);
                imageViewStep3.setImageResource(R.drawable.ic_step_three_passed);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        PhotoUtils.deleteImageFile();
        super.onDestroy();

        if ( mStepPhone != null )
        {
            mStepPhone.unRegisterContentObserver();
        }

    }

    public void setNextButtonText(String msg)
    {
        mBtnNext.setText(msg);
    }

    @Override
    public void setTitle(int titleResId)
    {
        mTxtTitle.setText(titleResId);
    }

    public void setTitle(String title)
    {
        mTxtTitle.setText(title);
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }

}

/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.act.password;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.net.oper.GetVerifyCodeOperation;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.ui.observer.SmsContentObserver;
import com.creditease.checkcars.ui.widget.CleanableEditText;
import com.creditease.checkcars.ui.widget.FixedViewFlipper;
import com.creditease.checkcars.ui.widget.watcher.PhoneNumberTextWatcher;
import com.creditease.utilframe.exception.HttpException;

/**
 * @author 子龍
 * @function
 * @date 2015年8月31日
 * @company CREDITEASE
 */
public class StepPhone extends BaseStep implements OnClickListener, TextWatcher
{
    /**
     * 失效代码
     */
    private final String SMS_VERFY_CODE_LOSE = "-1000";
    private FixedViewFlipper mVfFlipper;
    private CleanableEditText mEtPhone;
    private EditText mEtVerifyCode;
    private Button mBtnGetVerify;
    // private RelativeLayout mLayoutAgreement;
    private TextView mTextViewPhoneNumber;

    private String mVerifyCode = "111111";
    // private String mAreaCode = "+86";

    private String mPhone;
    private boolean mIsChange = true;
    private int mReSendTime = 60;
    private long recodeTime = 0;
    /**
     * 验证码失效时间
     */
    private long loseTime = 15 * 60 * 1000;
    private PhoneNumberTextWatcher phw;
    private SmsContentObserver contentSmsObserver;

    @SuppressLint( "HandlerLeak" )
    private Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if ( mReSendTime > 1 )
            {
                mReSendTime--;
                mBtnGetVerify.setEnabled(false);
                mBtnGetVerify.setText("重发" + mReSendTime + "秒");
                handler.sendEmptyMessageDelayed(0, 1000);
            } else
            {
                mReSendTime = 60;
                mBtnGetVerify.setEnabled(true);
                mBtnGetVerify.setText("重新获取");
            }
        }
    };

    private RequestListener verifyListener = new RequestListener()
    {

        @Override
        public void onDataError(String errorMsg, String result)
        {
            mActivity.showToast(errorMsg);
        }

        @Override
        public void onFailure(OperResponse response)
        {
            mActivity.showToast(response != null ? response.respmsg : "获取失败");
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            mActivity.showToast(msg);
        }

        @Override
        public void onSuccess(Bundle bundle)
        {
            String code = bundle.getString(Oper.BUNDLE_EXTRA_DATA);
            mVerifyCode = code;
            mVfFlipper.setInAnimation(mActivity, R.anim.push_left_in);
            mVfFlipper.setOutAnimation(mActivity, R.anim.push_left_out);
            if ( mVfFlipper.getDisplayedChild() == 0 )
            {
                nextStep();
            }
            setVertifyFocus();
            recodeTime = System.currentTimeMillis();
        }
    };

    public StepPhone(PasswordResetActivity activity, View contentRootView)
    {
        super(activity, contentRootView);
    }

    @Override
    public void initViews()
    {
        mEtPhone = ( CleanableEditText ) findViewById(R.id.step_phone_et_phone);
        mEtPhone.requestFocus();
        mBtnGetVerify = ( Button ) findViewById(R.id.step_phone_btn_getVerify);
        mEtVerifyCode = ( EditText ) findViewById(R.id.et_verify_code);
        mEtPhone.setHint("登陆手机号");

        mVfFlipper = ( FixedViewFlipper ) findViewById(R.id.step_phone_viewflipper);
        mVfFlipper.setMeasureAllChildren(false);
        mVfFlipper.setDisplayedChild(0);
        mTextViewPhoneNumber = ( TextView ) findViewById(R.id.step_phone_tv_phonenum);

        contentSmsObserver = new SmsContentObserver(mActivity, new Handler(), mEtVerifyCode);
        // 注册短信变化监听
        mActivity.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true,
                contentSmsObserver);
    }

    @Override
    public void initEvents()
    {
        phw = new PhoneNumberTextWatcher(mEtPhone);
        mEtPhone.addTextChangedListener(phw);
        mBtnGetVerify.setOnClickListener(this);
        // mBtnGetVerify.setEnabled(false);
        mBtnGetVerify.setText("重新获取");

        mEtVerifyCode.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void afterTextChanged(Editable s)
            {
                if ( checkVerfyCodeLose() == true )
                {
                    return;
                }
                String vcode = s.toString();
                if ( !TextUtils.isEmpty(vcode) && vcode.equals(mVerifyCode) )
                {
                    mOnNextActionListener.next();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {

            }
        });
    }

    private void callGetVerifyCodeRequest()
    {
        if ( isNull(mEtPhone) )
        {
            mEtPhone.requestFocus();
            return;
        }
        String phone = mEtPhone.getText().toString().trim().replace(" ", "");
        if ( matchPhone(phone) )
        {
            if ( PasswordResetActivity.CHANGE_TYPE_FORGOT_PASSWORD.equals(mActivity.getChangeType())
                    || PasswordResetActivity.CHANGE_TYPE_PASSWORD.equals(mActivity.getChangeType()) )
            {
            }
            OperationFactManager.getManager().getVerifyCode(mActivity, phone,
                    GetVerifyCodeOperation.TYPE_VERIFY_CODE_PWD, verifyListener);
            mReSendTime = 90;
            handler.sendEmptyMessage(0);
        } else
        {
            mActivity.showToast("请正确填写手机号码");
            mEtPhone.requestFocus();
            return;
        }

    }

    /**
     * 检查是否过期
     *
     * @return true:过期 false:未过期
     */
    private boolean checkVerfyCodeLose()
    {
        long curTime = System.currentTimeMillis();
        if ( (curTime - recodeTime) > loseTime )
        {
            mVerifyCode = SMS_VERFY_CODE_LOSE;
            return true;
        }
        return false;
    }

    @Override
    public void doNext()
    {
        int index = mVfFlipper.getDisplayedChild();
        if ( index == 1 )
        {
            mOnNextActionListener.next();
        } else
        {
            callGetVerifyCodeRequest();
        }

    }

    public String getPhoneNumber()
    {
        return mPhone;
    }


    @Override
    public boolean isChange()
    {
        return mIsChange;
    }

    private void nextStep()
    {
        mActivity.finishStep(2);
        if ( mVfFlipper.getDisplayedChild() == 0 )
        {
            mVfFlipper.showNext();
        }
        mActivity.setNextButtonText("下一步");
    }

    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.step_phone_btn_getVerify:
                Log.d("StepPhone", "get verify code");
                callGetVerifyCodeRequest();
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        mIsChange = true;
        // if (s.toString().length() > 0) {
        // mHtvNotice.setVisibility(View.VISIBLE);
        // char[] chars = s.toString().toCharArray();
        // StringBuffer buffer = new StringBuffer();
        // for (int i = 0; i < chars.length; i++) {
        // if (i % 4 == 2) {
        // buffer.append(chars[i] + "  ");
        // } else {
        // buffer.append(chars[i]);
        // }
        // }
        // mHtvNotice.setText(buffer.toString());
        // } else {
        // mHtvNotice.setVisibility(View.GONE);
        // }
    }

    public void setPhoneFocus()
    {
        mEtPhone.requestFocus();
        mActivity.showSoftKeyboard();
    }

    public void setVertifyFocus()
    {
        mEtVerifyCode.requestFocus();
        mActivity.showSoftKeyboard();
    }

    public void unRegisterContentObserver()
    {
        mActivity.getContentResolver().unregisterContentObserver(contentSmsObserver);
    }

    @Override
    public boolean validate()
    {
        mPhone = null;
        if ( isNull(mEtPhone) )
        {
            mActivity.showToast("请输入手机号");
            mEtPhone.requestFocus();
            return false;
        }

        String phone = mEtPhone.getText().toString().trim();
        mTextViewPhoneNumber.setText(phone);
        mPhone = phone.replace(" ", "");
        if ( !matchPhone(mPhone) )
        {
            mActivity.showToast("请正确填写手机号码");
            mEtPhone.requestFocus();
            return false;
        }
        String changeType = mActivity.getChangeType();
        if ( PasswordResetActivity.CHANGE_TYPE_PASSWORD.equals(changeType) )
        {
            String registPhone = SharePrefenceManager.getUserLoginName(mActivity);
            if ( !registPhone.equals(mPhone) )
            {
                mActivity.showToast("手机号与当前登录帐号不一致");
                return false;
            }
        }
        int index = mVfFlipper.getDisplayedChild();
        if ( index == 1 )
        {
            String verifyCode = mEtVerifyCode.getText().toString().trim();
            if ( isNull(mEtVerifyCode) )
            {
                mActivity.showToast("请填写验证码");
                mEtVerifyCode.requestFocus();
                return false;
            }

            if ( TextUtils.isEmpty(mVerifyCode) )
            {
                mActivity.showToast("请获取验证码");
                mEtVerifyCode.requestFocus();
                return false;
            }
            if ( checkVerfyCodeLose() == true )
            {
                mActivity.showToast("验证码过期，请重新获取");
                return false;
            }
            if ( !mVerifyCode.equals(verifyCode) )
            {
                mActivity.showToast("验证码不正确");
                mEtVerifyCode.requestFocus();
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

}

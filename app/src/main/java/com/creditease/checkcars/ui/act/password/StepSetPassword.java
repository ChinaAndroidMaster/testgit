/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.act.password;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.base.Result;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.ui.act.LoginActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.utilframe.exception.HttpException;

/**
 * 重置密码
 *
 * @author 子龍
 * @function
 * @date 2015年8月4日
 * @company CREDITEASE
 */
public class StepSetPassword extends BaseStep implements TextWatcher, RequestListener
{

    private EditText mEtOldPwd;
    private RelativeLayout mLayoutOldPwd;
    private EditText mEtPwd;
    private EditText mEtRePwd;

    private boolean mIsChange = true;

    public StepSetPassword(PasswordResetActivity activity, View contentRootView)
    {
        super(activity, contentRootView);
    }

    @Override
    public void initViews()
    {
        mLayoutOldPwd = ( RelativeLayout ) findViewById(R.id.reg_setpwd_layout_oldpwd);
        mEtOldPwd = ( EditText ) findViewById(R.id.reg_setpwd_et_oldpwd);
        mEtPwd = ( EditText ) findViewById(R.id.reg_setpwd_et_pwd);
        mEtRePwd = ( EditText ) findViewById(R.id.reg_setpwd_et_repwd);

        if ( PasswordResetActivity.CHANGE_TYPE_PASSWORD.equals(mActivity.getChangeType()) )
        {
            mLayoutOldPwd.setVisibility(View.VISIBLE);
        } else
        {
            mLayoutOldPwd.setVisibility(View.GONE);
        }
        mEtPwd.setHint("新密码");
        mEtRePwd.setHint("再次输入密码");
    }

    @Override
    public void initEvents()
    {
        mEtPwd.addTextChangedListener(this);
        mEtRePwd.addTextChangedListener(this);
    }

    /**
     * 重置密码
     * <p/>
     * 下午4:28:50
     */
    private void callGetUpdatePasswordRequest()
    {
        OperationFactManager.getManager().userPwdResetWS(mActivity, mActivity.getPhoneNumber(),
                getPassword(), this);
    }

    @Override
    public void doNext()
    {
        callGetUpdatePasswordRequest();
    }

    public String getPassword()
    {
        return mEtPwd.getText().toString();
    }


    @Override
    public boolean isChange()
    {
        return mIsChange;
    }

    @Override
    public void onSuccess(Bundle bundle)
    {

        OperResponse res = ( OperResponse ) bundle.getParcelable(Oper.BUNDLE_EXTRA_DATA);
        if ( res.result.equals(Result.RESULT_SUCCESS) )
        {
            String pwdString = getPassword();
            String pwd = DESEncrypt.encryption(pwdString);
            SharePrefenceManager.setUserPassword(mActivity, pwd);
            BaseActivity act = mActivity.getActivity(LoginActivity.class);
            if ( act == null )
            {
                BaseActivity.launch(mActivity, LoginActivity.class, mActivity.getIntent());
            }
            mActivity.finish();
            mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            mActivity.finishAllActivityExceptOne(LoginActivity.class);
        } else
        {
            mActivity.showToast("重置失败!");
        }
    }

    @Override
    public void onFailure(OperResponse response)
    {
        mActivity.showToast(response == null ? "重置失败" : response.respmsg);
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        mActivity.showToast(errorMsg);
    }


    @Override
    public void onRequestError(HttpException error, String msg)
    {
        mActivity.showToast(msg);
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        mIsChange = true;
    }

    public void setPwdFocus()
    {
        mEtPwd.requestFocus();
        mEtOldPwd.requestFocus();
        mActivity.showSoftKeyboard();
    }

    @Override
    public boolean validate()
    {
        String pwd = null;
        String rePwd = null;
        if ( PasswordResetActivity.CHANGE_TYPE_PASSWORD.equals(mActivity.getChangeType()) )
        {
            if ( isNull(mEtOldPwd) )
            {
                mActivity.showToast("请输入当前密码");
                mEtOldPwd.requestFocus();
                return false;
            } else
            {
                String oldPwd = mEtOldPwd.getText().toString().trim();
                oldPwd = DESEncrypt.encryption(oldPwd);
                if ( !SharePrefenceManager.getUserPassword(mContext).equals(oldPwd) )
                {
                    mActivity.showToast("输入密码错误，请重新输入");
                    mEtOldPwd.requestFocus();
                    return false;
                }
            }
        }
        if ( isNull(mEtPwd) )
        {
            mActivity.showToast("请输入新密码");
            mEtPwd.requestFocus();
            return false;
        } else
        {
            pwd = mEtPwd.getText().toString().trim();
            if ( pwd.length() < 6 )
            {
                mActivity.showToast("密码长度应大于等于6位");
                mEtPwd.requestFocus();
                return false;
            }
        }
        if ( isNull(mEtRePwd) )
        {
            mActivity.showToast("请再次输入密码");
            mEtRePwd.requestFocus();
            return false;
        } else
        {
            rePwd = mEtRePwd.getText().toString().trim();
            if ( !pwd.equals(rePwd) )
            {
                mActivity.showToast("两次输入的密码不一致");
                mEtRePwd.requestFocus();
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

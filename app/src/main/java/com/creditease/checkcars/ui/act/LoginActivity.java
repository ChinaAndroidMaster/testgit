/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.UserCore;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.main.MainActivity;
import com.creditease.checkcars.ui.act.main.saler.MainSalerActivity;
import com.creditease.checkcars.ui.act.password.PasswordResetActivity;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.CleanableEditText;
import com.creditease.checkcars.ui.widget.InputMethodRelativeLayout;
import com.creditease.checkcars.ui.widget.InputMethodRelativeLayout.OnSizeChangedListenner;
import com.creditease.checkcars.ui.widget.watcher.PhoneNumberTextWatcher;
import com.creditease.utilframe.exception.HttpException;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
public class LoginActivity extends BaseActivity implements OnClickListener, RequestListener
{

    private Button button;
    private CleanableEditText pwdET;
    private String pwdString;
    private CleanableEditText userNameET;
    private String userNameString;
    private TextView buttonReg;
    private PhoneNumberTextWatcher phw;
    private LoadingGifDialog loadDialog;
    private TextView forgetPwdTV;
    private InputMethodRelativeLayout layout;
    private ImageView logo;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_login);
        layout = ( InputMethodRelativeLayout ) findViewById(R.id.act_login_layout);

        userNameET = (( CleanableEditText ) findViewById(R.id.act_login_et_username));
        pwdET = (( CleanableEditText ) findViewById(R.id.act_login_et_pwd));
        button = (( Button ) findViewById(R.id.act_login_btn_login));
        buttonReg = (( TextView ) findViewById(R.id.act_login_btn_reg));
        forgetPwdTV = (( TextView ) findViewById(R.id.act_login_tv_forgetpwd));
        logo = ( ImageView ) findViewById(R.id.act_login_layout_logo);
    }

    @Override
    protected void initData()
    {
    }

    @Override
    protected void initEvents()
    {
        phw = new PhoneNumberTextWatcher(userNameET);
        userNameET.addTextChangedListener(phw);
        button.setOnClickListener(this);
        buttonReg.setOnClickListener(this);
        forgetPwdTV.setOnClickListener(this);
        buttonReg.setVisibility(View.GONE);
        userNameET.requestFocus();

        String xx = SharePrefenceManager.getUserLoginName(this);
        if ( !TextUtils.isEmpty(xx) )
        {
            char[] chs = xx.toCharArray();
            for ( char c : chs )
            {
                setPhoneNum(String.valueOf(c));
            }
        }

        layout.setOnSizeChangedListenner(new OnSizeChangedListenner()
        {

            @Override
            public void onSizeChange(boolean paramBoolean, int w, int h)
            {
                if ( paramBoolean )
                {
                    logo.setVisibility(View.GONE);
                } else
                {
                    logo.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void login()
    {
        pwdString = pwdET.getText().toString().trim();
        userNameString = userNameET.getText().toString().replace(" ", "");

        if ( TextUtils.isEmpty(userNameString) )
        {
            this.showToast("请输入手机号");
            return;
        }

        if ( TextUtils.isEmpty(pwdString) )
        {
            this.showToast("请输入密码");
            return;
        }

        if ( pwdString.length() < 6 )
        {
            this.showToast("密码不正确");
            return;
        }

        OperationFactManager.getManager().userLogin(this, userNameString, pwdString, this);
        // loadDialog.setText(R.string.text_report_add_loading);
        // TDialog.Builder.builderDialog(loadDialog).showDialog();
        loadDialog = new LoadingGifDialog(getContext());
        loadDialog.showDialog();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch ( id )
        {
            case R.id.act_login_btn_login:
                login();
                break;
            case R.id.act_login_tv_forgetpwd:
                Intent intent = new Intent();
                intent.putExtra(PasswordResetActivity.EXTRA_DATA_CHANGE_TYPE,
                        PasswordResetActivity.CHANGE_TYPE_FORGOT_PASSWORD);
                launch(getContext(), PasswordResetActivity.class, intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
        loadDialog.dismiss();
        showToast(R.string.str_login_success);
        // TODO
        UserCore userCore = bundle.getParcelable(Oper.BUNDLE_EXTRA_DATA);
        if ( userCore != null )
        {
            if ( userCore.judgeUserType(UserCore.USER_TYPE_APPRAISER) )
            {
                // 师傅
                launch(getContext(), MainActivity.class, getIntent());
                getContext().finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                SharePrefenceManager.setLoginTime(LoginActivity.this, System.currentTimeMillis());
            } else if ( userCore.judgeUserType(UserCore.USER_TYPE_MARKET) )
            {
                launch(getContext(), MainSalerActivity.class, null);
                getContext().finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                SharePrefenceManager.setLoginTime(LoginActivity.this, System.currentTimeMillis());
            }
        }
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
        showToast(R.string.str_login_failed);
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        loadDialog.dismiss();
        showToast(R.string.net_error);
    }

    private void setPhoneNum(String paramString)
    {
        int i = userNameET.getSelectionStart();
        Editable localEditable = userNameET.getEditableText();
        int length = localEditable.length();
        if ( (i < 0) || (i >= length) )
        {
            try
            {
                localEditable.append(paramString);
            } catch ( RuntimeException e )
            {
                e.printStackTrace();
            }
        } else
        {
            localEditable.insert(i, paramString);
        }
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }

}

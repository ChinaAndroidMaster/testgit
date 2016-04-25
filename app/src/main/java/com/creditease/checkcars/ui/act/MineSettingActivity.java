package com.creditease.checkcars.ui.act;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.NewVersion;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.tools.Util;
import com.creditease.checkcars.tools.Utils;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.main.MainActivity;
import com.creditease.checkcars.ui.act.password.PasswordResetActivity;
import com.creditease.checkcars.ui.dialog.CommitReportDialog;
import com.creditease.checkcars.ui.dialog.base.TDialog;

public class MineSettingActivity extends BaseActivity implements OnClickListener
{
    private ImageView mBackView;
    private TextView mBasicTitle;
    private CommitReportDialog mExitDialog;// 退出对话框
    private TextView mLogout;// 退出
    private TextView mPwdResetTV;
    private TextView mAboutme;
    private ImageView versionImgV;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.mine_setting);
        mBackView = ( ImageView ) findViewById(R.id.header_imgv_back);
        mBasicTitle = ( TextView ) findViewById(R.id.header_tv_title);
        mBasicTitle.setText(R.string.str_mine_setting_title);
        mPwdResetTV = ( TextView ) findViewById(R.id.mine_main_pwdreset_tv);
        mAboutme = ( TextView ) findViewById(R.id.mine_main_aboutme);
        mLogout = ( TextView ) findViewById(R.id.mine_main_logout);
        versionImgV = ( ImageView ) findViewById(R.id.mine_main_newversion);
        initExitDialog();
    }

    @Override
    protected void initData()
    {
        versionImgV.setVisibility(View.GONE);
        String version = Util.getAppVersionName(this);
        NewVersion nv = SharePrefenceManager.getNewVersion(getApplicationContext());
        if ( (nv != null) && !TextUtils.isEmpty(nv.versionName) )
        {
            if ( Utils.compareHaveNewVersion(version, nv.versionName) )
            {
                versionImgV.setVisibility(View.VISIBLE);
            } else
            {
                versionImgV.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initEvents()
    {
        mBackView.setOnClickListener(this);
        mPwdResetTV.setOnClickListener(this);
        mAboutme.setOnClickListener(this);
        mLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.header_imgv_back:
                closeOneAct(getContext());
                break;
            case R.id.mine_main_logout:
                TDialog.Builder.builderDialog(mExitDialog).showDialog();
                break;
            case R.id.mine_main_aboutme:
                BaseActivity.launch(getContext(), AboutUsActivity.class, null);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.mine_main_pwdreset_tv:
                Intent intent = new Intent();
                intent.putExtra(PasswordResetActivity.EXTRA_DATA_CHANGE_TYPE,
                        PasswordResetActivity.CHANGE_TYPE_PASSWORD);
                BaseActivity.launch(getContext(), PasswordResetActivity.class, intent);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            default:
                break;
        }

    }

    /**
     * 退出的对话框
     */
    private void initExitDialog()
    {
        mExitDialog = new CommitReportDialog(getContext());
        mExitDialog.setContent(R.string.str_logout_msg).setOnOKBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mExitDialog.dismiss();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                SharePrefenceManager.setUserId(getApplicationContext(), "");
                SharePrefenceManager.setAppraiserId(getApplicationContext(), "");
                SharePrefenceManager.setSalerId(getApplicationContext(), "");
                SharePrefenceManager.setLoginTime(getApplicationContext(), 0);
                startActivity(intent);
                if ( getContext() instanceof BaseActivity )
                {
                    getContext().closeOneAct(MainActivity.class);
                    getContext().finishAllActivityExceptOne(LoginActivity.class);
                }
                getContext().finish();
                getContext().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }).setOnCancelBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mExitDialog.dismiss();
            }
        });
    }


    @Override
    public BaseActivity getContext()
    {
        return this;
    }

}

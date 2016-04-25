package com.creditease.checkcars.ui.act;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.NewVersion;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.net.ver.VersionUpdateManager;
import com.creditease.checkcars.tools.Util;
import com.creditease.checkcars.tools.Utils;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年6月26日
 * @company CREDITEASE
 */
public class AboutUsActivity extends BaseActivity implements OnClickListener
{
    /**
     * 标题
     */
    private TextView title;

    private TextView versionCheckTV;
    private TextView txtVersion;
    /**
     * 返回图标
     */
    private ImageView backImgV;

    /**
     * 初始化View
     */
    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_aboutus);
        title = ( TextView ) findViewById(R.id.header_tv_title);
        backImgV = ( ImageView ) findViewById(R.id.header_imgv_back);
        txtVersion = ( TextView ) findViewById(R.id.aboutus_tv_version);
        versionCheckTV = ( TextView ) findViewById(R.id.aboutus_tv_version_check);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData()
    {
        title.setText(R.string.str_title_aboutus);
        String version = Util.getAppVersionName(this);
        txtVersion.setText("V" + version);
        Drawable newVerionDrable = getResources().getDrawable(R.drawable.icon_version_new);
        versionCheckTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        NewVersion nv = SharePrefenceManager.getNewVersion(getApplicationContext());
        if ( (nv != null) && !TextUtils.isEmpty(nv.versionName) )
        {
            if ( Utils.compareHaveNewVersion(version, nv.versionName) )
            {
                versionCheckTV.setCompoundDrawablesWithIntrinsicBounds(null, null, newVerionDrable, null);
            } else
            {
                versionCheckTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }


    @Override
    protected void initEvents()
    {
        backImgV.setOnClickListener(this);
        versionCheckTV.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.header_imgv_back:
                onBackPressed();
                break;
            case R.id.aboutus_tv_version_check:
                VersionUpdateManager.getInstance().checkVersionWS(false, getContext());
                break;
            default:
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
    public BaseActivity getContext()
    {
        return this;
    }

}

/**
 * Copyright © 2015 Credit Ease. All rights reserved.
 */
package com.creditease.checkcars.ui.act;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.creditease.checkcars.R;
import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.volley.ImageCacheManager;

/**
 * 获取师傅专有的二维码
 *
 * @author noahzl
 */
public class QRCodeActivity extends BaseActivity implements OnClickListener
{

    private NetworkImageView mNetworkImageView;
    /**
     * 标题
     */
    private TextView title;
    /**
     * 返回图标
     */
    private ImageView backImgV;

    @Override
    public BaseActivity getContext()
    {
        return this;
    }

    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_qrcode);
        mNetworkImageView = ( NetworkImageView ) findViewById(R.id.imageView_qrcode);
        title = ( TextView ) findViewById(R.id.header_tv_title);
        backImgV = ( ImageView ) findViewById(R.id.header_imgv_back);
    }

    @Override
    protected void initData()
    {
        title.setText(R.string.str_title_qrcode);
        mNetworkImageView.setDefaultImageResId(R.drawable.qrcode);
        mNetworkImageView.setErrorImageResId(R.drawable.qrcode);
        String uuid = SharePrefenceManager.getAppraiserId(this);
        mNetworkImageView.setImageUrl(Config.QRCODE + uuid, ImageCacheManager.getInstance()
                .getImageLoader());
    }

    @Override
    protected void initEvents()
    {
        backImgV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.header_imgv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

}

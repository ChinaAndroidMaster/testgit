package com.creditease.checkcars.ui.act;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.tools.NetWorkUtils.NetWorkState;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.CEWebView;
import com.creditease.checkcars.ui.widget.listener.IWebViewListener;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年3月4日
 * @company CREDITEASE
 */
public class CarReportActivity extends BaseActivity implements OnClickListener, IWebViewListener
{
    public static final String PARAM_REPORT_URL = "_report_url";
    private ImageView backImgV;
    private TextView titleTV;
    private CEWebView webView;
    private ImageView btnImgV;
    private String reportUrl;
    /**
     * 当加载网页出现错误进入onReceivedError方法中，那么canGoBack()方法会无效 因此使用此变量判断
     */
    private boolean isError = false;
    /**
     * 加载等待dialog
     */
    private LoadingGifDialog loadDialog;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_car_report);
        backImgV = ( ImageView ) findViewById(R.id.header_imgv_back);
        btnImgV = ( ImageView ) findViewById(R.id.header_imgv_btn);
        titleTV = ( TextView ) findViewById(R.id.header_tv_title);
        webView = ( CEWebView ) findViewById(R.id.report_webview);
        loadDialog = new LoadingGifDialog(getContext());
        loadDialog.showDialog();
    }


    @Override
    protected void initData()
    {
        webView.setWebViewListener(this);
        reportUrl = getIntent().getStringExtra(PARAM_REPORT_URL);
        webView.init(this, reportUrl);
    }

    @SuppressLint( "SetJavaScriptEnabled" )
    @Override
    protected void initEvents()
    {
        titleTV.setText(R.string.str_carreport_title);
        backImgV.setOnClickListener(this);
        btnImgV.setOnClickListener(this);
        if ( netWorkUtils.getConnectState() == NetWorkState.NONE )
        {
            // 无网络
            ZLToast.getToast().showToast(this, R.string.net_error);
        }
    }


    @Override
    public void onBackPressed()
    {
        if ( webView.canGoBack() && !isError )
        {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view)
    {
        switch ( view.getId() )
        {
            case R.id.header_imgv_back:
                onBackPressed();
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


    @Override
    public void onProgressChanged(WebView view, int newProgress)
    {
        setProgress(newProgress * 100);
        if ( newProgress == 100 )
        {
            loadDialog.dismiss();
        }
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {

    }


    @Override
    public void onPageFinished(WebView view, String url)
    {
        loadDialog.dismiss();
    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
        isError = true;
        loadDialog.dismiss();
    }


    @Override
    public void addMoreJavaScriptInterface(WebView webView)
    {

    }


    @Override
    public boolean overrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);
        return true;
    }

}

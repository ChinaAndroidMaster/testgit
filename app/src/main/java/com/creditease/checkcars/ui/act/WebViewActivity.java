package com.creditease.checkcars.ui.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.carcheck.StepOverallRanking;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.CEWebView;
import com.creditease.checkcars.ui.widget.listener.IWebViewListener;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年7月10日
 * @company CREDITEASE
 */
public class WebViewActivity extends BaseActivity implements OnClickListener, IWebViewListener
{
    public static final String Tag = "webv";
    public static final String PARAM_URL = "_url";
    public static final String PARAM_TITLE = "_title";
    public static final int RESULT_CODE = 1;
    public String value;
    private TextView titleTV;
    private ImageView backImgV;
    private ImageView settingImgV;
    private CEWebView webView;

    private String url;
    private String title;


    /**
     * 加载等待dialog
     */
    private LoadingGifDialog loadDialog;
    private long firstTime = 0;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_webv);
        titleTV = ( TextView ) findViewById(R.id.header_tv_title);
        backImgV = ( ImageView ) findViewById(R.id.header_imgv_back);
        settingImgV = ( ImageView ) findViewById(R.id.header_imgv_btn);
        webView = ( CEWebView ) findViewById(R.id.act_webv_webview);
    }

    @Override
    protected void initData()
    {
        url = getIntent().getStringExtra(PARAM_URL);
        title = getIntent().getStringExtra(PARAM_TITLE);
        backImgV.setVisibility(View.VISIBLE);
        settingImgV.setVisibility(View.GONE);
        titleTV.setText(title);
        loadDialog = new LoadingGifDialog(getContext());
        loadDialog.showDialog();
        // 设置监听器
        webView.setWebViewListener(this);
        // 初始化webview并加载网页
        webView.init(getContext(), url);
    }

    @Override
    protected void initEvents()
    {
        backImgV.setOnClickListener(this);
        settingImgV.setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        // if (webView.canGoBack()) {
        // webView.goBack();
        // return;
        // }
        // if (isSaler) {
        // quitApp();
        // return;
        // }
        super.onBackPressed();
    }

    /**
     * 退出当前app
     * <p/>
     * 下午8:28:59
     */
    public void quitApp()
    {
        long secondTime = System.currentTimeMillis();
        if ( (secondTime - firstTime) < 1000 )
        {// 如果两次按键时间间隔大于1000毫秒，则不退出
            super.onBackPressed();
            MobclickAgent.onKillProcess(getContext());
            finishAllActivity();
            // appExit();
        } else
        {
            showToast("再按一次返回键退出程序", 800);
            firstTime = secondTime;// 更新firstTime
        }
    }

    @Override
    public void onClick(View arg0)
    {
        int vId = arg0.getId();
        switch ( vId )
        {
            case R.id.header_imgv_back:
                onBackPressed();
                break;
            case R.id.header_imgv_btn:
                BaseActivity.launch(getContext(), MineSettingActivity.class, null);
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
    protected void onDestroy()
    {

        super.onDestroy();
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
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
        loadDialog.dismiss();
    }

    @Override
    public void addMoreJavaScriptInterface(WebView webView)
    {
        webView.addJavascriptInterface(new PriceJavaScriptInterface(), "androidback");
    }

    @Override
    public boolean overrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);
        return true;
    }

    /**
     * 返回JS评估结果
     */
    class PriceJavaScriptInterface
    {

        PriceJavaScriptInterface()
        {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void getValue(String valuejs)
        {
            // if (TextUtils.isEmpty(valuejs)) {
            // return;
            // }
            value = valuejs;
            Intent intent = new Intent();
            intent.putExtra(StepOverallRanking.KEY_PRICE, valuejs);
            setResult(RESULT_OK, intent); // 返回值
            finish();
        }
    }

}

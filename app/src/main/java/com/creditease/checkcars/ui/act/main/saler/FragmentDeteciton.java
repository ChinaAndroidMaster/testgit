package com.creditease.checkcars.ui.act.main.saler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.ui.act.WebViewActivity;
import com.creditease.checkcars.ui.act.main.BaseFragment;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.CEWebView;
import com.creditease.checkcars.ui.widget.listener.IWebViewListener;

/**
 * 活动版块
 *
 * @author 子龍
 * @function
 * @date 2015年7月8日
 * @company CREDITEASE
 */
public class FragmentDeteciton extends BaseFragment implements IWebViewListener
{
    public static final String TAG = FragmentDeteciton.class.getSimpleName();

    private CEWebView webView;
    /**
     * 加载等待dialog
     */
    private LoadingGifDialog loadDialog;
    private int time = 0;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_detection;
    }

    @Override
    protected void initView(View view)
    {
        webView = ( CEWebView ) view.findViewById(R.id.fragment_detection_webview);

    }

    @Override
    protected void initData()
    {
        time = 0;
        // 初始化webview并加载网页
        String salerHomePageUrl = SharePrefenceManager.getSalerHomePageUrl(getContext());
        webView.init(getContext(), salerHomePageUrl);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        loadDialog = new LoadingGifDialog(getContext());
        loadDialog.showDialog();
    }

    @Override
    protected void initEvents()
    {
        // 设置监听器
        webView.setWebViewListener(this);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress)
    {
        getActivity().setProgress(newProgress * 100);
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
    }

    @Override
    public boolean overrideUrlLoading(WebView view, String url)
    {
        if ( time == 0 )
        {
            view.loadUrl(url);
            time++;
        } else
        {
            Intent intent = new Intent();
            intent.putExtra(WebViewActivity.PARAM_TITLE, "检车销售");
            intent.putExtra(WebViewActivity.PARAM_URL, url);
            intent.setClass(getContext(), WebViewActivity.class);
            getContext().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return true;
    }


}

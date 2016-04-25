package com.creditease.checkcars.ui.widget;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.creditease.checkcars.ui.widget.listener.IWebViewListener;

@SuppressLint( {"NewApi", "SetJavaScriptEnabled"} )
public class CEWebView extends WebView
{

    private IWebViewListener mListener;

    private Context mContext;

    public CEWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr);
    }

    public CEWebView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public CEWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CEWebView(Context context)
    {
        super(context);
    }

    /**
     * 获取下载的目录
     */
    private static String getCachePath(Context context)
    {
        Boolean isSD = hasSD();
        if ( isSD )
        {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + "Android" + File.separator + "data" + File.separator + context.getPackageName()
                    + File.separator + "Cache" + File.separator + "webview";
        } else
        {
            return Environment.getDataDirectory().getAbsolutePath() + File.separator + "data"
                    + File.separator + context.getPackageName() + File.separator + "webview";
        }
    }

    /**
     * 检查是否有SD卡 参数
     */
    private static Boolean hasSD()
    {
        if ( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) )
        {
            return true;
        } else
        {
            return false;
        }
    }

    @SuppressWarnings( "deprecation" )
    public void init(Context context, String url)
    {
        mContext = context;
        // 设置滚动条样式为外部滚动
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        // when the webview scrolled,the edges was faded
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
        // 支持JavaScript
        getSettings().setJavaScriptEnabled(true);
        // 支持JavaScript的Window.open()方法
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // 页面内容缓存方式
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // app是否能缓存数据
        getSettings().setAppCacheEnabled(true);
        // 设置缓存数据路径
        getSettings().setAppCachePath(getCachePath(context));
        // 设置缓存空间
        getSettings().setAppCacheMaxSize(1024 * 1024 * 10);
        // 支持html本地存储数据 window.localStorage.setItem(key,value);
        getSettings().setDomStorageEnabled(true);
        // 宽高由html也面的ViewPort控制
        getSettings().setUseWideViewPort(true);
        // 支持手动缩放
        getSettings().setBuiltInZoomControls(true);
        // 支持多窗口，且必须重写 WebChromeClient#onCreateWindow
        // this.getSettings().setSupportMultipleWindows(true);
        // 支持页面缩放
        getSettings().setSupportZoom(true);
        getSettings().setDefaultZoom(adapterDisplay());
        // 适应屏幕
        getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        setWebChromeClient(new WebChromeClient()
        {

            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                if ( mListener != null )
                {
                    mListener.onProgressChanged(view, newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result)
            {
                // return super.onJsAlert(view, url, message, result);
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("提示").setMessage(message).setPositiveButton("确定", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                return true;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback)
            {
                super.onShowCustomView(view, callback);
            }
        });
        setWebViewClient(new WebViewClient()
        {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                if ( mListener != null )
                {
                    mListener.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                if ( mListener != null )
                {
                    mListener.onPageFinished(view, url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                // 页面上有数字会导致连接电话
                if ( url.indexOf("tel:") >= 0 )
                {
                    telPhone(url.substring(4));
                    return true;
                } else
                {
                    return mListener.overrideUrlLoading(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                // 加载错误处理
                if ( mListener != null )
                {
                    mListener.onReceivedError(view, errorCode, description, failingUrl);
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host,
                                                  String realm)
            {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }

        });
        if ( mListener != null )
        {
            mListener.addMoreJavaScriptInterface(this);
        }
        this.loadUrl(url);
    }

    public void setWebViewListener(IWebViewListener listener)
    {
        mListener = listener;
    }

    /**
     * 打电话
     *
     * @param phone
     */
    public void telPhone(String phone)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private ZoomDensity adapterDisplay()
    {
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        ZoomDensity zoomDensity = ZoomDensity.MEDIUM;
        switch ( screenDensity )
        {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        return zoomDensity;
    }

}

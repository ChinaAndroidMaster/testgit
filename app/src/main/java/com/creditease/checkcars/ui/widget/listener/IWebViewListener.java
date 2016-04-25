package com.creditease.checkcars.ui.widget.listener;

import android.graphics.Bitmap;
import android.webkit.WebView;

public interface IWebViewListener
{
    void onProgressChanged(WebView view, int newProgress);

    void onPageStarted(WebView view, String url, Bitmap favicon);

    boolean overrideUrlLoading(WebView view, String url);

    void onPageFinished(WebView view, String url);

    void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

    void addMoreJavaScriptInterface(WebView webView);
}

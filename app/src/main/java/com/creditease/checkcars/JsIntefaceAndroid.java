package com.creditease.checkcars;


import android.util.Log;
import android.webkit.JavascriptInterface;

public class JsIntefaceAndroid
{

    public String value;

    @JavascriptInterface
    public void getValue(String value)
    {
        Log.e("得到传递的值", value);
        this.value = value;
    }

    @JavascriptInterface
    public String toString()
    {
        // TODO Auto-generated method stub
        return "androidback";
    }


}

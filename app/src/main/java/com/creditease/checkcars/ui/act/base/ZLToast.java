package com.creditease.checkcars.ui.act.base;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creditease.checkcars.tools.Util;

/**
 * Toast
 *
 * @author 子龍
 * @function
 * @date 2015年3月4日
 * @company CREDITEASE
 */
public class ZLToast
{

    public static View toastView;
    private static ZLToast toast;
    private static TextView contTV;
    private Handler handler = new Handler();

    private ZLToast()
    {

    }

    public static ZLToast getToast()
    {
        if ( toast == null )
        {
            toast = new ZLToast();
        }
        return toast;
    }

    /**
     * 初始化自定义ToastView
     *
     * @param inflater
     * @param toastLayoutId
     * @param contTvId
     */
    public static void initToast(LayoutInflater inflater, int toastLayoutId, int contTvId)
    {
        toastView = inflater.inflate(toastLayoutId, null);
        contTV = ( TextView ) toastView.findViewById(contTvId);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param resID   文本资源ID
     */
    public void showToast(Context context, int resID)
    {
        if ( context == null )
        {
            return;
        }
        String text = Util.getStringFromRes(context, resID);
        Toast toast = Toast.makeText(context, text == null ? "" : text, Toast.LENGTH_SHORT);
        if ( toastView != null )
        {
            contTV.setText(text == null ? "" : text);
            toast.setView(toastView);
        }
        toast.show();
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param text    显示文本
     */
    public void showToast(Context context, String text)
    {
        if ( (text == null) || text.equals("") )
        {
            return;
        }
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        int gravity = toast.getGravity();
        int mx = toast.getXOffset();
        int my = toast.getYOffset();
        toast.setGravity(gravity, mx, my + 50);
        if ( toastView != null )
        {
            contTV.setText(text);
            toast.setView(toastView);
        }
        toast.show();
    }

    public void showToast(Context context, String text, int my)
    {
        if ( (text == null) || text.equals("") )
        {
            return;
        }
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(toast.getGravity(), toast.getXOffset(), toast.getYOffset() + my);
        if ( toastView != null )
        {
            contTV.setText(text);
            toast.setView(toastView);
        }
        toast.show();

    }

    public void showToast(Context context, String text, long duration)
    {
        if ( (text == null) || text.equals("") )
        {
            return;
        }
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        int gravity = toast.getGravity();
        int mx = toast.getXOffset();
        int my = toast.getYOffset();
        toast.setGravity(gravity, mx, my + 50);
        if ( toastView != null )
        {
            contTV.setText(text);
            toast.setView(toastView);
        }
        toast.show();

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                toast.cancel();
            }
        }, duration);
    }
}

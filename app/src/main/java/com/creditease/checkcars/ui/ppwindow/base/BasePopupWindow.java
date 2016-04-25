/**
 * Copyright © 2013 金焰科技. All rights reserved.
 */
package com.creditease.checkcars.ui.ppwindow.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * @author 子龍
 * @date 2014年11月21日
 * @company 宜信
 */
public class BasePopupWindow extends PopupWindow
{

    protected View mContentView;
    protected onSubmitClickListener mOnSubmitClickListener;

    public BasePopupWindow()
    {
        super();
    }

    public BasePopupWindow(Context context)
    {
        super(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @SuppressLint( "NewApi" )
    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BasePopupWindow(int width, int height)
    {
        super(width, height);
    }

    public BasePopupWindow(View contentView)
    {
        super(contentView);
    }

    @SuppressWarnings( "deprecation" )
    public BasePopupWindow(View contentView, int width, int height)
    {
        super(contentView, width, height, true);
        mContentView = contentView;
        setBackgroundDrawable(new BitmapDrawable());
    }

    public BasePopupWindow(View contentView, int width, int height, boolean focusable)
    {
        super(contentView, width, height, focusable);
    }

    public View findViewById(int id)
    {
        return mContentView.findViewById(id);
    }

    /**
     * 添加确认单击监听
     *
     * @param l
     */
    public void setOnSubmitClickListener(onSubmitClickListener l)
    {
        mOnSubmitClickListener = l;
    }

    @Override
    public void showAsDropDown(View anchor)
    {
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff)
    {
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity)
    {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y)
    {
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 显示在parent的中心
     *
     * @param parent
     */
    public void showViewCenter(View parent)
    {
        showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    /**
     * 显示在parent的上部并水平居中
     *
     * @param parent
     */
    public void showViewTopCenter(View parent)
    {
        showAtLocation(parent, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public interface onSubmitClickListener
    {
        void onClick();
    }

}

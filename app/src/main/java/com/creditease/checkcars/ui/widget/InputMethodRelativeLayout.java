package com.creditease.checkcars.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.RelativeLayout;

/**
 * 自定义布局解决键盘弹出挡住输入框的问题
 *
 * @author 子龍
 * @function
 * @date 2015年10月13日
 * @company CREDITEASE
 */
public class InputMethodRelativeLayout extends RelativeLayout
{
    protected OnSizeChangedListenner onSizeChangedListenner;
    private int width;
    private boolean sizeChanged = false; // 变化的标志
    private int height;
    @SuppressWarnings( "unused" )
    private int screenWidth; // 屏幕宽度
    private int screenHeight; // 屏幕高度

    @SuppressWarnings( "deprecation" )
    public InputMethodRelativeLayout(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        Display localDisplay = (( Activity ) paramContext).getWindowManager().getDefaultDisplay();
        screenWidth = localDisplay.getWidth();
        screenHeight = localDisplay.getHeight();
    }

    public InputMethodRelativeLayout(Context paramContext, AttributeSet paramAttributeSet,
                                     int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        width = widthMeasureSpec;
        height = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        // 监听不为空、宽度不变、当前高度与历史高度不为0
        if ( (onSizeChangedListenner != null) && (w == oldw) && (oldw != 0) && (oldh != 0) )
        {
            if ( (h >= oldh) || (Math.abs(h - oldh) <= ((1 * screenHeight) / 4)) )
            {
                if ( (h <= oldh) || (Math.abs(h - oldh) <= ((1 * screenHeight) / 4)) )
                {
                    return;
                }
                sizeChanged = false;
            } else
            {
                sizeChanged = true;
            }
            onSizeChangedListenner.onSizeChange(sizeChanged, oldh, h);
            measure((width - w) + getWidth(), (height - h) + getHeight());
        }
    }

    /**
     * 设置监听事件
     *
     * @param paramonSizeChangedListenner
     */
    public void setOnSizeChangedListenner(
            InputMethodRelativeLayout.OnSizeChangedListenner paramonSizeChangedListenner)
    {
        onSizeChangedListenner = paramonSizeChangedListenner;
    }

    /**
     * 大小改变的内部接口
     *
     * @author junjun
     */
    public abstract interface OnSizeChangedListenner
    {
        public abstract void onSizeChange(boolean paramBoolean, int w, int h);
    }
}

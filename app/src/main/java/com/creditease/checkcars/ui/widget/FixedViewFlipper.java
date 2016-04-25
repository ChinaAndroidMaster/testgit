package com.creditease.checkcars.ui.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/**
 * @author 子龍
 * @date 2014年5月9日
 * @company 宜信
 */
public class FixedViewFlipper extends ViewFlipper
{

    public FixedViewFlipper(Context context)
    {
        super(context);
    }

    public FixedViewFlipper(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow()
    {

        int apiLevel = Build.VERSION.SDK_INT;

        if ( apiLevel >= 7 )
        {
            try
            {
                super.onDetachedFromWindow();
            } catch ( IllegalArgumentException e )
            {
            } finally
            {
                super.stopFlipping();
            }
        } else
        {
            try
            {
                super.onDetachedFromWindow();
            } catch ( IllegalArgumentException e )
            {
                super.stopFlipping();
            }
        }
    }

}

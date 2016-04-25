/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author noah.zheng
 */
public class CreditEaseTextView extends TextView
{

    public CreditEaseTextView(Context context)
    {
        super(context);
    }

    public CreditEaseTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CreditEaseTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type)
    {
        if ( text == null )
        {
            text = "";
        }
        super.setText(text, type);
    }
}

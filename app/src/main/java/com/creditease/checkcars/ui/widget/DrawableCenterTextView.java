package com.creditease.checkcars.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class DrawableCenterTextView extends TextView
{

    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context)
    {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        //获取TextView上的Drawable
        Drawable[] drawables = getCompoundDrawables();
        if ( drawables != null )
        {
            Drawable drawableLeft = drawables[0];
            if ( drawableLeft != null )
            {
                //获取文字内容的宽度
                float textWidth = getPaint().measureText(getText().toString());
                int drawblePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                //drawbale的宽度
                drawableWidth = drawableLeft.getIntrinsicWidth();
                //文字+drawable的宽度
                float bodyWidth = textWidth + drawableWidth + drawblePadding;
                //移动到正中位置
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }

}

package com.creditease.checkcars.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

@SuppressLint( "ClickableViewAccessibility" )
public class CEViewPager extends ViewPager
{
    private ViewGroup parent;

    public CEViewPager(Context context)
    {
        super(context);
    }

    public CEViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if ( parent != null )
        {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0)
    {
        if ( parent != null )
        {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0)
    {
        if ( parent != null )
        {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(arg0);
    }

    public void setNestedpParent(ViewGroup parent)
    {
        this.parent = parent;
    }
}

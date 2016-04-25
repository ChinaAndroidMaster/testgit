package com.creditease.checkcars.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

@SuppressLint( "ClickableViewAccessibility" )
public class CEHorizontalListView extends HorizontalListView
{

    private ViewPager mViewPager;

    public CEHorizontalListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if ( mViewPager != null )
        {
            mViewPager.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if ( mViewPager != null )
        {
            mViewPager.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if ( mViewPager != null )
        {
            mViewPager.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(event);
    }

    public void setViewPager(ViewPager viewPager)
    {
        mViewPager = viewPager;
    }
}

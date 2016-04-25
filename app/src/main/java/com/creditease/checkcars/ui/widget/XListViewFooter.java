/**
 * Copyright © 2013 金焰科技. All rights reserved.
 */
package com.creditease.checkcars.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creditease.checkcars.R;

/**
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
@SuppressLint( "InflateParams" )
public class XListViewFooter extends LinearLayout
{
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private Context mContext;

    private View mContentView;
    private View mProgressBar;
    private TextView mHintView;
    private String hint_normal = "";
    private String hint_ready = "";

    public XListViewFooter(Context context)
    {
        super(context);
        initView(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    public int getBottomMargin()
    {
        LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams ) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }

    public void setBottomMargin(int height)
    {
        if ( height < 0 )
        {
            return;
        }
        LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams ) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide()
    {
        if ( mContentView == null )
        {
            return;
        }
        LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams ) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    @SuppressWarnings( "deprecation" )
    private void initView(Context context)
    {
        mContext = context;
        hint_normal = getResources().getString(R.string.xlistview_footer_hint_normal);
        hint_ready = getResources().getString(R.string.xlistview_footer_hint_ready);
        View moreView = LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
        mHintView = ( TextView ) moreView.findViewById(R.id.xlistview_footer_hint_textview);
    }

    /**
     * loading status
     */
    public void loading()
    {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * normal status
     */
    public void normal()
    {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void setShowTxt(String txt)
    {
        hint_normal = txt;
        mHintView.setText(txt);
    }

    public void setState(int state)
    {
        mHintView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mHintView.setVisibility(View.INVISIBLE);
        if ( state == STATE_READY )
        {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(hint_ready);
        } else if ( state == STATE_LOADING )
        {
            mProgressBar.setVisibility(View.VISIBLE);
        } else
        {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(hint_normal);
        }
    }

    /**
     * show footer
     */
    public void show()
    {
        LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams ) mContentView.getLayoutParams();
        lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

}

/**
 * Copyright © 2013 金焰科技. All rights reserved.
 */
package com.creditease.checkcars.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
//import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
//import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
//import android.widget.ImageView;
import android.widget.LinearLayout;
//import android.widget.ProgressBar;
import android.widget.TextView;

import com.creditease.checkcars.R;

/**
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
@SuppressLint( "InflateParams" )
public class XListViewHeader extends LinearLayout
{
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;
    private final int ROTATE_ANIM_DURATION = 180;
    public LoadingLayout mLoadingLayout;
    private LinearLayout mContainer;
    //  private ImageView mArrowImageView;
//  private ProgressBar mProgressBar;
    // private ImageView mProgressBar;
    private TextView mHintTextView;
    private int mState = STATE_NORMAL;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    public XListViewHeader(Context context)
    {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XListViewHeader(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    public int getVisiableHeight()
    {
        return mContainer.getHeight();
    }

    public void setVisiableHeight(int height)
    {
        if ( height < 0 )
        {
            height = 0;
        }
        LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams ) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    private void initView(Context context)
    {
        // 初始情况，设置下拉刷新view高度为0
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mContainer =
                ( LinearLayout ) LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        mLoadingLayout = ( LoadingLayout ) findViewById(R.id.xlistview_header_loading);
//    mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = ( TextView ) findViewById(R.id.xlistview_header_hint_textview);
//    mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);

        mRotateUpAnim =
                new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim =
                new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state)
    {
        if ( state == mState )
        {
            return;
        }

        if ( state == STATE_REFRESHING )
        { // 显示进度
//      mArrowImageView.clearAnimation();
//      mArrowImageView.setVisibility(View.INVISIBLE);
//      mProgressBar.setVisibility(View.VISIBLE);
//      AnimationDrawable ani = (AnimationDrawable) mProgressBar.getBackground();
//      if (ani != null) {
//        ani.setOneShot(false);
//        ani.start();
//      }
//      mLoadingLayout.startAnimation();
        } else
        { // 显示箭头图片
//      mArrowImageView.setVisibility(View.VISIBLE);
//      mProgressBar.setVisibility(View.INVISIBLE);
//      mLoadingLayout.clearAnimation();
        }

        switch ( state )
        {
            case STATE_NORMAL:
                if ( mState == STATE_READY )
                {
//          mArrowImageView.startAnimation(mRotateDownAnim);
//          mLoadingLayout.startAnimation();
                    mLoadingLayout.clearAnimation();
                }
                if ( mState == STATE_REFRESHING )
                {
//          mArrowImageView.clearAnimation();
//          mLoadingLayout.clearAnimation();
                    mLoadingLayout.clearAnimation();
                }
                mHintTextView.setText(R.string.xlistview_header_hint_normal);
                break;
            case STATE_READY:
                if ( mState != STATE_READY )
                {
//          mArrowImageView.clearAnimation();
//          mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(R.string.xlistview_header_hint_ready);
                }
                mLoadingLayout.startAnimation();
                break;
            case STATE_REFRESHING:
                mHintTextView.setText(R.string.xlistview_header_hint_loading);
                break;
            default:
        }

        mState = state;
    }

}

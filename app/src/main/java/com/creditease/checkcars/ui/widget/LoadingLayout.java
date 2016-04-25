package com.creditease.checkcars.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.creditease.checkcars.R;

public class LoadingLayout extends RelativeLayout
{

    private ImageView mLoadCarBody;
    private ImageView mLoadCarFront;
    private ImageView mLoadCarBehind;
    private ImageView mLoadCarLine;
    private LayoutInflater mInflater;
    private RelativeLayout mRootView;
    private Animation bodyAnim;
    private Animation wheelAnim;
    private Animation lineAnim;

    public LoadingLayout(Context context)
    {
        super(context);
        initView(context);
    }

    public LoadingLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context)
    {
        mInflater = LayoutInflater.from(context);
        mRootView = ( RelativeLayout ) mInflater.inflate(R.layout.loading, this);
        mLoadCarBody = ( ImageView ) mRootView.findViewById(R.id.loading_car_body);
        mLoadCarFront = ( ImageView ) mRootView.findViewById(R.id.loading_car_front);
        mLoadCarBehind = ( ImageView ) mRootView.findViewById(R.id.loading_car_behind);
        mLoadCarLine = ( ImageView ) mRootView.findViewById(R.id.loading_car_line);
        initAnimation(context);
    }


    private void initAnimation(Context context)
    {
        bodyAnim = AnimationUtils.loadAnimation(context, R.anim.car_body_translate);
        bodyAnim.setInterpolator(new LinearInterpolator());
        wheelAnim = AnimationUtils.loadAnimation(context, R.anim.car_wheel_ratate);
        wheelAnim.setInterpolator(new LinearInterpolator());
        lineAnim = AnimationUtils.loadAnimation(context, R.anim.car_line_translate);
        lineAnim.setInterpolator(new LinearInterpolator());

    }

    public void startAnimation()
    {
        mLoadCarLine.setVisibility(View.VISIBLE);
        mLoadCarBody.setAnimation(bodyAnim);
        mLoadCarFront.setAnimation(wheelAnim);
        mLoadCarBehind.setAnimation(wheelAnim);
        mLoadCarLine.setAnimation(lineAnim);
    }

    public void clearAnimation()
    {
        mLoadCarLine.setVisibility(View.GONE);
        mLoadCarBody.clearAnimation();
        mLoadCarFront.clearAnimation();
        mLoadCarBehind.clearAnimation();
        mLoadCarLine.clearAnimation();
    }
}

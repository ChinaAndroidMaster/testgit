package com.creditease.checkcars.ui.act.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 子龍
 * @function
 * @date 2015年7月8日
 * @company CREDITEASE
 */
public abstract class BaseFragment extends Fragment
{

    protected LayoutInflater inflater;

    private View view;

    protected Activity getContext()
    {
        return getActivity();
    }

    /*
     * 布局文件ID
     */
    protected abstract int getLayoutId();

    protected View getView(int layoutId)
    {
        return inflater.inflate(layoutId, null);
    }

    /**
     * 初始化view
     *
     * @param view 下午2:49:05
     */
    protected abstract void initView(View view);

    /**
     * 初始化数据
     * <p/>
     * 下午2:49:29
     */
    protected abstract void initData();

    /**
     * 初始化事件
     * <p/>
     * 下午2:49:19
     */
    protected abstract void initEvents();


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.inflater = inflater;
        if ( null != container )
        {
            ViewGroup parent = ( ViewGroup ) container.getParent();
            if ( (null != parent) && (view != null) )
            {
                parent.removeView(view);
            }
        }
        view = getView(getLayoutId());
        initView(view);// 控件初始化
        initEvents();
        initData();
        return view;
    }

}

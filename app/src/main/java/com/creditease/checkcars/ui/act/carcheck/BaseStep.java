package com.creditease.checkcars.ui.act.carcheck;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;

/**
 * @author 子龍
 * @function
 * @date 2015年11月5日
 * @company CREDITEASE
 */
public abstract class BaseStep
{
    public CarCheckActivity mContext;
    protected View rootView;
    protected CarReport report;
    protected LoadingGifDialog loadDialog;// 加载loading


    public BaseStep(CarCheckActivity activity, View contentRootView, CarReport report)
    {
        mContext = activity;
        rootView = contentRootView;
        this.report = report;
        loadDialog = new LoadingGifDialog(mContext);
        initViews();
        initEvents();
    }

    public View findViewById(int id)
    {
        return rootView.findViewById(id);
    }

    public abstract void initViews();

    public abstract void initEvents();

    public abstract boolean validate();

    public abstract void destroy();

    /**
     * toast
     *
     * @param text 下午4:40:26
     */
    protected void showToast(String text)
    {
        mContext.showToast(text);
    }

    /**
     * toast
     *
     * @param textResId 下午4:40:18
     */
    protected void showToast(int textResId)
    {
        mContext.showToast(textResId);
    }

    /**
     * @param intent
     * @param requestCode 下午2:20:24
     */
    protected void startActivityForResult(Intent intent, int requestCode)
    {
        mContext.startActivityForResult(intent, requestCode);
    }

    protected View getView(int layout)
    {
        return LayoutInflater.from(mContext).inflate(layout, null);
    }
}

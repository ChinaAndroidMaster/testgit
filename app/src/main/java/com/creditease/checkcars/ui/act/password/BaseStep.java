package com.creditease.checkcars.ui.act.password;

import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

public abstract class BaseStep
{
    protected PasswordResetActivity mActivity;
    protected Context mContext;
    protected onNextActionListener mOnNextActionListener;
    private View mContentRootView;

    public BaseStep(PasswordResetActivity activity, View contentRootView)
    {
        mActivity = activity;
        mContext = mActivity;
        mContentRootView = contentRootView;
        initViews();
        initEvents();
    }


    public View findViewById(int id)
    {
        return mContentRootView.findViewById(id);
    }

    public abstract void initViews();

    public abstract void initEvents();

    public abstract boolean isChange();

    public abstract boolean validate();

    public void doNext()
    {

    }

    public void doPrevious()
    {

    }

    public void nextAnimation()
    {

    }

    public void preAnimation()
    {

    }

    protected boolean isNull(EditText editText)
    {
        String text = editText.getText().toString().trim();
        if ( (text != null) && (text.length() > 0) )
        {
            return false;
        }
        return true;
    }

    /**
     * 字母 数字 汉子
     *
     * @param text
     * @return
     */
    protected boolean isValid(String text)
    {
        String matchReg = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{0,}$";
        if ( Pattern.compile(matchReg).matcher(text).matches() )
        {
            return true;
        }
        return false;
    }

    protected boolean matchPhone(String text)
    {
        // "^((13[0-9])|(15[0-9])|(18[0-9]))\d{8}$";
        String str = "^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";
        if ( Pattern.compile(str).matcher(text).matches() )
        {
            return true;
        }
        return false;
    }

    public void setOnNextActionListener(onNextActionListener listener)
    {
        mOnNextActionListener = listener;
    }

    public interface onNextActionListener
    {
        void next();
    }
}

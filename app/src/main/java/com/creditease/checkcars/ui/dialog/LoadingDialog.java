/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.dialog.base.BDialog;

/**
 * @author 子龍
 * @function
 * @date 2015年3月17日
 * @company CREDITEASE
 */
public class LoadingDialog extends BDialog
{

    private TextView mHtvText;

    public LoadingDialog(Context context)
    {
        super(context);
    }

    @Override
    public int getDialogViewLayoutId()
    {
        return R.layout.dialog_loading;
    }

    @Override
    public void initDialogView()
    {
        mHtvText = ( TextView ) view.findViewById(R.id.dialog_loading_text);
    }

    @Override
    public boolean isCanceledOnKeyBack()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCanceledOnTouchOutside()
    {
        return true;
    }

    public void setText(int textResId)
    {
        mHtvText.setText(textResId);
    }

    public void setText(String text)
    {
        mHtvText.setText(text);
    }
}

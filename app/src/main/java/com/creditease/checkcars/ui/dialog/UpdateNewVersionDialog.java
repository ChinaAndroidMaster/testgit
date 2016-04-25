/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.dialog.base.BDialog;

/**
 * @author 子龍
 * @function
 * @date 2015年4月24日
 * @company CREDITEASE
 */
public class UpdateNewVersionDialog extends BDialog
{

    private Button okBtn, cancelBtn;
    private TextView titleTV, contentTV;
    private boolean updateForce = false;

    public UpdateNewVersionDialog(Context context)
    {
        super(context);

    }

    @Override
    public int getDialogViewLayoutId()
    {
        return R.layout.dialog_update_version;
    }

    @Override
    public void initDialogView()
    {
        okBtn = ( Button ) view.findViewById(R.id.dialog_updateversion_btn_true);
        cancelBtn = ( Button ) view.findViewById(R.id.dialog_updateversion_btn_cancel);
        titleTV = ( TextView ) view.findViewById(R.id.dialog_updateversion_title);
        contentTV = ( TextView ) view.findViewById(R.id.dialog_updateversion_message);
    }

    @Override
    public boolean isCanceledOnKeyBack()
    {
        return updateForce;
    }

    @Override
    public boolean isCanceledOnTouchOutside()
    {
        return false;
    }

    public boolean isUpdateForce()
    {
        return updateForce;
    }

    public void setUpdateForce(boolean updateForce)
    {
        this.updateForce = updateForce;
    }

    public UpdateNewVersionDialog setContent(String text)
    {
        if ( contentTV != null )
        {
            contentTV.setText(text);
        }
        return this;
    }

    public UpdateNewVersionDialog setOnCancelBtnClickListener(
            android.view.View.OnClickListener onClickListener)
    {
        if ( (cancelBtn != null) && !isUpdateForce() )
        {
            cancelBtn.setOnClickListener(onClickListener);
        }
        return this;
    }

    public UpdateNewVersionDialog setOnOKBtnClickListener(
            android.view.View.OnClickListener onClickListener)
    {
        if ( okBtn != null )
        {
            okBtn.setOnClickListener(onClickListener);
        }
        return this;
    }

    public UpdateNewVersionDialog setTitle(String text)
    {
        if ( titleTV != null )
        {
            titleTV.setText(text);
        }
        return this;
    }

}

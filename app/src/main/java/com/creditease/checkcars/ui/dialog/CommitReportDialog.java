package com.creditease.checkcars.ui.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.dialog.base.BDialog;

public class CommitReportDialog extends BDialog
{
    private Button okBtn, cancelBtn;
    private TextView contentTV;

    public CommitReportDialog(Context context)
    {
        super(context);
    }

    @Override
    public int getDialogViewLayoutId()
    {
        return R.layout.dialog_commitreport;
    }

    @Override
    public void initDialogView()
    {
        okBtn = ( Button ) view.findViewById(R.id.dialog_commitreport_btn_true);
        cancelBtn = ( Button ) view.findViewById(R.id.dialog_commitreport_btn_cancel);
        contentTV = ( TextView ) view.findViewById(R.id.dialog_commitreport_message);
    }

    @Override
    public boolean isCanceledOnKeyBack()
    {
        return false;
    }

    @Override
    public boolean isCanceledOnTouchOutside()
    {
        return true;
    }

    public CommitReportDialog setContent(int resId)
    {
        if ( contentTV != null )
        {
            contentTV.setText(resId);
        }
        return this;
    }

    public CommitReportDialog setContent(String text)
    {
        if ( contentTV != null )
        {
            contentTV.setText(text);
        }
        return this;
    }

    public CommitReportDialog setOnCancelBtnClickListener(
            android.view.View.OnClickListener onClickListener)
    {
        if ( cancelBtn != null )
        {
            cancelBtn.setOnClickListener(onClickListener);
        }
        return this;
    }

    public CommitReportDialog setOnOKBtnClickListener(
            android.view.View.OnClickListener onClickListener)
    {
        if ( okBtn != null )
        {
            okBtn.setOnClickListener(onClickListener);
        }
        return this;
    }

}

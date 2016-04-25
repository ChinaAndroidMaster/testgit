package com.creditease.checkcars.ui.dialog.base;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author 子龍
 * @date 2014年11月7日
 * @company 宜信
 */
public abstract class BDialog
{

    protected Context mContext;
    protected View view;
    protected AlertDialog dialog;

    public BDialog(Context context)
    {
        init(context);
    }

    public void cancel()
    {
        dialog.cancel();
    }

    public void dismiss()
    {
        if ( dialog.isShowing() )
        {
            dialog.dismiss();
        }
    }

    public abstract int getDialogViewLayoutId();

    private void init(Context mContext)
    {
        dialog = new Builder(mContext).create();
        this.mContext = mContext;
        view = LayoutInflater.from(mContext).inflate(getDialogViewLayoutId(), null);
        initDialogView();
    }

    public abstract void initDialogView();

    public abstract boolean isCanceledOnKeyBack();

    public abstract boolean isCanceledOnTouchOutside();

    public void dismissCallback()
    {
    }

    public boolean isShowing()
    {
        return dialog.isShowing();
    }

    public void showDialog()
    {
        try
        {
            if ( dialog.isShowing() )
            {
                dialog.dismiss();
            }
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside());
            dialog.setOnKeyListener(new OnKeyListener()
            {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                {
                    switch ( keyCode )
                    {
                        case KeyEvent.KEYCODE_BACK:
                            return isCanceledOnKeyBack();
                    }
                    return false;
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
            {

                @Override
                public void onDismiss(DialogInterface dialog)
                {
                    dismissCallback();
                }
            });
            dialog.setView(view);

            dialog.show();
        } catch ( Exception e )
        {
            dialog.cancel();
        }
        // setView(view);
        // dialog.setContentView(view);
    }

}

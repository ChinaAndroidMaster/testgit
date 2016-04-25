package com.creditease.checkcars.ui.dialog.base;

import android.view.WindowManager;

public class TDialog
{
    private static TDialog zldialog;
    private BDialog dialog;

    private TDialog()
    {
    }

    /**
     * 获取Dialog工具 对象
     *
     * @param context
     * @return
     */
    private static TDialog getDialog()
    {
        if ( zldialog == null )
        {
            zldialog = new TDialog();
        }
        return zldialog;
    }

    protected TDialog builder(BDialog dialog)
    {
        this.dialog = dialog;
        return this;
    }

    public void showDialog()
    {
        if ( dialog != null )
        {
            try
            {
                if ( !dialog.isShowing() )
                {
                    dialog.showDialog();
                }
            } catch ( WindowManager.BadTokenException e )
            {
                try
                {
                    dialog.cancel();
                } catch ( Exception ee )
                {

                }
            }
        }
    }

    public static class Builder
    {
        public static TDialog builderDialog(BDialog dialog)
        {
            return getDialog().builder(dialog);
        }
    }

}

/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.dialog;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.widget.GifImageView;

/**
 * @author 子龍
 * @function
 * @date 2015年3月17日
 * @company CREDITEASE
 */
@SuppressLint( "NewApi" )
public class LoadingGifDialog
{

    protected View view;
    private GifImageView mGifView;
    private Context context;
    private Dialog dialog;

    public LoadingGifDialog(Context context)
    {
        this.context = context;
        init(context);
    }

    protected void init(Context context)
    {
        dialog = new Dialog(context, R.style.Translucent_NoTitle);
        view = LayoutInflater.from(context).inflate(getDialogViewLayoutId(), null);
        initDialogView();
    }

    public int getDialogViewLayoutId()
    {
        return R.layout.dialog_loading_gif;
    }

    public void initDialogView()
    {
        mGifView = ( GifImageView ) view.findViewById(R.id.dialog_loading_gifview);
        mGifView.setBackgroundColor(Color.TRANSPARENT);
        mGifView.setScaleType(ScaleType.FIT_XY);
        LayoutParams gifViewParams = new LinearLayout.LayoutParams(223, 89);
        mGifView.setLayoutParams(gifViewParams);
        new CustomAsynTask().execute();
    }

    public boolean isCanceledOnKeyBack()
    {
        return false;
    }

    public boolean isCanceledOnTouchOutside()
    {
        return true;
    }

    public void cancel()
    {
        dialog.cancel();
    }

    public void dismiss()
    {
        try
        {
            if ( dialog.isShowing() )
            {
                dialog.dismiss();
            }
        } catch ( IllegalArgumentException e )
        {
            cancel();
        }
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
            dialog.setContentView(view);

            dialog.show();
        } catch ( Exception e )
        {
            cancel();
        }
    }

    class CustomAsynTask extends AsyncTask< Void, Void, Object >
    {

        @Override
        protected Object doInBackground(Void... params)
        {
            AssetManager am = context.getResources().getAssets();
            InputStream gifInStream;
            try
            {
                gifInStream = am.open("loading_data_image.gif");
                return gifInStream;
            } catch ( IOException e )
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result)
        {
            mGifView.init(( InputStream ) result);
        }
    }
}

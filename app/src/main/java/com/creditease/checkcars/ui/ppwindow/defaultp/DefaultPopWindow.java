package com.creditease.checkcars.ui.ppwindow.defaultp;

import java.util.List;

import android.app.Activity;
import android.view.View;

import com.creditease.checkcars.ui.ppwindow.base.PullDownWindow;

/**
 * @author 子龍
 * @date 2014年6月18日
 * @company 宜信
 */
public class DefaultPopWindow extends PullDownWindow< DefaultObj >
{

    public DefaultPopWindow(Activity _activity, View _parentView, View _ShowlocationView, int width,
                            int h, InitViewDataPort< DefaultObj > port)
    {
        super(_activity, _parentView, _ShowlocationView, h, port, true);
    }

    @Override
    public View getView()
    {
        return null;
    }

    @Override
    public void setData(List< DefaultObj > data)
    {
        DefaultListAdapter adapter = new DefaultListAdapter(mContext, data);
        super.setAdapter(adapter);
    }


}

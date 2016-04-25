package com.creditease.checkcars.ui.ppwindow.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.creditease.checkcars.R;

/**
 * @author 子龍
 * @date 2014年6月18日
 * @company 宜信
 */
public abstract class BaseListAdapter< T > extends BaseAdapter
{
    public int selectPositon = -1;
    protected List< T > objList;
    protected LayoutInflater inflater;
    protected int textColor;
    protected int textSelectedColor;

    public BaseListAdapter(Context mContext, List< T > objList)
    {
        this.objList = objList;
        inflater = LayoutInflater.from(mContext);
        textColor = mContext.getResources().getColor(R.color.color_text_gray);
        textSelectedColor = mContext.getResources().getColor(R.color.color_text_black_gray);
    }

}

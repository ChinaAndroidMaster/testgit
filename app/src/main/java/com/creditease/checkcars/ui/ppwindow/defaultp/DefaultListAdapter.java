package com.creditease.checkcars.ui.ppwindow.defaultp;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.ppwindow.base.BaseListAdapter;

/**
 * @author 子龍
 * @date 2014年6月18日
 * @company 宜信
 */
public class DefaultListAdapter extends BaseListAdapter< DefaultObj >
{
    public DefaultListAdapter(Context mContext, List< DefaultObj > objList)
    {
        super(mContext, objList);
    }

    @Override
    public int getCount()
    {
        return objList == null ? 0 : objList.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return objList == null ? null : objList.get(arg0);
    }

    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }

    @SuppressLint( "InflateParams" )
    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder = null;
        if ( convertView == null )
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.popupwindow_listitem_tv, null);
            holder.textView = ( TextView ) convertView;
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        DefaultObj obj = objList.get(position);
        String title = obj.name;
        // if (obj.nameStrResId > 0)
        // holder.textView.setText(obj.nameStrResId);
        // else
        holder.textView.setText(title == null ? "(空)" : title);

        holder.textView.setBackgroundResource(R.drawable.bg_answer);
        holder.textView.setTextColor(textColor);
        holder.textView.setSelected(false);
        if ( selectPositon == position )
        {
            holder.textView.setSelected(true);
            holder.textView.setTextColor(textSelectedColor);
        }
        return convertView;
    }

    class ViewHolder
    {
        TextView textView;
    }

}

package com.creditease.checkcars.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.creditease.checkcars.R;
import com.creditease.utilframe.BitmapUtils;

public class PicAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List< String > imgPathList = new ArrayList< String >();
    private BitmapUtils utils;

    public PicAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        utils = new BitmapUtils(context);
    }

    @Override
    public int getCount()
    {
        return imgPathList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if ( convertView == null )
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listitem_picview, null);
            holder.imageView = ( ImageView ) convertView.findViewById(R.id.listitem_picview_imgv);
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        String p = imgPathList.get(position);
        setImage(p, holder.imageView);
        return convertView;
    }

    public void setImage(String imagePath, ImageView imgv)
    {
        if ( TextUtils.isEmpty(imagePath) )
        {
            imgv.setImageResource(R.drawable.icon);
            return;
        }
        utils.display(imgv, imagePath);
    }

    public void updateDataAndNoti(List< String > imgPathList)
    {
        this.imgPathList.clear();
        if ( imgPathList != null )
        {
            this.imgPathList.addAll(imgPathList);
        }
        notifyDataSetChanged();
    }

    class ViewHolder
    {
        ImageView imageView;
    }
}

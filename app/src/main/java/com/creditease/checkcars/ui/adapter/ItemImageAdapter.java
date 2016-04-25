package com.creditease.checkcars.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.creditease.checkcars.R;
import com.creditease.utilframe.BitmapUtils;

@SuppressLint( "InflateParams" )
public class ItemImageAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List< String > picPathList;
    private BitmapUtils utils;

    public ItemImageAdapter(List< String > picPathList, Context context)
    {
        mInflater = LayoutInflater.from(context);
        this.picPathList = picPathList;
        utils = new BitmapUtils(context);
    }

    @Override
    public int getCount()
    {
        return picPathList.size();
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
            convertView = mInflater.inflate(R.layout.listitem_imgv, null);
            holder.imageView = ( ImageView ) convertView.findViewById(R.id.item_carcheck_imgv);
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        String path = picPathList.get(position);
        setImage(path, holder.imageView);
        return convertView;
    }

    public void setImage(String imagePath, ImageView imgv)
    {
        if ( TextUtils.isEmpty(imagePath) )
        {
            imgv.setImageResource(R.drawable.icon_carcheck_takephone);
            return;
        }
        utils.display(imgv, imagePath);
    }

    class ViewHolder
    {
        ImageView imageView;
    }
}

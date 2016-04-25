package com.creditease.checkcars.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CCClassify;
import com.creditease.utilframe.BitmapUtils;

@SuppressLint( "InflateParams" )
public class ImageAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private CCClassify classify;
    private BitmapUtils utils;

    public ImageAdapter(CCClassify classify, Context context)
    {
        mInflater = LayoutInflater.from(context);
        this.classify = classify;
        utils = new BitmapUtils(context);
        if ( (classify != null) && (classify.mainItem != null)
                && (classify.mainItem.picPathList == null) )
        {
            classify.mainItem.picPathList = new ArrayList< String >(3);
        }
        if ( (classify != null) && !TextUtils.isEmpty(classify.mainItem.imgValue)
                && (classify.mainItem.picPathList.size() == 0) )
        {
            String[] values = classify.mainItem.imgValue.split(";");
            for ( String path : values )
            {
                classify.mainItem.picPathList.add(path);
            }
        }
    }

    @Override
    public int getCount()
    {
        return ((classify == null) || (classify.mainItem == null)) ? 1 : (classify.mainItem.picPathList
                .size() + 1);
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
        if ( position == 0 )
        {
            holder.imageView.setImageResource(R.drawable.icon_carcheck_takephone);
        } else
        {
            String path = classify.mainItem.picPathList.get(position - 1);
            setImage(path, holder.imageView);
        }
        return convertView;
    }

    public void setImage(String imagePath, ImageView imgv)
    {
        if ( TextUtils.isEmpty(imagePath) )
        {
            return;
        }
        utils.display(imgv, imagePath);
    }

    class ViewHolder
    {
        ImageView imageView;
    }
}

package com.creditease.checkcars.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.tools.ImageUtils;
import com.creditease.checkcars.tools.Util;

@SuppressLint( "InflateParams" )
public class WorkPhotoAdapter extends BaseAdapter
{

    private LayoutInflater mInflater;
    private ImageUtils utils;
    private List< String > mImagePaths = new ArrayList< String >();
    private int type;
    private Context context;
    public WorkPhotoAdapter(Context context, int type)
    {
        this.context = context;
        this.type = type;
        mInflater = LayoutInflater.from(context);
        utils = ImageUtils.getUtils(context);
        utils.configDefaultLoadFailedImage(R.drawable.mine_image_default);
        utils.configDefaultLoadingImage(R.drawable.mine_image_default);
    }

    @Override
    public int getCount()
    {
        return mImagePaths.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mImagePaths.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        String imagePath = mImagePaths.get(position);
        if ( convertView == null )
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mine_work_photo_item, null);
            holder.icon = ( ImageView ) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        if ( type == 1 )
        {
            LayoutParams params = holder.icon.getLayoutParams();
            params.width = Util.dip2px(context, 78);
            params.height = Util.dip2px(context, 54);
            holder.icon.setLayoutParams(params);
        }
        if ( !TextUtils.isEmpty(imagePath) )
        {
            utils.display(holder.icon, imagePath);
        }
        return convertView;
    }

    public void updateDataAndNotify(List< String > list)
    {
        mImagePaths.clear();
        mImagePaths.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder
    {
        public ImageView icon;
    }

}

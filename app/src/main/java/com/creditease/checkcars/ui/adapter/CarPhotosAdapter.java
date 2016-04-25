package com.creditease.checkcars.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CarImg;
import com.creditease.utilframe.BitmapUtils;

@SuppressLint( "InflateParams" )
public class CarPhotosAdapter extends BaseAdapter
{

    private LayoutInflater mInflater;
    private BitmapUtils utils;
    private List< CarImg > carImgList;
    public CarPhotosAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        utils = new BitmapUtils(context);
        utils.configDefaultLoadFailedImage(R.drawable.default_carimg);
        utils.configDefaultLoadingImage(R.drawable.default_carimg);
    }

    @Override
    public int getCount()
    {
        return carImgList == null ? 0 : carImgList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return carImgList == null ? null : carImgList.get(position);
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

        if ( convertView == null )
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.griditem_carimg, null);
            holder.icon = ( ImageView ) convertView.findViewById(R.id.griditem_carimg_imgv);
            holder.name = ( TextView ) convertView.findViewById(R.id.griditem_carimg_tv);
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        CarImg img = carImgList.get(position);
        if ( img != null )
        {
            utils.display(holder.icon, img.imgValue);
            holder.name.setText(img.imgName);
        }
        return convertView;
    }

    public void updateDataAndNotify(List< CarImg > list)
    {
        if ( list == null )
        {
            return;
        }
        carImgList = list;
        notifyDataSetChanged();
    }

    class ViewHolder
    {
        public ImageView icon;
        public TextView name;
    }

}

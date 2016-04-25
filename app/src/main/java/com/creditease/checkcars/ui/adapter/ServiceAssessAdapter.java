package com.creditease.checkcars.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.MineTopicsCore;
import com.creditease.checkcars.tools.ImageUtils;
import com.creditease.checkcars.tools.Utils;

@SuppressLint( "InflateParams" )
public class ServiceAssessAdapter extends BaseAdapter
{

    private LayoutInflater mInflater;
    private List< MineTopicsCore > mLists = new ArrayList< MineTopicsCore >();
    private ImageUtils utils;
    public ServiceAssessAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        utils = ImageUtils.getUtils(context);
        utils.configDefaultLoadFailedImage(R.drawable.mine_image_default);
        utils.configDefaultLoadingImage(R.drawable.mine_image_default);
    }

    @Override
    public int getCount()
    {
        return mLists.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mLists.get(position);
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
        MineTopicsCore topic = mLists.get(position);
        if ( convertView == null )
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mine_access_list_item, null);
            holder.mIconView = ( ImageView ) convertView.findViewById(R.id.mine_access_item_topicicon);
            holder.mNameView = ( TextView ) convertView.findViewById(R.id.mine_access_item_name);
            holder.mDescView = ( TextView ) convertView.findViewById(R.id.mine_access_item_desc);
            holder.mTimeView = ( TextView ) convertView.findViewById(R.id.mine_access_item_datetime);
            holder.mRatingBar = ( RatingBar ) convertView.findViewById(R.id.mine_access_item_ratingbar);
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        if ( !TextUtils.isEmpty(topic.headPic) )
        {
            utils.display(holder.mIconView, topic.headPic);
        }
        if ( !TextUtils.isEmpty(topic.nickName) )
        {
            holder.mNameView.setText(topic.nickName);
        }
        if ( !TextUtils.isEmpty(topic.remark) )
        {
            holder.mDescView.setText(topic.remark);
        } else
        {
            holder.mDescView.setText("默认好评");
        }
        String dateTime = Utils.timestamps2Date(topic.createTime, "yyyy-MM-dd");
        if ( !TextUtils.isEmpty(dateTime) )
        {
            holder.mTimeView.setText(dateTime);
        }
        holder.mRatingBar.setRating(topic.score);
        return convertView;
    }

    public void updateDataAndNotify(List< MineTopicsCore > list)
    {
        if ( list != null )
        {
            mLists.clear();
            mLists.addAll(list);
        }
        notifyDataSetChanged();
    }

    class ViewHolder
    {
        public ImageView mIconView;
        public TextView mNameView;
        public TextView mTimeView;
        public TextView mDescView;
        public RatingBar mRatingBar;
    }
}

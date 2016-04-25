package com.creditease.checkcars.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.utilframe.BitmapUtils;

public class TopicItemLayout extends RelativeLayout
{
    private LayoutInflater mInflater;
    private RelativeLayout mRootView;
    private ImageView topicIcon;
    private TextView topicName;
    private TextView topicTime;
    private TextView topicDesc;
    private RatingBar topicRatingBar;

    /**
     * 设置图片辅助类
     */
    private BitmapUtils bitmapUtils;

    public TopicItemLayout(Context context)
    {
        super(context);
        init(context);
    }

    public TopicItemLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public TopicItemLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    public void emptyShow()
    {
        topicIcon.setVisibility(View.GONE);
        topicName.setVisibility(View.GONE);
        topicTime.setVisibility(View.GONE);
        topicRatingBar.setVisibility(View.GONE);
        topicDesc.setText(getResources().getString(R.string.str_mine_no_topic));
        topicDesc.setGravity(Gravity.CENTER);
    }


    private void init(Context context)
    {
        mInflater = LayoutInflater.from(context);
        mRootView = ( RelativeLayout ) mInflater.inflate(R.layout.mine_topic_list_item, this);
        topicIcon = ( ImageView ) mRootView.findViewById(R.id.mine_topic_item_topicicon);
        topicName = ( TextView ) mRootView.findViewById(R.id.mine_topic_item_name);
        topicTime = ( TextView ) mRootView.findViewById(R.id.mine_topic_item_datetime);
        topicDesc = ( TextView ) mRootView.findViewById(R.id.mine_topic_item_desc);
        topicRatingBar = ( RatingBar ) mRootView.findViewById(R.id.mine_topic_item_ratingbar);
        setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.mine_image_default);
        bitmapUtils.configDefaultLoadingImage(R.drawable.mine_image_default);
    }

    public void show(String imgUrl, String name, String datetime, String desc, float score)
    {
        bitmapUtils.display(topicIcon, imgUrl);
        topicName.setText(name);
        topicTime.setText(datetime);
        topicDesc.setText(TextUtils.isEmpty(desc) ? "默认好评" : desc);
        topicRatingBar.setRating(score);
    }
}

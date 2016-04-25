package com.creditease.checkcars.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.QAnswer;

@SuppressLint( "InflateParams" )
public class AnswerAdapter extends BaseAdapter
{

    private LayoutInflater mInflater;
    private List< QAnswer > mLists = new ArrayList< QAnswer >();
    private Context mContext;
    private String answerdId;
    public AnswerAdapter(Context context, String answerdId)
    {
        mContext = context;
        this.answerdId = answerdId;
        mInflater = LayoutInflater.from(context);
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
        QAnswer answer = mLists.get(position);
        ViewHolder holder = null;
        if ( convertView == null )
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.answer_item, null);
            holder.mName = ( TextView ) convertView.findViewById(R.id.answer_item_name);
            holder.mContent = ( TextView ) convertView.findViewById(R.id.answer_item_content);
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        if ( answer != null )
        {
            String name;
            if ( answer.userInfo != null && !TextUtils.isEmpty(answer.userInfo.trueName) )
            {
                name = answer.userInfo.trueName;
            } else
            {
                name = mContext.getString(R.string.questtion_answer_title);
            }
            holder.mName.setText(name + ":");
            String content = answer.content;
            if ( !TextUtils.isEmpty(content) )
            {
                SpannableString value;
                if ( !TextUtils.isEmpty(answerdId) && TextUtils.equals(answerdId, answer.uuid) )
                {
                    value = generateSpnableText(content, R.drawable.answer_accepted, true);
                } else
                {
                    value = generateSpnableText(content, R.drawable.answer_accepted, false);
                }
                holder.mContent.setText(value);
            }
        }
        return convertView;
    }

    /**
     * 生成文本，大小或颜色不相同
     *
     * @param value
     * @param imageId
     * @param flag
     * @return
     */
    private SpannableString generateSpnableText(String value, int imageId, boolean flag)
    {
        SpannableString ss;
        if ( flag )
        {
            value = value + ".";
            ss = new SpannableString(value);
            ss.setSpan(new AbsoluteSizeSpan(14, true), 0, value.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 获取Drawable资源
            Drawable d = mContext.getResources().getDrawable(imageId);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            // 创建ImageSpan
            ImageSpan span = new ImageSpan(d, DynamicDrawableSpan.ALIGN_BASELINE);
            // 用ImageSpan替换文本
            ss.setSpan(span, value.length() - 1, value.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else
        {
            ss = new SpannableString(value);
            ss.setSpan(new AbsoluteSizeSpan(14, true), 0, value.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    public void updateDataAndNotify(List< QAnswer > list)
    {
        mLists.clear();
        mLists.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder
    {
        public TextView mContent;
        public TextView mName;
    }

}

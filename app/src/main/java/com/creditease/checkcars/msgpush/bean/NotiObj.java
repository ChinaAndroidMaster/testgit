package com.creditease.checkcars.msgpush.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 消息通知
 *
 * @author 子龍
 * @function
 * @date 2015年5月20日
 * @company CREDITEASE
 */
public class NotiObj implements Serializable, Parcelable
{

    /**
     *
     */
    private static final long serialVersionUID = 6631869378592934760L;

    /**
     * 显示通知
     */
    protected static final int SHOWNOTI_YES = 0;
    /**
     * 不显示
     */
    protected static final int SHOWNOTI_NO = 1;

    public int id;// 消息id
    public String sender; // 发送人
    public String toIds;// 接收人
    public String title;// 标题
    public int showNoti;// 是否显示通知栏
    public int msgType;// 消息类型
    public String content; // 消息内容
    public String data;// 消息数据

    public boolean showNoti()
    {
        return showNoti == SHOWNOTI_YES;
    }

    public NotiObj()
    {
        super();
    }

    private NotiObj(Parcel in)
    {
        id = in.readInt();
        sender = in.readString();
        toIds = in.readString();
        title = in.readString();
        showNoti = in.readInt();
        msgType = in.readInt();
        content = in.readString();
        data = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(sender);
        dest.writeString(toIds);
        dest.writeString(title);
        dest.writeInt(showNoti);
        dest.writeInt(msgType);
        dest.writeString(content);
        dest.writeString(data);
    }

    public static final Parcelable.Creator< NotiObj > CREATOR = new Parcelable.Creator< NotiObj >()
    {
        @Override
        public NotiObj createFromParcel(Parcel in)
        {
            return new NotiObj(in);
        }

        @Override
        public NotiObj[] newArray(int size)
        {
            return new NotiObj[size];
        }
    };

}

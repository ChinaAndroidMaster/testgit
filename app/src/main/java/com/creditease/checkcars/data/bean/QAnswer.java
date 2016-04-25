package com.creditease.checkcars.data.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class QAnswer implements Parcelable, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -7335770810430403150L;
    public String uuid;// 问题id
    public long createTime;// 创建时间
    public long modifyTime;// 更新时间
    public String fromId;// 回复人的id
    public int position;// 回复人的渠道 0师傅APP 1微信 2管理后台
    public String content;// 回复的内容
    public int agree;// 同意的人数
    public UserInfo userInfo;
    public String userInfoJson;

    public QAnswer()
    {
    }

    private QAnswer(Parcel in)
    {
        uuid = in.readString();
        createTime = in.readLong();
        modifyTime = in.readLong();
        fromId = in.readString();
        content = in.readString();
        agree = in.readInt();
        userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        userInfoJson = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(uuid);
        dest.writeLong(createTime);
        dest.writeLong(modifyTime);
        dest.writeString(fromId);
        dest.writeString(content);
        dest.writeInt(agree);
        dest.writeParcelable(userInfo, flags);
        dest.writeString(userInfoJson);
    }

    public static final Parcelable.Creator< QAnswer > CREATOR = new Parcelable.Creator< QAnswer >()
    {
        public QAnswer createFromParcel(Parcel in)
        {
            return new QAnswer(in);
        }

        public QAnswer[] newArray(int size)
        {
            return new QAnswer[size];
        }
    };

}

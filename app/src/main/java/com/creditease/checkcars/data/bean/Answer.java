package com.creditease.checkcars.data.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 检车
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */

public class Answer implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 1226529479820732557L;
    public String uuid;
    public String name;// 名称
    public String attributeId;// 检测项ID
    public String remark;// 描述
    public int isDefault;// 是否默认
    public int sequenceNum;// 排序
    public int weight;// 重要程度
    public boolean value;

    public Answer()
    {
    }

    public Answer(String name, boolean value, int weight)
    {
        super();
        this.name = name;
        this.value = value;
        this.weight = weight;
    }

    private Answer(Parcel in)
    {
        uuid = in.readString();
        name = in.readString();
        attributeId = in.readString();
        remark = in.readString();
        isDefault = in.readInt();
        sequenceNum = in.readInt();
        weight = in.readInt();
    }

    @Override
    public boolean equals(Object o)
    {
        if ( o == null )
        {
            return false;
        }
        if ( !(o instanceof Answer) )
        {
            return false;
        }
        Answer a = ( Answer ) o;
        return a.uuid.equals(uuid);
    }

    @Override
    public String toString()
    {
        return uuid + "---" + name;
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
        dest.writeString(name);
        dest.writeString(attributeId);
        dest.writeString(remark);
        dest.writeInt(isDefault);
        dest.writeInt(sequenceNum);
        dest.writeInt(weight);

    }

    public static final Parcelable.Creator< Answer > CREATOR = new Parcelable.Creator< Answer >()
    {
        @Override
        public Answer createFromParcel(Parcel in)
        {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size)
        {
            return new Answer[size];
        }
    };

}

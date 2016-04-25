package com.creditease.checkcars.data.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.utilframe.db.annotation.Id;
import com.creditease.utilframe.db.annotation.Table;

@Table( name = "user_pos_info" )
public class UserPositionInfo implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 8586076907106421071L;
    @Id
    public int _id;
    public String toId;// uuid
    public String fromId;// 客户端唯一识别码IMEI
    public String latitude;// 位置精度
    public String longitude;// 位置纬度
    public String addressName;// 具体地址
    public int toType; // ？？
    public int fromType; // 客户端类型
    public String recordTime;// 数据创建时间

    public UserPositionInfo()
    {
    }

    public UserPositionInfo(Parcel in)
    {
        toId = in.readString();
        fromId = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        addressName = in.readString();
        toType = in.readInt();
        fromType = in.readInt();
        recordTime = in.readString();
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(toId);
        dest.writeString(fromId);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(addressName);
        dest.writeInt(toType);
        dest.writeInt(fromType);
        dest.writeString(recordTime);
    }

    @Override
    public String toString()
    {
        return "toId = " + toId + "  fromId = " + fromId + "  latitude = " + latitude + "  longitude = "
                + longitude + "  addressName = " + addressName;
    }

    public static final Parcelable.Creator< UserPositionInfo > CREATOR =
            new Parcelable.Creator< UserPositionInfo >()
            {
                public UserPositionInfo createFromParcel(Parcel in)
                {
                    return new UserPositionInfo(in);
                }

                public UserPositionInfo[] newArray(int size)
                {
                    return new UserPositionInfo[size];
                }
            };

}

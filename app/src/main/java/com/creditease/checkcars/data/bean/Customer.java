package com.creditease.checkcars.data.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 子龍
 * @function
 * @date 2015年3月13日
 * @company CREDITEASE
 */
public class Customer implements Parcelable, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 3603169084048913985L;
    public int id;
    public String uuid;
    public String coreId;
    public String nickName;
    public String headPic;
    public String phoneNumber;

    public Customer()
    {
    }

    private Customer(Parcel in)
    {
        id = in.readInt();
        uuid = in.readString();
        coreId = in.readString();
        nickName = in.readString();
        headPic = in.readString();
        phoneNumber = in.readString();
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
        dest.writeString(uuid);
        dest.writeString(coreId);
        dest.writeString(nickName);
        dest.writeString(headPic);
        dest.writeString(phoneNumber);
    }

    public static final Parcelable.Creator< Customer > CREATOR = new Parcelable.Creator< Customer >()
    {
        public Customer createFromParcel(Parcel in)
        {
            return new Customer(in);
        }

        public Customer[] newArray(int size)
        {
            return new Customer[size];
        }
    };
}

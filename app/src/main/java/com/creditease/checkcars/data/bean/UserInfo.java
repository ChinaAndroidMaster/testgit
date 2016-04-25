package com.creditease.checkcars.data.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -2786414620397451106L;
    public String phoneNumber;//手机号
    public String headPic;//头像url
    public String nickName;//昵称
    public String trueName;//真实姓名

    public UserInfo()
    {
    }

    private UserInfo(Parcel in)
    {
        phoneNumber = in.readString();
        headPic = in.readString();
        nickName = in.readString();
        trueName = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(phoneNumber);
        dest.writeString(headPic);
        dest.writeString(nickName);
        dest.writeString(trueName);
    }


    public static final Parcelable.Creator< UserInfo > CREATOR = new Parcelable.Creator< UserInfo >()
    {
        public UserInfo createFromParcel(Parcel in)
        {
            return new UserInfo(in);
        }

        public UserInfo[] newArray(int size)
        {
            return new UserInfo[size];
        }
    };
}

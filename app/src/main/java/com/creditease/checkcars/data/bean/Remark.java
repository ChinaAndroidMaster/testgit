package com.creditease.checkcars.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Remark implements Parcelable
{

    public String motto;//类似于师傅宣言的内容
    public String specialty;//师傅擅长车系
    public String experience;//个人简介

    public Remark()
    {
    }

    public Remark(String motto, String specialty, String experience)
    {
        super();
        this.motto = motto;
        this.specialty = specialty;
        this.experience = experience;
    }

    private Remark(Parcel in)
    {
        motto = in.readString();
        specialty = in.readString();
        experience = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(motto);
        dest.writeString(specialty);
        dest.writeString(experience);
    }

    public static final Parcelable.Creator< Remark > CREATOR = new Parcelable.Creator< Remark >()
    {
        public Remark createFromParcel(Parcel in)
        {
            return new Remark(in);
        }

        public Remark[] newArray(int size)
        {
            return new Remark[size];
        }
    };

}

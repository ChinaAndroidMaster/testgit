package com.creditease.checkcars.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车检分类
 *
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
public class Note implements Parcelable, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 7051492817469117277L;
    public int id;
    public String name;
    public ArrayList< CCClassify > children;

    public Note()
    {
    }

    private Note(Parcel in)
    {
        id = in.readInt();
        name = in.readString();
        children = new ArrayList< CCClassify >(0);
        in.readList(children, CCClassify.class.getClassLoader());
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
        dest.writeString(name);
        dest.writeList(children);
    }

    public static final Parcelable.Creator< Note > CREATOR = new Parcelable.Creator< Note >()
    {
        public Note createFromParcel(Parcel in)
        {
            return new Note(in);
        }

        public Note[] newArray(int size)
        {
            return new Note[size];
        }
    };
}

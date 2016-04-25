package com.creditease.checkcars.net.oper.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.base.Result;

public class OperResponseForWeixin extends OperResponse implements Parcelable
{
    public int code;// 响应码 0000:成功
    public String msg;// 响应消息

    public static final Parcelable.Creator< OperResponseForWeixin > CREATOR =
            new Parcelable.Creator< OperResponseForWeixin >()
            {
                @Override
                public OperResponseForWeixin createFromParcel(Parcel in)
                {
                    return new OperResponseForWeixin(in);
                }

                @Override
                public OperResponseForWeixin[] newArray(int size)
                {
                    return new OperResponseForWeixin[size];
                }
            };

    public OperResponseForWeixin()
    {
    }

    private OperResponseForWeixin(Parcel in)
    {
        code = in.readInt();
        msg = in.readString();
        result = Result.getResult(in.readInt());
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(code);
        dest.writeString(msg);
        if ( result != null )
        {
            dest.writeInt(result.ordinal());
        }
    }
}

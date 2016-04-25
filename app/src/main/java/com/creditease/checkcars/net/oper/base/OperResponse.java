package com.creditease.checkcars.net.oper.base;

import android.os.Parcel;
import android.os.Parcelable;

public class OperResponse implements Parcelable
{
    public String respcode;// 响应码 0000:成功
    public String respmsg;// 响应消息
    public String data;// 响应数据

    public Result result;

    public static final Parcelable.Creator< OperResponse > CREATOR =
            new Parcelable.Creator< OperResponse >()
            {
                @Override
                public OperResponse createFromParcel(Parcel in)
                {
                    return new OperResponse(in);
                }

                @Override
                public OperResponse[] newArray(int size)
                {
                    return new OperResponse[size];
                }
            };

    public OperResponse()
    {
    }

    private OperResponse(Parcel in)
    {
        respcode = in.readString();
        respmsg = in.readString();
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
        dest.writeString(respcode);
        dest.writeString(respmsg);
        if ( result != null )
        {
            dest.writeInt(result.ordinal());
        }
    }
}

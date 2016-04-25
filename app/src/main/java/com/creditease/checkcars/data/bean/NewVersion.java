/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 子龍
 * @function
 * @date 2015年4月7日
 * @company CREDITEASE
 */
public final class NewVersion implements Parcelable
{
    /**
     * 强制更新
     */
    public static final int TYPE_UPDATE_FORCE = 1;
    /**
     * 非强制更新
     */
    public static final int TYPE_UPDATE_NORMAL = 0;

    public int platform;
    public String appName;
    public String title;
    public String versionName;
    public String remark;
    public String appUrl;
    public int status;
    public long createTime;
    public long modifyTime;
    public int updateType;

    public NewVersion()
    {

    }

    private NewVersion(Parcel in)
    {
        platform = in.readInt();
        appName = in.readString();
        title = in.readString();
        versionName = in.readString();
        remark = in.readString();
        appUrl = in.readString();
        status = in.readInt();
        createTime = in.readLong();
        modifyTime = in.readLong();
        updateType = in.readInt();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(platform);
        dest.writeString(appName);
        dest.writeString(title);
        dest.writeString(versionName);
        dest.writeString(remark);
        dest.writeString(appUrl);
        dest.writeInt(status);
        dest.writeLong(createTime);
        dest.writeLong(modifyTime);
        dest.writeInt(updateType);
    }

    public static final Parcelable.Creator< NewVersion > CREATOR = new Parcelable.Creator< NewVersion >()
    {
        public NewVersion createFromParcel(Parcel in)
        {
            return new NewVersion(in);
        }

        public NewVersion[] newArray(int size)
        {
            return new NewVersion[size];
        }
    };

}

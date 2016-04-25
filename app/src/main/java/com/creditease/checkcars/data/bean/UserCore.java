/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.data.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.utilframe.db.annotation.Id;
import com.creditease.utilframe.db.annotation.Table;

/**
 * 基本用户
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
@Table( name = "tab_usercore" )
public class UserCore implements Parcelable, Serializable
{

    /**
     * 普通用户
     */
    public static final int USER_TYPE_NORMAL = 0x0;
    /**
     * 师傅
     */
    public static final int USER_TYPE_APPRAISER = 0x1;
    /**
     * 运营
     */
    public static final int USER_TYPE_OPERATION = 0x2;
    /**
     * 销售
     */
    public static final int USER_TYPE_MARKET = 0x3;
    /**
     * 管理人员
     */
    public static final int USER_TYPE_MANAGER = 0x4;

    /**
     *
     */
    private static final long serialVersionUID = -934356123900686706L;
    /**
     *
     */
    // 基本身份
    @Id
    public int id;
    public String uuid;
    public String phoneNumber;// 手机号码
    public String headPic;// 头像
    public String password;
    /**
     * 加权位运算
     */
    public Long powerValue;// 用户类型（0客户1师傅2运营人员3销售4管理人员）
    public String email;
    public String loginName;
    public String nickName;
    public String trueName;
    public String createTime;
    public String modifyTime;
    public int deleted;

    public UserCore()
    {
    }

    /**
     * 加权用户类型
     *
     * @param userType
     * @return 下午6:51:53
     */
    public Long addUserType(Integer userType)
    {
        if ( powerValue == null )
        {
            powerValue = 0L;
        }
        powerValue = powerValue | (1 << userType);
        return powerValue;
    }


    /**
     * 判断用户是否属于某种类型
     *
     * @param userType 用户类型
     * @return 下午6:53:11
     */
    public boolean judgeUserType(Integer userType)
    {
        if ( powerValue == null )
        {
            return false;
        }
        long val = 1 << userType;
        if ( (powerValue & val) != 0 )
        {
            return true;
        } else
        {
            return false;
        }
    }


    private UserCore(Parcel in)
    {
        id = in.readInt();
        uuid = in.readString();
        phoneNumber = in.readString();
        headPic = in.readString();
        password = in.readString();
        powerValue = in.readLong();
        email = in.readString();
        loginName = in.readString();
        nickName = in.readString();
        trueName = in.readString();
        createTime = in.readString();
        modifyTime = in.readString();
        deleted = in.readInt();
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
        dest.writeString(phoneNumber);
        dest.writeString(headPic);
        dest.writeString(password);
        dest.writeLong(powerValue);
        dest.writeString(email);
        dest.writeString(loginName);
        dest.writeString(nickName);
        dest.writeString(trueName);
        dest.writeString(createTime);
        dest.writeString(modifyTime);
        dest.writeInt(deleted);
    }

    public static final Parcelable.Creator< UserCore > CREATOR = new Parcelable.Creator< UserCore >()
    {
        @Override
        public UserCore createFromParcel(Parcel in)
        {
            return new UserCore(in);
        }

        @Override
        public UserCore[] newArray(int size)
        {
            return new UserCore[size];
        }
    };

}

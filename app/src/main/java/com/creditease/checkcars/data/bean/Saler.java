package com.creditease.checkcars.data.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.utilframe.db.annotation.Id;
import com.creditease.utilframe.db.annotation.Table;

@Table( name = "tab_saler" )
public class Saler implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 5369367842438457622L;
    @Id
    public int id;
    public String uuid;
    public String coreId;// 对应用户基础信息表t_user_core中的uuid
    public Integer saleCode;// 销售码
    public String qrCodeUrl;
    public String invitationCode;// 邀请码
    public Long expireTime;// 过期时间
    public Integer curDayNum;// 当日单量
    public Integer curMonthNum;// 本月累计
    public Integer targetNum;// 客单目标
    public Integer targetType;// 类型:0 单/月
    public Integer cityOrder;// 所在城市排名
    public Integer countryOrder;// 全国排名
    public String incomeAmount;// 收入金额(某周期内的)
    public String cityId;// 服务城市,对应t_city表中的uuid
    public String depId;// 所属部门
    public String departmentName;// 所属部门名称
    public String price;// 提成单价
    public Integer priceType;// 计算类型:0 元/单
    public String phoneNumber;// 电话号码
    public String trueName;// 真实姓名
    public String headPic;// 头像
    public String homePageUrl;// 销售主页地址
    public String createTime;
    public String modifyTime;
    public int deleted;

    public Saler()
    {
    }

    private Saler(Parcel in)
    {
        id = in.readInt();
        uuid = in.readString();
        coreId = in.readString();
        saleCode = in.readInt();
        qrCodeUrl = in.readString();
        invitationCode = in.readString();
        expireTime = in.readLong();
        curDayNum = in.readInt();
        curMonthNum = in.readInt();
        targetNum = in.readInt();
        targetType = in.readInt();
        cityOrder = in.readInt();
        countryOrder = in.readInt();
        incomeAmount = in.readString();
        cityId = in.readString();
        depId = in.readString();
        departmentName = in.readString();
        price = in.readString();
        priceType = in.readInt();
        phoneNumber = in.readString();
        trueName = in.readString();
        headPic = in.readString();
        homePageUrl = in.readString();
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
        dest.writeString(coreId);
        dest.writeInt(saleCode);
        dest.writeString(qrCodeUrl);
        dest.writeString(invitationCode);
        dest.writeLong(expireTime);
        dest.writeInt(curDayNum);
        dest.writeInt(curMonthNum);
        dest.writeInt(targetNum);
        dest.writeInt(targetType);
        dest.writeInt(cityOrder);
        dest.writeInt(countryOrder);
        dest.writeString(incomeAmount);
        dest.writeString(cityId);
        dest.writeString(depId);
        dest.writeString(departmentName);
        dest.writeString(price);
        dest.writeInt(priceType);
        dest.writeString(phoneNumber);
        dest.writeString(trueName);
        dest.writeString(headPic);
        dest.writeString(homePageUrl);
        dest.writeString(createTime);
        dest.writeString(modifyTime);
        dest.writeInt(deleted);
    }

    public static final Parcelable.Creator< Saler > CREATOR = new Parcelable.Creator< Saler >()
    {
        @Override
        public Saler createFromParcel(Parcel in)
        {
            return new Saler(in);
        }

        @Override
        public Saler[] newArray(int size)
        {
            return new Saler[size];
        }
    };

}

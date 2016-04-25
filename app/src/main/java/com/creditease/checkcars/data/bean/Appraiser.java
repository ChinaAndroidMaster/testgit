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
 * 师傅
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
// @Table(name = "cc_appraiser_tab")
@Table( name = "appraiser_tab" )
public class Appraiser implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 2574052769090049621L;
    // 师傅身份
    @Id
    public int _id;
    public int id;
    public String uuid;// 用户id
    public String coreId;// 用户基础信息id
    public String trueName;// 真实姓名
    public String idCard;
    public int level;// 等级
    public String remark;// 个人简介
    public String maxPrice;// 车价区间最高值
    public String minPrice;// 车价区间最低值
    public int deleted;
    public String phoneNumber;// 电话号码
    public int score;// 积分
    public int appraiseNum;// 检车数量
    public String headPic;// 头像
    public String cityId;// 城市id
    public String provinceId;// 省id
    public String cityName;// 服务城市
    public String provinceName;// 服务省名字
    public int jobType;// 师傅类型 0 全职 1 兼职
    public String orderCount;// 总接单数
    // 部门信息
    public String departmentName;
    public String departmentId;
    // 工作状态
    public int status;// 状态 0 在职 1 离职 2 测试
    public int isProvideService;// 是否提供服务
    public String goodRate;// 好评率
    public int incomeOrder;// 收入排名
    public int finishNum;// 接单数
    public int finishOrder;// 接单数排名
    public String declaration;// 师傅宣言
    public String worksImgs;// 工作照
    public String incomeAmount;// 收入金额
    public String price;// 提成单价
    public int priceType;// 计算类型: 0 元/单 1 元/单

    public String salerHomePageUrl;// 销售主页


    public Appraiser()
    {

    }

    private Appraiser(Parcel in)
    {
        id = in.readInt();
        uuid = in.readString();
        coreId = in.readString();
        trueName = in.readString();
        idCard = in.readString();
        level = in.readInt();
        remark = in.readString();
        maxPrice = in.readString();
        minPrice = in.readString();
        deleted = in.readInt();
        phoneNumber = in.readString();
        score = in.readInt();
        appraiseNum = in.readInt();
        headPic = in.readString();
        cityId = in.readString();
        provinceId = in.readString();
        cityName = in.readString();
        provinceName = in.readString();
        jobType = in.readInt();
        orderCount = in.readString();
        departmentName = in.readString();
        departmentId = in.readString();
        status = in.readInt();
        isProvideService = in.readInt();
        goodRate = in.readString();
        incomeOrder = in.readInt();
        finishNum = in.readInt();
        finishOrder = in.readInt();
        declaration = in.readString();
        worksImgs = in.readString();
        incomeAmount = in.readString();
        price = in.readString();
        priceType = in.readInt();
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
        dest.writeString(trueName);
        dest.writeString(idCard);
        dest.writeString(cityId);
        dest.writeString(headPic);
        dest.writeString(provinceId);

        dest.writeInt(level);
        dest.writeString(remark);
        dest.writeString(maxPrice);
        dest.writeString(minPrice);
        dest.writeInt(deleted);
        dest.writeString(phoneNumber);
        dest.writeInt(score);
        dest.writeInt(appraiseNum);
        dest.writeString(cityName);
        dest.writeString(provinceName);
        dest.writeString(orderCount);
        dest.writeString(departmentName);
        dest.writeString(departmentId);
        dest.writeString(goodRate);
        dest.writeString(worksImgs);
        dest.writeString(incomeAmount);
        dest.writeString(price);
        dest.writeInt(jobType);
        dest.writeInt(status);
        dest.writeInt(isProvideService);
        dest.writeInt(incomeOrder);
        dest.writeInt(finishNum);
        dest.writeInt(finishOrder);
        dest.writeInt(priceType);
    }

    public static final Parcelable.Creator< Appraiser > CREATOR = new Parcelable.Creator< Appraiser >()
    {
        @Override
        public Appraiser createFromParcel(Parcel in)
        {
            return new Appraiser(in);
        }

        @Override
        public Appraiser[] newArray(int size)
        {
            return new Appraiser[size];
        }
    };

}

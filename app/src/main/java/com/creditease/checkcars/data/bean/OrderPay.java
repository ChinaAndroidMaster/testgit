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
 * 订单支付
 *
 * @author 子龍
 * @function
 * @date 2015年6月17日
 * @company CREDITEASE
 */
@Table( name = "order_pay_tab" )
public class OrderPay implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -5398628615265846199L;
    // 基本身份
    @Id
    public int id;
    public String tradeAmount;// 实际金额
    public String orignAmount;// 原价
    public String discountAmount;// 折扣
    public int payType;// 实付类型

    /**
     * 现金支付
     */
    public static final int TYPE_PAY_CASH = 0;
    /**
     * 微信支付
     */
    public static final int TYPE_PAY_WEIXIN = 1;

    public static final Parcelable.Creator< OrderPay > CREATOR = new Parcelable.Creator< OrderPay >()
    {
        @Override
        public OrderPay createFromParcel(Parcel in)
        {
            return new OrderPay(in);
        }

        @Override
        public OrderPay[] newArray(int size)
        {
            return new OrderPay[size];
        }
    };

    public OrderPay()
    {
    }

    private OrderPay(Parcel in)
    {
        id = in.readInt();
        tradeAmount = in.readString();
        orignAmount = in.readString();
        discountAmount = in.readString();
        payType = in.readInt();
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
        dest.writeString(tradeAmount);
        dest.writeString(orignAmount);
        dest.writeString(discountAmount);
        dest.writeInt(payType);
    }

}

package com.creditease.checkcars.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 支付实体
 *
 * @author 子龍
 * @function
 * @date 2015年11月17日
 * @company CREDITEASE
 */
public class PayBean implements Parcelable
{

    /**
     * 检车订单
     */
    public static final int TYPE_ORDER_CARCHECK = 0;

    /**
     * 质保订单
     */
    public static final int TYPE_ORDER_WARRANTY = 1;
    public String orderNmber;
    public String subjectName;
    public String subjectCont;
    public String subjectPrice;
    public String payerPhone;
    public String sellerId;
    public String orderId;
    public int orderType;

    public PayBean()
    {
    }

    /**
     * @param orderNmber   订单编号
     * @param subjectName  项目名称
     * @param subjectCont  项目描述
     * @param subjectPrice 支付金额
     * @param payerPhone   支付人手机号
     * @param sellerId     支付对象账号
     * @param orderId      订单uuid
     * @param orderType    订单状态
     */
    public PayBean(String orderNmber, String subjectName, String subjectCont, String subjectPrice,
                   String payerPhone, String sellerId, String orderId, int orderType)
    {
        super();
        this.orderNmber = orderNmber;
        this.subjectName = subjectName;
        this.subjectCont = subjectCont;
        this.subjectPrice = subjectPrice;
        this.payerPhone = payerPhone;
        this.sellerId = sellerId;
        this.orderId = orderId;
        this.orderType = orderType;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(orderNmber);
        dest.writeString(subjectName);
        dest.writeString(subjectCont);
        dest.writeString(subjectPrice);
        dest.writeString(payerPhone);
        dest.writeString(sellerId);
        dest.writeString(orderId);

    }

    public static final Parcelable.Creator< PayBean > CREATOR = new Parcelable.Creator< PayBean >()
    {
        @Override
        public PayBean createFromParcel(Parcel in)
        {
            return new PayBean(in);
        }

        @Override
        public PayBean[] newArray(int size)
        {
            return new PayBean[size];
        }
    };

    private PayBean(Parcel in)
    {
        orderNmber = in.readString();
        subjectName = in.readString();
        subjectCont = in.readString();
        subjectPrice = in.readString();
        payerPhone = in.readString();
        sellerId = in.readString();
        orderId = in.readString();
    }
}

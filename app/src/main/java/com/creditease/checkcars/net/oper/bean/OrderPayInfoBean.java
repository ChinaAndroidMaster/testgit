package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;
import java.util.List;

public class OrderPayInfoBean
{

    public static class Gift implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = -6622715693474459650L;
        public String name;
        public int giftNum;
        public String amount;
    }

    public static class OrderActivity implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 5373998789656724766L;
        public String name;
        public int giftNum;
        public String amount;
    }

    public static class PayInfoResult implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 3042154847512370057L;

        public String orderId;
        public double orignAmount;// 原始交易金额
        public double discountAmount;// 折扣金额
        public double tradeAmount;// 实际交易金额
        public double activityAmount;// 活动折扣金额
        public double giftAmount;// 礼券折扣金额
        public double oldCusAmount;// 老用户优惠
        public double otherPay;// 特殊优惠\额外支出（需要判断正负来决定显示内容）
        public double channelDiscount;// 渠道优惠
        public int reportNum;// 报告数
        public int modeType;// 0正常1打赏
        public int payType;// 支付类型0现金1微信支付
        public List< OrderActivity > octList;// 适用活动列表
        public List< Gift > glList;// 适用礼券列表

    }

    public static class RequestObj implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 2765872775406192043L;

        public String orderId;

        public RequestObj()
        {
        }

        public RequestObj(String orderId)
        {
            super();
            this.orderId = orderId;
        }
    }

}

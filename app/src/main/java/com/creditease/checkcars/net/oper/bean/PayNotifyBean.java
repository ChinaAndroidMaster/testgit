package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

/**
 * @author 子龍
 * @function
 * @date 2015年4月24日
 * @company CREDITEASE
 */
public class PayNotifyBean
{
    /**
     * 1交易创建
     */
    public static final int STATUS_PAY_CREATED = 0;
    /**
     * 2交易成功
     */
    public static final int STATUS_PAY_SUCCESS = 1;
    /**
     * 3交易失败
     */
    public static final int STATUS_PAY_FAILED = 2;

    public static class PayNotifyObj implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 4199507292902363948L;

        public int status;
        public String tradeId;
        public String sellerId;
        public String tradeAmount;
        public int platform;
        public int orderType;

        public PayNotifyObj()
        {
        }

        /**
         * @param status       : 支付状态 PayNotifyBean.STATUS_PAY_*
         * @param tradeNumber  :订单的tradeNumber
         * @param sellerId     :支付对象的账号
         * @param tradeAmount: 支付金额
         * @param platform:    支付平台: 0 支付宝
         */
        public PayNotifyObj(int status, String tradeId, String sellerId, String tradeAmount,
                            int platform, int orderType)
        {
            super();
            this.status = status;
            this.tradeId = tradeId;
            this.sellerId = sellerId;
            this.tradeAmount = tradeAmount;
            this.platform = platform;
            this.orderType = orderType;
        }
    }

}

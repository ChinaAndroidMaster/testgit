package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class OrderPayFinishBean
{

    public static class RequestObj implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 2765872775406192043L;

        public String orderId;

        public int payType;

        public RequestObj()
        {
        }

        public RequestObj(String orderId, int payType)
        {
            super();
            this.orderId = orderId;
            this.payType = payType;
        }
    }


}

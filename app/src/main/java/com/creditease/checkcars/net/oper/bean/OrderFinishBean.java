package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.bean.UserCore;

public class OrderFinishBean
{

    public static class OperResult implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 3042154847512370057L;
        public UserCore UserCore;
        public Appraiser Appraiser;
    }

    public static class RequestObj implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 2765872775406192043L;

        public String uuid;

        public int status;

        public String orderEndTime;

        public RequestObj()
        {
        }

        public RequestObj(String orderId, int status, String orderEndTime)
        {
            super();
            uuid = orderId;
            this.status = status;
            this.orderEndTime = orderEndTime;
        }
    }
}

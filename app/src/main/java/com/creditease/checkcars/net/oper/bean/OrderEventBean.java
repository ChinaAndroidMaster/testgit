package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class OrderEventBean
{

    public static class OrderEventObj implements Serializable
    {


        /**
         *
         */
        private static final long serialVersionUID = -8746381883791002888L;


        public String told;
        public int type;
        public int status;
        public long modifyTime;

        public OrderEventObj()
        {

        }

        public OrderEventObj(String told, int type, int status, long modifyTime)
        {
            super();
            this.told = told;
            this.type = type;
            this.status = status;
            this.modifyTime = modifyTime;
        }
    }

}

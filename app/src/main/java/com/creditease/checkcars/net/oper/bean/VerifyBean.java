package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

/**
 * @author 子龍
 * @function
 * @date 2015年4月24日
 * @company CREDITEASE
 */
public class VerifyBean
{

    public static class ResultObj implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 2818387169171004715L;
        public String code;

        public ResultObj()
        {
        }
    }

    public static class VerifyObj implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 4199507292902363948L;

        public String phoneNumber;// 电话号码

        public int forType;

        public VerifyObj()
        {
        }

        public VerifyObj(String phoneNumber, int forType)
        {
            super();
            this.phoneNumber = phoneNumber;
            this.forType = forType;
        }
    }

}

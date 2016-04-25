package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class CarPreReportBean
{
    public static class CarPreRepostObj implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = -3150955905977087929L;

        public String appraiserId;


        public CarPreRepostObj()
        {
        }

        public CarPreRepostObj(String appraiserId)
        {
            super();
            this.appraiserId = appraiserId;
        }
    }
}

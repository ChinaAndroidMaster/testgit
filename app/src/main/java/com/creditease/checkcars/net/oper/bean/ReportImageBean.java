package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class ReportImageBean
{
    public static final class RequestObj implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = -4029742059692653775L;

        public String clientUuid;

        public int imgType;

        public String interfaceVersion = "2";

        public RequestObj()
        {
        }

        public RequestObj(String uuid, int imgType)
        {
            super();
            clientUuid = uuid;
            this.imgType = imgType;
        }
    }
}

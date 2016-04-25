package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class ReportAddBean implements Serializable
{
    public static final class RequestObj
    {
        public String uuid;

        public String clientUuid;

        public String vin;

        public String interfaceVersion = "2";

        public RequestObj()
        {
        }

        public RequestObj(String uuid, String clientUuid, String vin)
        {
            super();
            this.uuid = uuid;
            this.clientUuid = clientUuid;
            this.vin = vin;
        }
    }

    /**
     *
     */
    private static final long serialVersionUID = -6056680450294061463L;
}

package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class ReportAttrImageDeleteBean
{
    public static final class RequestObj implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = -4029742059692653775L;

        public String clientUuid;

        public String attributeId;

        public RequestObj()
        {
        }

        public RequestObj(String clientUuid, String attributeId)
        {
            super();
            this.clientUuid = clientUuid;
            this.attributeId = attributeId;
        }
    }
}

package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class ReportGetBean implements Serializable
{
    public static final class RequestObj
    {
        public String uuid;

        public int toType;

        public RequestObj()
        {
        }

        public RequestObj(String uuid, int toType)
        {
            super();
            this.uuid = uuid;
            this.toType = toType;
        }
    }

    /**
     *
     */
    private static final long serialVersionUID = -6056680450294061463L;
    /**
     * 0表示师傅端查看报告
     */
    public static final int TYPE_APPRAISER = 0;

    /**
     * 1表示客户查看报告
     */
    public static final int TYPE_CUSTOMER = 1;
}

package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.creditease.checkcars.data.bean.CarOrder;

public class CarOrderBean
{
    public static class OrdersResult implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 2111160544473146047L;

        public ArrayList< CarOrder > dataList;
    }

    public static class RequestObj implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 1888200356901349285L;

        public String appraiserId;

        public int status;

        public int curPage;
        public int pageSize;
        public long modifyTime;
        public int type = 0;

        public RequestObj()
        {
        }

        public RequestObj(long modifyTime, String appraiserId, int status, int curPage, int pageSize)
        {
            super();
            this.appraiserId = appraiserId;
            this.status = status;
            this.curPage = curPage;
            this.pageSize = pageSize;
            this.modifyTime = modifyTime;
        }
    }

    public static class RequestObj2 implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = -6140670627247754036L;

        public String appraiserId;

        public int curPage;

        public int pageSize;
        public long modifyTime;
        public int type = 0;

        public RequestObj2()
        {
        }

        public RequestObj2(long modifyTime, String appraiserId, int curPage, int pageSize)
        {
            super();
            this.appraiserId = appraiserId;
            this.curPage = curPage;
            this.pageSize = pageSize;
            this.modifyTime = modifyTime;
        }
    }
}

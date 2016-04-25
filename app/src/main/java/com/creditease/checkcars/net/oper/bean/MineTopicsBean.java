package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.creditease.checkcars.data.bean.MineTopicsCore;

public class MineTopicsBean
{
    public static class MineTopicObj implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 7680463957631214225L;

        public int curPage;

        public int pageSize;

        public String toId;
        public long modifyTime;
        public int operType;

        public MineTopicObj()
        {

        }

        public MineTopicObj(int curPage, int pageSize, String uuid, long modifyTime, int operType)
        {
            super();
            this.curPage = curPage;
            this.pageSize = pageSize;
            toId = uuid;
            this.modifyTime = modifyTime;
            this.operType = operType;
        }
    }


    public static class MineTopicResult implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = -4031824233733395192L;

        public ArrayList< MineTopicsCore > dataList;
    }
}

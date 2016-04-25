package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class MineHeadPicBean
{
    public static class MineHeadObj implements Serializable
    {


        /**
         *
         */
        private static final long serialVersionUID = 8442843579054547167L;

        public String uuid;

        public MineHeadObj()
        {

        }

        public MineHeadObj(String coreId)
        {
            super();
            uuid = coreId;
        }
    }


    public static class MineHeadResult implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 4474831187442596234L;

    }
}

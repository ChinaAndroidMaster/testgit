package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

import com.creditease.checkcars.data.bean.Appraiser;

public class UserDataBean
{

    public static class UserDataObj implements Serializable
    {


        /**
         *
         */
        private static final long serialVersionUID = 5517170003958884193L;


        public UserDataObj()
        {
        }

        public UserDataObj(String uuid, int isProvideService, String declaration)
        {
            super();
            this.uuid = uuid;
            this.isProvideService = isProvideService;
            this.declaration = declaration;
        }

        public String uuid;
        public int isProvideService;
        public String declaration;
    }

    public static class UserDataResult implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = -821461850977177462L;
        public Appraiser Appraiser;
    }
}

package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.bean.Saler;
import com.creditease.checkcars.data.bean.UserCore;

public class LoginBean
{

    public static class LoginObj implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 4199507292902363948L;

        public String phoneNumber;

        public String password;

        public LoginObj()
        {
        }

        public LoginObj(String phoneNumber, String password)
        {
            super();
            this.phoneNumber = phoneNumber;
            this.password = password;
        }
    }

    public static class LoginResult implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 3042154847512370057L;
        public UserCore UserCore;
        public Appraiser Appraiser;
        public Saler Saler;

        public LoginResult()
        {
        }
    }
}

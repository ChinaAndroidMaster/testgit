package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class FeedbackBean
{

    public static class RequestObj implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = -3150955905977087929L;

        public String toId;

        public String content;

        public RequestObj()
        {
        }

        public RequestObj(String uuid, String content)
        {
            super();
            toId = uuid;
            this.content = content;
        }
    }

}

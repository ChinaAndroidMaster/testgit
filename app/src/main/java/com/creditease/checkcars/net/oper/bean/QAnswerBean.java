package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

public class QAnswerBean
{
    public static class QAnswerObj implements Serializable
    {


        /**
         *
         */
        private static final long serialVersionUID = -5430101724154616038L;

        public String questionId;

        public String fromId;

        public int position;
        public String content;

        public QAnswerObj()
        {
        }

        public QAnswerObj(String questionId, String fromId, int position, String content)
        {
            super();
            this.questionId = questionId;
            this.fromId = fromId;
            this.position = position;
            this.content = content;
        }
    }
}

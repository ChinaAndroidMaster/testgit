package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.creditease.checkcars.data.bean.QuestionAnswer;

public class QuestionAnswerBean
{

    public static class QuestionAnswerObj implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 1294642153654189578L;

        public int examine;

        public String modifyTime;
        public int curPage;
        public int pageSize;

        public QuestionAnswerObj(int examine, String modifyTime, int curPage, int pageSize)
        {
            this.examine = examine;
            this.modifyTime = modifyTime;
            this.curPage = curPage;
            this.pageSize = pageSize;
        }

    }

    public static class QuestionAnswerResult implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 8767933231723585979L;

        public ArrayList< QuestionAnswer > dataList;
    }

}

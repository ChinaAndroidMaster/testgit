package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

/**
 * @author 子龍
 * @function
 * @date 2015年4月24日
 * @company CREDITEASE
 */
public class VersionBean
{

    public static class VersionObj implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 4199507292902363948L;

        public int platform = 0;// 0 Android ;1 ios; 2 wp

        public String appName;// 应用名称

        public String versionName;// 版本号
        public int status = 1;// 上线

        public VersionObj()
        {
        }

        public VersionObj(String appName, String versionName)
        {
            super();
            this.appName = appName;
            this.versionName = versionName;
        }
    }

}

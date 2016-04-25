package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

import com.creditease.checkcars.data.bean.UserPositionInfo;

public class LocationBean
{
    public static class LocationObj implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = 4165414805179756944L;

        public String toId;

        public int toType;

        public int fromType;

        public String fromId;

        public String latitude;

        public String longitude;

        public String addressName;

        public String recordTime;

        public LocationObj()
        {
        }

        public LocationObj(UserPositionInfo info)
        {
            super();
            this.toId = info.toId;
            this.toType = info.toType;
            this.fromType = info.fromType;
            this.fromId = info.fromId;
            this.latitude = info.latitude;
            this.longitude = info.longitude;
            this.addressName = info.addressName;
            this.recordTime = info.recordTime;
        }
    }
}

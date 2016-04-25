package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

import com.creditease.checkcars.data.bean.Appraiser;

public class UserBasicDataBean
{
    public static class BasicDataObj implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = -775510741173593688L;

        public int id;

        public String uuid;

        public String coreId;
        public String trueName;
        public String idCard;
        public int level;
        public String remark;
        public String maxPrice;
        public String minPrice;
        public String phoneNumber;
        public int score;
        public int appraiseNum;
        public String headPic;
        public String cityId;
        public String provinceId;
        public String declaration;
        public String cityName;// 服务城市
        public String provinceName;// 服务省名字
        public int jobType;// 师傅类型 0 全职 1 兼职
        public String orderCount;// 总接单数
        // 部门信息
        public String departmentName;
        public String departmentId;
        // 工作状态
        public int status;// 状态 0 在职 1 离职 2 测试
        public int isProvideService;// 是否提供服务
        public String goodRate;// 好评率
        public int incomeOrder;// 收入排名
        public int finishNum;// 接单数
        public int finishOrder;// 接单数排名
        public String worksImgs;// 工作照
        public String incomeAmount;// 收入金额
        public String price;// 提成单价
        public int priceType;// 计算类型: 0 元/单 1 元/单

        public BasicDataObj()
        {

        }

        public BasicDataObj(Appraiser appraiser)
        {
            super();
            id = appraiser.id;
            uuid = appraiser.uuid;
            coreId = appraiser.coreId;
            trueName = appraiser.trueName;
            idCard = appraiser.idCard;
            level = appraiser.level;
            remark = appraiser.remark;
            minPrice = appraiser.minPrice;
            maxPrice = appraiser.maxPrice;
            phoneNumber = appraiser.phoneNumber;
            score = appraiser.score;
            headPic = appraiser.headPic;
            cityId = appraiser.cityId;
            provinceId = appraiser.provinceId;
            declaration = appraiser.declaration;
            jobType = appraiser.jobType;
            orderCount = appraiser.orderCount;
            departmentName = appraiser.departmentName;
            departmentId = appraiser.departmentId;
            status = appraiser.status;
            isProvideService = appraiser.isProvideService;
            goodRate = appraiser.goodRate;
            incomeOrder = appraiser.incomeOrder;
            finishNum = appraiser.finishNum;
            finishOrder = appraiser.finishOrder;
            worksImgs = appraiser.worksImgs;
            incomeAmount = appraiser.incomeAmount;
            price = appraiser.price;
            priceType = appraiser.priceType;
        }
    }

    public static class BasicDataResult implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = -1458318905333805071L;

        public BasicDataResult()
        {

        }

    }
}

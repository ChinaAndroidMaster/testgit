package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.text.TextUtils;

import com.creditease.checkcars.data.bean.CCClassify;
import com.creditease.checkcars.data.bean.CCItem;
import com.creditease.checkcars.data.bean.Car;
import com.creditease.checkcars.data.bean.CarReport;

public class ReportCommitBean
{
    public static final class Item implements Serializable
    {

        /**
         *
         */
        private static final long serialVersionUID = 7231771083678885277L;

        public String attributeId;

        public String attrValue;

        public String remark;
        public int answerType;
        public int answerRequired;
        public int isFill;

        public Item(String attributeId, String attrValue, String remark)
        {
            super();
            this.attributeId = attributeId;
            this.attrValue = attrValue;
            this.remark = remark;
        }

        public Item(String attributeId, String attrValue, String remark, int answerType,
                    int answerRequired, int isFill)
        {
            super();
            this.attributeId = attributeId;
            this.attrValue = attrValue;
            this.remark = remark;
            this.answerType = answerType;
            this.answerRequired = answerRequired;
            this.isFill = isFill;
        }
    }

    public static final class RequestObj implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = -8066403863265931566L;

        public String interfaceVersion = "2";

        public String clientUuid;

        public String remark;
        public String amount;
        public long startTime;
        public long endTime;
        public int level;
        public Car reportCar;
        public int isFill;
        public ArrayList< Item > allAtts = new ArrayList< Item >();

        public RequestObj()
        {
        }

        public RequestObj(CarReport report)
        {
            super();
            startTime = report.startTime;
            if ( report.endTime <= report.startTime )
            {
                // 时间异常
                endTime = System.currentTimeMillis();
            } else
            {
                endTime = report.endTime;
            }
            clientUuid = report.clientUuid;
            remark = report.remark;
            reportCar = report.reportCar;
            level = report.level;
            amount = report.amount;
            for ( CCClassify classify : report.cRoot.children )
            {
                if ( classify.attrs == null )
                {
                    continue;
                }
                if ( classify.mainItem != null )
                {
                    allAtts.add(new Item(((classify.mainItem.attributeId == null) || TextUtils
                            .isEmpty(classify.mainItem.attributeId)) ? classify.mainItem.uuid
                            : classify.mainItem.attributeId, classify.mainItem.attrValue,
                            classify.mainItem.remark, classify.mainItem.answerType,
                            classify.mainItem.answerRequired, classify.mainItem.isFill));
                }
                for ( CCItem i : classify.attrs )
                {
                    allAtts.add(new Item(
                            ((i.attributeId == null) || TextUtils.isEmpty(i.attributeId)) ? i.uuid
                                    : i.attributeId, i.attrValue, i.remark, i.answerType, i.answerRequired, i.isFill));
                }
            }
        }
    }
}

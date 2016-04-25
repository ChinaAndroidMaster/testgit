package com.creditease.checkcars.data.bean;

import com.creditease.utilframe.db.annotation.Id;
import com.creditease.utilframe.db.annotation.Table;

@Table( name = "order_event" )
public class OrderEvent
{
    @Id
    public int id;
    public String appraiserId;//师傅id
    public String toId;//报告者uuid
    /**
     * 各个事件的类型
     * 12  电话呼出时间  订单类别即type= 0 status 12
     * 7  开始检测时间     订单类别即type= 2 status 2
     * 1  提交报告时间      报告类别即type= 2 status 1
     * 6  重新修改报告时间  报告类别即type= 2 status 3
     */
    public int type;
    public int status;
    public long modifyTime;//具体时间
}

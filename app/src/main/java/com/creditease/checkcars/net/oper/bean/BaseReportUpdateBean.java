package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

/**
 * @author 子龍
 * @function
 * @date 2015年4月24日
 * @company CREDITEASE
 */
public class BaseReportUpdateBean implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 3473090797671528319L;

    public String updateTime;// 更新时间

    public BaseReportUpdateBean()
    {
    }

    public BaseReportUpdateBean(String updateTime)
    {
        super();
        this.updateTime = updateTime;
    }

}

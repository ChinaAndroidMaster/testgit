package com.creditease.checkcars.net.oper.bean;

import java.io.Serializable;

/**
 * @author 子龍
 * @function
 * @date 2015年4月24日
 * @company CREDITEASE
 */
public class PwdResetBean implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 7303116605224837197L;

    public String phoneNumber;// 电话号码

    public String newPassword;

    public PwdResetBean()
    {
    }

    public PwdResetBean(String phoneNumber, String newPassword)
    {
        super();
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
    }

}

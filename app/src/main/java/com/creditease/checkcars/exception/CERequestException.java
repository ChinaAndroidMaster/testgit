/**
 * Copyright © 2013 金焰科技. All rights reserved.
 */
package com.creditease.checkcars.exception;

/**
 * 网络请求失败异常
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public class CERequestException extends Exception
{

    /**
     *
     */
    private static final long serialVersionUID = -9081679616846813004L;

    public CERequestException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public CERequestException(String detailMsg)
    {
        super(detailMsg);

    }

    public CERequestException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public CERequestException(Throwable throwable)
    {
        super(throwable);
        // TODO Auto-generated constructor stub
    }

}

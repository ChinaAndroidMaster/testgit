/**
 * Copyright © 2013 金焰科技. All rights reserved.
 */
package com.creditease.checkcars.exception;

import com.creditease.checkcars.net.oper.base.OperResponse;

/**
 * 业务请求失败异常
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public class CEFailtureException extends Exception
{

    /**
     *
     */
    private static final long serialVersionUID = -9081679616846813004L;

    public OperResponse response;

    public CEFailtureException(OperResponse response)
    {
        super();
        this.response = response;

    }

    public CEFailtureException(String detailMsg, OperResponse response)
    {
        super(detailMsg);
        this.response = response;

    }

    public CEFailtureException(String detailMessage, Throwable throwable, OperResponse response)
    {
        super(detailMessage, throwable);
        this.response = response;
    }

    public CEFailtureException(Throwable throwable, OperResponse response)
    {
        super(throwable);
        this.response = response;
    }

}

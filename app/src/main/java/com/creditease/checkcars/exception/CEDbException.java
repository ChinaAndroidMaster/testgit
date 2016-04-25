package com.creditease.checkcars.exception;

/**
 * 数据异常
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public class CEDbException extends Exception
{
    private static final long serialVersionUID = 1L;

    public CEDbException()
    {
    }

    public CEDbException(String detailMessage)
    {
        super(detailMessage);
    }

    public CEDbException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    public CEDbException(Throwable throwable)
    {
        super(throwable);
    }

}

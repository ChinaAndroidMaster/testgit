package com.creditease.checkcars.exception;

/**
 * Http请求异常
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public class CEHttpException extends Exception
{
    private static final long serialVersionUID = 1L;

    public CEHttpException()
    {
    }

    public CEHttpException(String detailMessage)
    {
        super(detailMessage);
    }

    public CEHttpException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    public CEHttpException(Throwable throwable)
    {
        super(throwable);
    }
}

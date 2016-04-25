package com.creditease.checkcars.alipay;

/**
 * alipay异常
 *
 * @author 子龍
 * @function
 * @date 2015年11月17日
 * @company CREDITEASE
 */
public class AlipayException extends Exception
{

    /**
     *
     */
    private static final long serialVersionUID = -9104792150724604994L;

    public AlipayException()
    {
        super();
    }

    public AlipayException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    public AlipayException(String detailMessage)
    {
        super(detailMessage);
    }

    public AlipayException(Throwable throwable)
    {
        super(throwable);
    }

}

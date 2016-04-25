package com.creditease.checkcars.msgpush;

public class CEMsgPushExpection extends Exception
{

    /**
     *
     */
    private static final long serialVersionUID = -6382291936055759799L;

    public int id;

    public CEMsgPushExpection()
    {
        super();
    }

    public CEMsgPushExpection(String detailMessage)
    {
        super(detailMessage);
    }

    public CEMsgPushExpection(String detailMessage, int id)
    {
        super(detailMessage);
        this.id = id;
    }

    public CEMsgPushExpection(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    public CEMsgPushExpection(Throwable throwable)
    {
        super(throwable);
    }

}

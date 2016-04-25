package com.creditease.checkcars.alipay;

public interface IPayCallBack
{

    public void paySuccess(String result);

    public void payFailed(String resultStatus, String result);

}

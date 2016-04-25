package com.creditease.checkcars.alipay;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.creditease.checkcars.ui.act.base.ZLToast;

/**
 * 支付结果消息处理
 *
 * @author 子龍
 * @function
 * @date 2015年11月17日
 * @company CREDITEASE
 */
public class AlipayMessageHandler extends Handler
{

    private Context mContext;
    private static AlipayMessageHandler mHandler;
    private IPayCallBack payCallback;

    /**
     * this method must be called in main thread
     *
     * @param mContext 下午8:28:46
     */
    public static AlipayMessageHandler buildHandler(Context mContext)
    {
        if ( mHandler == null )
        {
            mHandler = new AlipayMessageHandler(mContext);
        }
        return mHandler;
    }

    public static AlipayMessageHandler getAlipayHandler() throws AlipayException
    {
        if ( mHandler == null )
        {
            throw new AlipayException("have not create AlipayMessageHandler object");
        }
        return mHandler;
    }

    /**
     * 设置支付回调监听
     *
     * @param payCallback 下午8:40:19
     */
    public void setPayCallback(IPayCallBack payCallback)
    {
        this.payCallback = payCallback;
    }

    /**
     * 设置Context
     *
     * @param mContext 下午8:24:57
     */
    public void setContext(Context mContext)
    {
        this.mContext = mContext;
    }

    private AlipayMessageHandler(Context mContext)
    {
        this.mContext = mContext;
    }

    /**
     * 支付flag
     */
    public static final int SDK_PAY_FLAG = 1;


    @Override
    public void handleMessage(Message msg)
    {
        switch ( msg.what )
        {
            case SDK_PAY_FLAG:
            {
                PayResult payResult = new PayResult(( String ) msg.obj);

                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                String resultInfo = payResult.getResult();
                // TODO
                String resultStatus = payResult.getResultStatus();

                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if ( TextUtils.equals(resultStatus, "9000") )
                {
                    ZLToast.getToast().showToast(mContext, "支付成功");
                    if ( payCallback != null )
                    {
                        payCallback.paySuccess(resultInfo);
                    }
                } else
                {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if ( TextUtils.equals(resultStatus, "8000") )
                    {
                        ZLToast.getToast().showToast(mContext, "支付结果确认中");
                    } else
                    {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        ZLToast.getToast().showToast(mContext, "支付失败");
                    }
                    if ( payCallback != null )
                    {
                        payCallback.payFailed(resultStatus, payResult.getMemo());
                    }
                }
                break;
            }

            default:
                break;
        }
        super.handleMessage(msg);
    }

}

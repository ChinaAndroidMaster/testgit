package com.creditease.checkcars.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import android.app.Activity;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.creditease.checkcars.data.bean.PayBean;

/**
 * @author 子龍
 * @function
 * @date 2015年11月17日
 * @company CREDITEASE
 */
public class AlipayUtil
{
    private static AlipayUtil util;
    private AlipayMessageHandler handler;

    private AlipayUtil()
    {
        try
        {
            handler = AlipayMessageHandler.getAlipayHandler();
        } catch ( AlipayException e )
        {
        }
    }

    public static AlipayUtil getPayUtil()
    {
        if ( util == null )
        {
            util = new AlipayUtil();
        }
        return util;
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     * @param activity
     * @param bean     下午8:53:58
     */
    public void pay(final Activity activity, PayBean bean, IPayCallBack payCallback)
    {
        if ( handler == null )
        {
            try
            {
                handler = AlipayMessageHandler.getAlipayHandler();
            } catch ( AlipayException e )
            {
                handler = AlipayMessageHandler.buildHandler(activity);
            }
        }
        handler.setPayCallback(payCallback);
        Random r = new Random();
        String orderNumber = bean.orderNmber + r.nextInt(10000);
        // 订单
        String orderInfo =
                getOrderInfo(activity, bean.subjectName, bean.subjectCont, bean.subjectPrice, orderNumber,
                        bean.payerPhone);
        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try
        {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch ( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable()
        {

            @Override
            public void run()
            {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                Message msg = handler.obtainMessage();
                msg.what = AlipayMessageHandler.SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     * @param context
     * @return 下午8:20:08
     */
    public String getSDKVersion(Activity context)
    {
        PayTask payTask = new PayTask(context);
        return payTask.getVersion();
    }

    /**
     * create the order info. 创建订单信息
     *
     * @param subject     商品名称
     * @param body        商品描述
     * @param price       价格
     * @param orderNumber 订单编号
     * @param payPhone    转账人电话
     * @return 下午8:16:19
     */
    public String getOrderInfo(Activity act, String subject, String body, String price,
                               String orderNumber, String payPhone)
    {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + AlipayConfig.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + AlipayConfig.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderNumber + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + AlipayConfig.NOTIFY_URL + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"" + AlipayConfig.RETURN_URL + "\"";
        // orderInfo += "&return_url=\"www.sfkc.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        // // 平台注册的客户端app id 用于当面支付&扫码支付
        // orderInfo += "&app_id=\"" + AlipayConfig.APP_ID + "\"";
        // // 客户端来源 用于当面支付&扫码支付
        // orderInfo +=
        // "appenv=\"system=" + AlipayConfig.SYSTEM + "^version=" + Util.getAppVersionName(act) + "\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content)
    {
        return SignUtils.sign(content, AlipayConfig.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType()
    {
        return "sign_type=\"RSA\"";
    }

}

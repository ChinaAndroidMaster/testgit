package com.creditease.checkcars.alipay;

import com.creditease.checkcars.config.Config;

/**
 * 支付宝支付参数
 *
 * @author 子龍
 * @function
 * @date 2015年11月17日
 * @company CREDITEASE
 */
public class AlipayConfig
{
    // 商户PID
    public static final String PARTNER = "2088121007799794";// 2088411003720176
    // 商户收款账号
    public static final String SELLER  = "yixinhuimin02@163.com";
    // public static final String SELLER = "Xiaoweimvcmvc@126.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANP9plVq93rJgUQgIE1bWQrIkwCVuuAGTZua8GtItsDPa8X0TjkCew+IgPvFlWLwHpw2egZwgMwFFsctfA9IlpaSxiXR9HxVbf2/Y2Wuimd36MC2vQqpLFhBsIgvhaqTrIVSs9/3oznJLy1QX8EJDUOW1P6FUryDRMQEk8d8y8c1AgMBAAECgYEAhqMHPCLWkNZf0fuxaFAG6lFfA7UU3elC6vvpi8m/wA88fJngAFcx/ziL6tufOBoW5tG2iFZ1hp4IzHF35GEOueTxLfuZoGI5wVPe1WxTBbT4fGN621OQayaGhfRuB1/ElQcim060rNIGBXLG86RDgTRgUc02MjpVWAyb0/rHLMECQQDvjeI2rj/+KRayPVk1MXl4rwY4s5B+LrYi0/1E9csM0v1pPvTFMReXhhc8aHubWshfca95J7RnP1/OE3hiw8AlAkEA4otYQtbpqU8gmORUVCWRLj6Vyvcc+Upi7mRPjwAbkZONaYRzuQSHhkEzysLGSpPGgTPNOHz70U7eVI2T+g910QJBAMQtZ4HsNU/JCmMxRmr/d0DT5L5unvgW/OWMwa7Fs2FvMEQOYJlUWMOn9kNsNT3GnK09BryrPVwIVEcJPt9y+1kCQBbJbVdLxrUBAIZgcRsFrFS6dtazhHaOxOS1skrv99XswYybhghWLEuUw2DhtpBJzO2yCDJmX+3twO9ATmIYrAECQCQCkVzJrGAAhdAYqpXv700pQe7Whk9KI4Ltii3Yv62MZfh0g9EpXRWrIJF4UOmQENAv4oCabKnoqLcpVUkN0cE=";
    // 支付宝公钥
    public static final String RSA_PUBLIC =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    // 支付服务器回调地址
    public static final String NOTIFY_URL = Config.ROOT_URL + "Common/checkNotifyData";
    /**
     * 支付宝处理完请求后，当前页面跳转到商户指定页面的路径
     */
    public static final String RETURN_URL = "m.alipay.com";
    /**
     * 客户端的appid
     */
    public static final String APP_ID = "2015102700559214";
    /**
     * 客户端平台
     */
    public static final String SYSTEM = "android";
}

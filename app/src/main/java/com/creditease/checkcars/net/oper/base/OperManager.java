/**
 * Copyright © 2014 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper.base;

import java.io.File;

import com.creditease.checkcars.exception.CEHttpException;
import com.creditease.utilframe.HttpUtils;
import com.creditease.utilframe.exception.HttpException;
import com.creditease.utilframe.http.RequestParams;
import com.creditease.utilframe.http.callback.RequestCallBack;
import com.creditease.utilframe.http.client.HttpRequest;

/**
 * @author 子龍
 * @date 2014年10月22日
 * @company 宜信
 */
public class OperManager
{

    public static enum HttpMethod
    {
        GET("GET"), POST("POST"), PUT("PUT"), HEAD("HEAD"), MOVE("MOVE"), COPY("COPY"), DELETE("DELETE"), OPTIONS(
            "OPTIONS"), TRACE("TRACE"), CONNECT("CONNECT");

        private final String value;

        HttpMethod(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return value;
        }
    }

    public static class RequestParam extends RequestParams
    {

        public RequestParam()
        {
            super();
        }

        public RequestParam(String charset)
        {
            super(charset);
        }

    }

    private static OperManager manager;

    private static HttpUtils httpUtils = new HttpUtils();

    public static OperManager getManager()
    {
        if ( manager == null )
        {
            manager = new OperManager();
        }
        return manager;
    }

    private OperManager()
    {
    }

    private HttpRequest.HttpMethod buildMethod(HttpMethod httpMethod)
    {
        switch ( httpMethod )
        {
            case GET:
                return HttpRequest.HttpMethod.GET;
            case POST:
                return HttpRequest.HttpMethod.POST;
            case PUT:
                return HttpRequest.HttpMethod.PUT;
            case HEAD:
                return HttpRequest.HttpMethod.HEAD;
            case MOVE:
                return HttpRequest.HttpMethod.MOVE;
            case COPY:
                return HttpRequest.HttpMethod.COPY;
            case DELETE:
                return HttpRequest.HttpMethod.DELETE;
            case OPTIONS:
                return HttpRequest.HttpMethod.OPTIONS;
            case TRACE:
                return HttpRequest.HttpMethod.TRACE;
            case CONNECT:
                return HttpRequest.HttpMethod.CONNECT;
            default:
                return HttpRequest.HttpMethod.POST;
        }
    }

    /**
     * 下载文件
     *
     * @param url      下载路径
     * @param target   存储目标路径
     * @param callback 回调
     */
    public void download(String url, String target, RequestCallBack< File > callback)
    {
        httpUtils.download(url, target, callback);
    }

    /**
     * @param url      下载路径
     * @param target   存储目标路径
     * @param params
     * @param callback
     */
    public void download(String url, String target, RequestParams params,
                         RequestCallBack< File > callback)
    {
        httpUtils.download(url, target, callback);
    }

    /**
     * 发送请求
     *
     * @param method
     * @param url
     * @param callBack
     */
    public < T > void sendRequest(HttpMethod method, String url, RequestCallBack< T > callBack)
    {
        httpUtils.send(buildMethod(method), url, callBack);
    }

    /**
     * 发送请求
     *
     * @param method
     * @param url
     * @param params
     * @param callBack
     */
    public < T > void sendRequest(HttpMethod method, String url, RequestParam params,
                                  RequestCallBack< T > callBack)
    {
        httpUtils.send(buildMethod(method), url, params, callBack);
    }

    /**
     * 发送请求
     *
     * @param httpMethod
     * @param url
     * @param params
     * @throws CEHttpException
     */
    public void sendRequestSync(HttpMethod httpMethod, String url, RequestParam params)
            throws CEHttpException
    {
        try
        {
            httpUtils.sendSync(buildMethod(httpMethod), url, params);
        } catch ( HttpException e )
        {
            throw new CEHttpException(e == null ? "HttpException" : e.getMessage());
        } catch ( Exception e )
        {
            throw new CEHttpException(e == null ? "HttpException" : e.getMessage());
        }
    }
}

/**
 * Copyright © 2014 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper.base;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.oper.base.OperManager.HttpMethod;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.utilframe.exception.HttpException;
import com.creditease.utilframe.http.ResponseInfo;
import com.creditease.utilframe.http.callback.RequestCallBack;

/**
 * @param <T>
 * @author 子龍
 * @date 2014年10月23日
 * @company 宜信
 */
public abstract class Oper< T > implements IDataParse, Runnable
{

    @SuppressWarnings( "hiding" )
    public class CERequestCallBack< T > extends RequestCallBack< T >
    {

        @Override
        public void onFailure(HttpException error, String msg)
        {
            if ( listener != null )
            {
                listener.onRequestError(error, msg);
            }
        }

        @Override
        public void onSuccess(ResponseInfo< T > responseInfo)
        {
            response = responseInfo;
            new Thread(Oper.this).start();
        }
    }

    /**
     * @author 子龍
     * @date 2014年10月23日
     * @company 宜信
     */
    public interface RequestListener
    {
        /**
         * 下行数据异常
         *
         * @param errorMsg
         * @param result
         */
        public void onDataError(String errorMsg, String result);

        /**
         * 业务操作失败
         *
         * @param response 下午6:23:53
         */
        public void onFailure(OperResponse response);

        /**
         * 链接失败
         *
         * @param error
         * @param msg
         */
        public void onRequestError(HttpException error, String msg);

        /**
         * 业务操作成功
         *
         * @param content
         */
        public void onSuccess(Bundle bundle);
    }

    private OperManager manager = OperManager.getManager();
    private RequestParam param;
    private RequestListener listener;
    private CERequestCallBack< T > callback = new CERequestCallBack< T >();
    protected Context mContext;
    protected final String venderId;
    public static final String BUNDLE_EXTRA_DATA = "_data";

    public static final String BUNDLE_EXTRA_RESPONSE = "_response";
    protected ThreadDBSaver saverThread = new ThreadDBSaver();

    private ResponseInfo< ? > response;

    protected OperHandler handle;

    public Oper(Context mContext, RequestListener listener)
    {
        super();
        this.mContext = mContext;
        this.listener = listener;
        venderId = SharePrefenceManager.getAppraiserId(mContext);
    }

    public Oper(Context mContext, RequestParam param, RequestListener listener)
    {
        super();
        this.mContext = mContext;
        this.param = param;
        this.listener = listener;
        venderId = SharePrefenceManager.getAppraiserId(mContext);
        handle = OperHandler.getHandel();
    }

    /**
     * 执行数据存储线程
     *
     * @param saver
     */
    public void doSaver(IDataSaver saver)
    {
        saverThread.excute(saver);
    }

    public void execute()
    {
        manager.sendRequest(HttpMethod.POST, getUrl(), param, callback);
    }

    public void execute(HttpMethod method)
    {
        manager.sendRequest(method, getUrl(), param, callback);
    }

    public RequestParam getParam()
    {
        return param;
    }

    /**
     * 接口连接地址
     *
     * @return
     */
    protected abstract String getUrl();

    @Override
    public void run()
    {
        if ( handle == null )
        {
            handle = OperHandler.getHandel();
        }
        String result = ( String ) response.result;
        result = DESEncrypt.decryption(result);
        final String re = result;
        try
        {
            final Bundle res = dataParseAndHandle(result);

            handle.post(new Runnable()
            {

                @Override
                public void run()
                {
                    if ( listener != null )
                    {
                        listener.onSuccess(res);
                    }
                }
            });

        } catch ( final CEFailtureException e )
        {
            handle.post(new Runnable()
            {

                @Override
                public void run()
                {
                    if ( listener != null )
                    {
                        listener.onFailure(e.response);
                    }
                }
            });

        } catch ( final CERequestException e )
        {
            handle.post(new Runnable()
            {

                @Override
                public void run()
                {
                    if ( listener != null )
                    {
                        listener.onDataError(e == null ? "CreditEaseRequestException" : e.getMessage(), re);
                    }
                }
            });

        }
    }

    /**
     * 设置上行参数
     *
     * @param param
     */
    public void setParam(RequestParam param)
    {
        this.param = param;
    }
}

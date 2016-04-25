package com.creditease.checkcars.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.creditease.checkcars.data.bean.OrderEvent;
import com.creditease.checkcars.data.db.OrderEventUtil;
import com.creditease.checkcars.data.db.base.DBAsyncTask;
import com.creditease.checkcars.data.db.base.DBFindCallBack;
import com.creditease.checkcars.data.db.base.DbHelper;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.base.Result;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.tools.NetWorkUtils.NetWorkState;
import com.creditease.utilframe.exception.HttpException;

public class CommitOrderEventUtils implements RequestListener, DBFindCallBack< OrderEvent >
{

    private OrderEvent mOrderEvent;
    private static CommitOrderEventUtils util;
    private NetWorkUtils netUtils;
    private Context context;
    private List< OrderEvent > events = new ArrayList< OrderEvent >();
    private List< OrderEvent > nativeEvents = new ArrayList< OrderEvent >();

    private CommitOrderEventUtils(Context context)
    {
        super();
        this.context = context;
        netUtils = new NetWorkUtils(context);
    }

    public static CommitOrderEventUtils getUtil(Context context)
    {
        if ( util == null )
        {
            util = new CommitOrderEventUtils(context);
        }
        return util;
    }

    public void recordTime(String toId, int type, int status, String clienUuid)
    {
        OrderEvent event = null;
        if ( type >= 0 && status >= 0 )
        {
            event = new OrderEvent();
            event.appraiserId = SharePrefenceManager.getAppraiserId(context);
            if ( TextUtils.isEmpty(clienUuid) )
            {
                event.toId = toId;
            } else
            {
                event.toId = clienUuid;
            }
            event.type = type;
            event.status = status;
            event.modifyTime = System.currentTimeMillis();
        }
        commitRecordTime(event);
    }

    private void commitRecordTime(OrderEvent event)
    {
        this.mOrderEvent = event;
        if ( netUtils != null && netUtils.getConnectState() == NetWorkState.NONE )
        {
            saveOrderEvent();
        } else
        {
            new DBAsyncTask< OrderEvent >(context, this).execute();
        }
    }

    private void saveOrderEvent()
    {
        try
        {
            if ( mOrderEvent != null )
            {
                OrderEventUtil.getUtil(context).saveOrUpdateOrderEvent(mOrderEvent);
            }
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
    }

    private void deleteOrderEvent()
    {
        try
        {
            OrderEventUtil.getUtil(context)
                    .deleteOrderEvents(SharePrefenceManager.getAppraiserId(context));
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
    }


    @Override
    public void dataCallBack(int operId, List< OrderEvent > result)
    {
        if ( nativeEvents != null && nativeEvents.size() > 0 )
        {
            nativeEvents.clear();
        }
        if ( result != null && result.size() > 0 )
        {
            if ( mOrderEvent != null )
            {
                result.add(mOrderEvent);
            }
            nativeEvents.addAll(result);
        } else
        {
            if ( mOrderEvent != null )
            {
                nativeEvents.add(mOrderEvent);
            }
        }
        if ( nativeEvents != null && nativeEvents.size() > 0 )
        {
            OperationFactManager.getManager().commitOrderEvent(context, nativeEvents, this);
        }
    }

    @Override
    public List< OrderEvent > doDBOperation(DbHelper helper, int operId) throws CEDbException
    {
        return OrderEventUtil.getUtil(context)
                .getOrderEventList(SharePrefenceManager.getAppraiserId(context));
    }

    @Override
    public void errorCallBack(int operId, CEDbException e)
    {
        if ( mOrderEvent != null )
        {
            events.clear();
            events.add(mOrderEvent);
            OperationFactManager.getManager().commitOrderEvent(context, events, this);
        }
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        saveOrderEvent();
    }

    @Override
    public void onFailure(OperResponse response)
    {
        saveOrderEvent();
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        saveOrderEvent();
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        OperResponse resp = bundle.getParcelable(Oper.BUNDLE_EXTRA_RESPONSE);
        // 如果上传失败则在本地保存，并在有网的时候上传,并删除本地数据
        if ( resp != null && resp.result != Result.RESULT_SUCCESS )
        {
            // 保存数据
            saveOrderEvent();
        } else
        {
            // 删除本地数据
            if ( nativeEvents != null && nativeEvents.size() > 0 )
            {
                new DBAsyncTask< OrderEvent >(context, new DBFindCallBack< OrderEvent >()
                {

                    @Override
                    public void dataCallBack(int operId, List< OrderEvent > result)
                    {

                    }

                    @Override
                    public List< OrderEvent > doDBOperation(DbHelper helper, int operId) throws CEDbException
                    {
                        deleteOrderEvent();
                        return null;
                    }

                    @Override
                    public void errorCallBack(int operId, CEDbException e)
                    {

                    }

                }).execute();
            }
        }
    }
}

package com.creditease.checkcars.data.db;

import java.util.List;

import android.content.Context;

import com.creditease.checkcars.data.bean.OrderEvent;
import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.data.db.base.DBWhereBuilder;
import com.creditease.checkcars.exception.CEDbException;

public class OrderEventUtil extends DBUtil< OrderEvent >
{

    private static OrderEventUtil util;

    protected OrderEventUtil(Context mContext)
    {
        super(mContext);
    }

    public static OrderEventUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new OrderEventUtil(context);
        }
        return util;
    }

    public List< OrderEvent > getOrderEventList(String appraiserId) throws CEDbException
    {
        return getObjListFromDB(DBSelector.from(OrderEvent.class).where("appraiserId", "=", appraiserId).orderBy("modifyTime", true));
    }

    public void saveOrUpdateOrderEvent(OrderEvent orderEvent) throws CEDbException
    {
        saveObjToDB(orderEvent);

    }

    public void deleteOrderEvents(String appraiserId) throws CEDbException
    {
        deleteObjFromDB(OrderEvent.class, DBWhereBuilder.b("appraiserId", "=", appraiserId));
    }

}

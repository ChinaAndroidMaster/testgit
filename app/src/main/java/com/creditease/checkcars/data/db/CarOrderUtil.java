package com.creditease.checkcars.data.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;

import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.bean.Customer;
import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.data.db.base.DBWhereBuilder;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * 检车数据库工具类
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public class CarOrderUtil extends DBUtil< CarOrder >
{

    private static CarOrderUtil util;

    public static CarOrderUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new CarOrderUtil(context);
        }
        return util;
    }

    protected CarOrderUtil(Context mContext)
    {
        super(mContext);
    }

    /**
     * 删除订单
     *
     * @param uuid
     * @throws CEDbException 上午11:27:41
     */
    public void deleteOrder(String uuid) throws CEDbException
    {
        deleteObjFromDB(CarOrder.class, DBWhereBuilder.b("uuid", "=", uuid));
    }


    /**
     * 获取检车订单列表
     *
     * @param phoneNumber
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public List< CarOrder > getCarOrderList(String appraiserId) throws CEDbException
    {
        List< CarOrder > list =
                getObjListFromDB(DBSelector.from(CarOrder.class).where("appraiserId", "=", appraiserId)
                        .and("isPreCheck", "=", "0").orderBy("modifyTime", true));
        if ( list == null )
        {
            return null;
        }
        getCarOrders(list);
        return list;
    }


    /**
     * 模糊查询，根据订单号、客户手机号、姓名等查询
     *
     * @param phoneNumber
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public List< CarOrder > searchCarOrderList(String value, int pageSize, int pageNo)
            throws CEDbException
    {
        List< CarOrder > list = getObjListFromDB(DBSelector.from(CarOrder.class)
                .where("phoneNumber", "like", "%" + value + "%").or("trueName", "like", "%" + value + "%")
                .or("orderNum", "like", "%" + value + "%").and("isPreCheck", "=", "0")
                .orderBy("modifyTime", true).limit(pageSize).offset((pageNo - 1) * pageSize));
        if ( list == null )
        {
            return null;
        }
        getCarOrders(list);
        return list;
    }

    /**
     * 获取固定个数的检车订单列表
     *
     * @param appraiserId
     * @param pageSize
     * @param pageNo
     * @return
     * @throws CEDbException
     */
    public List< CarOrder > getCarOrderList(String appraiserId, int pageSize, int pageNo)
            throws CEDbException
    {
        List< CarOrder > list = getObjListFromDB(DBSelector.from(CarOrder.class)
                .where("appraiserId", "=", appraiserId).and("isPreCheck", "=", "0")
                .orderBy("modifyTime", true).limit(pageSize).offset((pageNo - 1) * pageSize));
        if ( list == null )
        {
            return null;
        }
        getCarOrders(list);
        return list;
    }


    /**
     * 获取固定个数的检车订单列表
     *
     * @param appraiserId
     * @param pageSize
     * @param pageNo
     * @return
     * @throws CEDbException
     */
    public List< CarOrder > getCarOrderList(String appraiserId, int pageSize, int pageNo, int[] status)
            throws CEDbException
    {
        List< CarOrder > list =
                getObjListFromDB(DBSelector.from(CarOrder.class).where("appraiserId", "=", appraiserId)
                        .and("status", "in", status).and("isPreCheck", "=", "0").orderBy("modifyTime", true)
                        .limit(pageSize).offset((pageNo - 1) * pageSize));
        if ( list == null )
        {
            return null;
        }
        getCarOrders(list);
        return list;
    }

    private void getCarOrders(List< CarOrder > list)
    {
        if ( list != null && list.size() > 0 )
        {
            for ( CarOrder carOrder : list )
            {
                generateCarOrder(carOrder);
            }
        }
    }

    /**
     * 检车订单列表保存
     *
     * @param user
     * @throws CEDbException 下午6:41:45
     */
    public void saveOrUpdateOrderList(ArrayList< CarOrder > orderList, String appraiserId)
            throws CEDbException
    {
        ArrayList< String > uuids = new ArrayList< String >();
        for ( CarOrder cr : orderList )
        {
            uuids.add(cr.uuid);
        }
        saveOrUpdateObjListToDB(CarOrder.class, orderList, DBWhereBuilder.b("uuid", "in", uuids));
    }

    public void saveOrUpdateOrderList2(ArrayList< CarOrder > orderList, String appraiserId)
            throws CEDbException
    {
        saveOrUpdateObjListToDB(CarOrder.class, orderList,
                DBWhereBuilder.b("appraiserId", "=", appraiserId));
    }

    public void updateOrder(CarOrder order) throws CEDbException
    {
        updateObjToDB(order, DBWhereBuilder.b("uuid", "=", order.uuid));
    }

    public void saveOrUpdateOrder(CarOrder order, String uuid) throws CEDbException
    {
        saveOrUpdateObjToDB(CarOrder.class, order, DBWhereBuilder.b("uuid", "=", order.uuid));
    }

    /**
     * 返回预检测的订单
     *
     * @param appraiserId
     * @return
     * @throws CEDbException
     */
    public CarOrder getPreCarOrder(String appraiserId) throws CEDbException
    {
        CarOrder carOrder = getNewestObjFromDB(DBSelector.from(CarOrder.class)
                .where("appraiserId", "=", appraiserId).and("isPreCheck", "=", "1"));
        if ( carOrder != null )
        {
            generateCarOrder(carOrder);
        }
        return carOrder;
    }


    /**
     * 删除表数据
     *
     * @param clazz
     * @throws CEDbException
     */
    public void deleteOrder(Class< CarOrder > clazz) throws CEDbException
    {
        helper.delete(clazz, DBWhereBuilder.b("isPreCheck", "=", "0"));
    }

    private void generateCarOrder(CarOrder carOrder)
    {
        carOrder.appraiser =
                JsonUtils.parserJsonStringToObject(carOrder.appraiserJson, Appraiser.class);
        carOrder.customer = JsonUtils.parserJsonStringToObject(carOrder.customerJson, Customer.class);
        String json = carOrder.reportListJson;
        if ( !TextUtils.isEmpty(json) )
        {
            try
            {
                JSONArray ja = new JSONArray(json);
                if ( carOrder.reportList == null )
                {
                    carOrder.reportList = new ArrayList< CarReport >();
                }
                carOrder.reportList.clear();
                for ( int i = 0; i < ja.length(); i++ )
                {
                    carOrder.reportList
                            .add(JsonUtils.parserJsonStringToObject(ja.getJSONObject(i), CarReport.class));
                }

            } catch ( JSONException e )
            {
                e.printStackTrace();
            }
        }
    }

}

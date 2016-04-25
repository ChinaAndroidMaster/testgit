/**
 * Copyright © 2015 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.db.CarOrderUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.CarOrderBean.OrdersResult;
import com.creditease.checkcars.net.oper.bean.CarOrderBean.RequestObj;
import com.creditease.checkcars.net.oper.bean.CarOrderBean.RequestObj2;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author 子龍
 * @function
 * @date 2015年8月3日
 * @company CREDITEASE
 */
public final class GetCarOrdersOperation extends Oper< Object >
{

    private String appraiserId;
    private long modifyTime;

    public GetCarOrdersOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    /**
     * 解析数据
     *
     * @throws CEFailtureException
     * @throws CERequestException
     */
    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse resp = OperResponseFactory.parseResult(result);
        String data = DESEncrypt.decryption(resp.data);
        OrdersResult orderResult = JsonUtils.parserJsonStringToObject(data, OrdersResult.class);
        if ( orderResult.dataList != null )
        {
            // 保存订单列表
            try
            {
                for ( CarOrder order : orderResult.dataList )
                {
                    order.appraiserId = order.appraiser.uuid;
                    order.appraiserJson = JsonUtils.requestObjectBean(order.appraiser);
                    order.customerJson = JsonUtils.requestObjectBean(order.customer);
                    order.reportListJson = JsonUtils.requestObjectBean(order.reportList);
                    order.tradeAmount = order.payOrder != null ? order.payOrder.tradeAmount : "";
                    order.payType = order.payOrder != null ? order.payOrder.payType : -1;
                }
                if ( orderResult.dataList.size() > 0 )
                {
                    String appraiserId = SharePrefenceManager.getAppraiserId(mContext);
                    SharePrefenceManager.setOrdersNewestModifyTime(mContext,
                            orderResult.dataList.get(0).modifyTime, appraiserId);
                }
                if ( modifyTime == 0 )
                {
                    CarOrderUtil.getUtil(mContext).deleteOrder(CarOrder.class);
                }
                CarOrderUtil.getUtil(mContext).saveOrUpdateOrderList(orderResult.dataList, appraiserId);
            } catch ( CEDbException e )
            {
                e.printStackTrace();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        bundle.putParcelableArrayList(BUNDLE_EXTRA_DATA, orderResult.dataList);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_USERS_CARORDERS_GET_URL;
    }

    /**
     * 设置请求参数
     *
     * @param status   预约状态:0预下单1已回访2已分配3已取消4已生成(报告)5作废',
     * @param curPage  分页
     * @param pageSize 上午10:44:23
     */
    public void setParam(long modifyTime, int status, int curPage, int pageSize)
    {
        appraiserId = SharePrefenceManager.getAppraiserId(mContext);
        this.modifyTime = modifyTime;
        String json;
        if ( -1 == status )
        {
            RequestObj2 obj = new RequestObj2(modifyTime, appraiserId, curPage, pageSize);
            json = JsonUtils.requestObjectBean(obj);
        } else
        {
            RequestObj obj = new RequestObj(modifyTime, appraiserId, status, curPage, pageSize);
            json = JsonUtils.requestObjectBean(obj);
        }
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

}

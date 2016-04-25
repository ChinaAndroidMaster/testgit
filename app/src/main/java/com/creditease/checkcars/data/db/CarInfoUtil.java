package com.creditease.checkcars.data.db;

import java.util.List;

import android.content.Context;

import com.creditease.checkcars.data.bean.CarInfoBean;
import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.exception.CEDbException;

/**
 * 检车数据库工具类
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public class CarInfoUtil extends DBUtil< CarInfoBean >
{


    private static CarInfoUtil util;

    public static CarInfoUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new CarInfoUtil(context);
        }
        return util;
    }

    protected CarInfoUtil(Context mContext)
    {
        super(mContext);
    }

    /**
     * 获取车辆信息
     *
     * @param vin
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public List< CarInfoBean > getCarInfo(String vin) throws CEDbException
    {
        return getObjListFromDB(DBSelector.from(CarInfoBean.class).where("vin", "=", vin));
    }

    /**
     * 保存车辆信息
     *
     * @param carInfo
     * @throws CEDbException 下午6:41:45
     */
    public void saveOrUpdateCarInfo(CarInfoBean carInfo) throws CEDbException
    {
        saveObjToDB(carInfo);
    }
}

package com.creditease.checkcars.data.db;

import android.content.Context;

import com.creditease.checkcars.data.bean.Car;
import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.data.db.base.DBWhereBuilder;
import com.creditease.checkcars.exception.CEDbException;

/**
 * 检车数据库工具类
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public class CarUtil extends DBUtil< Car >
{


    private static CarUtil util;

    public static CarUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new CarUtil(context);
        }
        return util;
    }

    protected CarUtil(Context mContext)
    {
        super(mContext);
    }

    /**
     * 获取检车
     *
     * @param phoneNumber
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public Car getCar(String uuid) throws CEDbException
    {
        return getNewestObjFromDB(DBSelector.from(Car.class).where("uuid", "=", uuid));
    }

    /**
     * 检车保存
     *
     * @param user
     * @throws CEDbException 下午6:41:45
     */
    public void saveOrUpdateCar(Car car) throws CEDbException
    {
        updateAndSaveObj(car, DBWhereBuilder.b("uuid", "=", car.uuid));
    }
}

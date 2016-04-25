package com.creditease.checkcars.data.db;

import android.content.Context;

import com.creditease.checkcars.data.bean.Saler;
import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.data.db.base.DBWhereBuilder;
import com.creditease.checkcars.exception.CEDbException;

/**
 * 用户数据库工具类
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public class SalerUtil extends DBUtil< Saler >
{

    private static SalerUtil util;

    public static SalerUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new SalerUtil(context);
        }
        return util;
    }

    protected SalerUtil(Context mContext)
    {
        super(mContext);
    }

    /**
     * 获销售
     *
     * @param coreId UserCore 的uuid
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public Saler getSaler(String coreId) throws CEDbException
    {
        return getNewestObjFromDB(DBSelector.from(Saler.class).where("coreId", "=", coreId));
    }

    /**
     * 保存用户
     *
     * @param user
     * @throws CEDbException 下午6:41:45
     */
    public void saveOrUpdateSaler(Saler saler) throws CEDbException
    {
        updateAndSaveObj(saler, DBWhereBuilder.b("uuid", "=", saler.uuid));
    }

}

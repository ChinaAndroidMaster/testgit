package com.creditease.checkcars.data.db;

import java.util.List;

import android.content.Context;

import com.creditease.checkcars.data.bean.Appraiser;
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
public class AppraiserUtil extends DBUtil< Appraiser >
{


    private static AppraiserUtil util;

    public static AppraiserUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new AppraiserUtil(context);
        }
        return util;
    }

    protected AppraiserUtil(Context mContext)
    {
        super(mContext);
    }

    /**
     * 获取用户
     *
     * @param phoneNumber
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public Appraiser getAppraiser(String phoneNumber) throws CEDbException
    {
        return getNewestObjFromDB(DBSelector.from(Appraiser.class).where("phoneNumber", "=",
                phoneNumber));
    }


    /**
     * 获取用户
     *
     * @param userId
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public List< Appraiser > getAppraiserByUserId(String appraiserId) throws CEDbException
    {
        return getObjListFromDB(DBSelector.from(Appraiser.class).where("uuid", "=", appraiserId));
    }

    /**
     * 保存用户
     *
     * @param user
     * @throws CEDbException 下午6:41:45
     */
    public void saveOrUpdateAppraiser(Appraiser user) throws CEDbException
    {
        if ( user == null )
        {
            return;
        }
        updateAndSaveObj(user, DBWhereBuilder.b("uuid", "=", user.uuid));
    }


}

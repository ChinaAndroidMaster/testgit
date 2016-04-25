package com.creditease.checkcars.data.db;

import android.content.Context;

import com.creditease.checkcars.data.bean.UserCore;
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
public class UserUtil extends DBUtil< UserCore >
{

    private static UserUtil util;

    public static UserUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new UserUtil(context);
        }
        return util;
    }

    protected UserUtil(Context mContext)
    {
        super(mContext);
    }

    /**
     * 获取用户
     *
     * @param uuid
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public UserCore getUser(String uuid) throws CEDbException
    {
        return getNewestObjFromDB(DBSelector.from(UserCore.class).where("uuid", "=", uuid));
    }

    /**
     * 保存用户
     *
     * @param user
     * @throws CEDbException 下午6:41:45
     */
    public void saveOrUpdateUser(UserCore user) throws CEDbException
    {
        updateAndSaveObj(user, DBWhereBuilder.b("uuid", "=", user.uuid));
    }

}

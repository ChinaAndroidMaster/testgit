package com.creditease.checkcars.data.db;

import java.util.List;

import android.content.Context;

import com.creditease.checkcars.data.bean.UserPositionInfo;
import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.data.db.base.DBWhereBuilder;
import com.creditease.checkcars.exception.CEDbException;

public class UserPositionUtil extends DBUtil< UserPositionInfo >
{

    private static UserPositionUtil util;

    private UserPositionUtil(Context mContext)
    {
        super(mContext);
    }

    public static UserPositionUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new UserPositionUtil(context);
        }
        return util;
    }


    public List< UserPositionInfo > getUserPositionList(String appraiserId, String createTime)
            throws CEDbException
    {
        return getObjListFromDB(DBSelector.from(UserPositionInfo.class)
                .where("toId", "=", appraiserId).and("recordTime", "<", createTime + " 00:00:00"));
    }

    public void saveOrUpdateUserPosition(UserPositionInfo posInfo) throws CEDbException
    {
        saveObjToDB(posInfo);

    }

    public void deleteUserPosition(String appraiserId, String createTime) throws CEDbException
    {
        deleteObjFromDB(UserPositionInfo.class, DBWhereBuilder.b("toId", "=", appraiserId)
                .and("recordTime", "<", createTime + " 00:00:00"));
    }

}

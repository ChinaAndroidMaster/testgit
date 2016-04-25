package com.creditease.checkcars.data.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.creditease.checkcars.data.bean.MineTopicsCore;
import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.data.db.base.DBWhereBuilder;
import com.creditease.checkcars.exception.CEDbException;

public class MineTopicUtil extends DBUtil< MineTopicsCore >
{

    private static MineTopicUtil util;

    public static MineTopicUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new MineTopicUtil(context);
        }
        return util;
    }

    protected MineTopicUtil(Context mContext)
    {
        super(mContext);
    }

    /**
     * 查询服务评价所有数据
     *
     * @return
     * @throws CEDbException
     */
    public List< MineTopicsCore > getTopicListByToId(String uuid) throws CEDbException
    {
        return getObjListFromDB(DBSelector.from(MineTopicsCore.class).where("toId", "=", uuid));
    }

    /**
     * 保存或者更新服务评价表
     *
     * @param list
     * @param uuid
     * @throws CEDbException
     */
    public void saveOrUpdateTopics(ArrayList< MineTopicsCore > list, String uuid) throws CEDbException
    {
        saveOrUpdateObjListToDB(MineTopicsCore.class, list, DBWhereBuilder.b("toId", "=", uuid));
    }

}

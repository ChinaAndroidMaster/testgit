package com.creditease.checkcars.data.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.creditease.checkcars.data.bean.QuestionAnswer;
import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.data.db.base.DBWhereBuilder;
import com.creditease.checkcars.exception.CEDbException;

public class QuestionAnswerUtil extends DBUtil< QuestionAnswer >
{

    private static QuestionAnswerUtil util;

    public static QuestionAnswerUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new QuestionAnswerUtil(context);
        }
        return util;
    }

    protected QuestionAnswerUtil(Context mContext)
    {
        super(mContext);
    }

    /**
     * 获取存到本地的问答列表数据
     *
     * @param uuid
     * @return
     * @throws CEDbException
     */
    public List< QuestionAnswer > getQuestionAnswerList() throws CEDbException
    {
        return getObjListFromDB(DBSelector.from(QuestionAnswer.class).orderBy("modifyTime", true));
    }


    /**
     * 修改某问答的数据
     *
     * @param id
     * @throws CEDbException
     */
    public void saveOrUpdateAnswer(QuestionAnswer qa, String uuid) throws CEDbException
    {
        saveOrUpdateObjToDB(QuestionAnswer.class, qa, DBWhereBuilder.b("uuid", "=", uuid));
    }


    /**
     * 保存问题列表
     *
     * @param list
     * @param appraiserId
     * @throws CEDbException
     */
    public void saveOrUpdateQuestionAnswerList(ArrayList< QuestionAnswer > list) throws CEDbException
    {
        ArrayList< String > questionIds = new ArrayList< String >();
        for ( QuestionAnswer qa : list )
        {
            questionIds.add(qa.uuid);
        }
        saveOrUpdateObjListToDB(QuestionAnswer.class, list, DBWhereBuilder.b("uuid", "in", questionIds));
    }

}

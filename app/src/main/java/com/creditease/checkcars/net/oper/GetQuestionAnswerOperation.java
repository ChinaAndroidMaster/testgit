package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.QAnswer;
import com.creditease.checkcars.data.bean.QuestionAnswer;
import com.creditease.checkcars.data.db.QuestionAnswerUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.QuestionAnswerBean.QuestionAnswerObj;
import com.creditease.checkcars.net.oper.bean.QuestionAnswerBean.QuestionAnswerResult;
import com.creditease.checkcars.tools.JsonUtils;

public class GetQuestionAnswerOperation extends Oper< Object >
{


    public GetQuestionAnswerOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse res = OperResponseFactory.parseResult(result);
        String data = DESEncrypt.decryption(res.data);
        QuestionAnswerResult objResult =
                JsonUtils.parserJsonStringToObject(data, QuestionAnswerResult.class);
        String appraiserId = SharePrefenceManager.getAppraiserId(mContext);
        try
        {
            if ( objResult.dataList != null )
            {
                for ( QuestionAnswer qa : objResult.dataList )
                {
                    qa.qanswerJson = JsonUtils.requestObjectBean(qa.answers);
                    qa.userInfoJson = JsonUtils.requestObjectBean(qa.userInfo);
                    for ( QAnswer answer : qa.answers )
                    {
                        answer.userInfoJson = JsonUtils.requestObjectBean(answer.userInfo);
                    }
                }
                if ( objResult.dataList.size() > 0 )
                {
                    SharePrefenceManager.setQuestionAnswerTime(mContext,
                            String.valueOf(objResult.dataList.get(0).modifyTime), appraiserId);
                }
                // 保存问答列表
                QuestionAnswerUtil.getUtil(mContext).saveOrUpdateQuestionAnswerList(objResult.dataList);
            }
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, res);
        bundle.putParcelableArrayList(BUNDLE_EXTRA_DATA, objResult.dataList);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_GET_QUESTION_ANSWER_DATA;
    }

    /**
     * 设置参数并加密
     *
     * @param currentPage
     * @param pageSize
     * @param modifyTime
     */
    public void setParam(int currentPage, int pageSize, String modifyTime, int examine)
    {
        String json =
                JsonUtils.requestObjectBean(new QuestionAnswerObj(examine, modifyTime, currentPage,
                        pageSize));
        String data = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", data);
        setParam(param);
    }
}

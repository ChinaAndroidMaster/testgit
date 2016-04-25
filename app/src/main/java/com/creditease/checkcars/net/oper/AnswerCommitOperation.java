package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.QAnswerBean.QAnswerObj;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author zgb
 */
public class AnswerCommitOperation extends Oper< Object >
{

    public AnswerCommitOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse resp = OperResponseFactory.parseResult(result);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_UPDATE_QUESTION_ANSWER_DATA;
    }

    public void setParam(String questionId, String fromId, int position, String content)
    {
        String json =
                JsonUtils.requestObjectBean(new QAnswerObj(questionId, fromId, position, content));
        String data = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", data);
        setParam(param);
    }

}

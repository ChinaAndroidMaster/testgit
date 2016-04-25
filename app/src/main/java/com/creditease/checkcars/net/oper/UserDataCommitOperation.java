package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.db.AppraiserUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.base.Result;
import com.creditease.checkcars.net.oper.bean.UserDataBean.UserDataObj;
import com.creditease.checkcars.tools.JsonUtils;

public class UserDataCommitOperation extends Oper< Object >
{

    private Appraiser mAppraiser;
    private Context mContext;

    public UserDataCommitOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
        this.mContext = mContext;
    }

    /**
     * 返回请求结果
     */
    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse resp = OperResponseFactory.parseResult(result);
        if ( (resp != null) && resp.result == Result.RESULT_SUCCESS )
        {
            try
            {
                AppraiserUtil.getUtil(mContext).saveOrUpdateAppraiser(mAppraiser);
            } catch ( CEDbException e )
            {
                e.printStackTrace();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, resp);
        return bundle;
    }

    /**
     * return URL
     */
    @Override
    protected String getUrl()
    {
        return Config.WS_MINE_DATA_SAVE_URL;
    }

    /**
     * 请求参数
     *
     * @param appraiser
     */
    public void setParam(Appraiser appraiser)
    {
        mAppraiser = appraiser;
        // 将对象转化成json串,使用gson
        String json = JsonUtils.requestObjectBean(new UserDataObj(appraiser.uuid, appraiser.isProvideService, appraiser.declaration));
        // 对json串加密，des3方式加密,对称加密
        String basicData = DESEncrypt.encryption(json);
        // 创建参数对象
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", basicData);
        setParam(param);
    }

}

/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.db.AppraiserUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author zgb
 * @date 2014年10月23日
 * @company 宜信
 */
public final class GetBasicDataOperation extends Oper< Object >
{

    public GetBasicDataOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    /**
     * 解析数据
     *
     * @throws CEFailtureException
     * @throws CERequestException
     */
    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse res = OperResponseFactory.parseResult(result);
        String data = DESEncrypt.decryption(res.data);
        Appraiser obj = JsonUtils.parserJsonStringToObject(data, Appraiser.class);
        if ( obj != null )
        {
            try
            {
                // 保存用户数据
                AppraiserUtil.getUtil(mContext).saveOrUpdateAppraiser(obj);
                SharePrefenceManager.setHeadPic(mContext, obj.headPic, obj.uuid);
            } catch ( CEDbException e )
            {
                e.printStackTrace();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_DATA, obj);
        bundle.putString("userdata", "userdata");
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_MINE_BASIC_DATA;
    }

    /**
     * @param uuid
     */
    public void setParam(String uuid)
    {
        RequestParam param = new RequestParam();
        param.addBodyParameter("uuid", DESEncrypt.encryption(uuid));
        setParam(param);
    }

}

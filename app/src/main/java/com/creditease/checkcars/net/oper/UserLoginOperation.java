/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.db.AppraiserUtil;
import com.creditease.checkcars.data.db.SalerUtil;
import com.creditease.checkcars.data.db.UserUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.msgpush.MsgPushManager;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.LoginBean.LoginObj;
import com.creditease.checkcars.net.oper.bean.LoginBean.LoginResult;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * @author 子龍
 * @date 2014年10月23日
 * @company 宜信
 */
public final class UserLoginOperation extends Oper< Object >
{
    private String name;
    private String pwd;
    private Handler mHandler = new Handler(Looper.myLooper());

    public UserLoginOperation(Context mContext, RequestListener listener)
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
        final LoginResult obj = JsonUtils.parserJsonStringToObject(data, LoginResult.class);
        if ( obj != null )
        {
            try
            {
                // 保存用户数据
                if ( obj.UserCore != null )
                {
                    UserUtil.getUtil(mContext).saveOrUpdateUser(obj.UserCore);
                    SharePrefenceManager.setUserId(mContext, obj.UserCore.uuid);
                    // 保存头像
                    SharePrefenceManager.setHeadPic(mContext, obj.UserCore.headPic, obj.UserCore.uuid);
                }
                if ( obj.Appraiser != null )
                {
                    AppraiserUtil.getUtil(mContext).saveOrUpdateAppraiser(obj.Appraiser);
                    SharePrefenceManager.setAppraiserId(mContext, obj.Appraiser.uuid);
                }
                if ( obj.Saler != null )
                {
                    SalerUtil.getUtil(mContext).saveOrUpdateSaler(obj.Saler);
                    SharePrefenceManager.setSalerId(mContext.getApplicationContext(), obj.Saler.uuid);
                    SharePrefenceManager.setSalerHomePageUrl(mContext, obj.Saler.homePageUrl);
                }
                SharePrefenceManager.setUserLoginName(mContext, name);
                String pwd = DESEncrypt.encryption(this.pwd);
                SharePrefenceManager.setUserPassword(mContext, pwd);


                mHandler.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        if ( obj.UserCore != null )
                        {
                            MsgPushManager.getManager(mContext).setUserAlias(obj.UserCore.uuid);
                        }
                        if ( obj.Appraiser != null )
                        {
                            MsgPushManager.getManager(mContext).addUserTags(MsgPushManager.MSGPUSH_TAG_APPRAISER);
                        }
                        if ( obj.Saler != null )
                        {
                            MsgPushManager.getManager(mContext).addUserTags(MsgPushManager.MSGPUSH_TAG_SALER);
                        }
                    }
                });
            } catch ( CEDbException e )
            {
                Log.e("error", e.getMessage());
            }
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, res);
        if ( obj != null )
        {
            bundle.putParcelable(BUNDLE_EXTRA_DATA, obj.UserCore);
        }
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_USERS_USER_LOGIN_URL;
    }

    /**
     * 设置登录参数
     *
     * @param phoneNumber 电话
     * @param password    密码 上午10:44:36
     */
    public void setParam(String phoneNumber, String password)
    {
        name = phoneNumber;
        pwd = password;
        password = DESEncrypt.encryption(password);
        String json = JsonUtils.requestObjectBean(new LoginObj(phoneNumber, password));
        // ZLToast.getToast().showToast(mContext, json);
        String userData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", userData);
        setParam(param);
    }

}

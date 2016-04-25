/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.data.sp.manager;

import java.util.Arrays;

import android.content.Context;
import android.text.TextUtils;

import com.creditease.checkcars.data.bean.NewVersion;
import com.creditease.checkcars.data.sp.AccountSP;
import com.creditease.checkcars.data.sp.DefaultSP;
import com.creditease.checkcars.data.sp.LoginSP;
import com.creditease.checkcars.data.sp.ReportSP;
import com.creditease.checkcars.data.sp.UpdateTimeSP;
import com.creditease.checkcars.data.sp.base.SPConfig;
import com.creditease.checkcars.msgpush.MsgPushManager;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * Share preference Util 记录一些xml数据
 *
 * @author 子龍
 * @function
 * @date 2015年11月19日
 * @company CREDITEASE
 */
public final class SharePrefenceManager
{


    /******************************** ACCOUNT START ****************************************/
    /**
     * 获取师傅Id
     *
     * @param context
     * @return
     */
    public static String getAppraiserId(Context context)
    {
        return AccountSP.getSp(context).getString(SPConfig.SHARED_PREFS_APPRAISER_ID);
    }

    /**
     * 设置师傅Id
     *
     * @param context
     * @param userId
     */
    public static void setAppraiserId(Context context, String userId)
    {
        AccountSP.getSp(context).putString(SPConfig.SHARED_PREFS_APPRAISER_ID, userId);
    }

    /**
     * 获取销售Id
     *
     * @param context
     * @return
     */
    public static String getSalerId(Context context)
    {
        return AccountSP.getSp(context).getString(SPConfig.SHARED_PREFS_SALER_ID);
    }

    /**
     * 设置销售Id
     *
     * @param context
     * @param userId
     */
    public static void setSalerId(Context context, String userId)
    {
        AccountSP.getSp(context).putString(SPConfig.SHARED_PREFS_SALER_ID, userId);
    }


    /**
     * 获取用户头像地址
     *
     * @param context
     * @param uuid
     * @return 下午7:50:18
     */
    public static String getHeadPic(Context context, String uuid)
    {
        return AccountSP.getSp(context).getString(SPConfig.SHARED_PREFS_HEAD_PHOTO + "_" + uuid, "");
    }

    /**
     * 保存头像图片地址
     *
     * @param context
     * @param headPic
     */
    public static void setHeadPic(Context context, String headPic, String uuid)
    {
        AccountSP.getSp(context).putString(SPConfig.SHARED_PREFS_HEAD_PHOTO + "_" + uuid, headPic);
    }

    /**
     * 获取消息推送用户alias and aliastype
     *
     * @param context
     * @return 下午6:35:21
     */
    public static String[] getMsgPushAliasAndType(Context context)
    {
        String tags =
                DefaultSP.getSp(context).getString(SPConfig.SHARED_PREFS_MSGPUSH_ALIAS_AND_TYPE, "");
        return tags.split(",");
    }

    /**
     * 保存消息推送用户alias and aliastype
     *
     * @param context
     * @param aliass  下午6:34:52
     */
    public static void setMsgPushAliasAndType(Context context, String[] aliass)
    {
        String t = Arrays.toString(aliass);
        t = t.subSequence(1, t.length() - 1).toString();
        DefaultSP.getSp(context).putString(SPConfig.SHARED_PREFS_MSGPUSH_ALIAS_AND_TYPE, t);
    }

    /**
     * 获取消息推送用户标签
     *
     * @param context
     * @return 下午6:12:08
     */
    public static String[] getMsgPushTag(Context context)
    {
        String tags =
                DefaultSP.getSp(context).getString(SPConfig.SHARED_PREFS_MSGPUSH_TAG,
                        MsgPushManager.MSGPUSH_TAG_DEFAULT);
        return tags.split(",");
    }

    /**
     * 保存消息推送用户标签
     *
     * @param context
     * @param tags    下午6:15:40
     */
    public static void setMsgPushTag(Context context, String[] tags)
    {
        String t = Arrays.toString(tags);
        t = t.subSequence(1, t.length() - 1).toString();
        DefaultSP.getSp(context).putString(SPConfig.SHARED_PREFS_MSGPUSH_TAG, t);
    }


    /**
     * 获取用户Id
     *
     * @param context
     * @return
     */
    public static String getUserId(Context context)
    {
        return AccountSP.getSp(context).getString(SPConfig.SHARED_PREFS_USER_ID);
    }

    /**
     * 设置用户Id
     *
     * @param context
     * @param userId
     */
    public static void setUserId(Context context, String userId)
    {
        AccountSP.getSp(context).putString(SPConfig.SHARED_PREFS_USER_ID, userId);
    }


    /**
     * 获取用户名
     *
     * @param context
     * @return
     */
    public static String getUserName(Context context)
    {
        String userId = getUserId(context);
        return AccountSP.getSp(context).getString(SPConfig.SHARED_PREFS_USER_NAME + "_" + userId);
    }

    /**
     * 设置用户名
     *
     * @param context
     * @param userName
     */
    public static void setUserName(Context context, String userName)
    {
        String userId = getUserId(context);
        AccountSP.getSp(context).putString(SPConfig.SHARED_PREFS_USER_NAME + "_" + userId, userName);
    }

    /**
     * 获取用户密码
     *
     * @param context
     * @return
     */
    public static String getUserPassword(Context context)
    {
        String userId = getUserId(context);
        return AccountSP.getSp(context).getString(SPConfig.SHARED_PREFS_USER_PASSWORD + "_" + userId);
    }

    /**
     * 设置用户密码
     *
     * @param context
     * @param pwd
     */
    public static void setUserPassword(Context context, String pwd)
    {
        String userId = getUserId(context);
        AccountSP.getSp(context).putString(SPConfig.SHARED_PREFS_USER_PASSWORD + "_" + userId, pwd);
    }

    /**
     * 获取销售主页
     *
     * @param context
     * @return
     */
    public static String getSalerHomePageUrl(Context context)
    {
        String userId = getUserId(context);
        return AccountSP.getSp(context).getString(SPConfig.SHARED_PREFS_SALER_URL + "_" + userId);
    }

    /**
     * 设置销售主页
     *
     * @param context
     * @param pwd
     */
    public static void setSalerHomePageUrl(Context context, String salerHomePageUrl)
    {
        String userId = getUserId(context);
        AccountSP.getSp(context).putString(SPConfig.SHARED_PREFS_SALER_URL + "_" + userId,
                salerHomePageUrl);
    }

    /*********************************** ACCOUNT END *******************************************************/

    /**************************************** LOCAL START ******************************************************/
    /**
     * 获取版本号
     *
     * @param context
     * @return 上午11:51:31
     */
    public static String getAppVersion(Context context)
    {
        return DefaultSP.getSp(context).getString(SPConfig.SHARED_PREFS_VERSION, "");
    }

    /**
     * 保存版本号
     *
     * @param context
     * @param currentVersion 上午11:51:46
     */
    public static void setAppVersion(Context context, String currentVersion)
    {
        DefaultSP.getSp(context).putString(SPConfig.SHARED_PREFS_VERSION, currentVersion);
    }


    /**
     * 获取新版本
     *
     * @param context
     * @return 下午5:51:30
     */
    public static NewVersion getNewVersion(Context context)
    {
        String version = DefaultSP.getSp(context).getString(SPConfig.SHARED_PREFS_HAVE_VERSION, null);
        if ( !TextUtils.isEmpty(version) )
        {
            return JsonUtils.parserJsonStringToObject(version, NewVersion.class);
        }
        return null;
    }

    /**
     * 设置新版本
     *
     * @param context
     * @param haveNewVersion boolean 上午11:51:46
     */
    public static void setNewVersion(Context context, NewVersion newVersion)
    {
        String versionJson = newVersion == null ? null : JsonUtils.requestObjectBean(newVersion);
        DefaultSP.getSp(context).putString(SPConfig.SHARED_PREFS_HAVE_VERSION, versionJson);
    }

    /************************ login start *********************************/
    /**
     * 获取上一次登录时间
     *
     * @param context
     * @param defaultTime
     * @return
     */
    public static long getLastLoginTime(Context context, long defaultTime)
    {
        return LoginSP.getSp(context).getLong(SPConfig.SHARED_PREFS_LOGIN_TIME);
    }

    /**
     * 设置登录时间
     *
     * @param context
     * @param time
     */
    public static void setLoginTime(Context context, long time)
    {
        LoginSP.getSp(context).putLong(SPConfig.SHARED_PREFS_LOGIN_TIME, time);
    }

    /**
     * 获取登陆名
     *
     * @param context
     * @return 下午7:52:39
     */
    public static String getUserLoginName(Context context)
    {
        return LoginSP.getSp(context).getString(SPConfig.SHARED_PREFS_USER_LOGIN_NAME, "");
    }

    /**
     * 设置登录名
     *
     * @param context
     * @param userLoginName 下午7:53:02
     */
    public static void setUserLoginName(Context context, String userLoginName)
    {
        LoginSP.getSp(context).putString(SPConfig.SHARED_PREFS_USER_LOGIN_NAME, userLoginName);
    }

    /************************ login end *********************************/

    /************************ update time start *********************************/

    /**
     * 获取我的主页使用的最新时间
     *
     * @param context
     * @param topicTime
     * @return
     */
    public static String getMainTopicTime(Context context, String uuid)
    {
        return UpdateTimeSP.getSp(context).getString(
                SPConfig.SHARED_PREFS_MAIN_TOPICS_TIME + "_" + uuid, "0");
    }

    /**
     * 我的主页使用的最新时间
     *
     * @param context
     * @param topicTime
     */
    public static void setMainTopicTime(Context context, String topicTime, String uuid)
    {
        UpdateTimeSP.getSp(context).putString(SPConfig.SHARED_PREFS_MAIN_TOPICS_TIME + "_" + uuid,
                topicTime);
    }

    /**
     * 获取最新订单更新时间
     *
     * @param context
     * @return 下午4:51:40
     */
    public static String getOrdersNewestModifyTime(Context context, String uuid)
    {
        return UpdateTimeSP.getSp(context).getString(
                SPConfig.SHARED_PREFS_NEWEST_ORDERS_MODIFYTIME_FLAG + "_" + uuid, "0");
    }

    /**
     * 保存最新订单更新时间
     *
     * @param context
     * @param modifyTime 下午4:51:57
     */
    public static void setOrdersNewestModifyTime(Context context, String modifyTime, String uuid)
    {
        UpdateTimeSP.getSp(context).putString(
                SPConfig.SHARED_PREFS_NEWEST_ORDERS_MODIFYTIME_FLAG + "_" + uuid, modifyTime);
    }


    /**
     * 获取问答列表最后一次从服务器端获取数据的时间
     *
     * @param context
     * @return
     */
    public static String getQuestionAnswerTime(Context context, String uuid)
    {
        return UpdateTimeSP.getSp(context).getString(
                SPConfig.SHARED_PREFS_QUESTION_ANSWER_TIME + "_" + uuid, "0");
    }

    /**
     * 保存问答列表最后一次从服务器端获取数据的时间
     *
     * @param context
     * @param qaTime
     */
    public static void setQuestionAnswerTime(Context context, String qaTime, String uuid)
    {
        UpdateTimeSP.getSp(context).putString(SPConfig.SHARED_PREFS_QUESTION_ANSWER_TIME + "_" + uuid,
                qaTime);
    }

    /**
     * 服务评价也使用的数据最新时间
     *
     * @param context
     * @param topicTime
     * @return
     */
    public static String getTopicTime(Context context, String uuid)
    {
        return UpdateTimeSP.getSp(context).getString(SPConfig.SHARED_PREFS_TOPICS_TIME + "_" + uuid,
                "0");
    }

    /**
     * 服务评价也使用的数据最新时间
     *
     * @param context
     * @param topicTime
     */
    public static void setTopicTime(Context context, String topicTime, String uuid)
    {
        UpdateTimeSP.getSp(context)
                .putString(SPConfig.SHARED_PREFS_TOPICS_TIME + "_" + uuid, topicTime);
    }

    /************************ update time end *********************************/
    /**
     * 是否显示新手引导
     *
     * @param context
     * @return 下午7:20:15
     */
    public static boolean isShowGuide(Context context)
    {
        return DefaultSP.getSp(context).getBoolean(SPConfig.SHARED_PREFS_GUIDE_ISSHOW, false);
    }

    /**
     * 显示新手引导
     *
     * @param context 下午7:20:24
     */
    public static void showGuide(Context context)
    {
        DefaultSP.getSp(context).putBoolean(SPConfig.SHARED_PREFS_GUIDE_ISSHOW, true);
    }


    /**
     * 保存当前日期
     *
     * @param context
     * @param userId
     */
    public static void setCurrentDate(Context context, String apprasierId, String date)
    {
        DefaultSP.getSp(context).putString(SPConfig.SHARED_PREFS_CURRENT_DATE + apprasierId, date);
    }

    /**
     * 得到保存的日期
     *
     * @param context
     * @param appraiser
     * @return
     */
    public static String getCurrentDate(Context context, String apprasierId)
    {
        return DefaultSP.getSp(context).getString(SPConfig.SHARED_PREFS_CURRENT_DATE + apprasierId, "");
    }


    /**
     * 保存当前地址
     *
     * @param context
     * @param userId
     */
    public static void setCurrentAddr(Context context, String apprasierId, String address)
    {
        DefaultSP.getSp(context).putString(SPConfig.SHARED_PREFS_CURRENT_ADDR + apprasierId, address);
    }

    /**
     * 得到保存的日期
     *
     * @param context
     * @param appraiser
     * @return
     */
    public static String getCurrentAddr(Context context, String apprasierId)
    {
        return DefaultSP.getSp(context).getString(SPConfig.SHARED_PREFS_CURRENT_ADDR + apprasierId, "");
    }


    /**************************************** LOCAL END ******************************************************/

    /*************************************** REPORT START **************************************************/
    /**
     * 获取报告字典
     *
     * @param context
     * @return 上午11:44:19
     */
    public static String getBaseReport(Context context)
    {
        return ReportSP.getSp(context).getString(SPConfig.SHARED_PREFS_BASEREPORT_FLAG, "");
    }

    /**
     * 保存报告字典
     *
     * @param context
     * @param baseReport 上午11:44:43
     */
    public static void setBaseReport(Context context, String baseReport)
    {
        ReportSP.getSp(context).putString(SPConfig.SHARED_PREFS_BASEREPORT_FLAG, baseReport);
    }

    /**
     * 获取字典更新时间
     *
     * @param context
     * @return 上午11:44:56
     */
    public static String getBaseReportUpdateTime(Context context, String uuid)
    {
        return ReportSP.getSp(context).getString(
                SPConfig.SHARED_PREFS_BASEREPORT_UPDATE_TIME_FLAG + "_" + uuid, "0");
    }

    /**
     * 保存字典更新时间
     *
     * @param context
     * @param updateTime 上午11:45:11
     */
    public static void setBaseReportUpdateTime(Context context, String updateTime, String uuid)
    {
        ReportSP.getSp(context).putString(
                SPConfig.SHARED_PREFS_BASEREPORT_UPDATE_TIME_FLAG + "_" + uuid, updateTime);
    }


    /***************************************
     * REPORT END
     **************************************************/

    private SharePrefenceManager()
    {
        // No public constructor
    }

}

package com.creditease.checkcars.msgpush.bean;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.creditease.checkcars.msgpush.CEMsgPushExpection;
import com.creditease.checkcars.tools.JsonUtils;
import com.umeng.common.message.Log;

public class NotiFactory
{
    private static final String TAG = NotiFactory.class.getSimpleName();

    /**
     * 订单更新
     */
    public static final int TYPE_MSG_UPDATE_ORDERS = 1;
    /**
     * 新评论
     */
    public static final int TYPE_MSG_UPDATE_COMMENTS = 2;
    /**
     * 新版本
     */
    public static final int TYPE_MSG_UPDATE_VERSION = 3;

    /**
     * 订单重分配
     */
    public static final int TYPE_MSG_UPDATE_ORDER_REASSIGN = 4;

    public static NotiObj factory(String custom) throws CEMsgPushExpection
    {
        if ( TextUtils.isEmpty(custom) )
        {
            throw new CEMsgPushExpection("custom is empty");
        }
        try
        {
            JSONObject parser = new JSONObject(custom);
            if ( !parser.has("msgType") )
            {
                throw new CEMsgPushExpection("custom error format, no msgType");
            }
            int type = parser.optInt("msgType");
            switch ( type )
            {
                case TYPE_MSG_UPDATE_ORDERS:
                    return JsonUtils.parserJsonStringToObject(parser, OrderNoti.class);
                case TYPE_MSG_UPDATE_COMMENTS:
                    return JsonUtils.parserJsonStringToObject(parser, CommentsNoti.class);
                case TYPE_MSG_UPDATE_VERSION:
                    return JsonUtils.parserJsonStringToObject(parser, VersionNoti.class);
                case TYPE_MSG_UPDATE_ORDER_REASSIGN:
                    OrderReAssignNoti orn =
                            JsonUtils.parserJsonStringToObject(parser, OrderReAssignNoti.class);
                    if ( !TextUtils.isEmpty(orn.data) )
                    {
                        JSONObject obj = new JSONObject(orn.data);
                        orn.orderId = obj.optString("uuid");
                    }
                    return orn;
                default:
                    return JsonUtils.parserJsonStringToObject(parser, NotiObj.class);
            }
        } catch ( JSONException e )
        {
            Log.d(TAG, "JSONException=" + e.getMessage());
            throw new CEMsgPushExpection("JSONException:" + e.getMessage());
        } catch ( Exception e )
        {
            Log.d(TAG, "Exception=" + e.getMessage());
            throw new CEMsgPushExpection("error:" + e.getMessage());
        }
    }

}

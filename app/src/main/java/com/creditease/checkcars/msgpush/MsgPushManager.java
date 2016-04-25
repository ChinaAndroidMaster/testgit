package com.creditease.checkcars.msgpush;

import org.android.agoo.client.BaseRegistrar;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.UserCore;
import com.creditease.checkcars.data.db.UserUtil;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.utilframe.exception.HttpException;
// import com.umeng.common.message.Log;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

public class MsgPushManager implements RequestListener
{

    // private final static String TAG = "MsgPushManager";

    public static final String MSGPUSH_TAG_DEFAULT = "appraiser";
    public static final String MSGPUSH_TAG_APPRAISER = "appraiser";
    public static final String MSGPUSH_TAG_SALER = "saler";

    public static final String MSGPUSH_ALIAS_TYPE_DEFAULT = "shifukanche";

    private static MsgPushManager manager;

    private Context context;

    private PushAgent mPushAgent;

    public static final String CALLBACK_RECEIVER_ACTION = "callback_receiver_action";

    private MsgPushManager(Context context)
    {
        this.context = context;
        mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setDebugMode(false);
        mPushAgent.setMessageHandler(messageHandler);
    }

    public static MsgPushManager getManager(Context context)
    {
        if ( manager == null )
        {
            manager = new MsgPushManager(context);
        }
        return manager;
    }

    /**
     * 启动消息推送
     *
     * @param context 下午5:13:34
     */
    public void ableUmengPush(Context context)
    {
        this.context = context;
        if ( mPushAgent == null )
        {
            mPushAgent = PushAgent.getInstance(context);
        }
        // 打开umeng消息推送
        // if (!mPushAgent.isEnabled()) {
        mPushAgent.enable();
        // }
        mPushAgent.setPushIntentServiceClass(PushIntentService.class);
    }

    /**
     * 设置用户别名
     * <p/>
     * 下午6:04:59
     */
    public void setUserAlias(String alias)
    {
        new AddAliasTask(mPushAgent, alias, MsgPushManager.MSGPUSH_ALIAS_TYPE_DEFAULT, context)
                .execute();
    }

    /**
     * 设置用户标签
     * <p/>
     * 下午6:05:10
     */
    public void addUserTags(String tag)
    {
        new AddTagTask(mPushAgent, tag, context).execute();
    }

    /**
     * app启动
     * <p/>
     * 下午6:01:55
     */
    public void appStart()
    {
        if ( mPushAgent == null )
        {
            mPushAgent = PushAgent.getInstance(context);
        }
        // 打开umeng消息推送
        mPushAgent.onAppStart();
    }

    /**
     * 重启动
     * <p/>
     * 下午5:14:48
     */
    public void reEnableUmengPush()
    {
        // 若未成功，启动推送服务
        if ( !BaseRegistrar.isRegistered(context) )
        {
            mPushAgent.enable();
        }
    }

    /**
     * 上传消息推送用户数据
     * <p/>
     * 下午6:40:12
     *
     * @throws CEMsgPushExpection
     */
    public void updateMsgPushUserData() throws CEMsgPushExpection
    {
        String deviceToken = BaseRegistrar.getRegistrationId(context);
        if ( TextUtils.isEmpty(deviceToken) )
        {
            throw new CEMsgPushExpection("device not regist , deviceToken is null", 1);
        }
        String[] tags = SharePrefenceManager.getMsgPushTag(context);
        String[] aliass = SharePrefenceManager.getMsgPushAliasAndType(context);
        if ( (aliass == null) || (aliass.length < 2) )
        {
            throw new CEMsgPushExpection("alias and aliasType is null");
        }
        OperationFactManager.getManager().updateMsgPushUserDataWS(context, tags[0].trim(),
                aliass[0].trim(), aliass[1].trim(), deviceToken, this);
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        // TODO
    }

    @Override
    public void onFailure(OperResponse response)
    {

    }

    @Override
    public void onDataError(String errorMsg, String result)
    {

    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {

    }

    /**
     * 初始化消息推送 需要在主线程中初始化
     * <p/>
     * 下午5:13:09
     */
    public void initMSGPush()
    {

        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        mPushAgent.setRegisterCallback(mRegisterCallback);
        mPushAgent.setUnregisterCallback(mUnregisterCallback);
    }

    protected IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback()
    {

        @Override
        public void onRegistered(String deviceToken)
        {
            // TODO 注册
            // Intent intent = new Intent(CALLBACK_RECEIVER_ACTION);
            // context.sendBroadcast(intent);
            if ( TextUtils.isEmpty(deviceToken) )
            {
                mPushAgent.enable();
                return;
            }
            try
            {
                updateMsgPushUserData();
            } catch ( CEMsgPushExpection e )
            {
                String userId = SharePrefenceManager.getUserId(context);
                // 标签
                new AddAliasTask(mPushAgent, userId, MsgPushManager.MSGPUSH_ALIAS_TYPE_DEFAULT, context)
                        .execute();
                try
                {
                    UserCore user = UserUtil.getUtil(context).getUser(userId);
                    if ( user == null )
                    {
                        return;
                    }
                    if ( user.judgeUserType(UserCore.USER_TYPE_APPRAISER) )
                    {
                        MsgPushManager.getManager(context).addUserTags(MsgPushManager.MSGPUSH_TAG_APPRAISER);
                    }
                    if ( user.judgeUserType(UserCore.USER_TYPE_MARKET) )
                    {
                        MsgPushManager.getManager(context).addUserTags(MsgPushManager.MSGPUSH_TAG_SALER);
                    }
                } catch ( CEDbException e1 )
                {
                }
            }
        }

    };
    protected IUmengUnregisterCallback mUnregisterCallback = new IUmengUnregisterCallback()
    {

        @Override
        public void onUnregistered(String registrationId)
        {
        }
    };

    // 该类负责处理消息，包括通知消息和自定义消息
    protected UmengMessageHandler messageHandler = new UmengMessageHandler()
    {

        // 负责处理通知消息，该方法已经由消息推送SDK 完成
        @Override
        public void dealWithNotificationMessage(Context context, UMessage msg)
        {
            // Log.d(TAG, TAG + ":dealWithNotificationMessage=" + msg.custom);

        }

        // 负责处理自定义消息，需由用户处理。 若开发者需要处理自定义消息
        @Override
        public void dealWithCustomMessage(final Context context, final UMessage msg)
        {
            // Log.d(TAG, TAG + ":dealWithCustomMessage=" + msg.custom);
            MessageHandler.createHandler(context);
            MessageHandler.getMH().dealMessage(msg.custom);
        }

        // getNotification:若SDK默认的消息展示样式不符合开发者的需求，可通过覆盖该方法来自定义通知栏展示样式
        @Override
        public Notification getNotification(Context context, UMessage msg)
        {
            switch ( msg.builder_id )
            {
                case 1:
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    RemoteViews myNotificationView =
                            new RemoteViews(context.getPackageName(), R.layout.notification_view);
                    myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                    myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                    myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
                            getLargeIcon(context, msg));
                    myNotificationView.setImageViewResource(R.id.notification_small_icon,
                            getSmallIconId(context, msg));
                    builder.setContent(myNotificationView);
                    builder.setAutoCancel(true);
                    Notification mNotification = builder.build();
                    // 由于Android
                    // v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                    mNotification.contentView = myNotificationView;
                    return mNotification;
                default:
                    // 默认为0，若填写的builder_id并不存在，也使用默认。
                    return super.getNotification(context, msg);
            }
        }
    };

    /**
     * 该Handler是在BroadcastReceiver中被调用，故 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     */
    protected UmengNotificationClickHandler notificationClickHandler =
            new UmengNotificationClickHandler()
            {
                @Override
                public void dealWithCustomAction(Context context, UMessage msg)
                {
                    // TODO 负责处理消息的点击事件
                }
            };
}

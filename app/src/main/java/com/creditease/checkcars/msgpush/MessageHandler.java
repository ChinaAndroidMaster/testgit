package com.creditease.checkcars.msgpush;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.creditease.checkcars.AppStartActivity;
import com.creditease.checkcars.R;
import com.creditease.checkcars.data.db.CarOrderUtil;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.msgpush.bean.CommentsNoti;
import com.creditease.checkcars.msgpush.bean.NotiFactory;
import com.creditease.checkcars.msgpush.bean.NotiObj;
import com.creditease.checkcars.msgpush.bean.OrderNoti;
import com.creditease.checkcars.msgpush.bean.OrderReAssignNoti;
import com.creditease.checkcars.msgpush.bean.VersionNoti;
import com.creditease.checkcars.net.ver.VersionUpdateManager;
// import com.umeng.common.message.Log;
import com.creditease.checkcars.ui.act.NotifyMsgActivity;

/**
 * 信息处理
 *
 * @author 子龍
 * @function
 * @date 2015年7月24日
 * @company CREDITEASE
 */
public class MessageHandler extends Handler
{

    // private static String TAG = MessageHandler.class.getSimpleName();

    /**
     * 订单更新action
     */
    public static final String ACTION_BROADCAST_ORDERS_UPDATE = "action_order_update";

    /**
     * 评论更新action
     */
    public static final String ACTION_BROADCAST_COMMENTS_UPDATE = "action_comments_update";

    private static MessageHandler mh;
    private Context context;
    private NotificationManager mNotifyManager;
    private static final int NOTI_ID = 11;// 通知栏消息ID

    private KeyguardManager mKeyguardManager;
    /**
     * 存储多条推送消息，在屏幕锁屏时显示
     */
    public static ArrayList< NotiObj > mLists = new ArrayList< NotiObj >();

    private MessageHandler(Context context)
    {
        this.context = context;
        mNotifyManager = ( NotificationManager ) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mKeyguardManager = ( KeyguardManager ) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    /**
     * 需要在主线程里调用
     * <p/>
     * 上午11:56:13
     */
    public static void createHandler(Context context)
    {
        if ( mh == null )
        {
            mh = new MessageHandler(context);
        }
    }

    public static MessageHandler getMH()
    {
        return mh;
    }

    /**
     * 解析|处理消息
     *
     * @param custom 上午11:53:20
     */
    public void dealMessage(String custom)
    {
        NotiObj noti = null;
        try
        {
            noti = NotiFactory.factory(custom);
            if ( (noti != null) && !noti.toIds.contains(SharePrefenceManager.getAppraiserId(context))
                    && !noti.toIds.contains(SharePrefenceManager.getUserId(context))
                    && !noti.toIds.contains(SharePrefenceManager.getSalerId(context)) )
            {
                return;
            }
            showNotiMessage(noti);
            // 判断是否锁屏
            if ( !isScreenLock() )
            {
                if ( noti instanceof OrderNoti )
                {
                    // 订单更新
                    Message m = mh.obtainMessage(NotiFactory.TYPE_MSG_UPDATE_ORDERS);
                    mh.sendMessage(m);
                } else if ( noti instanceof CommentsNoti )
                {
                    // 评论更新
                    Message m = mh.obtainMessage(NotiFactory.TYPE_MSG_UPDATE_COMMENTS);
                    mh.sendMessage(m);
                } else if ( noti instanceof VersionNoti )
                {
                    // 版本更新
                    Message m = mh.obtainMessage(NotiFactory.TYPE_MSG_UPDATE_VERSION);
                    mh.sendMessage(m);
                } else if ( noti instanceof OrderReAssignNoti )
                {
                    // 订单重分配
                    Message m = mh.obtainMessage(NotiFactory.TYPE_MSG_UPDATE_ORDER_REASSIGN);
                    m.obj = noti;
                    mh.sendMessage(m);
                }
            } else
            {
                if ( noti != null )
                {
                    mLists.add(noti);
                }
                if ( mLists.size() > 0 )
                {
                    Intent intent = new Intent(context, NotifyMsgActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putParcelableArrayListExtra("msglist", mLists);
                    context.startActivity(intent);
                }
            }


        } catch ( CEMsgPushExpection e )
        {
            e.printStackTrace();
        }
    }

    /**
     * 当有新的订单分配和有新版本需要更新时在通知栏显示消息
     *
     * @param noti
     */
    private void showNotiMessage(NotiObj noti)
    {
        // if (((noti instanceof OrderNoti) && (noti.title != null) && noti.title.equals(context
        // .getString(R.string.str_carorder_new_order))) || (noti instanceof VersionNoti)) {
        Message m = mh.obtainMessage(0);
        m.obj = noti;
        mh.sendMessage(m);
        // }
    }

    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);
        int what = msg.what;
        switch ( what )
        {
            case 0:
                // 显示通知栏
                Object obj = msg.obj;
                if ( obj instanceof NotiObj )
                {
                    NotiObj n = ( NotiObj ) msg.obj;
                    if ( n.showNoti() )
                    {
                        showNoti(n);
                    }
                }
                break;
            case NotiFactory.TYPE_MSG_UPDATE_ORDERS:
                // 订单更新
                Intent intent = new Intent(ACTION_BROADCAST_ORDERS_UPDATE);
                intent.putExtra("type", 0);
                context.sendBroadcast(intent);
                break;
            case NotiFactory.TYPE_MSG_UPDATE_COMMENTS:
                // TODO 评论更新
                break;
            case NotiFactory.TYPE_MSG_UPDATE_VERSION:
                VersionUpdateManager.getInstance().checkVersionWS(true, context);
                break;
            case NotiFactory.TYPE_MSG_UPDATE_ORDER_REASSIGN:
                OrderReAssignNoti noti = ( OrderReAssignNoti ) msg.obj;
                if ( !TextUtils.isEmpty(noti.orderId) )
                {
                    try
                    {
                        CarOrderUtil.getUtil(context).deleteOrder(noti.orderId);
                        Intent intent2 = new Intent(ACTION_BROADCAST_ORDERS_UPDATE);
                        intent2.putExtra("type", 1);
                        context.sendBroadcast(intent2);
                    } catch ( CEDbException e )
                    {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示通知栏
     *
     * @param msg 下午7:16:12
     */
    public void showNoti(NotiObj msg)
    {
        // Log.a(TAG, TAG + ":showNoti=showNoti");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context).setDefaults(Notification.DEFAULT_ALL);
        // RemoteViews myNotificationView = new RemoteViews(
        // context.getPackageName(), R.layout.notification_view);
        // myNotificationView.setTextViewText(R.id.notification_title,
        // msg.title);
        // myNotificationView.setTextViewText(R.id.notification_text,
        // msg.content);
        // myNotificationView.setImageViewResource(R.id.notification_large_icon,
        // R.drawable.ic_launcher);
        // myNotificationView.setImageViewResource(R.id.notification_small_icon,
        // R.drawable.ic_launcher);
        builder.setContentTitle(context.getString(R.string.app_name)).setContentText(msg.content)
                .setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true);
        Intent intent = new Intent();
        intent.setClass(context, AppStartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        // builder.setContent(myNotificationView).setAutoCancel(true);
        Notification mNotification = builder.build();
        // 由于Android
        // v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
        // mNotification.contentView = myNotificationView;
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotifyManager.notify(NOTI_ID, mNotification);
    }


    @SuppressLint( "NewApi" )
    private boolean isScreenLock()
    {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
        {
            // Android4.1 之后添加的新方法
            return mKeyguardManager.isKeyguardLocked();
        } else
        {
            return mKeyguardManager.inKeyguardRestrictedInputMode();
        }
    }

}

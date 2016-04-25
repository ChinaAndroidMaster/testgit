package com.creditease.checkcars.ui.widget;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creditease.checkcars.AppStartActivity;
import com.creditease.checkcars.R;
import com.creditease.checkcars.msgpush.bean.NotiObj;
import com.creditease.checkcars.ui.act.NotifyMsgActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;


@SuppressLint( "InlinedApi" )
@SuppressWarnings( "deprecation" )
public class NotifyMsgItemLayout extends RelativeLayout
{
    private LayoutInflater mInflater;
    private RelativeLayout mRootView;
    private ImageView mLargerIcon;
    private TextView mNotiTitle;
    private TextView mNotiDesc;
    private TextView mNotiCustom;
    private ImageView mSmallIcon;
    private NotiObj mNotiObj;
    private Context mContext;

    public NotifyMsgItemLayout(Context context)
    {
        super(context);
    }

    public NotifyMsgItemLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public NotifyMsgItemLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    public NotifyMsgItemLayout(Context context, NotiObj notiObj)
    {
        this(context);
        this.mContext = context;
        this.mNotiObj = notiObj;
        init(context);
    }

    private void init(Context context)
    {
        mInflater = LayoutInflater.from(context);
        mRootView = ( RelativeLayout ) mInflater.inflate(R.layout.notification_view, this);
        mLargerIcon = ( ImageView ) mRootView.findViewById(R.id.notification_large_icon);
        mNotiTitle = ( TextView ) mRootView.findViewById(R.id.notification_title);
        mNotiDesc = ( TextView ) mRootView.findViewById(R.id.notification_text);
        mNotiCustom = ( TextView ) mRootView.findViewById(R.id.notification_custom);
        mSmallIcon = ( ImageView ) mRootView.findViewById(R.id.notification_small_icon);
        mNotiCustom.setVisibility(View.GONE);
        mSmallIcon.setVisibility(View.GONE);
        mNotiTitle.setTextColor(getResources().getColor(R.color.white));
        mNotiDesc.setTextColor(getResources().getColor(R.color.white));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(70));
        setLayoutParams(params);
        if ( mNotiObj != null )
        {
            mNotiTitle.setText(mNotiObj.title);
            mNotiDesc.setText(mNotiObj.content);
        }
        LayoutParams imageParams = new LayoutParams(dip2px(50), dip2px(50));
        imageParams.leftMargin = 20;
        mLargerIcon.setLayoutParams(imageParams);
        mLargerIcon.setImageResource(R.drawable.ic_launcher);

        this.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                KeyguardManager keyguardManager =
                        ( KeyguardManager ) mContext.getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
                keyguardLock.disableKeyguard();
                dealMessage();
                (( BaseActivity ) getContext()).closeOneAct(NotifyMsgActivity.class);
            }
        });
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue)
    {
        final float scale = getResources().getDisplayMetrics().density;
        return ( int ) ((dpValue * scale) + 0.5f);
    }

    /**
     * 处理消息事件
     */
    private void dealMessage()
    {
        if ( !isRunningForeground(mContext) )
        {
            startApp(mContext);
        }
    }


    /**
     * 启动app
     *
     * @param context
     */
    private void startApp(Context context)
    {
        Intent intent = new Intent(context, AppStartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * 师傅看车是否在前台运行
     *
     * @param context
     * @return
     */
    private boolean isRunningForeground(Context context)
    {
        ActivityManager am = ( ActivityManager ) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if ( cn != null )
        {
            String currentPackageName = cn.getPackageName();
            if ( !TextUtils.isEmpty(currentPackageName)
                    && currentPackageName.equals(context.getPackageName()) )
            {
                return true;
            }
        }
        return false;
    }

}

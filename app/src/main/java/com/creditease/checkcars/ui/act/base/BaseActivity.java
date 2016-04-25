/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.act.base;

import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.creditease.checkcars.BaseApplication;
import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.UserCore;
import com.creditease.checkcars.data.db.UserUtil;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.msgpush.CEMsgPushExpection;
import com.creditease.checkcars.msgpush.MsgPushManager;
import com.creditease.checkcars.tools.NetWorkUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年8月6日
 * @company CREDITEASE
 */
@SuppressLint( "ClickableViewAccessibility" )
public abstract class BaseActivity extends FragmentActivity
{
    /**
     * 前台
     */
    public static final int RUN_STATE_FRONT = 1;
    /**
     * 后台运行
     */
    public static final int RUN_STATE_ONBACKGROUNDER = 2;
    /**
     * 销毁
     */
    public static final int RUN_STATE_FINISHED = 3;
    public static final int RUN_STATE_NONE = -1;
    /**
     * 屏幕的宽度、高度、密度
     */
    public static int mScreenWidth;
    public static int mScreenHeight;
    public int run_state = RUN_STATE_NONE;
    protected BaseApplication mApplication;
    protected NetWorkUtils mNetWorkUtils;
    protected float mDensity;
    protected double mLatitude;
    protected double mLongitude;
    protected NetWorkUtils netWorkUtils = new NetWorkUtils(this);

    public static BaseActivity getCurrentActivity()
    {
        return ActStack.getStack().currentActivity();
    }

    public static void launch(Context c, Class< ? > activityClazz, Intent intent)
    {
        if ( intent == null )
        {
            intent = new Intent();
        }
        intent.setClass(c, activityClazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addActivity(getContext());
        umeng();
        init();
        initViews();
        initData();
        initEvents();
        View root = getWindow().getDecorView();
        setupUI(root);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        run_state = RUN_STATE_FRONT;
        if ( netWorkUtils.getConnectState().equals(NetWorkUtils.NetWorkState.NONE) )
        {
            showToast(R.string.net_error);
        } else
        {
            // 重启动 消息推送
            MsgPushManager.getManager(getContext()).reEnableUmengPush();
        }
    }

    @Override
    public void onPause()
    {
        run_state = RUN_STATE_ONBACKGROUNDER;
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        run_state = RUN_STATE_ONBACKGROUNDER;
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        run_state = RUN_STATE_FINISHED;
    }

    private void init()
    {
        run_state = RUN_STATE_FRONT;
        mApplication = ( BaseApplication ) getApplication();
        mNetWorkUtils = new NetWorkUtils(this);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
    }

    /**
     * 初始化视图
     **/
    protected abstract void initViews();

    /**
     * 初始化数据
     * <p/>
     * 上午11:32:16
     */
    protected abstract void initData();

    /**
     * 初始化事件
     **/
    protected abstract void initEvents();

    public abstract BaseActivity getContext();

    /**
     * Activity入栈
     *
     * @param act
     */
    public void addActivity(BaseActivity act)
    {
        if ( act != null )
        {
            ActStack.getStack().addActivityToStack(act);
        }
    }

    /**
     * 应用退出
     */
    public void appExit()
    {
        ActStack.getStack().appExit(this);
        System.gc();
    }

    public void closeOnActRequestCode(BaseActivity act, int requestCode)
    {
        ActStack.getStack().finishActivity(act, requestCode);
        overridePendingTransition(0, R.anim.push_right_out);
    }

    public void closeOneAct(BaseActivity act)
    {
        ActStack.getStack().finishActivity(act);
        overridePendingTransition(0, R.anim.push_right_out);
    }

    /**
     * 关闭某个activity
     *
     * @param cls
     */
    public void closeOneAct(Class< ? > cls)
    {
        ActStack.getStack().finishActivity(cls);
        overridePendingTransition(0, R.anim.push_right_out);

    }

    /**
     * 默认退出
     **/
    protected void defaultFinish()
    {
        super.finish();
    }

    @Override
    public void finish()
    {
        ActStack.getStack().removeActivityFromStack(this);
        super.finish();
    }

    @Override
    public void finishActivity(int requestCode)
    {
        ActStack.getStack().removeActivityFromStack(this);
        super.finishActivity(requestCode);
    }

    public void finishAllActivity()
    {
        ActStack.getStack().finishAllActivity();
    }

    /**
     * 关闭除了该类名意外的所有Activity
     *
     * @param cls
     */
    public void finishAllActivityExceptOne(Class< ? > cls)
    {
        ActStack.getStack().finishAllActivityExceptOne(cls);
    }

    /**
     * 关闭最外面的Activity
     */
    public void finishFrontActivity()
    {
        ActStack.getStack().finishFrontActivity();
    }

    /**
     * 通过类名获取Activity
     *
     * @param cls
     * @return
     */
    public < T > BaseActivity getActivity(Class< ? > cls)
    {
        return ActStack.getStack().getActivityFromStack(cls);
    }


    public void hideSoftKeyboard()
    {
        View currentFocus = getCurrentFocus();
        if ( currentFocus != null )
        {
            InputMethodManager imm = ( InputMethodManager ) getSystemService(INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    protected boolean isNetWork()
    {
        return !netWorkUtils.getConnectState().equals(NetWorkUtils.NetWorkState.NONE);
    }

    protected boolean matchEmail(String text)
    {
        if ( Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text).matches() )
        {
            return true;
        }
        return false;
    }

    protected boolean matchName(String text)
    {
        // 姓名验证
        String patenName = "^[\u4e00-\u9fa5]{1,10}[·.]{0,1}[\u4e00-\u9fa5]{1,10}$";
        if ( Pattern.compile(patenName).matcher(text).matches() )
        {
            return true;
        }
        return false;
    }

    protected boolean matchPhone(String text)
    {
        // "^((13[0-9])|(15[0-9])|(18[0-9]))\d{8}$";
        String str = "^((13[0-9])|(15[0-9])|(18[0-9])|(177))\\d{8}$";
        if ( Pattern.compile(str).matcher(text).matches() )
        {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed()
    {
        closeOneAct(this);
        super.onBackPressed();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        // Log.e("BaseActivity", "onLowMemory");
        // Util.memoryPanic();
    }


    public void setupUI(View view)
    {

        // Set up touch listener for non-text box views to hide keyboard.
        if ( !(view instanceof EditText) )
        {

            view.setOnTouchListener(new OnTouchListener()
            {

                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    hideSoftKeyboard();
                    return false;
                }

            });
        }

        // If a layout container, iterate over children and seed recursion.
        if ( view instanceof ViewGroup )
        {

            for ( int i = 0; i < (( ViewGroup ) view).getChildCount(); i++ )
            {

                View innerView = (( ViewGroup ) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    /**
     * 含有标题和内容的对话框
     **/
    protected AlertDialog showAlertDialog(String title, String message)
    {
        AlertDialog alertDialog =
                new AlertDialog.Builder(this).setTitle(title).setMessage(message).show();
        return alertDialog;
    }

    /**
     * 含有标题、内容、图标、两个按钮的对话框
     **/
    protected AlertDialog showAlertDialog(String title, String message, int icon,
                                          String positiveText, DialogInterface.OnClickListener onPositiveClickListener,
                                          String negativeText, DialogInterface.OnClickListener onNegativeClickListener)
    {
        AlertDialog alertDialog =
                new AlertDialog.Builder(this).setTitle(title).setMessage(message).setIcon(icon)
                        .setPositiveButton(positiveText, onPositiveClickListener)
                        .setNegativeButton(negativeText, onNegativeClickListener).show();
        return alertDialog;
    }

    /**
     * 含有标题、内容、两个按钮的对话框
     **/
    protected AlertDialog showAlertDialog(String title, String message, String positiveText,
                                          DialogInterface.OnClickListener onPositiveClickListener, String negativeText,
                                          DialogInterface.OnClickListener onNegativeClickListener)
    {
        AlertDialog alertDialog =
                new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                        .setPositiveButton(positiveText, onPositiveClickListener)
                        .setNegativeButton(negativeText, onNegativeClickListener).show();
        return alertDialog;
    }

    public void showSoftKeyboard()
    {
        View currentFocus = getCurrentFocus();
        if ( currentFocus != null )
        {
            InputMethodManager imm = ( InputMethodManager ) getSystemService(INPUT_METHOD_SERVICE);

            // imm.hideSoftInputFromWindow(currentFocus.getWindowToken(),
            // InputMethodManager.HIDE_NOT_ALWAYS);
            imm.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 短暂显示Toast提示(来自res)
     **/
    public void showToast(int resId)
    {
        ZLToast.getToast().showToast(this, resId);
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    public void showToast(String text)
    {
        ZLToast.getToast().showToast(this, text);
    }

    public void showToast(String text, int duration)
    {
        final Toast t = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        t.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                t.cancel();
            }
        }, duration);
    }

    /**
     * 通过Class跳转界面
     **/
    protected void startActivity(Class< ? > cls)
    {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(Class< ? > cls, Bundle bundle)
    {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if ( bundle != null )
        {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Action跳转界面
     **/
    protected void startActivity(String action)
    {
        startActivity(action, null);
    }

    /**
     * 含有Bundle通过Action跳转界面
     **/
    protected void startActivity(String action, Bundle bundle)
    {
        Intent intent = new Intent();
        intent.setAction(action);
        if ( bundle != null )
        {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * umeng统计
     * <p/>
     * 下午5:20:00
     */
    private void umeng()
    {
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
        MsgPushManager.getManager(getContext()).appStart();
    }


    /**
     * 更新消息推送数据
     * <p/>
     * 下午4:35:22
     */
    protected void updateMsgPushData()
    {
        try
        {
            MsgPushManager.getManager(getContext()).updateMsgPushUserData();
        } catch ( CEMsgPushExpection e )
        {
            String userId = SharePrefenceManager.getUserId(getApplicationContext());
            // 标签
            MsgPushManager.getManager(getContext()).setUserAlias(userId);

            try
            {
                UserCore user = UserUtil.getUtil(getApplicationContext()).getUser(userId);
                if ( user == null )
                {
                    return;
                }
                if ( user.judgeUserType(UserCore.USER_TYPE_APPRAISER) )
                {
                    MsgPushManager.getManager(getContext()).addUserTags(MsgPushManager.MSGPUSH_TAG_APPRAISER);
                }
                if ( user.judgeUserType(UserCore.USER_TYPE_MARKET) )
                {
                    MsgPushManager.getManager(getContext()).addUserTags(MsgPushManager.MSGPUSH_TAG_SALER);
                }
            } catch ( CEDbException e1 )
            {
            }
        }
    }

}

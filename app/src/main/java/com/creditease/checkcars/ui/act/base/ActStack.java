package com.creditease.checkcars.ui.act.base;

import java.util.Stack;

import android.content.Context;

public class ActStack
{
    public static Stack< BaseActivity > actStack = new Stack< BaseActivity >();
    private static ActStack stack;

    private ActStack()
    {
    }

    public static ActStack getStack()
    {
        if ( stack == null )
        {
            stack = new ActStack();
        }
        return stack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivityToStack(BaseActivity baseActivity)
    {
        if ( actStack == null )
        {
            actStack = new Stack< BaseActivity >();
        }
        actStack.add(baseActivity);
    }

    /**
     * 退出应用程序
     */
    public void appExit(Context context)
    {
        try
        {
            finishAllActivity();
            System.exit(0);
        } catch ( Exception e )
        {
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public BaseActivity currentActivity()
    {
        if ( actStack.size() <= 0 )
        {
            return null;
        }
        BaseActivity baseActivity = actStack.lastElement();
        return baseActivity;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(BaseActivity baseActivity)
    {
        if ( baseActivity != null )
        {
            if ( actStack.contains(baseActivity) )
            {
                actStack.remove(baseActivity);
            }
            baseActivity.finish();
            baseActivity = null;
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(BaseActivity baseActivity, int requestCode)
    {
        if ( baseActivity != null )
        {
            if ( actStack.contains(baseActivity) )
            {
                actStack.remove(baseActivity);
            }
            baseActivity.finishActivity(requestCode);
            baseActivity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class< ? > cls)
    {
        for ( int i = 0; i < actStack.size(); i++ )
        {
            BaseActivity baseActivity = actStack.get(i);
            if ( (null != baseActivity) && baseActivity.getClass().equals(cls) )
            {
                finishActivity(baseActivity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity()
    {
        while ( actStack.size() > 0 )
        {
            actStack.get(0).finish();
        }
    }

    /**
     * 除了这个Activity结束其他所有Activity
     */
    public void finishAllActivityExceptOne(Class< ? > cls)
    {
        while ( actStack.size() > 1 )
        {
            BaseActivity baseActivity = actStack.get(0);
            if ( (null != baseActivity) && !baseActivity.getClass().equals(cls) )
            {
                baseActivity.finish();
            }
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishFrontActivity()
    {
        BaseActivity baseActivity = actStack.lastElement();
        finishActivity(baseActivity);
    }

    public < T > BaseActivity getActivityFromStack(Class< ? > cls)
    {
        for ( BaseActivity BaseActivity : actStack )
        {
            if ( BaseActivity.getClass().equals(cls) )
            {
                return BaseActivity;
            }
        }
        return null;
    }

    /**
     * 移除
     *
     * @param baseActivity
     */
    public void removeActivityFromStack(BaseActivity baseActivity)
    {
        if ( (baseActivity != null) && actStack.contains(baseActivity) )
        {
            actStack.remove(baseActivity);
            baseActivity = null;
        }
    }
}

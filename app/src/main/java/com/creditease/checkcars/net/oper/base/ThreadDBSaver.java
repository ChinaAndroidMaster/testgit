package com.creditease.checkcars.net.oper.base;

import android.util.Log;

import com.creditease.checkcars.exception.CERequestException;

/**
 * 保存数据线程
 *
 * @author 子龍
 * @date 2014年12月29日
 * @company 宜信
 */
public class ThreadDBSaver extends Thread
{

    private IDataSaver saver;

    public void excute(IDataSaver saver)
    {
        this.saver = saver;
        start();
    }

    @Override
    public void run()
    {
        if ( saver != null )
        {
            try
            {
                saver.saver();
            } catch ( CERequestException e )
            {
                Log.e("error", e == null ? "ThreadDBSaver error" : e.getMessage());
            }
        }
        super.run();
    }

    public void setSaver(IDataSaver saver)
    {
        this.saver = saver;
    }

}

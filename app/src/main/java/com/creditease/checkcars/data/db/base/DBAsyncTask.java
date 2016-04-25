package com.creditease.checkcars.data.db.base;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.creditease.checkcars.exception.CEDbException;

public class DBAsyncTask< T > extends AsyncTask< Object, Integer, Bundle >
{
    private List< T > tList;

    private DBOperCallBack callback;
    private DBFindCallBack< T > findCallback;
    private int operId;
    private Context context;

    public DBAsyncTask(Context context, DBFindCallBack< T > findCallback)
    {
        this.findCallback = findCallback;
        this.context = context;
    }

    public DBAsyncTask(Context context, DBOperCallBack callback)
    {
        this.context = context;
        this.callback = callback;
    }

    public DBAsyncTask(Context context, int operId, DBFindCallBack< T > findCallback)
    {
        this.context = context;
        this.operId = operId;
        this.findCallback = findCallback;
    }

    public DBAsyncTask(Context context, int operId, DBOperCallBack callback)
    {
        this.operId = operId;
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected Bundle doInBackground(Object... params)
    {
        DbHelper helper = DbHelper.getDBHelper(context);
        if ( callback != null )
        {
            try
            {
                return callback.doDBOperation(helper, operId);
            } catch ( CEDbException e )
            {
            }
        }
        if ( findCallback != null )
        {
            try
            {
                tList = findCallback.doDBOperation(helper, operId);
            } catch ( CEDbException e )
            {
                Log.e("DBLOG", e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bundle result)
    {
        if ( callback != null )
        {
            callback.dataCallBack(operId, result);
        }
        if ( findCallback != null )
        {
            findCallback.dataCallBack(operId, tList);
        }
        super.onPostExecute(result);
    }

}

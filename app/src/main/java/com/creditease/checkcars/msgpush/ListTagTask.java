package com.creditease.checkcars.msgpush;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.umeng.message.PushAgent;

public class ListTagTask extends AsyncTask< Void, Void, List< String > >
{

    private PushAgent mPushAgent;

    public ListTagTask(PushAgent mPushAgent)
    {
        this.mPushAgent = mPushAgent;
    }

    @Override
    protected List< String > doInBackground(Void... params)
    {
        List< String > tags = new ArrayList< String >();
        try
        {
            tags = mPushAgent.getTagManager().list();
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        return tags;
    }

    @Override
    protected void onPostExecute(List< String > result)
    {
    }
}

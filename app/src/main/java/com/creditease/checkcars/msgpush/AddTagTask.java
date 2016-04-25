package com.creditease.checkcars.msgpush;

import android.content.Context;
import android.os.AsyncTask;

import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.umeng.message.PushAgent;
import com.umeng.message.tag.TagManager;

public class AddTagTask extends AsyncTask< Void, Void, String >
{

    private String tagString;
    private String[] tags;
    private PushAgent mPushAgent;
    private Context context;

    public AddTagTask(PushAgent mPushAgent, String tag, Context context)
    {
        this.mPushAgent = mPushAgent;
        tagString = tag;
        tags = tagString.split(",");
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params)
    {
        try
        {
            TagManager.Result result = mPushAgent.getTagManager().add(tags);
            return result.toString();
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        return "fail";
    }

    @Override
    protected void onPostExecute(String result)
    {
        if ( "fail".equals(result) )
        {

        } else
        {
            SharePrefenceManager.setMsgPushTag(context, tags);
            try
            {
                MsgPushManager.getManager(context).updateMsgPushUserData();
            } catch ( CEMsgPushExpection e )
            {
                e.printStackTrace();
            }
        }
    }
}

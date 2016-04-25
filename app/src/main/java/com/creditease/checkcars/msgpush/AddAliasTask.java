package com.creditease.checkcars.msgpush;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.umeng.message.PushAgent;

public class AddAliasTask extends AsyncTask< Void, Void, Boolean >
{
    private Context context;
    private String alias;
    private String aliasType;
    private String oldAlias;
    private String oldAliasType;
    private PushAgent mPushAgent;

    public AddAliasTask(PushAgent mPushAgent, String aliasString, String aliasTypeString,
                        Context context)
    {
        this.mPushAgent = mPushAgent;
        alias = aliasString;
        aliasType = aliasTypeString;
        this.context = context;
    }

    public AddAliasTask(PushAgent mPushAgent, String aliasString, String aliasTypeString,
                        String oldAliasString, String oldAliasTypeString, Context context)
    {
        this.mPushAgent = mPushAgent;
        alias = aliasString;
        aliasType = aliasTypeString;
        oldAlias = oldAliasString;
        oldAliasType = oldAliasTypeString;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        try
        {
            if ( !TextUtils.isEmpty(oldAlias) )
            {
                mPushAgent.removeAlias(oldAlias, oldAliasType);
            }
            return mPushAgent.addAlias(alias, aliasType);
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        if ( result )
        {
            SharePrefenceManager.setMsgPushAliasAndType(context, new String[]{alias, aliasType});
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

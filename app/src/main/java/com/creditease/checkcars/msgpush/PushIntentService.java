package com.creditease.checkcars.msgpush;


import org.android.agoo.client.BaseConstants;
import org.android.agoo.client.BaseRegistrar;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

// import com.umeng.common.message.Log;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

/**
 * Developer defined push intent service. Remember to call
 * {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}.
 *
 * @author lucas
 */
public class PushIntentService extends UmengBaseIntentService
{
    // private static final String TAG = "PushIntentService";

    public String getDeviceToken(Context context)
    {
        return BaseRegistrar.getRegistrationId(context);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    protected void onMessage(Context context, Intent intent)
    {
        super.onMessage(context, intent);
        MessageHandler.createHandler(context);
        try
        {
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            // Log.d(TAG, TAG + ":dealMessage=" + msg.custom);
            MessageHandler.getMH().dealMessage(msg.custom);
        } catch ( Exception e )
        {
            // Log.e(TAG, e.getMessage());
        }
    }

}

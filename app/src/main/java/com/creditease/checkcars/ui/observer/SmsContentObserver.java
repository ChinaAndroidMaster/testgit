/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.observer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

/**
 * @author noah.zheng
 */
public class SmsContentObserver extends ContentObserver
{

    public static final String SMS_URI_INBOX = "content://sms/inbox";
    private static final String SMS_PHONE = "1069046042318982";// zhouzhilong
    private Activity activity = null;
    private String smsContent = "";
    private EditText verifyText = null;

    // add

    public SmsContentObserver(Activity activity, Handler handler, EditText verifyText)
    {
        super(handler);
        this.activity = activity;
        this.verifyText = verifyText;
    }

    @SuppressWarnings( "deprecation" )
    @Override
    public void onChange(boolean selfChange)
    {
        super.onChange(selfChange);
        Cursor cursor = null;// 光标
        // 读取收件箱中指定号码的短信
        try
        {
            cursor =
                    activity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[]{"_id", "address", "body",
                            "read"}, "address=? and read=?", new String[]{SMS_PHONE, "0"}, "date desc");
            if ( cursor != null )
            {// 如果短信为未读模式
                cursor.moveToFirst();
                if ( cursor.moveToFirst() )
                {
                    String smsbody = cursor.getString(cursor.getColumnIndex("body"));
                    String regEx = "[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(smsbody.toString());
                    smsContent = m.replaceAll("").trim().toString();
                    verifyText.setText(smsContent);
                }
            }
        } catch ( Exception e )
        {
        }

    }
}

package com.creditease.checkcars.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

@SuppressLint( "SimpleDateFormat" )
public class Utils
{
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    public static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    public static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    private static String digitReg = "^[-+]?\\d{0,}$";

    /**
     * 计算内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param c
     * @return
     */
    public static long calculateLength(CharSequence c)
    {
        double len = 0;
        for ( int i = 0; i < c.length(); i++ )
        {
            int tmp = c.charAt(i);
            // if ((tmp > 0 && tmp < 48) || (tmp > 57 && tmp < 65) || tmp > 90
            // && tmp < 127) {
            if ( ((tmp > 0) && (tmp < 65)) || ((tmp > 90) && (tmp < 127)) )
            {
                len += 0.5;
            } else
            {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 将输入流转换为字符串
     *
     * @param input
     * @return
     */
    public static String convertStreamToString(InputStream input)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder builder = new StringBuilder();
        String line = null;
        try
        {
            while ( (line = reader.readLine()) != null )
            {
                builder.append(line + "\n");
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                input.close();
            } catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    @SuppressLint( "NewApi" )
    @SuppressWarnings( "deprecation" )
    public static void copy(String content, Context context)
    {
        // 得到剪贴板管理器
        int ver = getSDKVersionNumber();
        if ( ver > 11 )
        {
            android.content.ClipboardManager cmb =
                    ( android.content.ClipboardManager ) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content);
        } else
        {
            android.text.ClipboardManager cmb =
                    ( android.text.ClipboardManager ) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content);
        }
    }

    public static String getAmount(String amount)
    {
        if ( TextUtils.isEmpty(amount) )
        {
            return "";
        }
        if ( amount.contains(".") )
        {
            amount = amount.split("\\.")[0];
        }
        // 保证为数字
        if ( !Pattern.compile(digitReg).matcher(amount).matches() )
        {
            return "";
        }
        long totalTrade1 = Long.parseLong(amount);
        if ( totalTrade1 >= 100 )
        {
            String a = new DecimalFormat("##,###,###,#####").format(totalTrade1);
            int len = a.length();
            String xx = a.substring(0, len - 2) + "." + a.substring(len - 2);
            return xx;
        } else if ( (totalTrade1 >= 10) && (totalTrade1 < 100) )
        {
            return "0." + totalTrade1;
        } else if ( (totalTrade1 >= 0) && (totalTrade1 < 10) )
        {
            return "0.0" + totalTrade1;
        } else
        {
            String a = new DecimalFormat("##,###,###,#####").format(totalTrade1);
            int len = a.length();
            String xx = a.substring(0, len - 2) + "." + a.substring(len - 2);
            return xx;
        }
    }

    public static String getAmount2(String amount)
    {
        String FORMAT_DATA = "#,###,###,###,###.##";
        Double d = Double.parseDouble(amount);
        return new DecimalFormat(FORMAT_DATA).format(d / 100.0d);
    }

    public static int getApplicatinUserId(Context context)
    {

        try
        {
            ApplicationInfo ai =
                    context.getPackageManager().getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            return ai.uid;
        } catch ( NameNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static float getFloatAmount(String amount)
    {
        if ( TextUtils.isEmpty(amount) )
        {
            return 0;
        }
        amount = amount.replace(",", "");
        return Float.parseFloat(amount);
    }

    // 获取AppKey
    public static String getMetaValue(Context context, String metaKey)
    {
        Bundle metaData = null;
        String apiKey = null;
        if ( (context == null) || (metaKey == null) )
        {
            return null;
        }
        try
        {
            ApplicationInfo ai =
                    context.getPackageManager().getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if ( null != ai )
            {
                metaData = ai.metaData;
            }
            if ( null != metaData )
            {
                apiKey = metaData.getString(metaKey);
            }
        } catch ( NameNotFoundException e )
        {

        }
        return apiKey;
    }

    @SuppressWarnings( "deprecation" )
    public static int getSDKVersionNumber()
    {

        int sdkVersion;
        try
        {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);

        } catch ( NumberFormatException e )
        {

            sdkVersion = 0;
        }
        return sdkVersion;
    }

    public static boolean isIntegerDate(String date)
    {
        if ( TextUtils.isEmpty(date) )
        {
            return false;
        }
        String digitReg = "^\\d{0,}$";
        Pattern p = Pattern.compile(digitReg);
        Matcher matcher = p.matcher(date);
        return matcher.matches();
    }

    public static boolean isTopActivity(String activityName, Context context)
    {
        boolean isTop = false;
        ActivityManager am = ( ActivityManager ) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if ( cn.getClassName().contains(activityName) )
        {
            isTop = true;
        }
        return isTop;
    }

    public static boolean isUrl(String url)
    {
        if ( url == null )
        {
            return false;
        }
        String regEx =
                "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
                        + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
                        + "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
                        + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
                        + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
                        + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
                        + "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
                        + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(url);
        return matcher.matches();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if ( listAdapter == null )
        {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for ( int i = 0, len = listAdapter.getCount(); i < len; i++ )
        {
            View listItem = listAdapter.getView(i, null, listView);
            if ( listItem != null )
            {
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 20;
        listView.setLayoutParams(params);
    }

    /**
     * 时间戳转化成日期 yyyy-MM-dd
     *
     * @param timestamps
     * @return
     */
    public static String timestamps2Date(Long timestamps, String formatStr)
    {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String date = format.format(timestamps);
        return date;
    }


    /**
     * 比较是否有新版本
     *
     * @param currentVersions
     * @param newVersions
     * @return 下午6:17:21
     */
    public static boolean compareHaveNewVersion(String currentVersion, String newVersion)
    {
        if ( TextUtils.isEmpty(currentVersion) || TextUtils.isEmpty(newVersion) )
        {
            return false;
        }
        String[] currentVersions = currentVersion.split("\\.");
        String[] newVersions = newVersion.split("\\.");
        for ( int i = 0; i < newVersions.length; i++ )
        {
            int vi = Integer.parseInt(currentVersions[i]);
            int newVi = Integer.parseInt(newVersions[i]);
            if ( vi == newVi )
            {
                continue;
            } else
            {
                return vi < newVi;
            }
        }
        return false;
    }

}

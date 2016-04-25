package com.creditease.checkcars.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

/**
 * @author 子龍
 */
public class Util
{

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath();

    private static Properties props = null;

    private static String capitalize(String s)
    {
        if ( (s == null) || (s.length() == 0) )
        {
            return "";
        }
        char first = s.charAt(0);
        if ( Character.isUpperCase(first) )
        {
            return s;
        } else
        {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static boolean checkNetWorkStatus(Context context)
    {
        boolean result;
        ConnectivityManager cm =
                ( ConnectivityManager ) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if ( (netinfo != null) && netinfo.isConnected() )
        {
            result = true;
        } else
        {
            result = false;
        }
        return result;
    }

    public static void cleanCache(Context context)
    {
        context.deleteDatabase("webview.db");
        context.deleteDatabase("webviewCache.db");
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

    /**
     * create local report id
     *
     * @param context
     * @return 下午5:55:58
     */
    public static String createReportId(Context context, String appraiserId, String orderId)
    {
        StringBuilder rId = new StringBuilder();
        rId.append(appraiserId).append("_").append(orderId).append("_")
                .append(System.currentTimeMillis());
        return rId.toString();
    }

    /**
     * dip 2 px
     *
     * @param context
     * @param dipValue
     * @return 上午11:37:12
     */
    public static int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return ( int ) (dipValue * scale);
    }

    /**
     * 格式化名字 <br>
     * 用于保存微博图像，截取url的最后一段做为图像文件名
     *
     * @param url
     * @return
     */
    public static String formatName(String url)
    {
        if ( (url == null) || "".equals(url) )
        {
            return url;
        }
        int start = url.lastIndexOf("/");
        int end = url.lastIndexOf(".");
        if ( (start == -1) || (end == -1) )
        {
            return url;
        }
        return url.substring(start + 1, end);
    }

    /**
     * 格式化来源
     *
     * @param name
     * @return
     */
    public static String formatSource(String name)
    {
        if ( (name == null) || "".equals(name) )
        {
            return name;
        }
        int start = name.indexOf(">");
        int end = name.lastIndexOf("<");
        if ( (start == -1) || (end == -1) )
        {
            return name;
        }
        return name.substring(start + 1, end);
    }

    @SuppressLint( "NewApi" )
    public static String getAppSERIAL()
    {
        return Build.SERIAL;
    }

    /**
     * 获取手机的IMIE码作为手机的唯一识别码
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context)
    {
        String imei = (( TelephonyManager ) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return imei;
    }

    public static String getAppVersionName(Context context)
    {
        String versionName = "";
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("com.creditease.checkcars", 0);
            versionName = packageInfo.versionName;
            if ( TextUtils.isEmpty(versionName) )
            {
                return "";
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if ( model.startsWith(manufacturer) )
        {
            return capitalize(model);
        } else
        {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String getDeviceVersion()
    {
        return Build.VERSION.RELEASE;
    }

    public static String getDirNameFromUrl(String url)
    {
        if ( url == null )
        {
            return null;
        }
        char[] chars = url.toCharArray();
        int index = 0;
        for ( int i = chars.length - 1; i >= 0; i-- )
        {
            if ( chars[i] == '/' )
            {
                index = i;
                break;
            }
        }
        if ( index < 4 )
        {
            return "";
        }

        String url2 = url.substring(0, index);
        char[] chars2 = url2.toCharArray();
        int index2 = 0;
        for ( int i = chars2.length - 1; i >= 0; i-- )
        {
            if ( chars2[i] == '/' )
            {
                index2 = i;
                break;
            }
        }

        String dirName = url2.substring(index2 + 1);
        return dirName;
    }

    public static String getFileNameFromUrl(String url)
    {
        if ( url == null )
        {
            return null;
        }
        char[] chars = url.toCharArray();
        int start = 0;
        for ( int i = chars.length - 1; i >= 0; i-- )
        {
            if ( chars[i] == '/' )
            {
                start = i;
                break;
            }
        }
        int end = url.lastIndexOf(".");
        if ( (start < chars.length) && (start < end) && (end < chars.length) )
        {
            return url.substring(start + 1, end);
        } else
        {
            return null;
        }

    }

    public static long getFreeMenorySize()
    {
        long totleM = Runtime.getRuntime().freeMemory();
        return totleM;
    }

    public static Properties getProperties()
    {
        if ( props == null )
        {
            props = new Properties();
        }
        return props;

    }

    public static String getPropertyValue(Context context, String key)
    {
        String strValue = "";
        Properties props = getProperties();
        try
        {
            props.load(context.getAssets().open("config.properties"));
            strValue = props.getProperty(key);
        } catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
        return strValue;
    }

    public static String getPropertyValue(Context context, String key, String dir)
    {
        String strValue = "";

        try
        {
            props = getProperties();
            FileInputStream is =
                    new FileInputStream(new File(path + "/" + dir + "/" + "config.properties"));
            props.load(is);
            strValue = props.getProperty(key);
        } catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
        return strValue;
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

    public static String getStringFromRes(Context context, int resID)
    {
        String cont = null;
        try
        {
            cont = context.getResources().getString(resID);
        } catch ( Exception e )
        {
        }
        return cont;
    }

    public static boolean hasSdcard()
    {
        String state = Environment.getExternalStorageState();
        if ( state.equals(Environment.MEDIA_MOUNTED) )
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * 加亮 @****： 或 @****: 或@****[空格] <br>
     * Html.fromHtml(String) *
     *
     * @param str
     * @return
     */
    public static Spanned highLight(String str)
    {
        Pattern pattern = Pattern.compile("@[^\\s:：]+[:：\\s]");
        Matcher matcher = pattern.matcher(str);
        while ( matcher.find() )
        {
            String m = matcher.group();
            str = str.replace(m, "<font color=Navy>" + m + "</font>");
        }
        return Html.fromHtml(str);
    }

    /**
     * 正则判断是否则数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str)
    {
        if ( str == null )
        {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
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

    /**
     * 加亮 @****： 或 @****: 或@****[空格] <br>
     * SpannableString.setSpan(Object what, int start, int end, int flags)
     *
     * @param text
     * @return
     */
    // 这个和hightLight(String str) 的效率对比?
    public static SpannableString light(CharSequence text)
    {
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile("@[^\\s:：]+[:：\\s]");
        Matcher matcher = pattern.matcher(spannableString);
        while ( matcher.find() )
        {
            spannableString.setSpan(new ForegroundColorSpan(Color.CYAN), matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public static void log_e(String tag, String value)
    {
        Log.e(tag, value);
    }

    public static void log_i(String tag, String value)
    {
        Log.i(tag, value);
    }

    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return ( int ) (pxValue / scale);
    }

    public static void recycleBitmap(Bitmap img)
    {
        if ( (img != null) && !img.isRecycled() )
        {
            img.recycle();
            System.gc();
        }
    }

    private String getAvailMemory(Activity act)
    {// 获取android当前可用内存大小
        ActivityManager am = ( ActivityManager ) act.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(act, mi.availMem);// 将获取的内存大小规格化
    }

    public String getFPS2(Activity act)
    {
        Runtime runtime = Runtime.getRuntime();
        return "总:" + getTotalMemory() + ", " + "可用:" + getAvailMemory(act) + ","
                + (runtime.freeMemory() / 1024) + "M," + (runtime.maxMemory() / 1024) + "M,"
                + ((runtime.totalMemory() / 1024) + "M");
    }

    private String getTotalMemory()
    {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        // String[] arrayOfString;
        // long initial_memory = 0;
        BufferedReader localBufferedReader = null;
        try
        {
            FileReader localFileReader = new FileReader(str1);
            localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            String Str3 = localBufferedReader.readLine();
            if ( (str2 != null) && (str2.length() >= 9) )
            {
                String str4 = str2.substring(str2.length() - 9, str2.length() - 3);
                localBufferedReader.close();
                return "" + (Integer.parseInt(str4) / 1000) + "M" + "," + Str3 + "M";
            } else
            {
                localBufferedReader.close();
            }
        } catch ( IOException e )
        {
            if ( localBufferedReader != null )
            {
                try
                {
                    localBufferedReader.close();
                } catch ( IOException e1 )
                {
                }
            }
        }
        return "";
        // return Formatter.formatFileSize(getBaseContext(), initial_memory);//
        // Byte转换为KB或者MB，内存大小规格化
    }
}

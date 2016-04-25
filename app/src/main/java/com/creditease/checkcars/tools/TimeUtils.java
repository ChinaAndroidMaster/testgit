package com.creditease.checkcars.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.text.format.Time;

/**
 * 时间的工具类
 *
 * @author 子龍
 * @description 判断时间是否过期；时间格式转换
 */
@SuppressLint( "SimpleDateFormat" )
public class TimeUtils
{
    private static String defaultFormat = "yyyy/MM/dd";

    public static long getCurrentLongTime()
    {
        Date d = new Date();
        return d.getTime();
    }

    public static String getCurrentLongTimeStr()
    {
        Date d = new Date();
        long time = d.getTime();
        return time + "";
    }

    @SuppressWarnings( "deprecation" )
    public static String getNextMonthDay()
    {
        int mothDay = 1;// 次月反现日期
        Date now = new Date();
        Time time = new Time();
        time.setToNow();
        if ( time.year <= 2015 )
        {
            switch ( time.month + 1 )
            {
                case 1:
                case 2:
                case 10:
                    mothDay = 3;
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                case 8:
                case 11:
                    mothDay = 2;
                    break;
                case 7:
                    mothDay = 4;
                    break;
                case 9:
                    mothDay = 9;
                    break;
                case 12:
                    mothDay = 5;
                    break;
                default:
                    break;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM", Locale.CHINA);
            now.setMonth(now.getMonth() + 1);
            String nowDateStr = format.format(now);
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try
            {
                Date m = formatter1.parse(nowDateStr + "/" + mothDay + " 00:00:00");
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
                String nextMonth = formatter.format(m);
                return nextMonth;
            } catch ( ParseException e )
            {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    /**
     * @param date
     * @param format
     * @return
     * @description 获取特殊格式时间：如 1小时前
     */
    public static String getTimeStrDifStyle(Date date, String format)
    {
        if ( date == null )
        {
            return "";
        }
        // Time today = new Time("GMT+8");
        // today.setToNow();
        // long interval = today.toMillis(true) - date.getTime();
        Date d = new Date();

        long interval = d.getTime() - date.getTime();
        interval = interval < 0 ? -interval : interval;
        boolean isSameDay = isSameDay(date, d);
        if ( isSameDay )
        {
            if ( interval < (60 * 1000) )
            {
                return "刚刚";
                // return (interval / 1000) + "秒前";
            } else if ( interval < (60 * 60 * 1000) )
            {
                return (interval / (60 * 1000)) + "分钟前";
            } else
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
                return "今天 " + dateFormat.format(date);
            }
        } else
        {
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat((format == null) || "".equals(format) ? defaultFormat : format,
                            Locale.CHINA);
            return dateFormat.format(date);
        }
    }

    /**
     * @param date
     * @param format
     * @return
     * @description 获取特殊格式时间：如 1小时前
     */
    public static String getTimeStrDifStyle(long date, String format)
    {
        return getTimeStrDifStyle(longToDate(date), format);
    }

    /**
     * @param date
     * @param format
     * @return
     * @description 获取特殊格式时间：如 1小时前
     */
    public static String getTimeStrDifStyle(String longDate, String format)
    {
        return getTimeStrDifStyle(longToDate(longDate), format);
    }

    /**
     * 判断是否晚于22：30
     *
     * @return
     */
    public static boolean isAfterSomeTime()
    {
        Date comparandDate = new Date();
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat(defaultFormat, Locale.CHINA);
        String comparandDateStr = format.format(comparandDate);
        try
        {
            SimpleDateFormat FORMATTER1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            comparandDate = FORMATTER1.parse(comparandDateStr + " 22:30:00");
            long compareDateMill = now.getTime();
            long comparandDateMill = comparandDate.getTime();
            long kk = compareDateMill - comparandDateMill;
            if ( (kk > 0) && (kk < (1.5 * 60 * 60 * 1000)) )
            {
                return true;
            }
        } catch ( ParseException e )
        {
            return false;
        }
        return false;
    }

    // private static SimpleDateFormat FORMATTER1 = new SimpleDateFormat(
    // "yyyy/MM/dd HH:mm:ss");

    /**
     * 判断是否过期
     *
     * @param deadline
     * @return
     */
    public static boolean isOverDue(Date deadline)
    {
        Date d = new Date();
        long interval = d.getTime() - deadline.getTime();
        return interval >= 0;
    }

    /**
     * 判断是否过期
     *
     * @param deadline
     * @return
     * @throws ParseException
     */
    public static boolean isOverDue(long deadline)
    {
        return isOverDue(new Date(deadline));
    }

    /**
     * 判断是否过期
     *
     * @param deadline
     * @return
     * @throws ParseException
     */
    public static boolean isOverDue(String deadline)
    {
        if ( TextUtils.isEmpty(deadline) )
        {
            return false;
        }
        long milliseconds = Long.parseLong(deadline);
        return isOverDue(milliseconds);
    }

    /**
     * @param compareDate   比较日期
     * @param comparandDate 被比较日期
     * @return
     */
    public static boolean isSameDay(Date compareDate, Date comparandDate)
    {
        SimpleDateFormat format = new SimpleDateFormat(defaultFormat, Locale.CHINA);
        String comparandDateStr = format.format(comparandDate);
        try
        {
            SimpleDateFormat FORMATTER1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            comparandDate = FORMATTER1.parse(comparandDateStr + " 00:00:00");
            long compareDateMill = compareDate.getTime();
            long comparandDateMill = comparandDate.getTime();
            long kk = compareDateMill - comparandDateMill;
            if ( (kk > 0) && (kk < (24 * 60 * 60 * 1000)) )
            {
                return true;
            }
        } catch ( ParseException e )
        {
            return false;
        }
        return false;
    }

    /**
     * @param milliseconds
     * @return
     */
    public static Date longToDate(long milliseconds)
    {
        return new Date(milliseconds);
    }

    /**
     * long型字符串 转换为date
     *
     * @param longStr
     * @return
     */
    public static Date longToDate(String longStr)
    {
        if ( TextUtils.isEmpty(longStr) )
        {
            return null;
        }
        long milliseconds = Long.parseLong(longStr);
        return longToDate(milliseconds);
    }

    public static long timeToLong(int monthDay, int month, int year)
    {
        Time time = new Time();
        time.set(monthDay, month, year);
        return time.toMillis(true);

    }

    /**
     * 时间转换为String
     *
     * @param date
     * @param format
     * @return
     */
    public static String timeToString(Date date, String format)
    {
        if ( date == null )
        {
            return "";
        }
        SimpleDateFormat dateFormat =
                new SimpleDateFormat((format == null) || "".equals(format) ? defaultFormat : format,
                        Locale.CHINA);
        return dateFormat.format(date);
    }

    /**
     * 时间转换为String
     *
     * @param date
     * @param format
     * @return
     */
    public static String timeToString(long date, String format)
    {
        return timeToString(longToDate(date), format);
    }

    /**
     * 时间转换为String
     *
     * @param longdate
     * @param format
     * @return
     */
    public static String timeToString(String longdate, String format)
    {
        if ( TextUtils.isEmpty(longdate) )
        {
            return "";
        }
        return timeToString(longToDate(longdate), format);
    }

}

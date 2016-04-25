/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.tools;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import android.net.Uri;
import android.os.Environment;

/**
 * @author noah.zheng
 */
public class FileUtils
{

    /**
     * 创建根目录
     *
     * @param path 目录路径
     */
    public static void createDirFile(String path)
    {
        File dir = new File(path);
        if ( !dir.exists() )
        {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path)
    {
        File file = new File(path);
        if ( !file.exists() )
        {
            try
            {
                file.createNewFile();
            } catch ( IOException e )
            {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    /**
     * 删除文件
     *
     * @param path 文件的路径
     */
    public static void delAllFile(String path)
    {
        File file = new File(path);
        if ( !file.exists() )
        {
            return;
        }
        if ( !file.isDirectory() )
        {
            return;
        }
        String[] tempList = file.list();
        if ( tempList == null )
        {
            return;
        }
        File temp = null;
        for ( String element : tempList )
        {
            if ( "crop_user_portrait.jpg".equals(element) )
            {
                continue;
            }

            if ( path.endsWith(File.separator) )
            {
                temp = new File(path + element);
            } else
            {
                temp = new File(path + File.separator + element);
            }
            if ( temp.isFile() )
            {
                temp.delete();
            }
            if ( temp.isDirectory() )
            {
                delAllFile(path + "/" + element);
                delFolder(path + "/" + element);
            }
        }
    }

    public static void deleteFile(String path)
    {
        File file = new File(path);
        if ( !file.exists() )
        {
            return;
        }
        file.delete();
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹的路径
     */
    public static void delFolder(String folderPath)
    {
        delAllFile(folderPath);
        String filePath = folderPath;
        filePath = filePath.toString();
        java.io.File myFilePath = new java.io.File(filePath);
        myFilePath.delete();
    }

    /**
     * 换算文件大小
     *
     * @param size
     * @return
     */
    public static String formatFileSize(long size)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "未知大小";
        if ( size < 1024 )
        {
            fileSizeString = df.format(( double ) size) + "B";
        } else if ( size < 1048576 )
        {
            fileSizeString = df.format(( double ) size / 1024) + "K";
        } else if ( size < 1073741824 )
        {
            fileSizeString = df.format(( double ) size / 1048576) + "M";
        } else
        {
            fileSizeString = df.format(( double ) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static long getAllFileSize(String path)
    {
        long size = 0;
        File file = new File(path);
        if ( !file.exists() )
        {
            return 0;
        }
        if ( !file.isDirectory() )
        {
            return 0;
        }
        String[] tempList = file.list();
        if ( tempList == null )
        {
            return 0;
        }
        File temp = null;
        for ( String element : tempList )
        {
            if ( path.endsWith(File.separator) )
            {
                temp = new File(path + element);
            } else
            {
                temp = new File(path + File.separator + element);
            }
            if ( temp.isFile() )
            {
                size = size + temp.length();
            }
            if ( temp.isDirectory() )
            {
                size = size + getAllFileSize(path + "/" + element);
            }
        }

        return size;
    }

    /**
     * 获取文件的Uri
     *
     * @param path 文件的路径
     * @return
     */
    public static Uri getUriFromFile(String path)
    {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    public static boolean isFileExist(File file)
    {
        return file.exists();
    }

    public static boolean isFileExist(String filePath)
    {
        return isFileExist(new File(filePath));
    }

    /**
     * 判断SD是否可以
     *
     * @return
     */
    public static boolean isSdcardExist()
    {
        if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
        {
            return true;
        }
        return false;
    }

}

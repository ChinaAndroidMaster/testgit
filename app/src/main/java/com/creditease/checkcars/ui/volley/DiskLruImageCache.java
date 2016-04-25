package com.creditease.checkcars.ui.volley;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.creditease.checkcars.BuildConfig;
import com.creditease.utilframe.util.FileUtils;
import com.jakewharton.disklrucache.DiskLruCache;

/**
 * Implementation of DiskLruCache by Jake Wharton
 * modified from http://stackoverflow.com/questions/10185898/using-disklrucache-in-android-4-0-does-not-provide-for-opencache-method
 */
public class DiskLruImageCache implements ImageCache
{
    private static final String TAG = DiskLruImageCache.class.getSimpleName();
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static int IO_BUFFER_SIZE = 10 * 1024;
    private DiskLruCache mDiskCache;
    private CompressFormat mCompressFormat = CompressFormat.JPEG;
    private int mCompressQuality = 70;

    public DiskLruImageCache(Context context, String uniqueName, int diskCacheSize,
                             CompressFormat compressFormat, int quality)
    {
        try
        {
            final File diskCacheDir = getDiskCacheDir(context, uniqueName);
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize);
            mCompressFormat = compressFormat;
            mCompressQuality = quality;
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
            throws IOException, FileNotFoundException
    {
        OutputStream out = null;
        try
        {
            out = new BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        } finally
        {
            if ( out != null )
            {
                out.close();
            }
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName)
    {
//        final String cachePath = PhotoUtils.BIG_IMAGE_PATH;
//        uniqueName = "Data";
        final String cachePath = context.getCacheDir().getPath();
        Log.d("DiskLruImageCache", "cache PATH" + cachePath + uniqueName);
        return new File(cachePath + uniqueName);
    }

    @Override
    public void putBitmap(String key, Bitmap data)
    {

        DiskLruCache.Editor editor = null;
        try
        {
            editor = mDiskCache.edit(key);
            if ( editor == null )
            {
                return;
            }

            if ( writeBitmapToFile(data, editor) )
            {
                mDiskCache.flush();
                editor.commit();
                if ( BuildConfig.DEBUG )
                {
                    Log.d(TAG, "image put on disk cache " + key);
                }
            } else
            {
                editor.abort();
                if ( BuildConfig.DEBUG )
                {
                    Log.d(TAG, "ERROR on: image put on disk cache " + key);
                }
            }
        } catch ( IOException e )
        {
            if ( BuildConfig.DEBUG )
            {
                Log.d(TAG, "ERROR on: image put on disk cache " + key);
            }
            try
            {
                if ( editor != null )
                {
                    editor.abort();
                }
            } catch ( IOException ignored )
            {
            }
        }

    }

    @Override
    public Bitmap getBitmap(String key)
    {
        Log.d(TAG, "getBitmap:1  " + key);
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try
        {
            snapshot = mDiskCache.get(key);
            if ( snapshot == null )
            {
                String filePath = key + ".0";
                File folder = getCacheFolder();
                File file = new File(folder, filePath);
                Log.d(TAG, "getBitmap:4  " + file.getAbsolutePath());
                if ( file.exists() )
                {
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Log.d(TAG, "getBitmap:3  " + file.getAbsolutePath());
                    return bitmap;
                }
                return null;
            }
            Log.d(TAG, "getBitmap:2  " + key);
            final InputStream in = snapshot.getInputStream(0);
            if ( in != null )
            {
                final BufferedInputStream buffIn =
                        new BufferedInputStream(in, IO_BUFFER_SIZE);
                bitmap = BitmapFactory.decodeStream(buffIn);
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        } finally
        {
            if ( snapshot != null )
            {
                snapshot.close();
            }
        }

        if ( BuildConfig.DEBUG )
        {
            Log.d(TAG, bitmap == null ? "" : "image read from disk " + key);
        }

        return bitmap;

    }

    public boolean containsKey(String key)
    {

        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try
        {
            snapshot = mDiskCache.get(key);
            contained = snapshot != null;
        } catch ( IOException e )
        {
            e.printStackTrace();
        } finally
        {
            if ( snapshot != null )
            {
                snapshot.close();
            }
        }

        return contained;

    }

    public void clearCache()
    {
        if ( BuildConfig.DEBUG )
        {
            Log.d(TAG, "disk cache CLEARED");
        }
        try
        {
            mDiskCache.delete();
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public File getCacheFolder()
    {
        return mDiskCache.getDirectory();
    }

    public void removeAllCache()
    {
        File root = mDiskCache.getDirectory();
        FileUtils.delAllFile(root.getAbsolutePath());
//    	FileUtils.delAllFile(PhotoUtils.BIG_IMAGE_PATH);
    }

    public long getCurrentCacheSize()
    {
        File root = mDiskCache.getDirectory();
        //getAllFileSize
        return FileUtils.getAllFileSize(root.getAbsolutePath());
    }

    public void clearMemoryCache()
    {
        mDiskCache.clearCache();
    }

}

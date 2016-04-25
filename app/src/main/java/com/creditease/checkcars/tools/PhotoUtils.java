/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.FloatMath;

import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.ui.act.base.BaseActivity;

/**
 * @author noah.zheng
 */
public class PhotoUtils
{

    public static String IMAGE_DIR1 = Environment.getExternalStorageDirectory().toString()
            + File.separator + "carchecks" + File.separator;

    public static String CACHE_DIR = "images" + File.separator;

    public static String IMAGE_DIR = "image" + File.separator;
    // 图片在SD卡中的缓存路径
    public static String IMAGE_CACHE_PATH = IMAGE_DIR1 + CACHE_DIR;
    // 图片在SD卡中的头像路径
    public static String IMAGE_IMG_PATH = IMAGE_DIR1 + IMAGE_DIR;

    /**
     * 拍照请求码
     */
    public static final int REQUEST_CAMERA_CODE = 1001;

    /**
     * 照片选择
     */
    public static final int REQUEST_IMAGE_CODE = 1002;
    /**
     * 裁切照片选择
     */
    public static final int REQUEST_IMAGE_CUT_CODE = 1003;

    /**
     * 裁切照片选择
     */
    public static final int REQUEST_CAMERA_CUT_CODE = 1004;

    /**
     * 裁切照片选择
     */
    public static final int REQUEST_CUTCAMERA_CUT_CODE = 1005;

    // 相册的RequestCode
    public static final int INTENT_REQUEST_CODE_ALBUM = 0;

    // 照相的RequestCode
    public static final int INTENT_REQUEST_CODE_CAMERA = 1;
    // 裁剪照片的RequestCode
    public static final int INTENT_REQUEST_CODE_CROP = 2;
    // 滤镜图片的RequestCode
    public static final int INTENT_REQUEST_CODE_FLITER = 3;
    public static final int INTENT_REQUEST_CODE_CLICK_SELECT_PHOTO = 4;

    // 点击照相
    public static final int INTENT_REQUEST_CODE_CLICK_TAKE_PHOTO = 5;

    private static Uri mUri;

    /**
     * 判断图片高度和宽度是否过大
     *
     * @param bitmap 图片bitmap对象
     * @return
     */
    public static boolean bitmapIsLarge(Bitmap bitmap)
    {
        final int MAX_WIDTH = 60;
        final int MAX_HEIGHT = 60;
        Bundle bundle = getBitmapWidthAndHeight(bitmap);
        if ( bundle != null )
        {
            int width = bundle.getInt("width");
            int height = bundle.getInt("height");
            if ( (width > MAX_WIDTH) && (height > MAX_HEIGHT) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据比例缩放图片
     *
     * @param screenWidth 手机屏幕的宽度
     * @param filePath    图片的路径
     * @param ratio       缩放比例
     * @return
     */
    public static Bitmap CompressionPhoto(float screenWidth, String filePath, int ratio)
    {
        Bitmap bitmap = PhotoUtils.getBitmapFromFile(filePath);
        Bitmap compressionBitmap = null;
        float scaleWidth = screenWidth / (bitmap.getWidth() * ratio);
        float scaleHeight = screenWidth / (bitmap.getHeight() * ratio);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        try
        {
            compressionBitmap =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch ( Exception e )
        {
            return bitmap;
        }
        return compressionBitmap;
    }

    public static Bitmap compressPhoto(final Bitmap bitmap, int h, int w, int scale)
    {

        Bitmap compressionBitmap = null;
        float scaleWidth = w / (bitmap.getWidth() * scale);
        float scaleHeight = h / (bitmap.getHeight() * scale);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        try
        {
            compressionBitmap =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch ( Exception e )
        {
            return bitmap;
        }

        return compressionBitmap;
    }

    public static String copyFileUsing(String sourceP)
    {
        try
        {
            if ( TextUtils.isEmpty(sourceP) )
            {
                return "";
            }

            File source = new File(sourceP);
            if ( !source.exists() )
            {
                return "";
            }

            String tmp[] = sourceP.split("\\.");
            String ext = "";
            if ( tmp.length > 0 )
            {
                ext = tmp[tmp.length - 1];
            }

            FileUtils.createDirFile(IMAGE_CACHE_PATH);
            String newPath = IMAGE_CACHE_PATH + UUID.randomUUID().toString() + "." + ext;
            File dest = new File(newPath);
            InputStream input = null;
            OutputStream output = null;
            try
            {
                input = new FileInputStream(source);
                output = new FileOutputStream(dest);
                byte[] buf = new byte[1024];
                int bytesRead;
                while ( (bytesRead = input.read(buf)) > 0 )
                {
                    output.write(buf, 0, bytesRead);
                }
            } catch ( IOException e )
            {
            } finally
            {
                if ( input != null )
                {
                    input.close();
                }
                if ( output != null )
                {
                    output.close();
                }
            }

            return newPath;
        } catch ( Exception e1 )
        {
            e1.printStackTrace();
        }

        return "";
    }

    /**
     * 根据宽度和长度进行缩放图片
     *
     * @param path 图片的路径
     * @param w    宽度
     * @param h    长度
     * @return
     */
    public static Bitmap createBitmap(String path, int w, int h)
    {
        try
        {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if ( (srcWidth < w) || (srcHeight < h) )
            {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if ( srcWidth > srcHeight )
            {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = ( double ) srcWidth / w;
                destWidth = w;
                destHeight = ( int ) (srcHeight / ratio);
            } else
            {
                ratio = ( double ) srcHeight / h;
                destHeight = h;
                destWidth = ( int ) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = ( int ) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            Bitmap decodeFile = null;
            try
            {

                decodeFile = BitmapFactory.decodeFile(path, newOpts);
            } catch ( OutOfMemoryError e )
            {
                decodeFile = BitmapFactory.decodeFile(path, newOpts);
            }
            return decodeFile;
        } catch ( Exception e )
        {
            return null;
        }
    }

    public static void createFileDir()
    {
        FileUtils.createDirFile(IMAGE_DIR1);
        FileUtils.createDirFile(IMAGE_IMG_PATH);
        FileUtils.createDirFile(IMAGE_CACHE_PATH);
    }

    /**
     * 裁切拍照后的照片
     *
     * @param activity
     * @param requestCode
     */
    public static void cropImageUri(BaseActivity activity, int requestCode)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mUri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", dp2Px(activity, 80f));
        intent.putExtra("outputY", dp2Px(activity, 80f));
        intent.putExtra("return-data", true);
        if ( activity.getPackageManager().resolveActivity(intent, 0) != null )
        {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 裁剪图片
     *
     * @param context
     * @param activity
     * @param path     需要裁剪的图片路径
     */
    public static void cropPhoto(Context context, Activity activity, String path)
    {
        // Intent intent = new Intent(context, ImageFactoryActivity.class);
        // if (path != null) {
        // intent.putExtra("path", path);
        // intent.putExtra(ImageFactoryActivity.TYPE,
        // ImageFactoryActivity.CROP);
        // }
        // activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
    }

    /**
     * 裁剪图片
     *
     * @param context
     * @param activity
     * @param path     需要裁剪的图片路径
     */
    public static void cropPhotoClip(Context context, Activity activity, String path,
                                     String headPhotoBgUrl, int w, int h)
    {
        // Intent intent = new Intent(context, ImageFactoryClipActivity.class);
        // if (path != null) {
        // intent.putExtra(ImageFactoryClipActivity.EXTRA_DATA_BIG_PIC_PATH,
        // path);
        // intent.putExtra(ImageFactoryClipActivity.EXTRA_DATA_BG_PIC_PATH,
        // headPhotoBgUrl);
        // intent.putExtra(ImageFactoryClipActivity.EXTRA_DATA_PIC_H, h);
        // intent.putExtra(ImageFactoryClipActivity.EXTRA_DATA_PIC_W, w);
        // }
        // activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CROP);
    }

    /**
     * 删除图片缓存目录
     */
    public static void deleteImageFile()
    {
        File dir = new File(IMAGE_CACHE_PATH);
        if ( dir.exists() )
        {
            FileUtils.delFolder(IMAGE_CACHE_PATH);
        }
    }

    /**
     * dp转换成px
     *
     * @param activity
     * @param dp
     * @return
     */
    public static int dp2Px(Activity activity, float dp)
    {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return ( int ) ((dp * scale) + 0.5f);
    }

    /**
     * 滤镜图片
     *
     * @param context
     * @param activity
     * @param path     需要滤镜的图片路径
     */
    public static void fliterPhoto(Context context, Activity activity, String path)
    {
        // Intent intent = new Intent(context, ImageFactoryActivity.class);
        // if (path != null) {
        // intent.putExtra("path", path);
        // intent.putExtra(ImageFactoryActivity.TYPE,
        // ImageFactoryActivity.FLITER);
        // }
        // activity.startActivityForResult(intent, INTENT_REQUEST_CODE_FLITER);
    }

    /**
     * 获取颜色的圆角bitmap
     *
     * @param context
     * @param
     * @return
     */

    @SuppressLint( "DefaultLocale" )
    public static String getBigImage(String path, Context context)
    {
        if ( path.toUpperCase().endsWith("HTML") )
        {
            return "";
        }
        // String[] ss = path.split("/");
        // String destpath = "crop_user_portrait.jpg";
        FileUtils.createDirFile(PhotoUtils.IMAGE_DIR1);
        FileUtils.createDirFile(PhotoUtils.IMAGE_IMG_PATH);
        String venderId = SharePrefenceManager.getAppraiserId(context);
        String destpath = venderId + ".jpg";
        File file = new File(IMAGE_IMG_PATH + destpath);
        // if (file.exists()) {
        // return file.getAbsolutePath();
        // }

        try
        {
            if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
            {
                // Log.d("getBigImage", "DOWNLOAD URL: " + path);
                URL url = new URL(path);
                HttpURLConnection conn = ( HttpURLConnection ) url.openConnection();
                conn.setConnectTimeout(5000);
                // 获取到文件的大小
                InputStream is = conn.getInputStream();
                String imagePath = PhotoUtils.IMAGE_IMG_PATH;
                FileUtils.createDirFile(imagePath);
                File folder = new File(imagePath);
                //
                file = new File(folder, destpath);
                if ( file.exists() )
                {
                    file.delete();
                }
                file = new File(folder, destpath);
                // File file = FileUtils.createNewFile(destpath+name);
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                while ( (len = bis.read(buffer)) != -1 )
                {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                bis.close();
                is.close();
                return file.getAbsolutePath();
            } else
            {
                return null;
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据宽度和长度进行缩放图片
     *
     * @param path 图片的路径
     * @param screenWidth    宽度
     * @param screenHeight    长度
     * @return
     */
    public static Bitmap getBitmap(String path, int screenWidth, int screenHeight)
    {
        try
        {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if ( ((srcWidth * 2) >= screenWidth) && ((srcHeight * 2) >= screenHeight) )
            {
                ratio = 1;
                destWidth = srcWidth / 2;
                destHeight = srcHeight / 2;
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = ( int ) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            Bitmap decodeFile = null;
            try
            {

                decodeFile = BitmapFactory.decodeFile(path, newOpts);
            } catch ( OutOfMemoryError e )
            {
                decodeFile = BitmapFactory.decodeFile(path, newOpts);
            }
            return decodeFile;
        } catch ( Exception e )
        {
            return null;
        }
    }

    /**
     * 从文件中获取图片限制大小
     *
     * @param
     * @return
     */
    @SuppressLint( "FloatMath" )
    public static Bitmap getBitmapFromFile(Context context, int id, long maxMemSize)
    {
        Bitmap bitmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), id, opt);
        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;
        if ( (picHeight == 0) || (picWidth == 0) )
        {
            return null;
        }

        float sourMultiple = (picHeight * picWidth) / maxMemSize;
        int scale = ( int ) FloatMath.sqrt(sourMultiple);
        opt.inJustDecodeBounds = false;
        try
        {
            if ( scale > 1 )
            {
                opt.inSampleSize = scale;
                bitmap = BitmapFactory.decodeResource(context.getResources(), id, opt);
            } else
            {
                bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            }
        } catch ( OutOfMemoryError e )
        {
            e.printStackTrace();
            return null;
        }

        return bitmap;
    }

    /**
     * 从文件中获取图片
     *
     * @param path 图片的路径
     * @return
     */
    public static Bitmap getBitmapFromFile(String path)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    /**
     * 从文件中获取图片限制大小
     *
     * @param path 图片的路径
     * @return
     */
    @SuppressLint( "FloatMath" )
    public static Bitmap getBitmapFromFile(String path, long maxMemSize)
    {
        Bitmap bitmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opt);
        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;
        if ( (picHeight == 0) || (picWidth == 0) )
        {
            return null;
        }

        float sourMultiple = (picHeight * picWidth) / maxMemSize;
        float tiple = FloatMath.sqrt(sourMultiple);
        // int scale = new BigDecimal(tiple).setScale(0,
        // BigDecimal.ROUND_HALF_UP).intValue(); //四舍五入取整
        int scale = ( int ) tiple;
        opt.inJustDecodeBounds = false;
        try
        {
            if ( scale > 1 )
            {
                opt.inSampleSize = scale;
                bitmap = BitmapFactory.decodeFile(path, opt);
            } else
            {
                bitmap = BitmapFactory.decodeFile(path);
            }
        } catch ( OutOfMemoryError e )
        {
            e.printStackTrace();
            return null;
        }

        return bitmap;
    }

    /**
     * 从Uri中获取图片
     *
     * @param cr  ContentResolver对象
     * @param uri 图片的Uri
     * @return
     */
    public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri)
    {
        try
        {
            return BitmapFactory.decodeStream(cr.openInputStream(uri));
        } catch ( FileNotFoundException e )
        {

        }
        return null;
    }

    /**
     * 获取图片的长度和宽度
     *
     * @param bitmap 图片bitmap对象
     * @return
     */
    public static Bundle getBitmapWidthAndHeight(Bitmap bitmap)
    {
        Bundle bundle = null;
        if ( bitmap != null )
        {
            bundle = new Bundle();
            bundle.putInt("width", bitmap.getWidth());
            bundle.putInt("height", bitmap.getHeight());
            return bundle;
        }
        return null;
    }

    /**
     * @return 返回指定笔的文字高度
     */
    public static float getFontHeight(Paint paint)
    {
        FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint)
    {
        FontMetrics fm = paint.getFontMetrics();
        return fm.leading - fm.ascent;
    }

    /**
     * @return 返回指定笔和指定字符串的长度
     */
    public static float getFontlength(Paint paint, String str)
    {
        return paint.measureText(str);
    }

    public static Bitmap getLocalSign()
    {
        String path = PhotoUtils.IMAGE_CACHE_PATH + "user_sign_img.png";
        ;
        return BitmapFactory.decodeFile(path);
    }

    /**
     * 滤镜效果--LOMO
     *
     * @param bitmap
     * @return
     */
    public static Bitmap lomoFilter(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int dst[] = new int[width * height];
        bitmap.getPixels(dst, 0, width, 0, 0, width, height);

        int ratio = width > height ? (height * 32768) / width : (width * 32768) / height;
        int cx = width >> 1;
        int cy = height >> 1;
        int max = (cx * cx) + (cy * cy);
        int min = ( int ) (max * (1 - 0.8f));
        int diff = max - min;

        int ri, gi, bi;
        int dx, dy, distSq, v;

        int R, G, B;

        int value;
        int pos, pixColor;
        int newR, newG, newB;
        for ( int y = 0; y < height; y++ )
        {
            for ( int x = 0; x < width; x++ )
            {
                pos = (y * width) + x;
                pixColor = dst[pos];
                R = Color.red(pixColor);
                G = Color.green(pixColor);
                B = Color.blue(pixColor);

                value = R < 128 ? R : 256 - R;
                newR = (value * value * value) / 64 / 256;
                newR = (R < 128 ? newR : 255 - newR);

                value = G < 128 ? G : 256 - G;
                newG = (value * value) / 128;
                newG = (G < 128 ? newG : 255 - newG);

                newB = (B / 2) + 0x25;

                // ==========边缘黑暗==============//
                dx = cx - x;
                dy = cy - y;
                if ( width > height )
                {
                    dx = (dx * ratio) >> 15;
                } else
                {
                    dy = (dy * ratio) >> 15;
                }

                distSq = (dx * dx) + (dy * dy);
                if ( distSq > min )
                {
                    v = ((max - distSq) << 8) / diff;
                    v *= v;

                    ri = newR * v >> 16;
                    gi = newG * v >> 16;
                    bi = newB * v >> 16;

                    newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                    newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                    newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
                }
                // ==========边缘黑暗end==============//

                dst[pos] = Color.rgb(newR, newG, newB);
            }
        }
        Bitmap acrossFlushBitmap = null;
        // acrossFlushBitmap = Bitmap.createBitmap(width, height,
        // Bitmap.Config.RGB_565);
        try
        {
            acrossFlushBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        } catch ( OutOfMemoryError e )
        {

            acrossFlushBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        }
        acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
        return acrossFlushBitmap;
    }

    public static String savePhotoToSDCard(Bitmap bitmap, String path)
    {
        if ( (bitmap == null) || bitmap.isRecycled() )
        {
            return null;
        }
        if ( !FileUtils.isSdcardExist() )
        {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        File file = FileUtils.createNewFile(path);
        if ( file == null )
        {
            return null;
        }
        try
        {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
        } catch ( FileNotFoundException e1 )
        {
            return null;
        } finally
        {
            try
            {
                if ( fileOutputStream != null )
                {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch ( IOException e )
            {
                return null;
            }
        }
        return path;
    }

    /**
     * 保存图片到SD卡缓存
     *
     * @param bitmap 图片的bitmap对象
     * @return
     */
    public static String savePhotoToSDCardCache(Bitmap bitmap, String fileName, boolean isGif)
    {
        if ( !FileUtils.isSdcardExist() )
        {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        FileUtils.createDirFile(IMAGE_CACHE_PATH);

        String ext = ".jpg";
        if ( isGif )
        {
            ext = ".gif";
        }
        if ( TextUtils.isEmpty(fileName) )
        {
            fileName = UUID.randomUUID().toString() + ext;
        }
        String newFilePath = IMAGE_CACHE_PATH + fileName;
        File file = FileUtils.createNewFile(newFilePath);
        if ( file == null )
        {
            return null;
        }
        try
        {
            fileOutputStream = new FileOutputStream(newFilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
        } catch ( FileNotFoundException e1 )
        {
            e1.printStackTrace();
            return null;
        } finally
        {
            try
            {
                if ( fileOutputStream != null )
                {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch ( IOException e )
            {
                e.printStackTrace();
                return null;
            }
        }
        return newFilePath;
    }

    /**
     * 通过手机相册获取图片
     *
     * @param activity
     */
    public static boolean selectPhoto(Activity activity)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if ( activity.getPackageManager().resolveActivity(intent, 0) != null )
        {
            activity.startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
            return true;
        }
        return false;
    }

    /**
     * 通过手机相册获取图片
     *
     * @param activity
     */
    public static boolean selectPhoto(Activity activity, int requestCode)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if ( activity.getPackageManager().resolveActivity(intent, 0) != null )
        {
            activity.startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    /**
     * 调用相册且能对图片进行裁切
     *
     * @param activity
     * @param requestCode
     */
    public static void selectPhotoCut(BaseActivity activity, int requestCode)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", dp2Px(activity, 75f));
        intent.putExtra("outputY", dp2Px(activity, 75f));
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        if ( activity.getPackageManager().resolveActivity(intent, 0) != null )
        {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 调用相机前置
     *
     * @param activity
     * @param name
     * @param requestCode
     * @return
     */
    public static String startCamearPicCut(BaseActivity activity, String name, int requestCode)
    {
        // TODO Auto-generated method stub
        FileUtils.createDirFile(IMAGE_DIR1);
        FileUtils.createDirFile(IMAGE_IMG_PATH);
        String path = IMAGE_IMG_PATH + UUID.randomUUID().toString() + ".jpg";
        if ( !TextUtils.isEmpty(name) )
        {
            path = IMAGE_IMG_PATH + name;
        }
        File file = FileUtils.createNewFile(path);
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2);// 调用前置摄像头
        intent.putExtra("autofocus", true);// 自动对焦
        intent.putExtra("fullScreen", false);// 全屏
        intent.putExtra("showActionIcons", false);
        mUri = Uri.fromFile(file);
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        activity.startActivityForResult(intent, requestCode);
        return path;
    }

    /**
     * 通过手机照相获取图片
     *
     * @param activity
     * @return 照相后图片的路径
     */
    public static String takePicture(BaseActivity activity, String name)
    {
        FileUtils.createDirFile(IMAGE_CACHE_PATH);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String path = IMAGE_CACHE_PATH + UUID.randomUUID().toString() + ".jpg";
        if ( !TextUtils.isEmpty(name) )
        {
            path = IMAGE_CACHE_PATH + name;
        }
        File file = FileUtils.createNewFile(path);
        if ( file != null )
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
        return path;
    }

    /**
     * 通过手机照相获取图片
     *
     * @param activity
     * @return 照相后图片的路径
     */
    public static String takePicture(BaseActivity activity, String name, int requestCode)
    {
        FileUtils.createDirFile(IMAGE_DIR1);
        FileUtils.createDirFile(IMAGE_IMG_PATH);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String path = IMAGE_IMG_PATH + UUID.randomUUID().toString() + ".jpg";
        if ( !TextUtils.isEmpty(name) )
        {
            path = IMAGE_IMG_PATH + name;
        }
        File file = FileUtils.createNewFile(path);
        if ( file != null )
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        activity.startActivityForResult(intent, requestCode);
        return path;
    }

}

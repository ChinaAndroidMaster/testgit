package com.creditease.checkcars.net.ver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.NewVersion;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.tools.Util;
import com.creditease.checkcars.ui.act.base.ActStack;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.dialog.UpdateNewVersionDialog;
import com.creditease.checkcars.ui.dialog.base.TDialog;
import com.creditease.utilframe.exception.HttpException;

/**
 * @author 子龍
 * @function
 * @date 2015年4月7日
 * @company CREDITEASE
 */
public class VersionUpdateManager implements RequestListener
{
    public static interface OnMethodCallback
    {
        public void closeClick();

        public void onError();

        public void openClick();

        public void process(int process);

    }

    private static final int NOTIFICATION_ID = 12;
    private static NotificationManager mNotifyManager;
    private static Builder mBuilder;
    private int size;
    // private static Handler handler;
    private static volatile VersionUpdateManager mVersionUpdateManager;

    public static VersionUpdateManager getInstance()
    {
        if ( mVersionUpdateManager == null )
        {
            mVersionUpdateManager = new VersionUpdateManager();
        }
        return mVersionUpdateManager;
    }

    private OnMethodCallback mCallback;

    private Context mContext;

    private static boolean isDownloading = false;

    private static void installApk(final Context activity, File file)
    {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    private LoadingGifDialog loadDialog;

    private boolean isAuto = false;

    private VersionUpdateManager()
    {
        super();
        // loadDialog = new LoadingGifDialog(mContext);
        // loadDialog.setText(R.string.str_check_version_ing);
    }

    public void checkVersionWS(boolean isAuto, Context context)
    {
        this.isAuto = isAuto;
        mContext = ActStack.getStack().currentActivity();
        if ( mContext == null )
        {
            return;
        }
        // mContext = context;
        String versionNumber = Util.getAppVersionName(context);
        String appName = context.getResources().getString(R.string.app_name);
        OperationFactManager.getManager().versionCheck(context, appName, versionNumber, this);
        if ( !isAuto )
        {
            loadDialog = new LoadingGifDialog(context);
            loadDialog.showDialog();
        }
    }

    public void closeClickMethod()
    {
        if ( mCallback != null )
        {
            mCallback.closeClick();
        }
    }

    public void downLoadApk(final Context activity, final NewVersion version)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                initNotification(activity);
                closeClickMethod();
                try
                {
                    // Displays the progress bar for the first time.
                    mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                    File file = getFileFromServer(version.appUrl);
                    // Log.i("VersionUpdate", "VersionUpdate+file.length()=="
                    // + file.length());
                    if ( size == file.length() )
                    {
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("下载完成").setProgress(100, 100, false);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        // 执行的数据类型
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        PendingIntent pIntent =
                                PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        mBuilder.setContentIntent(pIntent);
                        mBuilder.setAutoCancel(true);
                        openClickMethod();
                        // 执行动作
                        installApk(activity, file);
                        isDownloading = false;
                    }
                } catch ( Exception e )
                {
                    // Message msg = new Message();
                    // msg.what = DOWN_ERROR;
                    // handler.sendMessage(msg);
                    e.printStackTrace();
                    mBuilder.setAutoCancel(true);
                    mBuilder.setContentText("下载失败").setProgress(0, 0, false);
                    if ( mCallback != null )
                    {
                        mCallback.onError();
                    }
                    // mBuilder.
                    isDownloading = false;
                } finally
                {
                    mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                }
            }
        }).start();
    }

    public File getFileFromServer(String path) throws Exception
    {
        int currentPro = 0;
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        String externalStorageState = Environment.getExternalStorageState();
        if ( externalStorageState.equals(Environment.MEDIA_MOUNTED) )
        {
            URL url = new URL(path);
            HttpURLConnection conn = ( HttpURLConnection ) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            size = conn.getContentLength();
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "shifukanche.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[512 * 1024];
            int len;
            int total = 0;
            while ( (len = bis.read(buffer)) != -1 )
            {
                fos.write(buffer, 0, len);
                fos.flush();
                total += len;
                // Log.i("VersionUpdate", "VersionUpdate+total==" + total);
                // Log.i("VersionUpdate", "VersionUpdate+size==" + size);
                // 获取当前下载量
                // Sets the progress indicator to a max value, the
                // current completion percentage, and "determinate"
                // state
                int i = ( int ) ((( float ) total / size) * 100);
                currentPro = i;
                if ( mCallback != null )
                {
                    mCallback.process(currentPro);
                }
                // Log.i("VersionUpdate", "VersionUpdate+i==" + i);
                mBuilder.setProgress(100, currentPro, false);
                mBuilder.setContentText(currentPro + "%");
                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
            fos.flush();
            fos.close();
            bis.close();
            is.close();
            return file;
        } else
        {
            return null;
        }
    }

    private void initNotification(final Context activity)
    {
        // 建立notification,前面有学习过，不解释了，不懂看搜索以前的文章
        mNotifyManager = ( NotificationManager ) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(activity);
        mBuilder.setContentTitle("师傅看车版本更新").setContentText("正在下载..")
                .setSmallIcon(R.drawable.ic_launcher).setProgress(0, 0, false).setOngoing(false);

    }

    public boolean isDownloading()
    {
        return isDownloading;
    }

  /*
   * 从服务器中下载APK
   */

    @Override
    public void onDataError(String errorMsg, String result)
    {
        if ( loadDialog != null )
        {
            loadDialog.dismiss();
        }
    }

    @Override
    public void onFailure(OperResponse response)
    {
        if ( loadDialog != null )
        {
            loadDialog.dismiss();
        }
        if ( "0053".equals(response.respcode) && !isAuto )
        {
            ZLToast.getToast().showToast(mContext, response.respmsg);
        }
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        if ( loadDialog != null )
        {
            loadDialog.dismiss();
        }
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        if ( loadDialog != null )
        {
            loadDialog.dismiss();
        }
        NewVersion res = bundle.getParcelable(Oper.BUNDLE_EXTRA_DATA);
        SharePrefenceManager.setNewVersion(mContext, res);
        String versionNumber = Util.getAppVersionName(mContext);
        showUpdataDialog(mContext, res, versionNumber);
    }

    public void openClickMethod()
    {
        if ( mCallback != null )
        {
            mCallback.openClick();
        }
    }

    public synchronized void setOnMethodCallback(OnMethodCallback callback)
    {
        mCallback = callback;
    }

    /*
     *
     * 弹出对话框通知用户更新程序
     *
     * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮 3.通过builder 创建一个对话框
     * 4.对话框show()出来
     */
    public void showUpdataDialog(final Context mContext, final NewVersion version, String versionName)
    {
        String msg = "本地版本:  V" + versionName + "\n";
        msg += "最新版本: V " + version.versionName + "\n";
        if ( !TextUtils.isEmpty(version.remark) )
        {
            msg += "升级内容: \n" + version.remark;
        }
        final UpdateNewVersionDialog dialog = new UpdateNewVersionDialog(mContext);
        dialog.setTitle("版本更新");
        dialog.setContent(msg);
        dialog.setUpdateForce(version.updateType == NewVersion.TYPE_UPDATE_FORCE);

        dialog.setOnOKBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
                isDownloading = true;
                downLoadApk(mContext, version);
            }
        }).setOnCancelBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
            }
        });
        TDialog.Builder.builderDialog(dialog).showDialog();
    }

}

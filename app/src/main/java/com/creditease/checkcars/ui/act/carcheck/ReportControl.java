package com.creditease.checkcars.ui.act.carcheck;

import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.CCClassify;
import com.creditease.checkcars.data.bean.CCItem;
import com.creditease.checkcars.data.bean.Car;
import com.creditease.checkcars.data.bean.CarImg;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.db.CarReportUtil;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.ReportImageBean;
import com.creditease.checkcars.net.oper.bean.ReportImageBean.RequestObj;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.net.uploadimg.ImageUploadManager;
import com.creditease.checkcars.net.uploadimg.UploadImage;
import com.creditease.checkcars.tools.CommitOrderEventUtils;
import com.creditease.checkcars.tools.JsonUtils;
import com.creditease.checkcars.tools.NetWorkUtils;
import com.creditease.checkcars.ui.act.SelectPhotoActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.utilframe.exception.HttpException;

/**
 * 报告控制
 *
 * @author 子龍
 * @function
 * @date 2015年11月4日
 * @company CREDITEASE
 */
public class ReportControl
{

    public ImageControl imageControl;
    public SyncControl syncControl;
    public CommitControl commitControl;
    public Car car;
    public CarReport report;
    public String orderId;
    public boolean isGlobalDestroy = false;
    public ISave iSave;
    protected NetWorkUtils netWorkUtils;
    private BaseActivity mContext;
    private SaveReportThread thread;// 保存报告线程
    /**
     * constructor
     *
     * @param context
     */
    public ReportControl(BaseActivity context)
    {
        mContext = context;
        imageControl = new ImageControl();
        syncControl = new SyncControl();
        commitControl = new CommitControl();
        netWorkUtils = new NetWorkUtils(context);
    }

    /**
     * 同步提交报告的监听回调
     *
     * @param listener 下午7:31:00
     */
    public void setCommitRequestListener(RequestListener listener)
    {
        commitControl.setRequestListener(listener);
    }

    /**
     * 提交报告
     * <p/>
     * 下午7:37:36
     */
    public void commitReport()
    {
        commitControl.commitReport(report);
    }

    public boolean isNeedAsyncBeforeCommit()
    {
        return commitControl.isNeedAsyncBeforeCommit;
    }

    public void setNeedAsyncBeforeCommit(boolean isNeedAsyncBeforeCommit)
    {
        commitControl.isNeedAsyncBeforeCommit = isNeedAsyncBeforeCommit;
    }

    /**
     * 同步报告
     * <p/>
     * 下午7:38:05
     */
    public void syncReport(CarReport report)
    {
        syncControl.syncReport(report);
    }

    /**
     * 初始化报告
     *
     * @param carReport 下午6:38:07
     */
    public void initCarReport(CarReport carReport, String orderId)
    {
        if ( carReport == null )
        {
            return;
        }
        this.orderId = orderId;
        report = carReport;
        car = report.reportCar;
        initPhotoValue();
        commitControl.setReport(carReport);
    }

    public void setISave(ISave iSave)
    {
        this.iSave = iSave;
    }

    public void initPhotoValue()
    {
        if ( car == null )
        {
            return;
        }
        if ( car.imgList.size() == 8 )
        {
            String path = car.imgList.get(0).imgValue;
            if ( !TextUtils.isEmpty(path) )
            {
                imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_1;
            }
            path = car.imgList.get(1).imgValue;
            if ( !TextUtils.isEmpty(path) )
            {
                imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_2;
            }
            path = car.imgList.get(2).imgValue;
            if ( !TextUtils.isEmpty(path) )
            {
                imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_3;
            }
            path = car.imgList.get(3).imgValue;
            if ( !TextUtils.isEmpty(path) )
            {
                imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_4;
            }
            path = car.imgList.get(4).imgValue;
            if ( !TextUtils.isEmpty(path) )
            {
                imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_5;
            }
            path = car.imgList.get(5).imgValue;
            if ( !TextUtils.isEmpty(path) )
            {
                imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_6;
            }
            path = car.imgList.get(6).imgValue;
            if ( !TextUtils.isEmpty(path) )
            {
                imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_7;
            }
        }

    }

    /**
     * 拍照的大类小项
     *
     * @param classify
     * @param item     下午6:04:16
     */
    public void takePhoto(CCClassify classify, CCItem item)
    {
        imageControl.classify = classify;
        imageControl.item = item;
    }

    /**
     * 启动上传图片服务
     * <p/>
     * 下午6:10:33
     */
    public void startUploadImageService()
    {
        if ( (report != null) && (report.isSync == CarReport.SYNC_YES) )
        {
            ImageUploadManager.getManager(mContext.getApplicationContext()).startService(
                    mContext.getApplicationContext());
        }
    }

    /**
     * 提交检测项图片
     *
     * @param imgSavePath
     * @param imageType   下午6:13:18
     */
    private void uploadItemImage(String imgSavePath, int imageType)
    {
        RequestObj obj = new ReportImageBean.RequestObj(report.clientUuid, imageType);
        String json = JsonUtils.requestObjectBean(obj);
        // 添加图片到上传管理器中
        UploadImage img =
                new UploadImage(imgSavePath, Config.WS_USERS_REPORT_UPLOAD_IMAGE_ITEM_URL, "para", json,
                        true);
        try
        {
            ImageUploadManager.getManager(mContext.getApplicationContext()).addUploadImage(img);
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
        saveReport();
        startUploadImageService();
    }

    /**
     * 照片是否都已完成
     *
     * @return 下午5:35:18
     */
    public boolean isPhotosAllToken()
    {
        return ImageControl.VALUE_PHOTO_ALL == imageControl.photo_value;
    }

    /**
     * 图片回调处理
     *
     * @param requestCode
     * @param data        下午6:17:34
     */
    public void photoHandle(int requestCode, Intent data)
    {
        if ( requestCode == ImageControl.REQUEST_CCITEM_IMG_OPER )
        {
            int oper = data.getIntExtra(PicViewActivity.PARAM_OPER, -1);
            switch ( oper )
            {
                case 0:
                    break;
                case 1:
                    // 删除
                    for ( String path : imageControl.item.picPathList )
                    {
                        String uploadId = report.clientUuid + "_" + imageControl.item.uuid + "_" + path;
                        // 清除上传的图片池
                        ImageUploadManager.getManager(mContext.getApplicationContext()).removeUploadImage(
                                uploadId);
                    }
                    imageControl.item.picPathList.clear();
                    imageControl.item.imgValue = null;
                    saveReport();
                    break;
            }
        } else
        {
            String savePath = data.getStringExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_SAVEPATH);
            int action = data.getIntExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION, 0);
            if ( savePath == null )
            {
                return;
            }
            if ( action == ImageControl.REQUEST_CAMERA_CODE_CHECKITEM )
            {
                if ( imageControl.item == null )
                {
                    return;
                }
                imageControl.item.imgValue = savePath;
                if ( imageControl.item.picPathList == null )
                {
                    imageControl.item.picPathList = new ArrayList< String >(0);
                }
                imageControl.item.picPathList.add(savePath);
                boolean bool =
                        (imageControl.item.attributeId == null)
                                || TextUtils.isEmpty(imageControl.item.attributeId);
                String json =
                        "{\"clientUuid\":\"" + report.clientUuid + "\",\"attributeId\":\""
                                + (bool ? imageControl.item.uuid : imageControl.item.attributeId)
                                + "\",\"interfaceVersion\":\"2\"}";
                // 添加图片到上传管理器中
                UploadImage img =
                        new UploadImage(report.clientUuid + "_" + imageControl.item.uuid + "_" + savePath,
                                savePath, Config.WS_USERS_REPORT_UPDATE_IMAGE_URL, "para", json, true);
                try
                {
                    ImageUploadManager.getManager(mContext.getApplicationContext()).addUploadImage(img);
                } catch ( CEDbException e )
                {
                    e.printStackTrace();

                }
                saveReport();
                startUploadImageService();
            } else
            {
                int type = 0;
                switch ( requestCode )
                {
                    case ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_FRONT:
                    {
                        CarImg img = car.getCarImg(CarImg.TYPE_IMG_FRONT);
                        if ( img != null )
                        {
                            img.imgValue = savePath;
                        }
                        imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_1;
                        type = CarImg.TYPE_IMG_FRONT;
                    }
                    break;
                    case ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_BACK:
                    {
                        CarImg img = car.getCarImg(CarImg.TYPE_IMG_BACK);
                        if ( img != null )
                        {
                            img.imgValue = savePath;
                        }
                        imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_2;
                        type = CarImg.TYPE_IMG_BACK;
                    }
                    break;
                    case ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_BRAND:
                    {
                        CarImg img = car.getCarImg(CarImg.TYPE_IMG_BRAND);
                        if ( img != null )
                        {
                            img.imgValue = savePath;
                        }
                        imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_3;
                        type = CarImg.TYPE_IMG_BRAND;
                    }
                    break;
                    case ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_ENGINE:
                    {
                        CarImg img = car.getCarImg(CarImg.TYPE_IMG_ENGINE);
                        if ( img != null )
                        {
                            img.imgValue = savePath;
                        }
                        imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_4;
                        type = CarImg.TYPE_IMG_ENGINE;
                    }
                    break;
                    case ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_CCR:
                    {
                        CarImg img = car.getCarImg(CarImg.TYPE_IMG_CCR);
                        if ( img != null )
                        {
                            img.imgValue = savePath;
                        }
                        imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_5;
                        type = CarImg.TYPE_IMG_CCR;
                    }
                    break;
                    case ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_PANEL:
                    {
                        CarImg img = car.getCarImg(CarImg.TYPE_IMG_PANEL);
                        if ( img != null )
                        {
                            img.imgValue = savePath;
                        }
                        imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_6;
                        type = CarImg.TYPE_IMG_PANEL;
                    }
                    break;
                    case ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_TRUNK:
                    {
                        CarImg img = car.getCarImg(CarImg.TYPE_IMG_TRUNK);
                        if ( img != null )
                        {
                            img.imgValue = savePath;
                        }
                        imageControl.photo_value = imageControl.photo_value | ImageControl.VALUE_PHOTO_7;
                        type = CarImg.TYPE_IMG_TRUNK;
                    }
                    break;
                    case ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_UNDERPAN:
                    {
                        CarImg img = car.getCarImg(CarImg.TYPE_IMG_UNDERPAN);
                        if ( img != null )
                        {
                            img.imgValue = savePath;
                        }
                        type = CarImg.TYPE_IMG_UNDERPAN;
                    }
                    break;
                    default:
                        break;
                }
                // 上传图片
                uploadItemImage(savePath, type);
            }
        }
    }

    /**
     * 销毁control
     * <p/>
     * 下午5:34:41
     */
    public void destroy()
    {

        saveThreadOver();

        if ( imageControl != null )
        {
            imageControl.destroy();
            imageControl = null;
        }
        syncControl = null;
        commitControl = null;
        car = null;
        report = null;
        isGlobalDestroy = true;
    }

    /**
     * 保存报告
     * <p/>
     * 下午5:21:36
     */
    public void saveReport()
    {
        if ( thread == null )
        {
            thread = new SaveReportThread();
            thread.start();
        } else
        {
            thread.wakeUp();
        }
    }

    /**
     * 结束保存
     * <p/>
     * 下午5:21:51
     */
    public void saveThreadOver()
    {
        if ( thread != null )
        {
            thread.stopSave();
        }
        thread = null;
    }

    protected boolean isNetWork()
    {
        return !netWorkUtils.getConnectState().equals(NetWorkUtils.NetWorkState.NONE);
    }


    /**
     * 保存接口
     *
     * @author 子龍
     * @function
     * @date 2015年11月4日
     * @company CREDITEASE
     */
    public interface ISave
    {
        public void save();
    }

    /**
     * 照片控制
     *
     * @author 子龍
     * @function
     * @date 2015年11月4日
     * @company CREDITEASE
     */
    public class ImageControl
    {
        /**
         * 预览并操作检测项问题图片
         */
        public final static int REQUEST_CCITEM_IMG_OPER = 110;
        /**
         * 检测项拍照
         */
        public final static int REQUEST_CAMERA_CODE_CHECKITEM = 112;
        /**
         * 七张必拍项
         */
        public final static int REQUEST_CAMERA_CODE_BASE_CAR_FRONT = 113;// 左前45度
        public final static int REQUEST_CAMERA_CODE_BASE_CAR_BACK = 114;// 右后45度
        public final static int REQUEST_CAMERA_CODE_BASE_CAR_ENGINE = 115;// 发动机舱
        public final static int REQUEST_CAMERA_CODE_BASE_CAR_BRAND = 116;// 厂牌
        public final static int REQUEST_CAMERA_CODE_BASE_CAR_CCR = 117;// 中控全貌
        public final static int REQUEST_CAMERA_CODE_BASE_CAR_PANEL = 118;// 仪表盘
        public final static int REQUEST_CAMERA_CODE_BASE_CAR_TRUNK = 119;// 行李箱
        public final static int REQUEST_CAMERA_CODE_BASE_CAR_UNDERPAN = 120;// 底盘（非必拍）
        public final static int VALUE_PHOTO_ALL = 0x1111111;
        public final static int VALUE_PHOTO_1 = 0x0000001;
        public final static int VALUE_PHOTO_2 = 0x0000010;
        public final static int VALUE_PHOTO_3 = 0x0000100;
        public final static int VALUE_PHOTO_4 = 0x0001000;
        public final static int VALUE_PHOTO_5 = 0x0010000;
        public final static int VALUE_PHOTO_6 = 0x0100000;
        public final static int VALUE_PHOTO_7 = 0x1000000;
        // 拍照的检测大项
        public CCClassify classify;
        // 拍照的检测小项
        public CCItem item;
        public int photo_value = 0x0;// 拍照完成度
        public ImageControl()
        {
        }

        public void destroy()
        {
            classify = null;
            item = null;
        }
    }

    /**
     * 报告同步控制器
     *
     * @author 子龍
     * @function
     * @date 2015年11月4日
     * @company CREDITEASE
     */
    public class SyncControl implements RequestListener
    {
        CarReport report;
        private boolean isSyncing = false;// 是否正在同步reportId

        public SyncControl()
        {
        }

        public void setReport(CarReport report)
        {
            this.report = report;
        }

        /**
         * sync report
         */
        public void syncReport(CarReport report)
        {
            if ( isGlobalDestroy )
            {
                return;
            }
            if ( !isNetWork() )
            {
                mContext.showToast("您的网络不顺畅~请检查您的网络");
                return;
            }
            if ( report == null )
            {
                return;
            }
            setReport(report);
            if ( !isSyncing && (report.isSync == CarReport.SYNC_NO) )
            {
                String vin = "";
                if ( report.reportCar != null )
                {
                    vin = report.reportCar.carVin;
                }
                OperationFactManager.getManager().addCarReport(mContext, orderId, report.clientUuid, vin,
                        this);
                isSyncing = true;
            }
        }

        @Override
        public void onDataError(String errorMsg, String result)
        {
            isSyncing = false;
            syncReport(report);
        }

        @Override
        public void onFailure(OperResponse response)
        {

            if ( "0072".equals(response.respcode) )
            {
                report.isSync = CarReport.SYNC_YES;
                startUploadImageService();
                try
                {
                    CarReportUtil.getUtil(mContext.getApplicationContext()).updateReport(report);
                } catch ( CEDbException e )
                {
                    e.printStackTrace();
                }
                if ( isNeedAsyncBeforeCommit() )
                {
                    commitReport();
                    setNeedAsyncBeforeCommit(false);
                }
                report = null;
            }
            isSyncing = false;
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            isSyncing = false;
            syncReport(report);
        }

        @Override
        public void onSuccess(Bundle bundle)
        {
            isSyncing = false;
            report.isSync = CarReport.SYNC_YES;
            startUploadImageService();
            try
            {
                CarReportUtil.getUtil(mContext.getApplicationContext()).updateReport(report);
            } catch ( CEDbException e )
            {
                e.printStackTrace();
            }
            if ( isNeedAsyncBeforeCommit() )
            {
                commitReport();
                setNeedAsyncBeforeCommit(false);
            }
            report = null;
        }
    }

    /**
     * 报告提交控制
     *
     * @author 子龍
     * @function
     * @date 2015年11月4日
     * @company CREDITEASE
     */
    public class CommitControl implements RequestListener
    {
        public boolean isNeedAsyncBeforeCommit = false;// 提交报告前是否需要同步
        private boolean isCommitState = false;// 是否正在提交报告
        private RequestListener listener;
        private CarReport report;

        public CarReport getReport()
        {
            return report;
        }

        public void setReport(CarReport report)
        {
            this.report = report;
        }

        public void setRequestListener(RequestListener listener)
        {
            this.listener = listener;
        }

        public void commitReport(CarReport report)
        {
            if ( isGlobalDestroy )
            {
                return;
            }
            if ( !isNetWork() )
            {
                mContext.showToast("您的网络不顺畅~请检查您的网络");
                return;
            }
            if ( report == null )
            {
                return;
            }
            setReport(report);
            if ( isCommitState )
            {
                mContext.showToast("正在提交中...");
            }
            if ( report.isSync == CarReport.SYNC_YES )
            {
                if ( !isCommitState && isNetWork() )
                {
                    OperationFactManager.getManager().commitCarReport(mContext, report, this);
                    isCommitState = true;
                    // 记录提交报告的时间并发送给服务器
                    CommitOrderEventUtils.getUtil(mContext.getApplicationContext()).recordTime(
                            report.orderId, 2, 1, report.clientUuid);
                }
            } else
            {
                isNeedAsyncBeforeCommit = true;
                syncReport(report);
            }
        }

        @Override
        public void onSuccess(Bundle bundle)
        {
            isCommitState = false;
            if ( listener != null )
            {
                listener.onSuccess(bundle);
            }
            report = null;
            destroy();
        }

        @Override
        public void onFailure(OperResponse response)
        {
            isCommitState = false;
            if ( listener != null )
            {
                listener.onFailure(response);
            }
            report = null;
        }

        @Override
        public void onDataError(String errorMsg, String result)
        {
            isCommitState = false;
            if ( listener != null )
            {
                listener.onDataError(errorMsg, result);
            }
            report = null;
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            isCommitState = false;
            if ( listener != null )
            {
                listener.onRequestError(error, msg);
            }
            report = null;
        }
    }

    /**
     * 保存数据
     *
     * @author 子龍
     * @function
     * @date 2015年3月20日
     * @company CREDITEASE
     */
    class SaveReportThread extends Thread
    {

        private boolean loop = false;


        public SaveReportThread()
        {
            loop = true;
        }

        @Override
        public void run()
        {
            while ( loop )
            {
                LockSupport.park();
                if ( loop && (iSave != null) )
                {
                    iSave.save();
                }
            }
        }

        public void stopSave()
        {
            loop = false;
            wakeUp();
        }

        public void wakeUp()
        {
            LockSupport.unpark(this);
        }
    }
}

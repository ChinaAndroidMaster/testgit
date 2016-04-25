package com.creditease.checkcars.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.creditease.checkcars.data.bean.UserPositionInfo;
import com.creditease.checkcars.data.db.UserPositionUtil;
import com.creditease.checkcars.data.db.base.DBAsyncTask;
import com.creditease.checkcars.data.db.base.DBFindCallBack;
import com.creditease.checkcars.data.db.base.DbHelper;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.tools.NetWorkUtils.NetWorkState;
import com.creditease.utilframe.exception.HttpException;

/**
 * 使用高德地图SDK,采用WIFI+基站的方式定位
 *
 * @author zgb
 */
@SuppressLint( {"HandlerLeak", "SimpleDateFormat"} )
public class LocalUtils implements AMapLocationListener, RequestListener
{
    private static LocalUtils utils;
    private LocationManagerProxy mLocationManagerProxy;
    private Context mContext;
    public String address;
    private double latitude;
    private double longitude;
    private String addressName;
    private static final int MSG_STOP = 1;
    private SimpleDateFormat df;
    private SimpleDateFormat sdf;
    private String date;

    private LocalUtils(Context context)
    {
        super();
        mContext = context;
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static LocalUtils getUtils(Context context)
    {
        if ( utils == null )
        {
            utils = new LocalUtils(context);
        }
        return utils;
    }


    private Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch ( msg.what )
            {
                case MSG_STOP:
                    if ( !TextUtils.isEmpty(utils.address) )
                    {
                        // 如果已获取地址，则停止定位
                        utils.stopLocation();
                    }
                    break;

                default:
                    break;
            }
        }

    };


    /**
     * 初始化定位
     */
    private void init()
    {
        // 初始化定位，采用AGPS方法定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(mContext);
        mLocationManagerProxy.setGpsEnable(true);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15,
                this);
    }

    @Override
    public void onLocationChanged(AMapLocation amap)
    {
        address = amap.getCity() + amap.getDistrict();
        latitude = amap.getLatitude();
        longitude = amap.getLongitude();
        addressName = amap.getAddress();
        // storagePositionInfo(String.valueOf(latitude), String.valueOf(longitude), addressName);
        commitMasterLocation();
        mHandler.sendEmptyMessage(MSG_STOP);
    }

    @Override
    public void onLocationChanged(Location location)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    /**
     * 开始定位
     */
    public void startLocation()
    {
        init();
    }

    /**
     * 停止定位
     */
    public void stopLocation()
    {
        // 移除定位请求
        mLocationManagerProxy.removeUpdates(this);
        // 销毁定位
        mLocationManagerProxy.destroy();
    }

    /**
     * 存储位置信息到数据库
     *
     * @param latitude
     * @param longitude
     * @param addressName
     */
    @SuppressWarnings( "unused" )
    private void storagePositionInfo(String latitude, String longitude, String addressName)
    {
        String apprasierId = SharePrefenceManager.getAppraiserId(mContext);
        String address = SharePrefenceManager.getCurrentAddr(mContext, apprasierId);
        // 比较地址是否相同，相同则不再保存，否则保存并更新本地的数据
        if ( address.equals(addressName) )
        {
            return;
        } else
        {
            SharePrefenceManager.setCurrentAddr(mContext, apprasierId, addressName);
        }
        String deviceToken = Util.getIMEI(mContext);
        UserPositionInfo info = new UserPositionInfo();
        info.toId = apprasierId;
        info.toType = 1;
        info.fromId = deviceToken;
        info.fromType = 2;
        info.latitude = latitude;
        info.longitude = longitude;
        info.addressName = addressName;
        info.recordTime = df.format(new Date());
        try
        {
            UserPositionUtil.getUtil(mContext).saveObjToDB(info);
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
    }


    /**
     * 当日数据，次日发送到服务器端
     *
     * @param
     */
    public void commitMasterPositionsByDay()
    {
        String apprasierId = SharePrefenceManager.getAppraiserId(mContext);
        date = sdf.format(new Date());
        // 取得保存的日期
        final String saveDate = SharePrefenceManager.getCurrentDate(mContext, apprasierId);
        // 每日仅仅保存一次，故通过日期比较来判定是否要上传数据
        if ( !saveDate.equals(date) )
        {
            // 更新当前日期
            SharePrefenceManager.setCurrentDate(mContext, apprasierId, date);
        } else
        {
            return;
        }
        new DBAsyncTask< UserPositionInfo >(mContext, new DBFindCallBack< UserPositionInfo >()
        {

            @Override
            public void dataCallBack(int operId, List< UserPositionInfo > result)
            {

            }

            @Override
            public List< UserPositionInfo > doDBOperation(DbHelper helper, int operId) throws CEDbException
            {
                NetWorkUtils netUtils = new NetWorkUtils(mContext);
                if ( (netUtils != null) && (netUtils.getConnectState() != NetWorkState.NONE) )
                {
                    // 发送今日之前的所有数据
                    String apprasierId = SharePrefenceManager.getAppraiserId(mContext);
                    // 获取当日之前的所有数据
                    List< UserPositionInfo > list =
                            UserPositionUtil.getUtil(mContext).getUserPositionList(apprasierId, date);
                    // 上传数据到服务器
                    if ( (list != null) && (list.size() > 0) )
                    {
                        for ( UserPositionInfo info : list )
                        {
                            OperationFactManager.getManager().commitMasterLocation(mContext, info, null);
                        }
                    }
                }
                return null;
            }

            @Override
            public void errorCallBack(int operId, CEDbException e)
            {

            }

        }).execute();
    }

    // private boolean isEligible(String firstTime,String secondTime){
    // try {
    // Date dt1 = sdf.parse(firstTime);
    // Date dt2 = sdf.parse(secondTime);
    // if(dt1.getTime() < dt2.getTime()){
    // return true;
    // }else{
    // return false;
    // }
    // } catch (ParseException e) {
    // e.printStackTrace();
    // return false;
    // }
    // }

    /**
     * 提交数据到服务器端
     */
    public void commitMasterLocation()
    {
        String apprasierId = SharePrefenceManager.getAppraiserId(mContext);
        /**
         * 使用IMEI作为唯一的手机识别码
         */
        String deviceToken = Util.getIMEI(mContext);
        String address = SharePrefenceManager.getCurrentAddr(mContext, apprasierId);
        // 比较地址是否相同，相同则不再保存，否则保存并更新本地的数据
        if ( address.equals(addressName) )
        {
            return;
        } else
        {
            SharePrefenceManager.setCurrentAddr(mContext, apprasierId, addressName);
        }
        UserPositionInfo info = new UserPositionInfo();
        info = new UserPositionInfo();
        info.toId = apprasierId;
        info.toType = 1;
        info.fromId = deviceToken;
        info.fromType = 2;
        info.latitude = String.valueOf(latitude);
        info.longitude = String.valueOf(longitude);
        info.addressName = addressName;
        info.recordTime = df.format(new Date());// 暂时未使用
        if ( !TextUtils.isEmpty(apprasierId) && !TextUtils.isEmpty(addressName) )
        {
            OperationFactManager.getManager().commitMasterLocation(mContext, info, null);
        }
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        new DBAsyncTask< UserPositionInfo >(mContext, new DBFindCallBack< UserPositionInfo >()
        {

            @Override
            public void dataCallBack(int operId, List< UserPositionInfo > result)
            {

            }

            @Override
            public List< UserPositionInfo > doDBOperation(DbHelper helper, int operId) throws CEDbException
            {
                String apprasierId = SharePrefenceManager.getAppraiserId(mContext);
                // 删除已成功提交到服务器端的本地数据
                UserPositionUtil.getUtil(mContext.getApplicationContext()).deleteUserPosition(apprasierId,
                        date);
                return null;
            }

            @Override
            public void errorCallBack(int operId, CEDbException e)
            {

            }

        }).execute();
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {

    }

    @Override
    public void onFailure(OperResponse response)
    {

    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {

    }


}

package com.creditease.checkcars.data.db;

import java.io.IOException;

import android.content.Context;
import android.text.TextUtils;

import com.creditease.checkcars.data.bean.CCClassify;
import com.creditease.checkcars.data.bean.CCItem;
import com.creditease.checkcars.data.bean.Car;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.tools.JsonUtils;
import com.creditease.checkcars.tools.Util;
import com.creditease.checkcars.tools.Utils;

/**
 * @author 子龍
 * @function
 * @date 2015年4月29日
 * @company CREDITEASE
 */
public class BaseReportUtil
{
    class SaveThread extends Thread
    {
        private int type;
        private String content;
        final static int TYPE_SAVE = 0;
        final static int TYPE_UPDATE = 1;

        public SaveThread(int type, String content)
        {
            this.type = type;
            this.content = content;
        }

        @Override
        public void run()
        {
            switch ( type )
            {
                case TYPE_SAVE:
                    if ( !TextUtils.isEmpty(SharePrefenceManager.getBaseReport(context)) )
                    {
                        return;
                    }
                    try
                    {
                        String reportStr =
                                Utils.convertStreamToString(context.getAssets().open("checkitem.txt"));
                        CarReport cr = JsonUtils.parserJsonStringToObject(reportStr, CarReport.class);
                        SharePrefenceManager.setBaseReport(context, reportStr);
                        String appraiserId = SharePrefenceManager.getAppraiserId(context);
                        SharePrefenceManager.setBaseReportUpdateTime(context, cr.updateTime, appraiserId);
                    } catch ( IOException e )
                    {
                    }
                    break;
                case TYPE_UPDATE:
                    CarReport cr = JsonUtils.parserJsonStringToObject(content, CarReport.class);
                    String appraiserId = SharePrefenceManager.getAppraiserId(context);
                    SharePrefenceManager.setBaseReportUpdateTime(context, cr.updateTime, appraiserId);
                    SharePrefenceManager.setBaseReport(context, content);
                    break;
                default:
                    break;
            }
            super.run();
        }
    }

    private Context context;

    private static BaseReportUtil util;

    public static BaseReportUtil getBRUtil(Context context)
    {
        if ( util == null )
        {
            util = new BaseReportUtil(context);
        }
        return util;
    }

    private BaseReportUtil(Context context)
    {
        this.context = context;
    }


    /**
     * 创建报告
     *
     * @param orderId
     * @return CarReport
     * @throws IOException 下午5:26:52
     */
    public CarReport createBaseReport(String orderId, String clientUuid) throws CEDbException
    {
        String appraiserId = SharePrefenceManager.getAppraiserId(context);
        String reportStr = SharePrefenceManager.getBaseReport(context);
        final CarReport cr = JsonUtils.parserJsonStringToObject(reportStr, CarReport.class);
        if ( cr == null )
        {
            return null;
        }
        cr.reportCar = new Car();
        cr.orderId = orderId;
        if ( TextUtils.isEmpty(clientUuid) )
        {
            cr.clientUuid = Util.createReportId(context, appraiserId, orderId);
        } else
        {
            cr.clientUuid = clientUuid;
        }
        cr.status = CarReport.STATUS_INIT;
        cr.isSync = CarReport.SYNC_NO;
        cr.startTime = System.currentTimeMillis();// 开始检车时间
        for ( CCClassify ccClassify : cr.cRoot.children )
        {
            if ( "重点检测".equals(ccClassify.name) )
            {
                ccClassify.doSwitch = CCClassify.SWITCH_NO;
                continue;
            }
            CCItem c = new CCItem("主属性");
            if ( (ccClassify.attrs.size() > 0) && ccClassify.attrs.contains(c) )
            {
                int index = ccClassify.attrs.indexOf(c);
                CCItem item = ccClassify.attrs.get(index);
                item.isFill = CCItem.SWITCH_ON;
                ccClassify.attrs.set(index, item);
            }
        }
        CarReportUtil.getUtil(context).saveReport(cr);
        return cr;
    }

    /**
     * 保存/更新报告数字字典
     *
     * @param context
     * @param cr      下午5:26:12
     */
    public void saveOrUpdateBaseReport(Context context, String cr)
    {
        if ( TextUtils.isEmpty(cr) )
        {
            new SaveThread(SaveThread.TYPE_SAVE, "").start();
        } else
        {
            new SaveThread(SaveThread.TYPE_UPDATE, cr).start();
        }
    }

}

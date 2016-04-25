package com.creditease.checkcars.data.db;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.creditease.checkcars.data.bean.Car;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.bean.Note;
import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.data.db.base.DBWhereBuilder;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.tools.JsonUtils;

/**
 * 报告数据库工具类
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public class CarReportUtil extends DBUtil< CarReport >
{

    private static CarReportUtil util;

    public static CarReportUtil getUtil(Context context)
    {
        if ( util == null )
        {
            util = new CarReportUtil(context);
        }
        return util;
    }

    protected CarReportUtil(Context mContext)
    {
        super(mContext);
    }

    /**
     * 获取报告
     *
     * @param phoneNumber
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public CarReport getInitReport(String orderId) throws CEDbException
    {
        CarReport report =
                getNewestObjFromDB(DBSelector.from(CarReport.class).where("orderId", "=", orderId)
                        .and("status", "=", CarReport.STATUS_INIT));
        if ( report != null )
        {
            report.cRoot = JsonUtils.parserJsonStringToObject(report.reportJson, Note.class);
            report.reportCar = JsonUtils.parserJsonStringToObject(report.carJson, Car.class);
        }
        return report;
    }


    /**
     * @param orderId
     * @param uuid
     * @return
     * @throws CEDbException
     */
    public CarReport getReportByClientUuid(String clientUuid) throws CEDbException
    {
        CarReport report =
                getNewestObjFromDB(DBSelector.from(CarReport.class).where("clientUuid", "=", clientUuid));
        if ( report != null )
        {
            report.cRoot = JsonUtils.parserJsonStringToObject(report.reportJson, Note.class);
            report.reportCar = JsonUtils.parserJsonStringToObject(report.carJson, Car.class);
        }
        return report;
    }


    /**
     * 获取某个预检测订单下的所有报告
     *
     * @param orderId
     * @return
     * @throws CEDbException 下午6:41:58
     */
    public List< CarReport > getAllReportByOrderId(String orderId) throws CEDbException
    {
        List< CarReport > reports =
                getObjListFromDB(DBSelector.from(CarReport.class).where("orderId", "=", orderId));

        if ( (reports != null) && (reports.size() > 0) )
        {
            for ( CarReport report : reports )
            {
                if ( report != null )
                {
                    if ( !TextUtils.isEmpty(report.reportJson) )
                    {
                        report.cRoot = JsonUtils.parserJsonStringToObject(report.reportJson, Note.class);
                    }
                    if ( !TextUtils.isEmpty(report.carJson) )
                    {
                        report.reportCar = JsonUtils.parserJsonStringToObject(report.carJson, Car.class);
                    }
                }
            }
            return reports;
        }
        return null;
    }


    /**
     * 保存报告
     *
     * @param user
     * @throws CEDbException 下午6:41:45
     */
    public void saveOrUpdateReport(CarReport report) throws CEDbException
    {
        report.carJson = JsonUtils.requestObjectBean(report.reportCar);
        report.reportJson = JsonUtils.requestObjectBean(report.cRoot);
        updateAndSaveObj(report, DBWhereBuilder.b("uuid", "=", report.uuid));
    }

    /**
     * 保存报告
     *
     * @param report
     * @throws CEDbException 下午5:30:50
     */
    public void saveReport(CarReport report) throws CEDbException
    {
        report.carJson = JsonUtils.requestObjectBean(report.reportCar);
        report.reportJson = JsonUtils.requestObjectBean(report.cRoot);
        saveObjToDB(report);
    }

    /**
     * 更新报告
     *
     * @param report
     * @throws CEDbException 下午5:31:40
     */
    public void updateReport(CarReport report) throws CEDbException
    {
        report.carJson = JsonUtils.requestObjectBean(report.reportCar);
        report.reportJson = JsonUtils.requestObjectBean(report.cRoot);
        updateObjToDB(report, DBWhereBuilder.b("clientUuid", "=", report.clientUuid));
    }

}

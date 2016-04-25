package com.creditease.checkcars.net.oper.manager;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.bean.OrderEvent;
import com.creditease.checkcars.data.bean.UserCore;
import com.creditease.checkcars.data.bean.UserPositionInfo;
import com.creditease.checkcars.net.oper.AddPreCarReportOperation;
import com.creditease.checkcars.net.oper.AnswerCommitOperation;
import com.creditease.checkcars.net.oper.BaseReportUpdateOperation;
import com.creditease.checkcars.net.oper.CarOrderFinishOperation;
import com.creditease.checkcars.net.oper.FinishOrderPayOperation;
import com.creditease.checkcars.net.oper.GetBasicDataOperation;
import com.creditease.checkcars.net.oper.GetCarInfoByVinOperation;
import com.creditease.checkcars.net.oper.GetCarOrdersOperation;
import com.creditease.checkcars.net.oper.GetMasterLocation;
import com.creditease.checkcars.net.oper.GetMineTopicsOperation;
import com.creditease.checkcars.net.oper.GetOrderPayInfoOperation;
import com.creditease.checkcars.net.oper.GetPreCarReportOperation;
import com.creditease.checkcars.net.oper.GetQuestionAnswerOperation;
import com.creditease.checkcars.net.oper.GetReportByUuidOperation;
import com.creditease.checkcars.net.oper.GetVerifyCodeOperation;
import com.creditease.checkcars.net.oper.OrderEventOperation;
import com.creditease.checkcars.net.oper.PaySuccessNotifyOperation;
import com.creditease.checkcars.net.oper.ReportAddOperation;
import com.creditease.checkcars.net.oper.ReportAttrImageDeleteOperation;
import com.creditease.checkcars.net.oper.ReportCommitOperation;
import com.creditease.checkcars.net.oper.ReportImageUpdateOperation;
import com.creditease.checkcars.net.oper.UpdateMsgPushUserDataOperation;
import com.creditease.checkcars.net.oper.UploadHeadPicOperation;
import com.creditease.checkcars.net.oper.UserDataCommitOperation;
import com.creditease.checkcars.net.oper.UserFeedBackOperation;
import com.creditease.checkcars.net.oper.UserLoginOperation;
import com.creditease.checkcars.net.oper.UserLogoutOperation;
import com.creditease.checkcars.net.oper.UserPwdResetOperation;
import com.creditease.checkcars.net.oper.UserRegisterOperation;
import com.creditease.checkcars.net.oper.VersionCheckOperation;
import com.creditease.checkcars.net.oper.WeiXinPayNotiOperation;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperManager.HttpMethod;

public class OperationFactManager
{
    private static OperationFactManager manager;

    public static OperationFactManager getManager()
    {
        if ( manager == null )
        {
            manager = new OperationFactManager();
        }
        return manager;
    }

    private OperationFactManager()
    {
    }

    /**
     * 添加报告
     *
     * @param mContext
     * @param orderID  订单ID
     * @param listener 下午7:59:26
     */
    public void addCarReport(Context mContext, String orderID, String clientUuid, String vin,
                             RequestListener listener)
    {
        ReportAddOperation oper = new ReportAddOperation(mContext, listener);
        oper.setParam(orderID, clientUuid, vin);
        oper.execute();
    }

    /**************************** 用户管理********** *************************/

    /**
     * 提交报告
     *
     * @param mContext
     * @param report
     * @param listener 下午8:02:10
     */
    public void commitCarReport(Context mContext, CarReport report, RequestListener listener)
    {
        ReportCommitOperation oper = new ReportCommitOperation(mContext, listener);
        oper.setParam(report);
        oper.execute();
    }

    /**
     * 提交车图片
     *
     * @param mContext
     * @param imageType Car.Type
     * @param imagePath
     * @param uuid
     * @param listener  下午9:19:53
     */
    public void commitCarReportImage(Context mContext, int imageType, String imagePath, String uuid,
                                     RequestListener listener)
    {
        ReportImageUpdateOperation oper = new ReportImageUpdateOperation(mContext, listener);
        oper.setParam(imageType, imagePath, uuid);
        oper.execute();
    }

    /**
     * 提交用户反馈
     *
     * @param mContext
     * @param orderId
     * @param listener 下午3:50:50
     */
    public void commitFeedbackWS(Context mContext, String uuid, String feedback,
                                 RequestListener listener)
    {
        UserFeedBackOperation oper = new UserFeedBackOperation(mContext, listener);
        oper.setParam(uuid, feedback);
        oper.execute();
    }

    /**
     * 删除报告检测项图片
     *
     * @param mContext
     * @param reportId
     * @param attrId
     * @param listener 下午8:26:41
     */
    public void deleteReportAttrImageWS(Context mContext, String reportId, String attrId,
                                        RequestListener listener)
    {
        ReportAttrImageDeleteOperation oper = new ReportAttrImageDeleteOperation(mContext, listener);
        oper.setParam(reportId, attrId);
        oper.execute();
    }

    /******************************** 检车业务 ****************************************/

    /**
     * 完成订单
     *
     * @param mContext
     * @param orderId
     * @param status
     * @param orderEndTime
     * @param listener     上午10:36:48
     */
    public void finishCarOrder(Context mContext, String orderId, int status, String orderEndTime,
                               RequestListener listener)
    {
        CarOrderFinishOperation oper = new CarOrderFinishOperation(mContext, listener);
        oper.setParam(orderId, status, orderEndTime);
        oper.execute();
    }

    /**
     * 完成订单支付
     *
     * @param mContext
     * @param phoneNumber
     * @param newPwd
     * @param listener    下午8:06:57
     */
    public void finishOrderPayWS(Context mContext, String orderId, int payType,
                                 RequestListener listener)
    {
        FinishOrderPayOperation oper = new FinishOrderPayOperation(mContext, listener);
        oper.setParam(orderId, payType);
        oper.execute();
    }

    /**
     * 查询支付详情
     *
     * @param mContext
     * @param orderId
     * @param listener 下午1:11:37
     */
    public void getCarInfoByVinWS(Context mContext, String vin, RequestListener listener)
    {
        GetCarInfoByVinOperation oper = new GetCarInfoByVinOperation(mContext, listener);
        oper.setParam(vin);
        oper.execute(HttpMethod.GET);
    }

    /**
     * 查询订单列表
     *
     * @param mContext
     * @param status
     * @param curPage
     * @param pageSize
     * @param listener 上午10:56:15
     */
    public void getCarOrders(Context mContext, long modifyTime, int status, int curPage, int pageSize,
                             RequestListener listener)
    {
        GetCarOrdersOperation oper = new GetCarOrdersOperation(mContext, listener);
        oper.setParam(modifyTime, status, curPage, pageSize);
        oper.execute();
    }

    /**
     * 查询支付详情
     *
     * @param mContext
     * @param orderId
     * @param listener 下午1:11:37
     */
    public void getOrderPayInfo(Context mContext, CarOrder order, RequestListener listener)
    {
        GetOrderPayInfoOperation oper = new GetOrderPayInfoOperation(mContext, listener);
        oper.setParam(order);
        oper.execute();
    }

    /**
     * 获取问答模块的数据
     *
     * @param mContext
     * @param examine     问题状态 0 审核通过 1 等待审核 2 审核不通过
     * @param currentPage
     * @param pageSize
     * @param modifyTime
     * @param listener
     */
    public void getQuestionAnswerData(Context mContext, int examine, int currentPage, int pageSize,
                                      String modifyTime, RequestListener listener)
    {
        GetQuestionAnswerOperation oper = new GetQuestionAnswerOperation(mContext, listener);
        oper.setParam(currentPage, pageSize, modifyTime, examine);
        oper.execute();
    }

    public void getReportByUuidWS(Context mContext, String reportId, RequestListener listener)
    {
        GetReportByUuidOperation oper = new GetReportByUuidOperation(mContext, listener);
        oper.setParam(reportId);
        oper.execute();
    }

    /**
     * 获取对师傅的评价接口
     *
     * @param context
     * @param currentPage
     * @param pageSize
     * @param uuid
     * @param operType
     */
    public void getTopicsList(Context mContext, int currentPage, int pageSize, String uuid,
                              long modifyTime, int operType, int operId, RequestListener listener)
    {
        GetMineTopicsOperation oper = new GetMineTopicsOperation(mContext, listener, operId);
        oper.setParam(currentPage, pageSize, uuid, modifyTime, operType);
        oper.execute();
    }

    /**
     * 获取师傅基础数据
     *
     * @param mContext
     * @param uuid
     * @param listener
     */
    public void getUserBasicData(Context mContext, String uuid, RequestListener listener)
    {
        GetBasicDataOperation oper = new GetBasicDataOperation(mContext, listener);
        oper.setParam(uuid);
        oper.execute();
    }

    /**
     * 获取验证码
     *
     * @param mContext
     * @param phoneNumber
     * @param type
     * @param listener
     */
    public void getVerifyCode(Context mContext, String phoneNumber, int type,
                              RequestListener listener)
    {
        GetVerifyCodeOperation oper = new GetVerifyCodeOperation(mContext, listener);
        oper.setParam(phoneNumber, type);
        oper.execute();
    }

    public void saveOrUpdateUserBasicData(Context mContext, Appraiser appraiser,
                                          RequestListener listener)
    {
        UserDataCommitOperation oper = new UserDataCommitOperation(mContext, listener);
        oper.setParam(appraiser);
        oper.execute();
    }

    /**
     * 更新报告数据字典
     *
     * @param mContext
     * @param listener 上午10:40:12
     */
    public void updateBaseReport(Context mContext, RequestListener listener)
    {
        BaseReportUpdateOperation oper = new BaseReportUpdateOperation(mContext, listener);
        oper.setParam();
        oper.execute();
    }

    /**
     * 上传消息推送用户数据
     *
     * @param mContext
     * @param tag
     * @param alias
     * @param aliasType
     * @param deviceToken
     * @param listener    下午5:43:45
     */
    public void updateMsgPushUserDataWS(Context mContext, String tag, String alias, String aliasType,
                                        String deviceToken, RequestListener listener)
    {
        UpdateMsgPushUserDataOperation oper = new UpdateMsgPushUserDataOperation(mContext, listener);
        oper.setParam(tag, alias, aliasType, deviceToken);
        oper.execute();
    }

    /**
     * 修改问答信息，如当某人回答了某个问题时，需要将回答上传给服务器
     *
     * @param context
     * @param qa
     * @param listener
     */
    public void updateQuestionAnswerData(Context mContext, String questionId, String fromId,
                                         int position, String content, RequestListener listener)
    {
        AnswerCommitOperation oper = new AnswerCommitOperation(mContext, listener);
        oper.setParam(questionId, fromId, position, content);
        oper.execute();
    }

    /**
     * 上传师傅头像
     *
     * @param mContext
     * @param imgFile
     * @param coreId
     * @param listener
     */
    public void uploadHeadPic(Context mContext, Bitmap bitmap, String coreId,
                              RequestListener listener)
    {
        UploadHeadPicOperation oper = new UploadHeadPicOperation(mContext, listener);
        oper.setParam(bitmap, coreId);
        oper.execute();
    }

    /**
     * 用户登录
     *
     * @param mContext
     * @param user
     * @param listener
     */
    public void userLogin(Context mContext, String phoneNumber, String password,
                          RequestListener listener)
    {
        UserLoginOperation oper = new UserLoginOperation(mContext, listener);
        oper.setParam(phoneNumber, password);
        oper.execute();
    }

    /**
     * 用户登出
     *
     * @param mContext
     * @param listener
     */
    public void userLogout(Context mContext, RequestListener listener)
    {
        UserLogoutOperation oper = new UserLogoutOperation(mContext, listener);
        oper.setParam();
        oper.execute();
    }

    /**
     * 重置密码
     *
     * @param mContext
     * @param phoneNumber
     * @param newPwd
     * @param listener    下午4:05:02
     */
    public void userPwdResetWS(Context mContext, String phoneNumber, String newPwd,
                               RequestListener listener)
    {
        UserPwdResetOperation oper = new UserPwdResetOperation(mContext, listener);
        oper.setParam(phoneNumber, newPwd);
        oper.execute();
    }

    /**
     * 用户注册
     *
     * @param mContext
     * @param user
     * @param listener
     */
    public void userRegist(Context mContext, UserCore user, RequestListener listener)
    {
        UserRegisterOperation oper = new UserRegisterOperation(mContext, listener);
        oper.setParam(user);
        oper.execute();
    }

    /**************************** app管理********** *************************/
    /**
     * 版本更新
     *
     * @param mContext
     * @param appName
     * @param versionName
     * @param listener    下午4:59:21
     */
    public void versionCheck(Context mContext, String appName, String versionName,
                             RequestListener listener)
    {
        VersionCheckOperation oper = new VersionCheckOperation(mContext, listener);
        oper.setParam(appName, versionName);
        oper.execute();
    }

    /**
     * 微信 支付提醒
     *
     * @param mContext
     * @param order
     * @param listener 下午6:06:29
     */
    public void weixinPayNoti(Context mContext, CarOrder order, RequestListener listener)
    {
        WeiXinPayNotiOperation oper = new WeiXinPayNotiOperation(mContext, listener);
        oper.setParam(order);
        oper.execute();
    }

    /**
     * 记录订单电话呼出时间，开始检车时间、提交修改报告的时间接口
     *
     * @param context
     * @param events
     * @param listener
     */
    public void commitOrderEvent(Context context, List< OrderEvent > events, RequestListener listener)
    {
        OrderEventOperation oper = new OrderEventOperation(context, listener);
        oper.setParam(events);
        oper.execute();
    }

    /**
     * 记录师傅的位置
     *
     * @param context
     * @param toId
     * @param toType
     * @param fromId
     * @param latitude
     * @param longitude
     * @param addressName
     * @param listener
     */
    public void commitMasterLocation(Context context, UserPositionInfo info,
                                     RequestListener listener)
    {
        GetMasterLocation oper = new GetMasterLocation(context, listener);
        oper.setParam(info);
        oper.execute();
    }


    /**
     * 支付宝支付结果通知
     *
     * @param context
     * @param status      支付状态 PayNotifyBean.STATUS_PAY_*
     * @param tradeId     订单的order number
     * @param orderType   订单状态 PayBean.TYPE_ORDER_*
     * @param sellerId    支付对象的账号
     * @param tradeAmount 支付金额
     * @param listener    上午11:43:20
     */
    public void alipyPayResultNotify(Context context, int status, String tradeId, int orderType,
                                     String sellerId, String tradeAmount, RequestListener listener)
    {
        PaySuccessNotifyOperation oper = new PaySuccessNotifyOperation(context, listener);
        oper.setParam(status, tradeId, orderType, sellerId, tradeAmount);
        oper.execute();
    }

    /**
     * 师傅增加预检测把报告
     *
     * @param context
     * @param listener
     */
    public void addPreCarReport(Context context, RequestListener listener)
    {
        AddPreCarReportOperation oper = new AddPreCarReportOperation(context, listener);
        oper.setParam();
        oper.execute();
    }

    /**
     * 师傅查询预检测报告
     *
     * @param context
     * @param listener
     */
    public void getPreCarReport(Context context, RequestListener listener)
    {
        GetPreCarReportOperation oper = new GetPreCarReportOperation(context, listener);
        oper.setParam();
        oper.execute();
    }
}

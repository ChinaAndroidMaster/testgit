package com.creditease.checkcars.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.utilframe.db.annotation.Table;

/**
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
@Table( name = "carorder_tab" )
public class CarOrder implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 8520171511761576218L;

    /**
     * 全部
     */
    public static final int STATUS_ALL = -1;

    /**
     * 预下单
     */
    public static final int STATUS_PREORDER = 0;
    /**
     * 已回访
     */
    public static final int STATUS_REVISIT = 1;
    /**
     * 已分配
     */
    public static final int STATUS_ASSIGNED = 2;
    /**
     * 已取消
     */
    public static final int STATUS_CANCELED = 3;
    /**
     * 检测中
     */
    public static final int STATUS_CHECKING = 4;
    /**
     * 已经作废
     */
    public static final int STATUS_OBSOLETE = 5;

    /**
     * 6：支付成功
     */
    public static final int STATUS_PAY_SUCCESS = 6;

    /**
     * 7:支付初始化
     */

    public static final int STATUS_PAY_PREPARED = 7;
    /**
     * 8:支付处理
     */
    public static final int STATUS_PAYING = 8;
    /**
     * 9：支付失败
     */
    public static final int STATUS_PAY_FAILED = 9;
    /**
     * 10：完成服务
     */
    public static final int STATUS_FINISH_SERVICE = 10;

    /**
     * 服务类型:二手车检测服务
     */
    public static final int TYPE_SERVICE_0 = 0;// 二手车检测服务
    /**
     * 服务类型:夏日安全套餐
     */
    public static final int TYPE_SERVICE_1 = 1;//

    public static final int TYPE_SERVICE_B = 2;// B端订单

    /**
     * 现金收款后，未转账
     */
    public static final int ACCOUNT_TRANSFER_UNDO = 0;
    /**
     * 现金收款后，已转账
     */
    public static final int ACCOUNT_TRANSFER_DONE = 1;

    public int id;
    public String uuid;
    public String customerId;
    public String orderTime;
    public String cityId;
    public String zoneId;
    public String address;
    /**
     * 师傅ID
     */
    public String appraiserId;
    /**
     * 订单状态
     */
    public int status;
    public String phoneNumber;
    public String trueName;
    public String carBrand;
    public String carSeries;
    public String description;
    public String createTime;
    public String modifyTime;
    /**
     * 服务类型
     */
    public int serviceType;
    public String p_name;
    /**
     * 订单金额
     */
    public String tradeAmount;

    public int payType;
    /**
     * 分配时间(预约时间)
     */
    public String assignTime;

    /**
     * 提示
     */
    public String remark;
    /**
     * 订单号
     */
    public String orderNum;

    /**
     * 现金收款后是否转账：0 否 1 是
     */
    public int isCashTransfer;

    public int isPreCheck;// 是否是预检测 0 不是 1是

    public String businessId;// B端id
    public int quantity;// 检测限量

    public Appraiser appraiser;
    public Customer customer;
    public OrderPay payOrder;
    public ArrayList< CarReport > reportList;
    public ArrayList< CarReport > showReportList;

    public String appraiserJson;
    public String customerJson;
    public String reportListJson;

    public static final Parcelable.Creator< CarOrder > CREATOR = new Parcelable.Creator< CarOrder >()
    {
        @Override
        public CarOrder createFromParcel(Parcel in)
        {
            return new CarOrder(in);
        }

        @Override
        public CarOrder[] newArray(int size)
        {
            return new CarOrder[size];
        }
    };

    public CarOrder()
    {
    }

    public CarOrder(int id, String phoneNumber, int status)
    {
        super();
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    private CarOrder(Parcel in)
    {
        id = in.readInt();
        uuid = in.readString();
        customerId = in.readString();
        orderTime = in.readString();
        cityId = in.readString();
        zoneId = in.readString();
        address = in.readString();
        appraiserId = in.readString();
        status = in.readInt();
        phoneNumber = in.readString();
        trueName = in.readString();
        carBrand = in.readString();
        carSeries = in.readString();
        description = in.readString();
        createTime = in.readString();
        modifyTime = in.readString();
        orderNum = in.readString();
        // try {
        // appraiser = in.readParcelable(Appraiser.class.getClassLoader());
        // customer = in.readParcelable(Customer.class.getClassLoader());
        //
        // } catch (Exception e) {
        // }
        // try {
        // reportList = new ArrayList<CarReport>();
        // in.readList(reportList, CarReport.class.getClassLoader());
        // } catch (Exception e) {
        // }
        appraiserJson = in.readString();
        customerJson = in.readString();
        reportListJson = in.readString();
        serviceType = in.readInt();
        p_name = in.readString();
        // try {
        // payOrder = in.readParcelable(OrderPay.class.getClassLoader());
        // } catch (Exception e) {
        // }
        tradeAmount = in.readString();
        // if (payOrder != null) {
        // tradeAmount = payOrder.tradeAmount;
        // }
        assignTime = in.readString();
        remark = in.readString();
        payType = in.readInt();
        isCashTransfer = in.readInt();
        isPreCheck = in.readInt();
        businessId = in.readString();
        quantity = in.readInt();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(uuid);
        dest.writeString(customerId);
        dest.writeString(orderTime);
        dest.writeString(cityId);
        dest.writeString(zoneId);
        dest.writeString(address);
        dest.writeString(appraiserId);
        dest.writeInt(status);
        dest.writeString(phoneNumber);
        dest.writeString(trueName);
        dest.writeString(carBrand);
        dest.writeString(carSeries);
        dest.writeString(description);
        dest.writeString(createTime);
        dest.writeString(modifyTime);
        dest.writeString(orderNum);
        // dest.writeParcelable(appraiser, flags);
        // dest.writeParcelable(customer, flags);
        // if (reportList != null && reportList.size() > 0) {
        // dest.writeList(reportList);
        // }
        dest.writeString(appraiserJson);
        dest.writeString(customerJson);
        dest.writeString(reportListJson);
        dest.writeInt(serviceType);
        dest.writeString(p_name);
        if ( payOrder != null )
        {
            // dest.writeParcelable(payOrder, flags);
            tradeAmount = payOrder.tradeAmount;
        }
        dest.writeString(tradeAmount);
        dest.writeString(assignTime);
        dest.writeString(remark);
        dest.writeInt(payType);
        dest.writeInt(isCashTransfer);
        dest.writeInt(isPreCheck);
        dest.writeString(businessId);
        dest.writeInt(quantity);
    }

}

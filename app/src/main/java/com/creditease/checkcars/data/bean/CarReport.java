package com.creditease.checkcars.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.utilframe.db.annotation.Table;

/**
 * 车检项目
 *
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
@Table( name = "cc_report_tab" )
public class CarReport implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 219170999411833503L;
    public int id;
    public String uuid;
    public String orderId;
    public String remark;
    public Car reportCar;
    public Note cRoot;
    public String carJson;
    public String reportJson;
    public int status;
    public int level;
    public String viewUrl;
    public String amount;// 估价
    public String updateTime;
    public String clientUuid;
    public int isSync = SYNC_NO;// 判断报告ID是否已与服务器同步
    public long startTime = 0;// 开始检测时间
    public long endTime = 0;// 结束检测时间
    public String reason;// 审核不通过的原因
    public ArrayList< CarImg > imgList;

    /**
     * 已同步
     */
    public static final int SYNC_YES = 1;//
    /**
     * 未同步
     */
    public static final int SYNC_NO = 0;//

    /**
     * 优
     */
    public static final int LEVEL_1 = 0;
    /**
     * 良
     */
    public static final int LEVEL_2 = 1;
    /**
     * 差
     */
    public static final int LEVEL_3 = 2;

    /**
     * 中
     */
    public static final int LEVEL_4 = 3;

    /**
     * 初始化
     */
    public static final int STATUS_INIT = 0;

    /**
     * 待审核
     */
    public static final int STATUS_FINISH = 1;
    /**
     * 审核通过
     */
    public static final int STATUS_PASSED = 2;

    /**
     * 拒绝
     */
    public static final int STATUS_REFUSE = 3;

    public CarReport()
    {

    }

    public CarReport(String remark)
    {
        super();
        this.remark = remark;
    }

    private CarReport(Parcel in)
    {
        id = in.readInt();
        uuid = in.readString();
        orderId = in.readString();
        remark = in.readString();
        reportCar = in.readParcelable(Car.class.getClassLoader());
        cRoot = in.readParcelable(Car.class.getClassLoader());
        carJson = in.readString();
        reportJson = in.readString();
        status = in.readInt();
        viewUrl = in.readString();
        level = in.readInt();
        amount = in.readString();
        updateTime = in.readString();
        clientUuid = in.readString();
        isSync = in.readInt();
        startTime = in.readLong();
        endTime = in.readLong();
        reason = in.readString();
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
        dest.writeString(orderId);
        dest.writeString(remark);
        dest.writeParcelable(reportCar, flags);
        dest.writeParcelable(cRoot, flags);
        dest.writeString(carJson);
        dest.writeString(reportJson);
        dest.writeInt(status);
        dest.writeString(viewUrl);
        dest.writeInt(level);
        dest.writeString(amount);
        dest.writeString(updateTime);
        dest.writeString(clientUuid);
        dest.writeInt(isSync);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(reason);
    }

    public static final Parcelable.Creator< CarReport > CREATOR = new Parcelable.Creator< CarReport >()
    {
        public CarReport createFromParcel(Parcel in)
        {
            return new CarReport(in);
        }

        public CarReport[] newArray(int size)
        {
            return new CarReport[size];
        }
    };
}

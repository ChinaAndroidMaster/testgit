package com.creditease.checkcars.data.bean;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.utilframe.db.annotation.Table;

/**
 * 检车
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */

@Table( name = "cc_car" )
public class Car implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 车类型：进口
     */
    public static final int CARTYPE_IMPORT = 1;
    /**
     * 车类型：国产
     */
    public static final int CARTYPE_DOMESTIC = 2;

    public String uuid;
    public String carBrand;// 车型号
    public String carYear;// 挂牌日期
    public String carNumber;//
    public String outTime;// 出厂日期
    public String carVin;// VIN号
    public String mileage;// 里程
    public String engineType;// 发动机型号
    public String environmentStandard;// 排放依据标准
    public String yearType;// 年款标准
    public int carType;// 车生产类型
    public String fourTwoOne;// 四证两险一发票
    public String userManual;// 用户手册
    public String factoryCertificate;// 出厂凭证
    public String registrationInformation;// 注册资料
    // public String requiredType;//填选规则

    public List< CarImg > imgList = CarImg.getCarImgs();

    // public String carImage;// 车图
    // public String carImage_front;
    // public String carImage_back;
    // public String carImage_brand;
    // public String carImage_engine;
    // public String carImage_ccr;
    // public String carImage_panel;
    // public String carImage_trunk;
    // public String carImage_underpan;

    public Car()
    {
    }

    public boolean isCarTypeNull()
    {
        return (carType != CARTYPE_IMPORT) && (carType != CARTYPE_DOMESTIC);
    }


    private Car(Parcel in)
    {
        uuid = in.readString();
        carBrand = in.readString();
        carYear = in.readString();
        carNumber = in.readString();
        outTime = in.readString();
        carVin = in.readString();
        mileage = in.readString();

        // carImage = in.readString();
        // carImage_front = in.readString();
        // carImage_back = in.readString();
        // carImage_brand = in.readString();
        // carImage_engine = in.readString();
        // carImage_ccr = in.readString();
        // carImage_panel = in.readString();
        // carImage_trunk = in.readString();
        engineType = in.readString();
        environmentStandard = in.readString();

        yearType = in.readString();
        carType = in.readInt();
        fourTwoOne = in.readString();
        userManual = in.readString();
        factoryCertificate = in.readString();
        registrationInformation = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(uuid);
        dest.writeString(carBrand);
        dest.writeString(carYear);
        dest.writeString(carNumber);
        dest.writeString(outTime);
        dest.writeString(carVin);
        dest.writeString(mileage);
        // dest.writeString(carImage);

        // dest.writeString(carImage_front);
        // dest.writeString(carImage_back);
        // dest.writeString(carImage_brand);
        // dest.writeString(carImage_engine);
        // dest.writeString(carImage_ccr);
        // dest.writeString(carImage_panel);
        // dest.writeString(carImage_trunk);
        dest.writeString(engineType);
        dest.writeString(environmentStandard);

        dest.writeString(yearType);
        dest.writeInt(carType);
        dest.writeString(fourTwoOne);
        dest.writeString(userManual);
        dest.writeString(factoryCertificate);
        dest.writeString(registrationInformation);

    }

    public static final Parcelable.Creator< Car > CREATOR = new Parcelable.Creator< Car >()
    {
        @Override
        public Car createFromParcel(Parcel in)
        {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size)
        {
            return new Car[size];
        }
    };


    /**
     * 获取车图片
     *
     * @param type 车图片类型(CarImg.Type_*)
     * @return 下午4:41:01
     */
    public CarImg getCarImg(int imgType)
    {
        int index = imgList.indexOf(new CarImg(imgType));
        if ( index >= 0 )
        {
            return imgList.get(index);
        }
        return null;
    }
}

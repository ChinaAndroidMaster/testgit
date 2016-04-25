package com.creditease.checkcars.data.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.utilframe.db.annotation.Id;
import com.creditease.utilframe.db.annotation.Table;

/***
 * 车信息
 *
 * @author 子龍
 * @function
 * @date 2015年7月6日
 * @company CREDITEASE
 */
@Table( name = "car_info" )
public class CarInfoBean implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 3112375379690464075L;

    /**
     * "name": "长安 SC5014XJH5 救护车", "brand": "长安", "productionDate": "2012年款 ", "model": "JL465Q5",
     * "engineType": "JL465Q5", "displacement": "39", "power": "1012", "type": "救护车", "reatedQuality":
     * "", "totalQuality": "", "equipmentQuality": "990", "combustionType": "", "emissionStandards":
     * "GB18352.2-2001", "shaftNum": "2", "shaftdistance": "2350", "shaftLoad": "", "springNum": "",
     * "tireNum": "4", "tireSpecifications": "165/70R13LT,155R13LT", "departureAngle": "28/29",
     * "beforeAfterHanging": "590/695", "beforeWheelTrack": "1280", "afterWheelTrack": "1290",
     * "carLong": "3635", "carWidth": "1475", "carHigh": "2100", "crateLong": "", "crateWidth": "",
     * "crateHight": "", "maxSpeed": "100", "carrying": "6", "cabCarring": "", "turnToType": "方向盘",
     * "trailerTotalQuality": "", "loadQualityFactor": "", "semiSaddleBearingQuelity": "",
     * "engineProducers": "重庆长安汽车股份有限公司"
     */

    public CarInfoBean()
    {
    }

    @Id
    public int _id;
    /**
     * vin码
     **/
    public String vin;
    /**
     * 产品名称
     */
    public String name;
    /**
     * 品牌
     */
    public String brand;
    /**
     * 制造年份
     */
    public String productionDate;
    /**
     * 产品型号
     */
    public String model;
    /**
     * 发动机型号
     */
    public String engineType;
    /**
     * 排量（CC）
     */
    public String displacement;
    /**
     * 功率（KW）
     */
    public String power;
    /**
     * 产品类型
     */
    public String type;
    /**
     * 额定质量
     */
    public String reatedQuality;
    /**
     * 总质量
     */
    public String totalQuality;
    /**
     * 装备质量
     */
    public String equipmentQuality;
    /**
     * 燃料种类
     */
    public String combustionType;
    /**
     * 排放依据标准
     */
    public String emissionStandards;
    /**
     * 轴数
     */
    public String shaftNum;
    /**
     * 轴距
     */
    public String shaftdistance;
    /**
     * 轴荷
     */
    public String shaftLoad;
    /**
     * 弹簧片数
     */
    public String springNum;
    /**
     * 轮胎数
     */
    public String tireNum;
    /**
     * 轮胎规格
     */
    public String tireSpecifications;
    /**
     * 接近离去角
     */
    public String departureAngle;
    /**
     * 前悬后悬
     */
    public String beforeAfterHanging;
    /**
     * 前轮距
     */
    public String beforeWheelTrack;
    /**
     * 后轮距
     */
    public String afterWheelTrack;
    /**
     * 整车长
     */
    public String carLong;
    /**
     * 整车宽
     */
    public String carWidth;
    /**
     * 整车高
     */
    public String carHigh;
    /**
     * 货箱长
     */
    public String crateLong;
    /**
     * 货箱宽
     */
    public String crateWidth;
    /**
     * 货箱高
     */
    public String crateHight;
    /**
     * 最高车速
     */
    public String maxSpeed;
    /**
     * 额定载客量
     */
    public String carrying;
    /**
     * 驾驶室准乘人数
     */
    public String cabCarring;
    /**
     * 转向形式
     */
    public String turnToType;
    /**
     * 准拖挂车总质量
     */
    public String trailerTotalQuality;
    /**
     * 载质量利用系数
     */
    public String loadQualityFactor;
    /**
     * 半挂车鞍座最大承载质量
     */
    public String semiSaddleBearingQuelity;
    /**
     * 发动机生产商
     */
    public String engineProducers;

    private CarInfoBean(Parcel in)
    {
        vin = in.readString();
        name = in.readString();
        brand = in.readString();
        productionDate = in.readString();
        model = in.readString();
        engineType = in.readString();
        displacement = in.readString();
        power = in.readString();
        type = in.readString();
        reatedQuality = in.readString();
        totalQuality = in.readString();
        equipmentQuality = in.readString();
        combustionType = in.readString();
        emissionStandards = in.readString();
        shaftNum = in.readString();
        shaftdistance = in.readString();
        shaftLoad = in.readString();
        springNum = in.readString();
        tireNum = in.readString();
        tireSpecifications = in.readString();
        departureAngle = in.readString();
        beforeAfterHanging = in.readString();
        beforeWheelTrack = in.readString();
        afterWheelTrack = in.readString();
        carLong = in.readString();
        carWidth = in.readString();
        carHigh = in.readString();
        crateLong = in.readString();
        crateWidth = in.readString();
        crateHight = in.readString();
        maxSpeed = in.readString();
        carrying = in.readString();
        cabCarring = in.readString();
        turnToType = in.readString();
        trailerTotalQuality = in.readString();
        loadQualityFactor = in.readString();
        semiSaddleBearingQuelity = in.readString();
        engineProducers = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(vin);
        dest.writeString(name);
        dest.writeString(brand);
        dest.writeString(productionDate);
        dest.writeString(model);
        dest.writeString(engineType);
        dest.writeString(displacement);
        dest.writeString(power);
        dest.writeString(type);
        dest.writeString(reatedQuality);
        dest.writeString(totalQuality);

        dest.writeString(equipmentQuality);
        dest.writeString(combustionType);
        dest.writeString(emissionStandards);
        dest.writeString(shaftNum);
        dest.writeString(shaftdistance);
        dest.writeString(shaftLoad);
        dest.writeString(springNum);
        dest.writeString(tireNum);
        dest.writeString(tireSpecifications);
        dest.writeString(departureAngle);

        dest.writeString(beforeAfterHanging);
        dest.writeString(beforeWheelTrack);
        dest.writeString(afterWheelTrack);
        dest.writeString(carLong);
        dest.writeString(carWidth);
        dest.writeString(carHigh);
        dest.writeString(crateLong);
        dest.writeString(crateWidth);
        dest.writeString(crateHight);
        dest.writeString(maxSpeed);

        dest.writeString(carrying);
        dest.writeString(cabCarring);
        dest.writeString(turnToType);
        dest.writeString(trailerTotalQuality);
        dest.writeString(loadQualityFactor);
        dest.writeString(semiSaddleBearingQuelity);
        dest.writeString(engineProducers);
    }

    public static final Parcelable.Creator< CarInfoBean > CREATOR =
            new Parcelable.Creator< CarInfoBean >()
            {
                public CarInfoBean createFromParcel(Parcel in)
                {
                    return new CarInfoBean(in);
                }

                public CarInfoBean[] newArray(int size)
                {
                    return new CarInfoBean[size];
                }
            };

    @Override
    public String toString()
    {
        return "name = " + name + "  engineProducers = " + engineProducers;
    }


}

package com.creditease.checkcars.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.checkcars.R;

/**
 * 车图片
 *
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */

public class CarImg implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4368429710720345450L;

    // 0车前45度1车后45度2厂牌3发动机舱4中控全貌5仪表盘6后备箱7基本信息封面图
    /**
     * 基本信息封面图
     */
    public static final int TYPE_IMG_BASE = 7;// 基本信息封面图
    /**
     * 车前45度
     */
    public static final int TYPE_IMG_FRONT = 0;// 车前45度
    /**
     * 车后45度
     */
    public static final int TYPE_IMG_BACK = 1;// 车后45度
    /**
     * 厂牌
     */
    public static final int TYPE_IMG_BRAND = 2;// 厂牌
    /**
     * 发动机舱
     */
    public static final int TYPE_IMG_ENGINE = 3;// 发动机舱
    /**
     * 中控全貌
     */
    public static final int TYPE_IMG_CCR = 4;// 中控全貌
    /**
     * 仪表盘
     */
    public static final int TYPE_IMG_PANEL = 5;// 仪表盘
    /**
     * 后备箱
     */
    public static final int TYPE_IMG_TRUNK = 6;// 后备箱
    /**
     * 底盘
     */
    public static final int TYPE_IMG_UNDERPAN = 8;// 底盘

    public String uuid;
    public String reportId;// 报告ID
    public int imgType;// 图片类型
    public String imgValue;// 图片地址
    public String imgName;// 图片名称

    public CarImg()
    {
    }

    public CarImg(int imgType, String imgName)
    {
        super();
        this.imgType = imgType;
        this.imgName = imgName;
    }

    public CarImg(int imgType)
    {
        super();
        this.imgType = imgType;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if ( o == null )
        {
            return false;
        }
        if ( o instanceof CarImg )
        {
            CarImg c = ( CarImg ) o;
            return c.imgType == imgType;
        } else
        {
            return false;
        }
    }

    private CarImg(Parcel in)
    {
        uuid = in.readString();
        reportId = in.readString();
        imgType = in.readInt();
        imgValue = in.readString();
        imgName = in.readString();
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
        dest.writeString(reportId);
        dest.writeInt(imgType);
        dest.writeString(imgValue);
        dest.writeString(imgName);
    }

    public static final Parcelable.Creator< CarImg > CREATOR = new Parcelable.Creator< CarImg >()
    {
        @Override
        public CarImg createFromParcel(Parcel in)
        {
            return new CarImg(in);
        }

        @Override
        public CarImg[] newArray(int size)
        {
            return new CarImg[size];
        }
    };

    public static List< CarImg > getCarImgs()
    {
        List< CarImg > list = new ArrayList< CarImg >();
        list.add(new CarImg(TYPE_IMG_FRONT, "车前45度"));
        list.add(new CarImg(TYPE_IMG_BACK, "车后45度"));
        list.add(new CarImg(TYPE_IMG_BRAND, "厂牌"));
        list.add(new CarImg(TYPE_IMG_ENGINE, "发动机舱"));
        list.add(new CarImg(TYPE_IMG_CCR, "中控全貌"));
        list.add(new CarImg(TYPE_IMG_PANEL, "仪表盘"));
        list.add(new CarImg(TYPE_IMG_TRUNK, "后备箱"));
        list.add(new CarImg(TYPE_IMG_UNDERPAN, "底盘前段"));
        return list;
    }

    public static String getName(Context context, int type)
    {
        switch ( type )
        {
            case TYPE_IMG_FRONT:
                return context.getResources().getString(R.string.carimg_front45);
            case TYPE_IMG_BACK:
                return context.getResources().getString(R.string.carimg_back45);
            case TYPE_IMG_BRAND:
                return context.getResources().getString(R.string.carimg_brand);
            case TYPE_IMG_ENGINE:
                return context.getResources().getString(R.string.carimg_engine);
            case TYPE_IMG_CCR:
                return context.getResources().getString(R.string.carimg_ccr);
            case TYPE_IMG_PANEL:
                return context.getResources().getString(R.string.carimg_panel);
            case TYPE_IMG_TRUNK:
                return context.getResources().getString(R.string.carimg_trunk);
            case TYPE_IMG_UNDERPAN:
                return context.getResources().getString(R.string.carimg_underpan);
        }
        return "";
    }

}

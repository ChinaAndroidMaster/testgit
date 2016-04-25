package com.creditease.checkcars.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车检分类
 *
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
public class CCClassify implements Parcelable, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 6239918747687028108L;
    public static final int SWITCH_YES = 0; // 开关：开
    public static final int SWITCH_NO = 1;// 开关：关

    public String uuid;
    public String reportId;// 报告ID
    public int attrValue;// 检测结果
    public String imgValue;// 图片
    public String name;// 检测名称
    public String desption;
    public String imgUrl;
    public CCItem mainItem;//主项目的修饰类
    public int doSwitch;// 是否检测该类目

    public ArrayList< CCItem > attrs;// 子检测项目

    public int progress = 0;

    public CCClassify()
    {
    }

    public CCClassify(int attrValue, String imgValue, String attrName)
    {
        super();
        this.attrValue = attrValue;
        this.imgValue = imgValue;
        name = attrName;
        attrs = CCItem.getTestData();
    }

    public static ArrayList< CCClassify > getTestData()
    {
        ArrayList< CCClassify > list = new ArrayList< CCClassify >();
        list.add(new CCClassify(0, "", "基础项目"));
        list.add(new CCClassify(0, "", "外表检测"));
        list.add(new CCClassify(0, "", "内部检测"));
        return list;
    }

    private CCClassify(Parcel in)
    {
        uuid = in.readString();
        reportId = in.readString();
        attrValue = in.readInt();
        imgValue = in.readString();
        name = in.readString();
        desption = in.readString();
        imgUrl = in.readString();
        doSwitch = in.readInt();
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
        dest.writeInt(attrValue);
        dest.writeString(imgValue);
        dest.writeString(name);
        dest.writeString(desption);
        dest.writeString(imgUrl);
        dest.writeInt(doSwitch);
    }

    public static final Parcelable.Creator< CCClassify > CREATOR =
            new Parcelable.Creator< CCClassify >()
            {
                @Override
                public CCClassify createFromParcel(Parcel in)
                {
                    return new CCClassify(in);
                }

                @Override
                public CCClassify[] newArray(int size)
                {
                    return new CCClassify[size];
                }
            };
}

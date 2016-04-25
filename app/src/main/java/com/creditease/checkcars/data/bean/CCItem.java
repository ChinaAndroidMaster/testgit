package com.creditease.checkcars.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 车检项目
 *
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
public class CCItem implements Parcelable, Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -2588482452664686343L;
    /**
     *
     */
    public static final String VALUE_DEFAULT = "-1";
    /**
     * 检测结果 yes
     */
    public static final String VALUE_YES = "0";
    /**
     * 检测结果no
     */
    public static final String VALUE_NO = "1";

    public static final int CHECKED_DEFAULT = 0;
    public static final int CHECKED_YES = 1;

    /**
     * 问题项，非必填
     */
    public static final int TYPE_REQUIRED_NO = 0;
    /**
     * 问题项，图片必拍
     */
    public static final int TYPE_REQUIRED_IMAGE = 1;

    /**
     * 问题项，备注必填
     */
    public static final int TYPE_REQUIRED_DESCEIB = 2;

    /**
     * 都必填
     */
    public static final int TYPE_REQUIRED_ALL = 3;

    /**
     * 回答类型：选择（0）
     */
    public static final int ANSWER_TYPE_SELECT = 0;
    /**
     * 回答类型：填写（1）
     */
    public static final int ANSWER_TYPE_INPUT = 1;

    /**
     * 问题类型（选填）
     */
    public static final int ANSWER_REQUIRED_UNNEED = 0;
    /**
     * 问题类型（必填）
     */
    public static final int ANSWER_REQUIRED_NEED = 1;
    /**
     * 是否检测某一大项
     */
    public static final int SWITCH_ON = 1;
    /**
     * 是否检测某一大项
     */
    public static final int SWITCH_OFF = 0;

    public int id;
    public String uuid;
    public String attributeId;
    public String name;
    public String reportId;// 报告ID
    public String attrValue = VALUE_DEFAULT;// 检测结果
    public String attrValueName;
    public String imgValue;// 图片
    public String attrName;// 检测名称
    public String remark;// 备注
    public int isChecked = CHECKED_DEFAULT;
    public int weight;// 检测项目重要级别：0重要1关注2注意3不重要4正常
    public int requiredType = TYPE_REQUIRED_NO;// 问题必操作项
    public List< Answer > answerList;
    public Answer answer;
    public int answerType;// 回答类型
    public int answerRequired;// 是否必操作
    public ArrayList< String > picPathList = new ArrayList< String >(3);
    public int isFill;
    public int selectPosition;

    public CCItem(String attrValue, String attrName)
    {
        super();
        this.attrValue = attrValue;
        name = this.attrName = attrName;
    }

    public CCItem(String attrName)
    {
        super();
        name = this.attrName = attrName;

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
        if ( o instanceof CCItem )
        {
            CCItem item = ( CCItem ) o;
            return ((item.attrName != null) && item.attrName.equals(attrName))
                    || ((item.name != null) && item.name.equals(name));
        }
        return false;
    }

    /**
     * 设置项目检测结果
     *
     * @param checked 下午12:42:53
     */
    public void valueAttr(boolean checked)
    {
        attrValue = checked ? VALUE_YES : VALUE_NO;
    }

    public void valueAttr(Answer value)
    {
        answer = value;
        if ( TextUtils.isEmpty(value.uuid) )
        {
            attrValue = value.value ? VALUE_YES : VALUE_NO;
        } else
        {
            attrValue = value.uuid;
            weight = value.weight;
            // attrValue = (weight == 4 )? VALUE_NO : VALUE_YES;
        }
        attrValueName = value.name;
    }

    public boolean isChecked()
    {
        return isChecked == CHECKED_YES;
    }

    public void checked()
    {
        isChecked = CHECKED_YES;
    }

    /**
     * 获取检测结果
     *
     * @return 下午12:43:41
     */
    public boolean getAttrValue()
    {
        return attrValue.equals(VALUE_YES);
    }

    public static ArrayList< CCItem > getTestData()
    {
        ArrayList< CCItem > list = new ArrayList< CCItem >();
        list.add(new CCItem(VALUE_DEFAULT, "1. 检测轮胎"));
        list.add(new CCItem(VALUE_DEFAULT, "2. 检测发动机"));
        list.add(new CCItem(VALUE_DEFAULT, "3. 检测内饰"));
        return list;
    }

    private CCItem(Parcel in)
    {
        id = in.readInt();
        uuid = in.readString();
        name = in.readString();
        reportId = in.readString();
        attrValue = in.readString();
        imgValue = in.readString();
        attrName = in.readString();
        isChecked = in.readInt();
        remark = in.readString();
        if ( TextUtils.isEmpty(name) )
        {
            name = attrName;
        } else
        {
            attrName = name;
        }
        weight = in.readInt();
        requiredType = in.readInt();
        answerType = in.readInt();
        answerRequired = in.readInt();
        selectPosition = in.readInt();
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
        dest.writeString(name);
        dest.writeString(reportId);
        dest.writeString(attrValue);
        dest.writeString(imgValue);
        dest.writeString(attrName);
        dest.writeInt(isChecked);
        dest.writeString(remark);
        dest.writeInt(weight);
        dest.writeInt(requiredType);
        dest.writeInt(answerType);
        dest.writeInt(answerRequired);
        dest.writeInt(selectPosition);
    }

    public static final Parcelable.Creator< CCItem > CREATOR = new Parcelable.Creator< CCItem >()
    {
        @Override
        public CCItem createFromParcel(Parcel in)
        {
            return new CCItem(in);
        }

        @Override
        public CCItem[] newArray(int size)
        {
            return new CCItem[size];
        }
    };
}

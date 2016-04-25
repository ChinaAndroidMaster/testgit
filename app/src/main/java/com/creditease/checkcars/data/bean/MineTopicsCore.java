package com.creditease.checkcars.data.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.utilframe.db.annotation.Id;
import com.creditease.utilframe.db.annotation.Table;

/**
 * @author zgb
 *         对师傅评价的基本信息
 */
@Table( name = "topics_tab" )
public class MineTopicsCore implements Parcelable, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -7211424521678216990L;
    @Id
    public String orderId;//订单uuid
    public int score;//评分
    public String remark;//评价
    public String fromId;//评价人
    public String toId;//被评价人
    public int operType;//操作类型 0 客户对评估师傅评价 1 评估师傅对客户评价
    public String headPic;//头像
    public String nickName;//昵称
    public Long createTime;
    public Long modifyTime;

    public MineTopicsCore()
    {
    }

    public MineTopicsCore(Parcel in)
    {
        orderId = in.readString();
        score = in.readInt();
        remark = in.readString();
        fromId = in.readString();
        toId = in.readString();
        operType = in.readInt();
        headPic = in.readString();
        nickName = in.readString();
        createTime = in.readLong();
        modifyTime = in.readLong();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(orderId);
        dest.writeInt(score);
        dest.writeString(remark);
        dest.writeString(fromId);
        dest.writeString(toId);
        dest.writeInt(operType);
        dest.writeString(headPic);
        dest.writeString(nickName);
        dest.writeLong(createTime);
        dest.writeLong(modifyTime);
    }


    public static final Parcelable.Creator< MineTopicsCore > CREATOR = new Parcelable.Creator< MineTopicsCore >()
    {
        public MineTopicsCore createFromParcel(Parcel in)
        {
            return new MineTopicsCore(in);
        }

        public MineTopicsCore[] newArray(int size)
        {
            return new MineTopicsCore[size];
        }
    };
}

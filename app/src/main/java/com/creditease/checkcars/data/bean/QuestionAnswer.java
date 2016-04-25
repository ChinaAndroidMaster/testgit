package com.creditease.checkcars.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.creditease.utilframe.db.annotation.Id;
import com.creditease.utilframe.db.annotation.Table;

/**
 * 问答模块的实体类
 *
 * @author zgb
 */
@Table( name = "question_tab" )
public class QuestionAnswer implements Parcelable, Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 5850582387007584242L;
    @Id
    public String uuid;// 问题uuid
    public long createTime;// 问题创建时间
    public long modifyTime;// 问题更新时间
    public String title;// 问题
    public String fromId;// 问题发起人id
    public String answerId;// 被采纳答案的回复人的id
    public String source;// 问题来源
    public String sourceUrl;// 问题来源url
    public String imgValue;// 图片地址集合,以；号分割
    public int score;// 问题评级
    public int examine;// 问题状态 0 审核通过 1 等待审核 2 审核不通过
    public int status;// 状态
    public String remark;// 问题描述
    public List< QAnswer > answers = new ArrayList< QAnswer >();// 回复内容列表
    public UserInfo userInfo;// 提问问题的人的基本资料
    public String qanswerJson;// 回复列表的json格式串
    public String userInfoJson;// userinfo对象的json

    public QuestionAnswer()
    {

    }

    private QuestionAnswer(Parcel in)
    {
        uuid = in.readString();
        createTime = in.readLong();
        modifyTime = in.readLong();
        title = in.readString();
        fromId = in.readString();
        answerId = in.readString();
        source = in.readString();
        sourceUrl = in.readString();
        imgValue = in.readString();
        score = in.readInt();
        examine = in.readInt();
        if ( answers != null )
        {
            in.readList(answers, QAnswer.class.getClassLoader());
        }
        userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        qanswerJson = in.readString();
        userInfoJson = in.readString();
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
        dest.writeLong(createTime);
        dest.writeLong(modifyTime);
        dest.writeString(title);
        dest.writeString(fromId);
        dest.writeString(answerId);
        dest.writeString(source);
        dest.writeString(sourceUrl);
        dest.writeString(imgValue);
        dest.writeInt(score);
        dest.writeInt(examine);
        dest.writeList(answers);
        dest.writeParcelable(userInfo, flags);
        dest.writeString(qanswerJson);
        dest.writeString(userInfoJson);
    }

    public static final Parcelable.Creator< QuestionAnswer > CREATOR = new Parcelable.Creator< QuestionAnswer >()
    {
        public QuestionAnswer createFromParcel(Parcel in)
        {
            return new QuestionAnswer(in);
        }

        public QuestionAnswer[] newArray(int size)
        {
            return new QuestionAnswer[size];
        }
    };
}

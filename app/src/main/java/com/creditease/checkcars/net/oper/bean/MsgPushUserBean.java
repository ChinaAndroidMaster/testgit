package com.creditease.checkcars.net.oper.bean;

/**
 * @author 子龍
 * @function
 * @date 2015年4月24日
 * @company CREDITEASE
 */
public class MsgPushUserBean
{

    public String uuid;// 师傅的UUID
    public String alias;// 别名
    public String aliasType;// 别名类型
    public String deviceToken;// token
    public String tag;// 用户标签
    public int platformType;// 0 Android, 1 IOS

    public MsgPushUserBean()
    {
    }

    public MsgPushUserBean(String uuid, String alias, String aliasType, String deviceToken,
                           String tag, int platformType)
    {
        super();
        this.uuid = uuid;
        this.alias = alias;
        this.aliasType = aliasType;
        this.deviceToken = deviceToken;
        this.tag = tag;
        this.platformType = platformType;
    }

}

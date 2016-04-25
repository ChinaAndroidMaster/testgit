/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper.base;

/**
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public enum Result
{
    RESULT_SUCCESS, // 成功
    RESULT_FAILED, // 失败
    RESULT_UNKNOW;// 未知

    public static Result getResult(int status)
    {
        Result s = null;
        Result[] values = Result.values();
        for ( Result v : values )
        {
            if ( v.ordinal() == status )
            {
                s = v;
                break;
            }
        }
        return s;
    }
}

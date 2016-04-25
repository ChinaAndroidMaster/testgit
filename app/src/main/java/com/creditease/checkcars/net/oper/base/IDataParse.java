/**
 * Copyright © 2014 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper.base;

import android.os.Bundle;

import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;

/**
 * @author 子龍
 * @date 2014年10月23日
 * @company 宜信
 */
public interface IDataParse
{

    /**
     * 下行数据解析
     *
     * @param result
     * @return
     * @throws CERequestException
     */
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException;

}

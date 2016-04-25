/**
 * Copyright © 2014 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.oper.base;

import com.creditease.checkcars.exception.CERequestException;

/**
 * @author 子龍
 * @date 2014年10月23日
 * @company 宜信
 */
public interface IDataSaver
{

    /**
     * 下行数据存储
     *
     * @param result
     * @return
     * @throws CERequestException
     */
    public void saver() throws CERequestException;

}

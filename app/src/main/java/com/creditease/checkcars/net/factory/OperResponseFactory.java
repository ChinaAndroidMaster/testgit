package com.creditease.checkcars.net.factory;

import android.text.TextUtils;

import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.base.Result;
import com.creditease.checkcars.net.oper.bean.OperResponseForWeixin;
import com.creditease.checkcars.tools.JsonUtils;
import com.google.gson.JsonSyntaxException;

/**
 * @author 子龍
 * @function
 * @date 2015年3月12日
 * @company CREDITEASE
 */
public final class OperResponseFactory
{
    public static OperResponse parseResult(String wsResponse) throws CERequestException,
            CEFailtureException
    {
        if ( TextUtils.isEmpty(wsResponse) )
        {
            throw new CERequestException(wsResponse);
        }
        try
        {
            OperResponse res = JsonUtils.parserJsonStringToObject(wsResponse, OperResponse.class);
            res.result = "0000".equals(res.respcode) ? Result.RESULT_SUCCESS : Result.RESULT_FAILED;
            if ( res.result.equals(Result.RESULT_FAILED) )
            {
                throw new CEFailtureException(res);
            }
            return res;
        } catch ( JsonSyntaxException e )
        {
            throw new CEFailtureException("JsonSyntaxException", e, null);
        }
    }

    public static OperResponseForWeixin parseResultForWeixin(String wsResponse)
            throws CERequestException, CEFailtureException
    {
        if ( wsResponse == null )
        {
            throw new CERequestException(wsResponse);
        }
        OperResponseForWeixin res =
                JsonUtils.parserJsonStringToObject(wsResponse, OperResponseForWeixin.class);
        res.result = res.code == 0 ? Result.RESULT_SUCCESS : Result.RESULT_FAILED;
        if ( res.result.equals(Result.RESULT_FAILED) )
        {
            throw new CEFailtureException(res);
        }
        return res;
    }
}

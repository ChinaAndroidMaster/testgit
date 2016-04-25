package com.creditease.checkcars.data.db.base;

import android.os.Bundle;

import com.creditease.checkcars.exception.CEDbException;

public interface DBOperCallBack
{

    public void dataCallBack(int operId, Bundle result);


    public Bundle doDBOperation(DbHelper helper, int operId) throws CEDbException;

    public void errorCallBack(int operId, CEDbException e);
}

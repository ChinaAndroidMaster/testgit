package com.creditease.checkcars.data.db.base;

import java.util.List;

import com.creditease.checkcars.exception.CEDbException;

public interface DBFindCallBack< T >
{

    public void dataCallBack(int operId, List< T > result);

    public List< T > doDBOperation(DbHelper helper, int operId) throws CEDbException;

    public void errorCallBack(int operId, CEDbException e);
}

package com.creditease.checkcars.data.db.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.creditease.checkcars.exception.CEDbException;

/**
 * 用于数据表操作基类
 *
 * @param <T>
 * @author 子龍
 * @date 2014年12月18日
 * @company 宜信
 */
public abstract class DBUtil< T >
{

    protected DbHelper helper;
    protected Context mContext;

    protected DBUtil(Context mContext)
    {
        helper = DbHelper.getDBHelper(mContext);
        this.mContext = mContext;
    }

    /**
     * 删除表数据
     *
     * @param clazz
     * @throws CEDbException
     */
    public void deleteAll(Class< T > clazz) throws CEDbException
    {
        helper.deleteAll(clazz);
    }

    /**
     * 删除数据
     *
     * @param <T>
     * @param t
     */
    public void deleteObjFromDB(Class< T > entityType, DBWhereBuilder whereBuilder)
            throws CEDbException
    {
        helper.delete(entityType, whereBuilder);
    }

    /**
     * 删除数据
     *
     * @param selector
     * @throws CEDbException
     */
    public void deleteObjFromDB(DBSelector selector) throws CEDbException
    {
        helper.delete(selector);
    }

    /**
     * 删除数据
     *
     * @param <T>
     * @param t
     */
    public void deleteObjFromDB(T t) throws CEDbException
    {
        helper.delete(t);
    }

    /**
     * 获取最新数据
     *
     * @param venderId
     * @return
     */
    public T getNewestObjFromDB(DBSelector selector) throws CEDbException
    {
        return helper.findFirst(selector);
    }

    /**
     * 获取列表
     *
     * @param <T>
     * @param venderId
     * @param param
     * @return
     */
    public List< T > getObjListFromDB(DBSelector selector) throws CEDbException
    {
        return helper.findAll(selector);
    }

    public boolean isTableExistData(Class< T > clazz) throws CEDbException
    {
        return helper.count(clazz) > 0;
    }

    /**
     * 保存所有数据
     *
     * @param tList
     * @throws CEDbException
     */
    public void saveAll(ArrayList< T > tList) throws CEDbException
    {
        helper.saveAll(tList);
    }

    public void saveObjToDB(T t) throws CEDbException
    {
        helper.save(t);
    }

    /**
     * 保存/更新数据列表
     *
     * @param clazzEntity
     * @param tList
     * @param dbWB
     * @throws CEDbException
     */
    public void saveOrUpdateObjListToDB(Class< T > clazzEntity, ArrayList< T > tList, DBWhereBuilder dbWB)
            throws CEDbException
    {
        if ( helper.isTableExist(clazzEntity) && (helper.count(clazzEntity) > 0) )
        {
            helper.delete(clazzEntity, dbWB);
        }
        helper.saveAll(tList);
    }

    /**
     * 保存/更新数据
     *
     * @param clazzEntity
     * @param t
     * @param dbWB
     * @throws CEDbException
     */
    public void saveOrUpdateObjToDB(Class< T > clazzEntity, T t, DBWhereBuilder dbWB)
            throws CEDbException
    {
        helper.delete(clazzEntity, dbWB);
        helper.save(t);
    }

    /**
     * 保存/更新数据
     *
     * @param t
     * @param whereBuilder
     * @throws CEDbException
     */
    public void updateAndSaveObj(T t, DBWhereBuilder whereBuilder) throws CEDbException
    {
        DBSelector selector = DBSelector.from(t.getClass()).where(whereBuilder);
        Object o = helper.findFirst(selector);
        if ( o != null )
        {
            @SuppressWarnings( "unchecked" )
            Class< T > clazz = ( Class< T > ) t.getClass();
            helper.delete(clazz, whereBuilder);
        }
        helper.save(t);
    }

    public void updateObjToDB(T t, DBWhereBuilder dbWB) throws CEDbException
    {
        helper.update(t, dbWB);
    }
}

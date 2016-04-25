package com.creditease.checkcars.data.db.base;

import com.creditease.utilframe.db.sqlite.DbModelSelector;
import com.creditease.utilframe.db.sqlite.WhereBuilder;

public class CEDbModeSelector
{
    public static CEDbModeSelector from(Class< ? > entityType)
    {
        return new CEDbModeSelector(entityType);
    }

    public DbModelSelector dbMSeletor;

    private CEDbModeSelector(Class< ? > entityType)
    {
        dbMSeletor = DbModelSelector.from(entityType);
    }

    public CEDbModeSelector and(String columnName, String op, Object value)
    {
        dbMSeletor.and(columnName, op, value);
        return this;
    }

    public CEDbModeSelector and(WhereBuilder where)
    {
        dbMSeletor.and(where);
        return this;
    }

    public CEDbModeSelector expr(String expr)
    {
        dbMSeletor.expr(expr);
        return this;
    }

    public CEDbModeSelector expr(String columnName, String op, Object value)
    {
        dbMSeletor.expr(columnName, op, value);
        return this;
    }

    public Class< ? > getEntityType()
    {
        return dbMSeletor.getEntityType();
    }

    public CEDbModeSelector groupBy(String columnName)
    {
        dbMSeletor.groupBy(columnName);
        return this;
    }

    public CEDbModeSelector having(WhereBuilder whereBuilder)
    {
        dbMSeletor.having(whereBuilder);
        return this;
    }

    public CEDbModeSelector limit(int limit)
    {
        dbMSeletor.limit(limit);
        return this;
    }

    public CEDbModeSelector offset(int offset)
    {
        dbMSeletor.offset(offset);
        return this;
    }

    public CEDbModeSelector or(String columnName, String op, Object value)
    {
        dbMSeletor.or(columnName, op, value);
        return this;
    }

    public CEDbModeSelector or(WhereBuilder where)
    {
        dbMSeletor.or(where);
        return this;
    }

    public CEDbModeSelector orderBy(String columnName)
    {
        dbMSeletor.orderBy(columnName);
        return this;
    }

    public CEDbModeSelector orderBy(String columnName, boolean desc)
    {
        dbMSeletor.orderBy(columnName, desc);
        return this;
    }

    public CEDbModeSelector select(String... columnExpressions)
    {
        dbMSeletor.select(columnExpressions);
        return this;
    }

    @Override
    public String toString()
    {
        return dbMSeletor.toString();
    }

    public CEDbModeSelector where(String columnName, String op, Object value)
    {
        dbMSeletor.where(columnName, op, value);
        return this;
    }

    public CEDbModeSelector where(WhereBuilder whereBuilder)
    {
        dbMSeletor.where(whereBuilder);
        return this;
    }
}

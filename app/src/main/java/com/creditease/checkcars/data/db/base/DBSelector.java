package com.creditease.checkcars.data.db.base;

import com.creditease.utilframe.db.sqlite.Selector;
import com.creditease.utilframe.db.sqlite.WhereBuilder;

public class DBSelector
{

    public static DBSelector from(Class< ? > entityType)
    {
        return new DBSelector(entityType);
    }

    public Selector selector;

    private DBSelector(Class< ? > entityType)
    {
        selector = Selector.from(entityType);
    }

    public DBSelector and(String columnName, String op, Object value)
    {
        selector.and(columnName, op, value);
        return this;
    }

    public DBSelector and(WhereBuilder where)
    {
        if ( where != null )
        {
            selector.and(where);
        }
        return this;
    }

    public DBSelector expr(String expr)
    {
        selector.expr(expr);
        return this;
    }

    public DBSelector expr(String columnName, String op, Object value)
    {
        selector.expr(columnName, op, value);
        return this;
    }

    public DBSelector limit(int limit)
    {
        selector.limit(limit);
        return this;
    }

    public DBSelector offset(int offset)
    {
        selector.offset(offset);
        return this;
    }

    public DBSelector or(String columnName, String op, Object value)
    {
        selector.or(columnName, op, value);
        return this;
    }

    public DBSelector or(WhereBuilder where)
    {
        if ( where != null )
        {
            selector.or(where);
        }
        return this;
    }

    public DBSelector orderBy(String columnName)
    {
        selector.orderBy(columnName);
        return this;
    }

    public DBSelector orderBy(String columnName, boolean desc)
    {
        selector.orderBy(columnName, desc);
        return this;
    }

    public DBSelector where(DBWhereBuilder whereBuilder)
    {
        if ( whereBuilder != null )
        {
            selector.where(whereBuilder.wb);
        }
        return this;
    }

    public DBSelector where(String columnName, String op, Object value)
    {
        selector.where(columnName, op, value);
        return this;
    }

    public DBSelector where(WhereBuilder whereBuilder)
    {
        if ( whereBuilder != null )
        {
            selector.where(whereBuilder);
        }
        return this;
    }

}

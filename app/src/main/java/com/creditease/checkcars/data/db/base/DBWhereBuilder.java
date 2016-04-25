package com.creditease.checkcars.data.db.base;

import com.creditease.utilframe.db.sqlite.WhereBuilder;

public class DBWhereBuilder
{

    /**
     * create new instance
     *
     * @return
     */
    public static DBWhereBuilder b()
    {
        return new DBWhereBuilder();
    }

    /**
     * create new instance
     *
     * @param columnName
     * @param op         operator: "=","<","LIKE","IN","BETWEEN"...
     * @param value
     * @return
     */
    public static DBWhereBuilder b(String columnName, String op, Object value)
    {
        return new DBWhereBuilder(columnName, op, value);
    }

    public WhereBuilder wb;

    private DBWhereBuilder()
    {
        wb = WhereBuilder.b();
    }

    private DBWhereBuilder(String columnName, String op, Object value)
    {
        wb = WhereBuilder.b(columnName, op, value);
    }

    /**
     * add AND condition
     *
     * @param columnName
     * @param op         operator: "=","<","LIKE","IN","BETWEEN"...
     * @param value
     * @return
     */
    public DBWhereBuilder and(String columnName, String op, Object value)
    {
        wb.and(columnName, op, value);
        return this;
    }

    public DBWhereBuilder expr(String expr)
    {
        wb.expr(expr);
        return this;
    }

    public DBWhereBuilder expr(String columnName, String op, Object value)
    {
        wb.expr(columnName, op, value);
        return this;
    }

    public int getWhereItemSize()
    {
        return wb.getWhereItemSize();
    }

    /**
     * add OR condition
     *
     * @param columnName
     * @param op         operator: "=","<","LIKE","IN","BETWEEN"...
     * @param value
     * @return
     */
    public DBWhereBuilder or(String columnName, String op, Object value)
    {
        wb.or(columnName, op, value);
        return this;
    }

    @Override
    public String toString()
    {
        return wb.toString();
    }

}

package com.creditease.checkcars.data.db.base;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.creditease.checkcars.exception.CEDbException;
import com.creditease.utilframe.DbUtils;
import com.creditease.utilframe.DbUtils.DbUpgradeListener;
import com.creditease.utilframe.db.table.DbModel;
import com.creditease.utilframe.exception.DbException;

/**
 * DbUtils封装类
 *
 * @author 子龍
 * @date 2014年12月18日
 * @company 宜信
 */
public class DbHelper
{

    public static final String DB_NAME = "checkcars_db";
    public static final int DB_VERSION = 18;

    private static DbHelper helper;
    private static DbUtils dbUtils;

    public static DbHelper getDBHelper(Context context)
    {
        if ( helper == null )
        {
            helper = new DbHelper(context);
        }
        return helper;
    }

    public static void initDB(final Context mContext)
    {
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                if ( helper == null )
                {
                    helper = new DbHelper(mContext);
                }
            }
        }).start();

    }

    @SuppressWarnings( "unused" )
    private Context mContext;

    private DbUpgradeListener dbListener = new DbUpgradeListener()
    {

        @Override
        public void onUpgrade(DbUtils db, int oldVersion, int newVersion)
        {
            try
            {
                switch ( oldVersion )
                {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 7:
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN quantity varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN businessId varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isPreCheck integer");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isCashTransfer integer");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN orderNum varchar");

                        db.execNonQuery("ALTER TABLE appraiser_tab ADD COLUMN salerHomePageUrl varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN payType integer");

                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN reason varchar");

                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN startTime integer");
                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN endTime integer");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN remark varchar");

                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN isSync integer");
                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN clientUuid varchar");
                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN requiredType integer");
                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN weight integer");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN assignTime varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN tradeAmount varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN p_name varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN serviceType integer");

                        break;
                    case 10:
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN quantity varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN businessId varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isPreCheck integer");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isCashTransfer integer");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN orderNum varchar");

                        db.execNonQuery("ALTER TABLE appraiser_tab ADD COLUMN salerHomePageUrl varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN payType integer");

                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN reason varchar");

                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN startTime integer");
                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN endTime integer");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN remark varchar");

                        break;
                    case 11:
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN quantity varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN businessId varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isPreCheck integer");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isCashTransfer integer");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN orderNum varchar");

                        db.execNonQuery("ALTER TABLE appraiser_tab ADD COLUMN salerHomePageUrl varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN payType integer");

                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN reason varchar");

                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN startTime integer");
                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN endTime integer");


                        break;
                    case 12:
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN quantity varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN businessId varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isPreCheck integer");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isCashTransfer integer");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN orderNum varchar");

                        db.execNonQuery("ALTER TABLE appraiser_tab ADD COLUMN salerHomePageUrl varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN payType integer");
                        db.execNonQuery("ALTER TABLE cc_report_tab ADD COLUMN reason varchar");


                        break;
                    case 13:
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN quantity varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN businessId varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isPreCheck integer");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isCashTransfer integer");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN orderNum varchar");

                        db.execNonQuery("ALTER TABLE appraiser_tab ADD COLUMN salerHomePageUrl varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN payType integer");

                        break;
                    case 14:
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN quantity varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN businessId varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isPreCheck integer");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isCashTransfer integer");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN orderNum varchar");

                        db.execNonQuery("ALTER TABLE appraiser_tab ADD COLUMN salerHomePageUrl varchar");

                        break;
                    case 15:
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN quantity varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN businessId varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isPreCheck integer");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isCashTransfer integer");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN orderNum varchar");

                    case 16:
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN quantity varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN businessId varchar");

                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isCashTransfer integer");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN isPreCheck integer");
                        break;
                    case 17:
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN quantity varchar");
                        db.execNonQuery("ALTER TABLE carorder_tab ADD COLUMN businessId varchar");
                        break;
                    default:
                        break;
                }
            } catch ( DbException e )
            {
                Log.e("DBUtil",
                        ("DbException" + e) == null ? "ADD COLUMN serviceType error" : e.getMessage());
            }

        }
    };

    private DbHelper(Context mContext)
    {
        this.mContext = mContext;
        try
        {
            dbUtils = DbUtils.create(mContext, DB_NAME, DB_VERSION, dbListener);
        } catch ( Exception e )
        {
        }
    }

    // public DbUtils getDbUtils() {
    // if (dbUtils == null) {
    // try {
    // dbUtils = DbUtils.create(mContext, DB_NAME, DB_VERSION,
    // dbListener);
    // } catch (Exception e) {
    // }
    // }
    // return dbUtils;
    // }

    /**
     * 统计表中数量
     *
     * @param <T>
     * @param entityType
     * @return
     * @throws CEDbException
     */
    public < T > long count(Class< T > entityType) throws CEDbException
    {
        try
        {
            return dbUtils.count(entityType);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 统计数量
     *
     * @param selector
     * @return
     * @throws CEDbException
     */
    public long count(DBSelector selector) throws CEDbException
    {
        if ( selector == null )
        {
            throw new CEDbException("selector can not be null");
        }
        try
        {
            return dbUtils.count(selector.selector);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 删除数据
     *
     * @param <T>
     * @param entityType
     * @param whereBuilder
     * @throws CEDbException
     */
    public < T > void delete(Class< T > entityType, DBWhereBuilder whereBuilder) throws CEDbException
    {
        if ( whereBuilder == null )
        {
            throw new CEDbException("whereBuilder can not be null");
        }
        try
        {
            dbUtils.delete(entityType, whereBuilder.wb);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 删除数据
     *
     * @param entity
     * @throws CEDbException
     */
    public < T > void delete(T entity) throws CEDbException
    {
        try
        {
            dbUtils.delete(entity);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 删除表中所有数据
     *
     * @param <T>
     * @param entityType
     * @throws CEDbException
     */
    public < T > void deleteAll(Class< T > entityType) throws CEDbException
    {
        try
        {
            dbUtils.deleteAll(entityType);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 删除数据列表
     *
     * @param <T>
     * @param entities
     * @throws CEDbException
     */
    public < T > void deleteAll(List< T > entities) throws CEDbException
    {
        try
        {
            dbUtils.deleteAll(entities);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 获取数据列表
     *
     * @param <T>
     * @param entityType
     * @return
     * @throws CEDbException
     */
    public < T > List< T > findAll(Class< T > entityType) throws CEDbException
    {
        try
        {
            return dbUtils.findAll(entityType);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 获取数据列表
     *
     * @param <T>
     * @param selector
     * @return
     * @throws CEDbException
     */
    public < T > List< T > findAll(DBSelector selector) throws CEDbException
    {
        if ( selector == null )
        {
            throw new CEDbException("selector can not be null");
        }
        try
        {
            return dbUtils.findAll(selector.selector);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * @param selector
     * @return
     * @throws CEDbException
     */
    public DbModel findDbModelFirst(CEDbModeSelector selector) throws CEDbException
    {
        try
        {
            return dbUtils.findDbModelFirst(selector.dbMSeletor);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 获取表中第一条数据
     *
     * @param <T>
     * @param entityType
     * @return
     * @throws CEDbException
     */
    public < T > T findFirst(Class< T > entityType) throws CEDbException
    {
        try
        {
            return dbUtils.findFirst(entityType);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 获取第一条数据
     *
     * @param <T>
     * @param selector
     * @return
     * @throws CEDbException
     */
    public < T > T findFirst(DBSelector selector) throws CEDbException
    {
        if ( selector == null )
        {
            throw new CEDbException("selector can not be null");
        }
        try
        {
            return dbUtils.findFirst(selector.selector);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 判断表是否存在
     *
     * @param entityType
     * @return
     * @throws CEDbException
     */
    public < T > boolean isTableExist(Class< T > entityType) throws CEDbException
    {
        try
        {
            return dbUtils.tableIsExist(entityType);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 保存数据
     *
     * @param entity
     * @throws CEDbException
     */
    public void save(Object entity) throws CEDbException
    {
        try
        {
            dbUtils.save(entity);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 保存数据列表
     *
     * @param <T>
     * @param entities
     * @throws CEDbException
     */
    public < T > void saveAll(List< T > entities) throws CEDbException
    {
        try
        {
            dbUtils.saveAll(entities);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

    /**
     * 更新数据
     *
     * @param entity
     * @param whereBuilder
     * @throws CEDbException
     */
    public < T > void update(T entity, DBWhereBuilder whereBuilder) throws CEDbException
    {
        try
        {
            dbUtils.update(entity, whereBuilder.wb);
        } catch ( DbException e )
        {
            throw new CEDbException(e);
        } catch ( Exception e )
        {
            throw new CEDbException(e);
        }
    }

}

package com.creditease.checkcars.net.uploadimg;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.creditease.checkcars.data.db.base.DBSelector;
import com.creditease.checkcars.data.db.base.DBUtil;
import com.creditease.checkcars.data.db.base.DBWhereBuilder;
import com.creditease.checkcars.exception.CEDbException;

/**
 * 上传图片数据库工具类
 *
 * @author 子龍
 * @date 2014年12月4日
 * @company 宜信
 */
public class UploadImageDBUtil extends DBUtil< UploadImage >
{
    private static UploadImageDBUtil tUtil;

    private UploadImageDBUtil(Context context)
    {
        super(context);
    }

    public static UploadImageDBUtil getDBUtil(Context context)
    {
        if ( tUtil == null )
        {
            tUtil = new UploadImageDBUtil(context);
        }
        return tUtil;
    }

    public void deleteUploadImage(String venderId, String uploadId) throws CEDbException
    {
        DBSelector s =
                DBSelector.from(UploadImage.class).where("venderId", "=", venderId)
                        .and("uploadId", "=", uploadId);
        super.deleteObjFromDB(s);
    }

    public List< UploadImage > getUploadImageList(String venderId) throws CEDbException
    {
        DBSelector selector = DBSelector.from(UploadImage.class).where("venderId", "=", venderId);
        return super.getObjListFromDB(selector);
    }

    public void saveOrUpdateUploadImage(UploadImage image) throws CEDbException
    {
        if ( image == null )
        {
            return;
        }
        super.saveOrUpdateObjToDB(UploadImage.class, image,
                DBWhereBuilder.b("uploadId", "=", image.uploadId));
    }

    /**
     * 保存渠道数据
     *
     * @param uploadImageList
     * @throws CEDbException
     */
    public void saveOrUpdateUploadImageList(ArrayList< UploadImage > uploadImageList)
            throws CEDbException
    {
        if ( uploadImageList == null )
        {
            return;
        }
        int commentListSize = uploadImageList.size();
        if ( commentListSize > 0 )
        {
            List< String > needToDeleteChannelIds = new ArrayList< String >(uploadImageList.size());
            for ( int i = 0; i < commentListSize; i++ )
            {
                needToDeleteChannelIds.add(uploadImageList.get(i).uploadId);
            }
            super.saveOrUpdateObjListToDB(UploadImage.class, uploadImageList,
                    DBWhereBuilder.b("uploadId", "in", needToDeleteChannelIds.toArray()));
        }
    }

}

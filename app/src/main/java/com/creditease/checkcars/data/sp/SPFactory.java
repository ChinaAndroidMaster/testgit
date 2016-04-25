package com.creditease.checkcars.data.sp;

import android.content.Context;

import com.creditease.checkcars.data.sp.base.SP;
import com.creditease.checkcars.data.sp.base.SPConfig;

public class SPFactory
{

    /**
     * @param sp_index SPConfig 选择数据存储在某个sp文件中 SHARED_PREFS_FILENAME_DEFAULT_INDEX :0x0001
     *                 SHARED_PREFS_FILENAME_LOGIN_INDEX : 0x0002 SHARED_PREFS_FILENAME_ACCOUNT_INDEX : 0x0003
     * @return
     */
    public static SP factory(Context context, int sp_index)
    {
        switch ( sp_index )
        {
            case SPConfig.SHARED_PREFS_FILENAME_DEFAULT_INDEX:
                return DefaultSP.getSp(context);
            case SPConfig.SHARED_PREFS_FILENAME_LOGIN_INDEX:
                return LoginSP.getSp(context);
            case SPConfig.SHARED_PREFS_FILENAME_ACCOUNT_INDEX:
                return AccountSP.getSp(context);
        }
        return DefaultSP.getSp(context);
    }
}

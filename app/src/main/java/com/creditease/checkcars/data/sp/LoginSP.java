package com.creditease.checkcars.data.sp;

import android.content.Context;

import com.creditease.checkcars.data.sp.base.SP;
import com.creditease.checkcars.data.sp.base.SPConfig;

public class LoginSP extends SP
{

    public static SP getSp(Context context)
    {
        if ( sp == null )
        {
            sp = new LoginSP(context);
        }
        return sp;
    }

    protected LoginSP(Context context)
    {
        super(context);
    }

    @Override
    public String getSPFileName()
    {
        return SPConfig.SHARED_PREFS_FILENAME_LOGIN;
    }

}

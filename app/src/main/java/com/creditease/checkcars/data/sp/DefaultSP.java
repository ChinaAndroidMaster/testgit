package com.creditease.checkcars.data.sp;

import android.content.Context;

import com.creditease.checkcars.data.sp.base.SP;
import com.creditease.checkcars.data.sp.base.SPConfig;

public class DefaultSP extends SP
{

    public static SP getSp(Context context)
    {
        if ( sp == null )
        {
            sp = new DefaultSP(context);
        }
        return sp;
    }

    protected DefaultSP(Context context)
    {
        super(context);
    }

    @Override
    public String getSPFileName()
    {
        return SPConfig.SHARED_PREFS_FILENAME;
    }

}

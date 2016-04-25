package com.creditease.checkcars;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.creditease.checkcars.data.bean.UserCore;
import com.creditease.checkcars.data.db.BaseReportUtil;
import com.creditease.checkcars.data.db.UserUtil;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.OperHandler;
import com.creditease.checkcars.tools.CommitOrderEventUtils;
import com.creditease.checkcars.ui.act.LoginActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.main.MainActivity;
import com.creditease.checkcars.ui.act.main.saler.MainSalerActivity;

public class AppStartActivity extends BaseActivity
{

    private final long LOGIN_USEFUL_TIME = 1000 * 60 * 60 * 24 * 15;

    @Override
    public BaseActivity getContext()
    {
        return this;
    }

    @Override
    protected void initData()
    {
        OperHandler.initOperHandler();
        BaseReportUtil.getBRUtil(getApplicationContext()).saveOrUpdateBaseReport(
                getApplicationContext(), null);
        final String userId = SharePrefenceManager.getUserId(getApplicationContext());

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                Intent intent = null;
                if ( TextUtils.isEmpty(userId) || isOutoffDate() )
                {
                    intent = new Intent(getContext(), LoginActivity.class);
                } else
                {
                    try
                    {
                        UserCore user = UserUtil.getUtil(getApplicationContext()).getUser(userId);
                        if ( user == null )
                        {
                            intent = new Intent(getContext(), LoginActivity.class);
                        } else if ( user.judgeUserType(UserCore.USER_TYPE_APPRAISER) )
                        {
                            intent = new Intent(getContext(), MainActivity.class);
                        } else if ( user.judgeUserType(UserCore.USER_TYPE_MARKET) )
                        {
                            intent = new Intent(getContext(), MainSalerActivity.class);
                        }
                    } catch ( CEDbException e )
                    {
                        e.printStackTrace();
                    }

                }
                startActivity(intent);
                getContext().finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        }, 2000);
        if ( !TextUtils.isEmpty(userId) )
        {
            // 提交保存在本地的订单和报告的事件记录
            CommitOrderEventUtils.getUtil(getApplicationContext()).recordTime(null, -1, -1, null);
        }

    }

    @Override
    protected void initEvents()
    {

    }

    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_main);
    }

    /**
     * 登录是否超时
     *
     * @return
     */
    private boolean isOutoffDate()
    {
        long currentTime = System.currentTimeMillis();
        long lastTime = SharePrefenceManager.getLastLoginTime(this, -1);
        long diff = currentTime - lastTime;
        if ( (lastTime > 0) && ((diff - LOGIN_USEFUL_TIME) >= 0) )
        {
            return true;
        }
        SharePrefenceManager.setLoginTime(this, currentTime);
        return false;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    // private void updateAllOrders() {
    // String currentVer = Util.getAppVersionName(this);
    // String ver = SharePrefenceManager.getAppVersion(this);
    // if ("".equals(ver) || !currentVer.equals(ver)) {
    // OperationFactManager.getManager().getCarOrders(getContext(), 0, CarOrder.STATUS_ALL, 1, 200,
    // null);
    // }
    // }

}

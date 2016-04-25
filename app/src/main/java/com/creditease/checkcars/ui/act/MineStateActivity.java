package com.creditease.checkcars.ui.act;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.db.AppraiserUtil;
import com.creditease.checkcars.data.db.base.DBAsyncTask;
import com.creditease.checkcars.data.db.base.DBFindCallBack;
import com.creditease.checkcars.data.db.base.DbHelper;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.tools.LocalUtils;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.widget.SlipButton;
import com.creditease.utilframe.exception.HttpException;

@SuppressLint( "HandlerLeak" )
public class MineStateActivity extends BaseActivity implements OnClickListener,
        DBFindCallBack< Appraiser >, RequestListener
{
    public static final int MSG_WHAT_MESSAGE = 1;
    private static final int MSG_WHAT_ERROR = 2;
    private SlipButton mSlipButton;// 滑块开关按钮
    private TextView mLocalText;// 位置View
    private TextView mBasicTitle;
    private TextView mIsworktitle;
    private ImageView mBackView;
    /**
     * 用户uuid
     */
    private String appraiserId;
    private Appraiser mAppraiser;
    private Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch ( msg.what )
            {
                case MSG_WHAT_MESSAGE:
                    String address = LocalUtils.getUtils(getApplicationContext()).address;
                    if ( !TextUtils.isEmpty(address) )
                    {
                        mLocalText.setText(address);
                    } else
                    {
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_MESSAGE, 5 * 1000);
                    }
                    break;
                case MSG_WHAT_ERROR:
                    backToDefault();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void initViews()
    {
        setContentView(R.layout.mine_current_state);
        mBackView = ( ImageView ) findViewById(R.id.header_imgv_back);
        mBasicTitle = ( TextView ) findViewById(R.id.header_tv_title);
        mIsworktitle = ( TextView ) findViewById(R.id.mine_state_isworktitle);
        mSlipButton = ( SlipButton ) findViewById(R.id.mine_state_slipButton);
        mLocalText = ( TextView ) findViewById(R.id.mine_state_localtext);
        mBasicTitle.setText(R.string.str_mine_state_title);
    }

    @Override
    protected void initData()
    {
        mIsworktitle.setText(R.string.str_mine_level_state1);
        String address = LocalUtils.getUtils(getApplicationContext()).address;
        if ( !TextUtils.isEmpty(address) )
        {
            mLocalText.setText(address);
        } else
        {
            mHandler.sendEmptyMessage(MSG_WHAT_MESSAGE);
        }
        appraiserId = SharePrefenceManager.getAppraiserId(getContext());
        // 加载本地订单数据
        new DBAsyncTask< Appraiser >(getContext(), this).execute();
    }

    @Override
    protected void initEvents()
    {
        mBackView.setOnClickListener(this);
        mSlipButton.setOnChangedListener(new SlipButton.OnChangedListener()
        {

            @Override
            public void OnChanged(boolean CheckState)
            {
                if ( CheckState )
                {
                    mAppraiser.isProvideService = 0;
                    mIsworktitle.setText(R.string.str_mine_level_state1);
                } else
                {
                    mAppraiser.isProvideService = 1;
                    mIsworktitle.setText(R.string.str_mine_level_state2);
                }
                OperationFactManager.getManager().saveOrUpdateUserBasicData(getContext(), mAppraiser,
                        MineStateActivity.this);
            }
        });
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.header_imgv_back:
                closeOneAct(getContext());
                break;
            default:
                break;
        }
    }

    @Override
    public List< Appraiser > doDBOperation(DbHelper helper, int operId) throws CEDbException
    {
        return AppraiserUtil.getUtil(getApplicationContext()).getAppraiserByUserId(appraiserId);
    }

    @Override
    public void dataCallBack(int operId, List< Appraiser > result)
    {
        if ( (result != null) && (result.size() > 0) )
        {
            mAppraiser = result.get(0);
            if ( mAppraiser.isProvideService == 0 )
            {
                mIsworktitle.setText(R.string.str_mine_level_state1);
                mSlipButton.setCheck(true);
            } else if ( mAppraiser.isProvideService == 1 )
            {
                mIsworktitle.setText(R.string.str_mine_level_state2);
                mSlipButton.setCheck(false);
            }
        }
    }


    @Override
    public void errorCallBack(int operId, CEDbException e)
    {

    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        // 状态变化保存成功,更新本地数据
        try
        {
            AppraiserUtil.getUtil(getApplicationContext()).saveOrUpdateAppraiser(mAppraiser);
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(OperResponse response)
    {
        showToast(response.respmsg);
        mHandler.sendEmptyMessage(MSG_WHAT_ERROR);
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        showToast(R.string.net_error);
        mHandler.sendEmptyMessage(MSG_WHAT_ERROR);
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        showToast(R.string.str_mine_save_failure);
        mHandler.sendEmptyMessage(MSG_WHAT_ERROR);
    }

    /**
     * 状态变化保存失败，还原到默认值
     */
    private void backToDefault()
    {
        if ( mAppraiser.isProvideService == 0 )
        {
            mAppraiser.isProvideService = 1;
            mIsworktitle.setText(R.string.str_mine_level_state2);
            mSlipButton.setCheck(false);
            mSlipButton.invalidate();
        } else if ( mAppraiser.isProvideService == 1 )
        {
            mAppraiser.isProvideService = 0;
            mIsworktitle.setText(R.string.str_mine_level_state1);
            mSlipButton.setCheck(true);
            mSlipButton.invalidate();
        }
    }


    @Override
    public BaseActivity getContext()
    {
        return this;
    }
}

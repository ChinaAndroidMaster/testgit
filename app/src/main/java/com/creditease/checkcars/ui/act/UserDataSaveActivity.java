package com.creditease.checkcars.ui.act;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.utilframe.exception.HttpException;
import com.umeng.analytics.MobclickAgent;

public class UserDataSaveActivity extends BaseActivity implements OnClickListener, RequestListener,
        DBFindCallBack< Appraiser >
{

    /**
     * 返回
     */
    private ImageView mBackView;
    /**
     * 标题
     */
    private TextView mBasicTitle;
    /**
     * 保存
     */
    private TextView mSaveView;
    /**
     * 师傅宣言
     */
    private EditText mFatherWord;
    /**
     * 用户uuid
     */
    private String appraiserId;
    /**
     * 加载等待dialog
     */
    private LoadingGifDialog loadDialog;
    /**
     * 师傅基础数据对象
     */
    private Appraiser mAppraiser;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.mine_edit_data);
        loadDialog = new LoadingGifDialog(getContext());
        mBackView = ( ImageView ) findViewById(R.id.header_imgv_back);
        mBasicTitle = ( TextView ) findViewById(R.id.header_tv_title);
        mBasicTitle.setText(R.string.str_mine_fatherword_title);
        mSaveView = ( TextView ) findViewById(R.id.header_tv_btn);
        mSaveView.setVisibility(View.VISIBLE);
        mSaveView.setText(R.string.str_mine_save);
        mFatherWord = ( EditText ) findViewById(R.id.mine_edit_fatherword);
    }

    @Override
    protected void initData()
    {
        appraiserId = SharePrefenceManager.getAppraiserId(getContext());
        // 加载本地个人名片数据
        new DBAsyncTask< Appraiser >(getContext(), this).execute();
    }

    @Override
    protected void initEvents()
    {
        mBackView.setOnClickListener(this);
        mSaveView.setOnClickListener(this);
    }


    @Override
    public void onResume()
    {
        MobclickAgent.onResume(getContext());
        super.onResume();
    }

    @Override
    public void onPause()
    {
        MobclickAgent.onPause(getContext());
        super.onPause();
    }

    @Override
    public void onDestroy()
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
            case R.id.header_tv_btn:
                saveData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        loadDialog.dismiss();
        showToast(R.string.str_mine_save_success);
    }

    @Override
    public void onFailure(OperResponse response)
    {
        loadDialog.dismiss();
        showToast(response.respmsg);
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        loadDialog.dismiss();
        showToast(R.string.net_error);
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        loadDialog.dismiss();
        showToast(R.string.str_mine_save_failure);
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
            if ( !TextUtils.isEmpty(mAppraiser.declaration) )
            {
                mFatherWord.setText(mAppraiser.declaration);
            }
        }
    }

    @Override
    public void errorCallBack(int operId, CEDbException e)
    {

    }

    /**
     * 保存数据
     */
    private void saveData()
    {
        String fatherWord = mFatherWord.getText().toString().trim();
        if ( !TextUtils.isEmpty(fatherWord) )
        {
            mAppraiser.declaration = fatherWord;
        } else
        {
            showToast(R.string.str_mine_word_null);
            return;
        }
        OperationFactManager.getManager().saveOrUpdateUserBasicData(this, mAppraiser, this);
//    loadDialog.setText(R.string.text_report_add_loading);
//    TDialog.Builder.builderDialog(loadDialog).showDialog();
        loadDialog.showDialog();
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    @Override
    public void showToast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }
}

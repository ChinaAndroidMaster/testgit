package com.creditease.checkcars.ui.act;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.MineTopicsCore;
import com.creditease.checkcars.data.db.MineTopicUtil;
import com.creditease.checkcars.data.db.base.DBAsyncTask;
import com.creditease.checkcars.data.db.base.DBFindCallBack;
import com.creditease.checkcars.data.db.base.DbHelper;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.adapter.ServiceAssessAdapter;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.XListView;
import com.creditease.checkcars.ui.widget.XListView.IXListViewListener;
import com.creditease.utilframe.BitmapUtils;
import com.creditease.utilframe.exception.HttpException;

public class MineServiceAssessActivity extends BaseActivity implements
        OnClickListener, RequestListener, DBFindCallBack< MineTopicsCore >
{
    private static final int PAGE_SIZE = 100;
    private static final int OPER_TYPE = 0;
    private int current_page = 1;
    private ImageView mBackView;
    private XListView listView;
    private TextView infoTV;
    private LoadingGifDialog loadDialog;
    private TextView mBasicTitle;
    private ServiceAssessAdapter mAdapter;
    /**
     * 设置图片辅助类
     */
    private BitmapUtils bitmapUtils;
    /**
     * 用户uuid
     */
    private String appraiserId;

    private TextView mDivider;// listview中header中的高度为20px的分割线
    private String newestModifyTime = "0";//获取本地数据最后一条记录的时间
    private boolean isFirst = true;//判断是否是第一次请求数据

    @Override
    protected void initViews()
    {
        setContentView(R.layout.mine_access_list);
        mBackView = ( ImageView ) findViewById(R.id.header_imgv_back);
        mBasicTitle = ( TextView ) findViewById(R.id.header_tv_title);
        mBasicTitle.setText(R.string.str_mine_service_assess);
        infoTV = ( TextView ) findViewById(R.id.mine_access_ordersinfo);
        infoTV.setVisibility(View.GONE);
        listView = ( XListView ) findViewById(R.id.mine_access_listview);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new IXListViewListener()
        {

            @Override
            public void onRefresh()
            {
                current_page = 1;
                loadTopics();
            }

            @Override
            public void onLoadMore()
            {
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView< ? > parent, View view,
                                    int position, long id)
            {

            }
        });
        loadDialog = new LoadingGifDialog(getContext());
//		loadDialog.setText("加载中...");
//		TDialog.Builder.builderDialog(loadDialog).showDialog();
        loadDialog.showDialog();
        bitmapUtils = new BitmapUtils(getContext());
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.mine_image_default);
        bitmapUtils.configDefaultLoadingImage(R.drawable.mine_image_default);
    }

    @Override
    protected void onRestart()
    {
        if ( !isFirst )
        {
            loadTopics();//加载服务评价
        }
        super.onRestart();
    }

    @Override
    protected void initEvents()
    {
        mBackView.setOnClickListener(this);
        mAdapter = new ServiceAssessAdapter(getContext());
        listView.setAdapter(mAdapter);
        listView.addHeaderView(initHeaderView());
    }

    @Override
    protected void initData()
    {
        appraiserId = SharePrefenceManager.getAppraiserId(getContext());
        newestModifyTime = SharePrefenceManager.getMainTopicTime(getContext(), appraiserId);
        new DBAsyncTask< MineTopicsCore >(getContext(), this).execute();
    }

    private RelativeLayout initHeaderView()
    {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                20);
        mDivider = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 20);
        mDivider.setLayoutParams(params);
        mDivider.setBackgroundColor(getResources().getColor(
                R.color.color_mine_interval));
        mDivider.setId(R.id.mDivider);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(mDivider, layoutParams);

        LayoutParams txtParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        TextView txtDivider = new TextView(getContext());
        params = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        txtDivider.setLayoutParams(params);
        txtDivider.setBackgroundColor(getResources().getColor(
                R.color.color_mine_divider));
        txtDivider.setId(R.id.txtDivider);
        txtParams.addRule(RelativeLayout.BELOW, 1);
        txtParams.addRule(RelativeLayout.ALIGN_LEFT, 1);
        relativeLayout.addView(txtDivider, txtParams);
        return relativeLayout;
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

    public void loadTopics()
    {
        newestModifyTime = SharePrefenceManager.getMainTopicTime(getContext(), appraiserId);
        long modifyTime = TextUtils.isEmpty(newestModifyTime) ? 0 : Long
                .parseLong(newestModifyTime);
        OperationFactManager.getManager().getTopicsList(getContext(),
                current_page, PAGE_SIZE, appraiserId, modifyTime, OPER_TYPE, 1, this);
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        listView.stopRefresh();
        loadDialog.dismiss();
        ArrayList< MineTopicsCore > list = bundle
                .getParcelableArrayList(Oper.BUNDLE_EXTRA_DATA);
        if ( list != null && list.size() > 0 )
        {
            new DBAsyncTask< MineTopicsCore >(getContext(), this).execute();
        }
    }

    @Override
    public void onFailure(OperResponse response)
    {
        listView.stopRefresh();
        loadDialog.dismiss();
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        listView.stopRefresh();
        loadDialog.dismiss();
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        listView.stopRefresh();
        loadDialog.dismiss();
    }

    @Override
    public List< MineTopicsCore > doDBOperation(DbHelper helper, int operId)
            throws CEDbException
    {
        return MineTopicUtil.getUtil(getApplicationContext()).getTopicListByToId(appraiserId);
    }

    @Override
    public void dataCallBack(int operId, List< MineTopicsCore > result)
    {
        if ( isFirst )
        {
            newestModifyTime = "0";
            loadTopics();//加载服务评价
            isFirst = false;
        } else
        {
            loadDialog.dismiss();
            if ( result != null && result.size() > 0 )
            {
                newestModifyTime = String.valueOf(result.get(0).modifyTime);
            }
        }
        SharePrefenceManager.setMainTopicTime(getContext(), newestModifyTime, appraiserId);
        mAdapter.updateDataAndNotify(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorCallBack(int operId, CEDbException e)
    {

    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }
}

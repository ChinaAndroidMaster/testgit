package com.creditease.checkcars.ui.act;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.db.CarOrderUtil;
import com.creditease.checkcars.data.db.CarReportUtil;
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
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.act.carcheck.CarCheckActivity;
import com.creditease.checkcars.ui.adapter.CarPreReportAdapter;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.DrawableCenterTextView;
import com.creditease.checkcars.ui.widget.XListView;
import com.creditease.utilframe.exception.HttpException;

public class CarPreReportActivity extends BaseActivity implements OnClickListener,
        DBFindCallBack< CarOrder >
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
     * 加载等待dialog
     */
    private LoadingGifDialog loadDialog;

    /**
     * 数据列表控件
     */
    private XListView mListView;
    /**
     * 开始检测按钮
     */
    private TextView mStartView;

    /**
     * 数据列表为空时显示的布局
     */
    private LinearLayout mEmptyLayout;
    /**
     * 开始预检按钮+XListListView的父控件
     */
    private LinearLayout mCotentLayout;
    /**
     * 自定义TextView，左图片+文字居中显示
     */
    private DrawableCenterTextView mAddReportView;

    /**
     * 列表控件适配器
     */
    private CarPreReportAdapter mAdapter;

    private Context mContext;
    /**
     * 是否是第一次进到页面
     */
    private boolean isFirstLoadLocalData = true;
    /**
     * 预检车订单变量
     */
    private CarOrder carOrder;

    /**
     * 增加预检测车辆的请求回调
     */
    private RequestListener mAddPreReportListener = new RequestListener()
    {

        @Override
        public void onSuccess(Bundle bundle)
        {
            loadDialog.dismiss();
            if ( bundle != null )
            {
                carOrder = bundle.getParcelable(Oper.BUNDLE_EXTRA_DATA);
                gotoCarCheck(carOrder);
            }
        }

        @Override
        public void onDataError(String errorMsg, String result)
        {
            loadDialog.dismiss();
            ZLToast.getToast().showToast(getContext(), errorMsg);
        }

        @Override
        public void onFailure(OperResponse response)
        {
            loadDialog.dismiss();
            ZLToast.getToast().showToast(getContext(), response.respmsg);
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            loadDialog.dismiss();
            ZLToast.getToast().showToast(getContext(), R.string.net_error);
        }
    };

    /**
     * 查询预检测车辆的请求回调
     */
    private RequestListener mGetPreReportListener = new RequestListener()
    {

        @Override
        public void onSuccess(Bundle bundle)
        {
            loadDialog.dismiss();
            if ( bundle != null )
            {
                CarOrder carOrder = bundle.getParcelable(Oper.BUNDLE_EXTRA_DATA);
                if ( (carOrder != null) && (carOrder.reportList != null) && (carOrder.reportList.size() > 0)
                        && (mAdapter != null) )
                {
                    new DBAsyncTask< CarOrder >(getContext(), CarPreReportActivity.this).execute();
                } else
                {
                    // 防止出现多个订单的情况出现
                    if ( carOrder != null )
                    {
                        mCotentLayout.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    } else
                    {
                        mCotentLayout.setVisibility(View.GONE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        public void onDataError(String errorMsg, String result)
        {
            loadDialog.dismiss();
            ZLToast.getToast().showToast(getContext(), errorMsg);
        }

        @Override
        public void onFailure(OperResponse response)
        {
            loadDialog.dismiss();
            ZLToast.getToast().showToast(getContext(), response.respmsg);
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            loadDialog.dismiss();
            ZLToast.getToast().showToast(getContext(), R.string.net_error);
        }
    };


    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_car_pre_report);
        mBackView = ( ImageView ) findViewById(R.id.header_imgv_back);
        mBasicTitle = ( TextView ) findViewById(R.id.header_tv_title);
        mBasicTitle.setText(R.string.str_car_prereport);
        mListView = ( XListView ) findViewById(R.id.car_pre_report_listview);
        mEmptyLayout = ( LinearLayout ) findViewById(R.id.car_pre_report_empty_layout);
        mStartView = ( TextView ) findViewById(R.id.car_pre_report_start);
        mCotentLayout = ( LinearLayout ) findViewById(R.id.car_pre_report_content_layout);
        mAddReportView = ( DrawableCenterTextView ) findViewById(R.id.car_pre_report_addreport);
        mContext = this;
        loadDialog = new LoadingGifDialog(getContext());
        loadDialog.showDialog();
    }

    @Override
    protected void initData()
    {
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mAdapter = new CarPreReportAdapter(getContext());
        mListView.setAdapter(mAdapter);
        new DBAsyncTask< CarOrder >(getContext(), this).execute();
    }

    @Override
    protected void initEvents()
    {
        mBackView.setOnClickListener(this);
        mStartView.setOnClickListener(this);
        mAddReportView.setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if ( !isFirstLoadLocalData )
        {
            loadDialog.showDialog();
            OperationFactManager.getManager().getPreCarReport(getContext(), mGetPreReportListener);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.header_imgv_back:
                closeOneAct(getContext());
                break;
            case R.id.car_pre_report_start:
            case R.id.car_pre_report_addreport:
                if ( carOrder != null )
                {
                    gotoCarCheck(carOrder);
                } else
                {
                    loadDialog.showDialog();
                    OperationFactManager.getManager().addPreCarReport(getContext(), mAddPreReportListener);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void dataCallBack(int operId, List< CarOrder > result)
    {
        if ( !isFirstLoadLocalData )
        {
            if ( carOrder != null )
            {
                mCotentLayout.setVisibility(View.VISIBLE);
                mEmptyLayout.setVisibility(View.GONE);
                if ( (carOrder.reportList != null) && (carOrder.reportList.size() > 0) )
                {
                    mAdapter.updateDataAndNotify(carOrder);
                }
            } else
            {
                mCotentLayout.setVisibility(View.GONE);
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
        }
        if ( isFirstLoadLocalData )
        {
            isFirstLoadLocalData = false;
            OperationFactManager.getManager().getPreCarReport(getContext(), mGetPreReportListener);
        } else
        {
            loadDialog.dismiss();
        }
    }

    @Override
    public List< CarOrder > doDBOperation(DbHelper helper, int operId) throws CEDbException
    {
        String appraiserId = SharePrefenceManager.getAppraiserId(mContext);
        carOrder = CarOrderUtil.getUtil(getApplicationContext()).getPreCarOrder(appraiserId);
        if ( carOrder != null )
        {
            // 通过order的uuid获取所有的预检测报告
            List< CarReport > reports =
                    CarReportUtil.getUtil(getApplicationContext()).getAllReportByOrderId(carOrder.uuid);
            if ( (reports != null) && (reports.size() > 0) && (carOrder.reportList != null)
                    && (carOrder.reportList.size() > 0) )
            {
                // 同步CarOrder中的reportList
                for ( int i = 0; i < carOrder.reportList.size(); i++ )
                {
                    CarReport ordeReport = carOrder.reportList.get(i);
                    for ( CarReport report : reports )
                    {
                        if ( ordeReport.clientUuid.equals(report.clientUuid) )
                        {
                            ordeReport.reportCar = report.reportCar;
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void errorCallBack(int operId, CEDbException e)
    {
        loadDialog.dismiss();
    }

    /**
     * 跳转到检车页面
     *
     * @param order
     */
    private void gotoCarCheck(CarOrder order)
    {
        if ( order != null )
        {
            Intent intent = new Intent();
            Bundle b = new Bundle();
            b.putParcelable(CarCheckActivity.PARAM_ORDER, order);
            intent.putExtras(b);
            BaseActivity.launch(mContext, CarCheckActivity.class, intent);
            (( Activity ) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }
}

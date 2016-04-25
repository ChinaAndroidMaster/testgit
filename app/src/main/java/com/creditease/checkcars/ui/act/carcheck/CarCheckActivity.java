package com.creditease.checkcars.ui.act.carcheck;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CCClassify;
import com.creditease.checkcars.data.bean.CCItem;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.db.BaseReportUtil;
import com.creditease.checkcars.data.db.CarReportUtil;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.tools.CommitOrderEventUtils;
import com.creditease.checkcars.tools.Util;
import com.creditease.checkcars.ui.act.SelectPhotoActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.act.carcheck.ReportControl.ISave;
import com.creditease.checkcars.ui.act.main.MainActivity;
import com.creditease.checkcars.ui.adapter.CarCheckAdapter.IPhoto;
import com.creditease.checkcars.ui.adapter.ViewPagerAdapter;
import com.creditease.checkcars.ui.dialog.CommitReportDialog;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.dialog.base.TDialog;
import com.creditease.utilframe.exception.HttpException;
import com.umeng.analytics.MobclickAgent;

/**
 * 检车
 *
 * @author 子龍
 * @function
 * @date 2015年11月6日
 * @company CREDITEASE
 */
@SuppressLint( "InflateParams" )
public class CarCheckActivity extends BaseActivity implements OnClickListener,
        ViewPager.OnPageChangeListener, OnCheckedChangeListener, RequestListener, IPhoto, ISave
{

    public static final String PARAM_ORDER = "_param_order";
    public static final String PARAM_REPORT = "_param_report";
    public static final String PARAM_CILENT_UUID = "param_cilent_uuid";
    private final int VIEWIDs[] = new int[]{R.layout.include_car_record, R.layout.include_car_imgs,
            R.layout.page_listv, R.layout.include_car_comment};
    public ReportControl reportControl;// 报告控制器
    public StepCarBaseRecord stepBaseRecord;// 基本信息录入
    public StepOverallRanking stepOverallRanking;// 综合评级
    public StepPhotos stepPhotos;// 必拍照
    public StepCCClassify stepCCCassify;// 大检测项
    public ViewPager mViewPager;
    public CarReport report;
    private String clientUuid;
    private ImageView backImgV;// 返回按钮
    private TextView titleTV;// 标题
    private TextView titleBtn;// 标题按钮
    private LoadingGifDialog loadDialog;// 加载loading
    private TextView reasonTV;// 不通过view
    /*******************************
     * 导航栏
     *****************************************/
    private float density;// 像素密度
    private int width = 120;// dip
    private int textSelectedColor;
    private int textUnselectedColor;
    private RadioGroup mRadioGroup;
    private ImageView mImageView;// 导航imgv
    private HorizontalScrollView mHorizontalScrollView;
    private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离
    /********************************
     * view集合
     ***************************************/
    private LayoutInflater inflater;
    private List< String > titles = new ArrayList< String >();
    private List< View > views = new ArrayList< View >();
    /*****************
     * 数据
     ****************/
    private CarOrder order;
    /**********************
     * 检车
     ****************************/
    // private Map<String, CarCheckAdapter> adapterMap = new HashMap<String, CarCheckAdapter>();
    // private Map<String, ImageAdapter> imgAdapterMap = new HashMap<String, ImageAdapter>();

    // 拍照的检测大项
    // private CCClassify classify;

    private boolean isModifyReport = false;// 是否是修改报告
    private String refuseReason = "";// 报告不通过原因


    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if ( action.equals(ConnectivityManager.CONNECTIVITY_ACTION) )
            {
                ConnectivityManager connectivityManager =
                        ( ConnectivityManager ) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if ( (info != null) && info.isAvailable() )
                {
                    if ( (report != null) && (report.isSync == CarReport.SYNC_NO) )
                    {
                        syncReport();
                    } else
                    {
                        startUploadImageService();
                    }
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registNetBroadcastReceiver();// 注册广播
        showLoadDialog();
    }

    public void showLoadDialog()
    {
        loadDialog = new LoadingGifDialog(getContext());
        loadDialog.showDialog();
    }

    @Override
    public void onResume()
    {
        loadDialog.dismiss();
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
    protected void onDestroy()
    {
        super.onDestroy();
        unRegisterNetBroadcastReceiver();
        stepBaseRecord.destroy();
        reportControl.destroy();
    }


    /**
     * 初始化View
     */
    @Override
    protected void initViews()
    {
        init();
        setContentView(R.layout.act_car_check);
        titleTV = ( TextView ) findViewById(R.id.header_tv_title);
        backImgV = ( ImageView ) findViewById(R.id.header_imgv_back);
        titleBtn = ( TextView ) findViewById(R.id.header_tv_btn);
        titleBtn.setVisibility(View.GONE);
        titleBtn.setText(R.string.str_carcheck_photos);
        mViewPager = ( ViewPager ) findViewById(R.id.carcheck_viewpager);
        mHorizontalScrollView = ( HorizontalScrollView ) findViewById(R.id.carcheck_horizontalScrollView);
        mRadioGroup = ( RadioGroup ) findViewById(R.id.carcheck_radiogroup);
        mImageView = ( ImageView ) findViewById(R.id.carcheck_tip_imgv);
        density = getContext().getResources().getDisplayMetrics().density;
        reasonTV = ( TextView ) findViewById(R.id.carcheck_reason_tv);

        // 初始化 ReportControl
        reportControl = new ReportControl(getContext());
        reportControl.setISave(this);
        reportControl.setCommitRequestListener(this);

        /*******************************************************************/
        Bundle b = getIntent().getExtras();
        if ( b != null )
        {
            order = b.getParcelable(PARAM_ORDER);
            report = b.getParcelable(PARAM_REPORT);
            clientUuid = b.getString(PARAM_CILENT_UUID);
            isModifyReport = report != null ? (report.status == CarReport.STATUS_REFUSE) : false;
            if ( isModifyReport )
            {
                refuseReason = report.reason;
                if ( !TextUtils.isEmpty(refuseReason) )
                {
                    reasonTV.setVisibility(View.VISIBLE);
                }
                reasonTV.setText(refuseReason);
                report.isSync = CarReport.SYNC_YES;
            } else
            {
                reasonTV.setVisibility(View.GONE);
            }
        }
        initViewData();
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData()
    {
        titleTV.setText(R.string.str_carcheck_title_detail);
    }

    @Override
    protected void initEvents()
    {
        backImgV.setOnClickListener(this);
        titleBtn.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        reasonTV.setOnClickListener(this);
    }

    private void initChildView(View view, int index)
    {
        if ( index == 0 )
        {
            stepBaseRecord = new StepCarBaseRecord(this, view, report, order.uuid);
        } else if ( index == 1 )
        {
            // initPhotosView(view);
            stepPhotos = new StepPhotos(this, view, report);
        } else if ( index == (titles.size() - 1) )
        {
            stepOverallRanking = new StepOverallRanking(this, view, report);
        }
    }

    private void initViewData()
    {
        // 初始化报告
        report = getReport();
        if ( report == null )
        {
            ZLToast.getToast().showToast(getContext(), "初始化报告失败");
            closeOneAct(this);
            return;
        }
        reportControl.initCarReport(report, order.uuid);
        syncReport();
        if ( report.cRoot.children != null )
        {
            titles.add(0, "基本信息");
            titles.add("必拍照");
            for ( CCClassify cc : report.cRoot.children )
            {
                titles.add(cc.name);
            }
            titles.add("综合评级");
        }
        for ( int i = 0, len = titles.size(); i < len; i++ )
        {
            final RadioButton rb = ( RadioButton ) inflater.inflate(R.layout.radio_btn, null);
            rb.setId(i);
            if ( i == 0 )
            {
                rb.setTextColor(textSelectedColor);
            } else
            {
                rb.setTextColor(textUnselectedColor);
            }
            rb.setMaxWidth(( int ) (width * density));
            rb.setMinWidth(( int ) (width * density));
            rb.setText(titles.get(i));
            rb.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    rb.setChecked(true);
                    mViewPager.setCurrentItem(rb.getId());
                }
            });
            mRadioGroup.addView(rb);
        }
        View view;
        views.add(view = getView(VIEWIDs[0]));
        initChildView(view, 0);
        views.add(view = getView(VIEWIDs[1]));
        initChildView(view, 1);
        stepCCCassify = new StepCCClassify(this, null, VIEWIDs[2], report);
        views.addAll(stepCCCassify.getViewList());
        views.add(view = getView(VIEWIDs[3]));
        initChildView(view, views.size() - 1);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, views, titles);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
        startUploadImageService();
    }

    /**
     * 初始化报告
     *
     * @return 下午5:48:43
     */
    private CarReport getReport()
    {
        try
        {
            if ( isModifyReport )
            {
                // modify
                report =
                        CarReportUtil.getUtil(getApplicationContext()).getReportByClientUuid(report.clientUuid);
                report.isSync = CarReport.SYNC_YES;
                // 记录开始检车时间
                CommitOrderEventUtils.getUtil(getApplicationContext()).recordTime(order.uuid, 2, 3,
                        report.clientUuid);
            } else
            {
                if ( report == null )
                {
                    // 普通初始化
                    if ( !TextUtils.isEmpty(clientUuid) )
                    {
                        report =
                                CarReportUtil.getUtil(getApplicationContext()).getReportByClientUuid(clientUuid);
                    }
                    if ( report == null )
                    {
                        report =
                                BaseReportUtil.getBRUtil(getApplicationContext()).createBaseReport(order.uuid,
                                        clientUuid);
                    }
                    if ( TextUtils.isEmpty(report.clientUuid) )
                    {
                        String appraiserId = SharePrefenceManager.getAppraiserId(getApplicationContext());
                        report.clientUuid =
                                Util.createReportId(getApplicationContext(), appraiserId, order.uuid);
                        report.isSync = CarReport.SYNC_NO;
                    }
                }
                // 记录开始检车时间
                CommitOrderEventUtils.getUtil(getApplicationContext()).recordTime(order.uuid, 2, 2,
                        report.clientUuid);
            }
            return report;
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取报告ID
     *
     * @return 下午6:16:32
     */
    public String getReportId()
    {
        return report.clientUuid;
    }

    private View getView(int layout)
    {
        View view = inflater.inflate(layout, null);
        return view;
    }

    private void init()
    {
        inflater = ( LayoutInflater ) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        textSelectedColor = getResources().getColor(R.color.color_text_selected);
        textUnselectedColor = getResources().getColor(R.color.black);
    }

    @Override
    public void takePhoto(CCClassify classify, CCItem item)
    {
        reportControl.takePhoto(classify, item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ( (resultCode == RESULT_OK) && (data != null) )
        {
            if ( requestCode == StepOverallRanking.REQUEST_CODE_PRICE )
            {
                // 估价回调
                Bundle bundle = data.getExtras();
                String str = bundle.getString(StepOverallRanking.KEY_PRICE);
                stepOverallRanking.setCarValue(str);
            } else
            {
                // 有控制进行图片处理
                reportControl.photoHandle(requestCode, data);
                if ( requestCode == ReportControl.ImageControl.REQUEST_CCITEM_IMG_OPER )
                {
                    // 检测小项图片处理 通知 update
                    stepCCCassify.notifyDataSetChanged();
                } else
                {
                    int action = data.getIntExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION, 0);
                    // 拍照处理
                    if ( action == ReportControl.ImageControl.REQUEST_CAMERA_CODE_CHECKITEM )
                    {
                        stepCCCassify.notifyDataSetChanged();
                    } else
                    {
                        stepPhotos.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        saveReport();
        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {

        for ( int i = 0, len = group.getChildCount(); i < len; i++ )
        {
            if ( i != checkedId )
            {
                (( RadioButton ) group.getChildAt(i)).setTextColor(textUnselectedColor);
            } else
            {
                (( RadioButton ) group.getChildAt(i)).setTextColor(textSelectedColor);
            }
        }
        AnimationSet _AnimationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = null;
        if ( checkedId == ( int ) mCurrentCheckedRadioLeft )
        {
            return;
        }
        float ll = checkedId * width * density;
        translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, ll, 0f, 0f);
        _AnimationSet.addAnimation(translateAnimation);
        _AnimationSet.setFillBefore(false);
        _AnimationSet.setFillAfter(true);
        _AnimationSet.setDuration(100);
        mImageView.startAnimation(_AnimationSet);// 开始上面蓝色横条图片的动画切换
        mCurrentCheckedRadioLeft = ll;// 更新当前蓝色横条距离左边的距离
        mHorizontalScrollView.smoothScrollTo(( int ) mCurrentCheckedRadioLeft - ( int ) (width * density),
                0);

    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        Intent intent;
        switch ( id )
        {
            case R.id.header_imgv_back:
                saveReport();
                onBackPressed();
                break;
            case R.id.header_tv_btn:
                // TODO 标题右侧按钮
                break;
            case R.id.carcheck_reason_tv:
                intent = new Intent();
                intent.putExtra(ReportAuditDetailActivity.PARAM_REASON, refuseReason);
                launch(getContext(), ReportAuditDetailActivity.class, intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {
    }

    @Override
    public void onPageScrollStateChanged(int arg0)
    {
    }

    @Override
    public void onPageSelected(int index)
    {
        (( RadioButton ) mRadioGroup.getChildAt(index)).setChecked(true);
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        loadDialog.dismiss();
        BaseActivity act = getActivity(MainActivity.class);
        if ( act != null )
        {
            (( MainActivity ) act).loadOrders();
        }
        closeOneAct(getContext());
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
        showToast(errorMsg);
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        loadDialog.dismiss();
        showToast(R.string.net_error);
    }


    /**
     * 注册联网监听
     * <p/>
     * 下午5:13:25
     */
    private void registNetBroadcastReceiver()
    {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    /**
     * 注销联网监听
     * <p/>
     * 下午5:13:25
     */
    private void unRegisterNetBroadcastReceiver()
    {
        unregisterReceiver(mReceiver);
    }


    /**
     * 预览图片/操作删除
     *
     * @param item
     * @param position 下午12:17:37
     */
    public void reviewPics(CCClassify ccClassify, CCItem item, int position)
    {
        takePhoto(ccClassify, item);
        Intent intent = new Intent();
        intent.setClass(getContext(), PicViewActivity.class);
        intent.putStringArrayListExtra(PicViewActivity.PARAM_PICS, item.picPathList);
        intent.putExtra(PicViewActivity.PARAM_REPORT_ID, getReportId());
        boolean bool = (item.attributeId == null) || TextUtils.isEmpty(item.attributeId);
        intent.putExtra(PicViewActivity.PARAM_ITEM_ID, bool ? item.uuid : item.attributeId);
        intent.putExtra(PicViewActivity.PARAM_ITEM_POSITION, position);
        startActivityForResult(intent, ReportControl.ImageControl.REQUEST_CCITEM_IMG_OPER);
    }

    @Override
    public void save()
    {
        try
        {
            if ( report != null )
            {
                if ( (stepBaseRecord == null) || (stepCCCassify == null) || (stepOverallRanking == null) )
                {
                    return;
                }
                report.reportCar = stepBaseRecord.initCar();
                report.cRoot.children = stepCCCassify.getCCClassifyList();
                report.remark = stepOverallRanking.getComment();
                report.amount = stepOverallRanking.getCarValue();
                report.status = CarReport.STATUS_INIT;
                report.orderId = order.uuid;
                CarReportUtil.getUtil(getApplicationContext()).updateReport(report);
            }
        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
    }


    /**
     * 启动上传图片服务
     * <p/>
     * 下午7:59:32
     */
    public void startUploadImageService()
    {
        reportControl.startUploadImageService();
    }

    /**
     * sync report
     */
    public void syncReport()
    {
        reportControl.syncReport(report);
    }


    /**
     * 验证报告完成度
     *
     * @return 下午6:43:03
     */
    public boolean verify()
    {
        /**************** 车基本信息 ***************************/
        stepBaseRecord.initCar();
        if ( !stepBaseRecord.validate() )
        {
            mViewPager.setCurrentItem(0);
            return false;
        }
        /************************ 基本检测项 ***************************/
        if ( !reportControl.isPhotosAllToken() )
        {
            ZLToast.getToast().showToast(getContext(), "请完成车辆七张必拍照录入");
            mViewPager.setCurrentItem(1);
            return false;
        }
        // 八大检测项校验
        if ( !stepCCCassify.validate() )
        {
            return false;
        }
        if ( !stepOverallRanking.validate() )
        {
            return false;
        }
        return true;
    }

    /**
     * 保存报告
     * <p/>
     * 下午4:11:17
     */
    public void saveReport()
    {
        // 加上非空判断
        if ( reportControl != null )
        {
            reportControl.saveReport();
        }
    }

    /**
     * 提交报告
     * <p/>
     * 下午12:57:53
     */
    public void commitReport()
    {
        saveReport();
        if ( !verify() )
        {
            return;
        }
        final CommitReportDialog dialog = new CommitReportDialog(getContext());
        dialog.setOnOKBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
                report.reportCar = stepBaseRecord.getCar();
                report.cRoot.children = stepCCCassify.getCCClassifyList();
                report.remark = stepOverallRanking.getComment();
                report.amount = stepOverallRanking.getCarValue();
                if ( report.endTime == 0 )
                {
                    report.endTime = System.currentTimeMillis();
                }
                // 提交报告
                reportControl.commitReport();
                // loadDialog.showDialog();
                showLoadDialog();
            }
        }).setOnCancelBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
            }
        });
        TDialog.Builder.builderDialog(dialog).showDialog();
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }
}

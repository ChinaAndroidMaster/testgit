package com.creditease.checkcars.ui.act;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CarInfoBean;
import com.creditease.checkcars.data.db.CarInfoUtil;
import com.creditease.checkcars.data.db.base.DBAsyncTask;
import com.creditease.checkcars.data.db.base.DBFindCallBack;
import com.creditease.checkcars.data.db.base.DbHelper;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.tools.JsonUtils;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.CEWebView;
import com.creditease.checkcars.ui.widget.CleanableEditText;
import com.creditease.checkcars.ui.widget.listener.IWebViewListener;
import com.creditease.utilframe.exception.HttpException;

/**
 * 检车页面的车辆信息查看与活动模块的查询车辆公用页面
 *
 * @author zgb
 */
public class ShowCarInfoActivity extends BaseActivity implements OnClickListener, IWebViewListener,
        OnEditorActionListener, DBFindCallBack< CarInfoBean >, RequestListener
{

    public final static int FROM_CAR_CHECK = 1;
    public final static int FROM_VIN_QUERY = 2;
    /**
     * 本地网页地址
     */
    private final static String URL = "file:///android_asset/showcarinfo.html";
    /**
     * 标题栏
     */
    private RelativeLayout mTitleLayout;

    /**
     * 输入框栏
     */
    private LinearLayout mQueryLayout;

    /**
     * 回退按钮
     */
    private ImageView mBackView;

    /**
     * 标题
     */
    private TextView mTitleView;

    /**
     * 加载信息显示的网页的webview
     */
    private CEWebView mWebView;

    /**
     * 输入框
     */
    private CleanableEditText mSearchText;
    /**
     * 取消按钮，退出搜索页
     */
    private TextView mCancel;

    /**
     * 车辆vin码
     */
    private String vin;

    /**
     * 车辆信息bean
     */
    private CarInfoBean carInfo;

    /**
     * 车辆每项的具体信息集合
     */
    private List< String[] > mList;

    /**
     * 车辆信息的json字符串
     */
    private String mData;

    /**
     * 类型 1 从检车处跳转过来 2 从活动模块车辆信息跳转过来
     */
    private int type;

    /**
     * 加载loading
     */
    private LoadingGifDialog loadDialog;

    /**
     * 装载输入框的字符串
     */
    private String value;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.show_car_info);
        type = getIntent().getIntExtra("type", 0);
        mTitleLayout = ( RelativeLayout ) findViewById(R.id.show_car_info_title_layout);
        mQueryLayout = ( LinearLayout ) findViewById(R.id.show_car_info_query_layout);
        mBackView = ( ImageView ) findViewById(R.id.header_imgv_back);
        mTitleView = ( TextView ) findViewById(R.id.header_tv_title);
        mTitleView.setText(R.string.show_carinfo_title);

        mSearchText = ( CleanableEditText ) findViewById(R.id.show_car_info_edit);
        mCancel = ( TextView ) findViewById(R.id.show_car_info_cancel);

        if ( type == FROM_CAR_CHECK )
        {
            mTitleLayout.setVisibility(View.VISIBLE);
        } else if ( type == FROM_VIN_QUERY )
        {
            mQueryLayout.setVisibility(View.VISIBLE);
            loadDialog = new LoadingGifDialog(getContext());
        }

        mWebView = ( CEWebView ) findViewById(R.id.show_car_info_webview);
    }

    @Override
    protected void initData()
    {
        // 设置监听器 重点在于设置监听器必须在init()方法之前调用
        mWebView.setWebViewListener(ShowCarInfoActivity.this);
        if ( type == FROM_CAR_CHECK )
        {
            Bundle bundle = getIntent().getExtras();
            if ( bundle != null )
            {
                carInfo = ( CarInfoBean ) bundle.getSerializable("carInfo");
            }
            initWebView();
        }
    }

    @Override
    protected void initEvents()
    {
        if ( type == FROM_CAR_CHECK )
        {
            mBackView.setOnClickListener(this);
        } else if ( type == FROM_VIN_QUERY )
        {
            mCancel.setOnClickListener(this);
            mSearchText.setOnEditorActionListener(this);
        }
    }

    private void initWebView()
    {
        if ( carInfo != null )
        {
            vin = carInfo.vin;
            mList = initListData();
            mData = JsonUtils.requestObjectBean(mList);
            // 初始化webview并加载网页
            mWebView.init(getContext(), URL);
        }
    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }

    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.header_imgv_back:
            case R.id.show_car_info_cancel:
                closeOneAct(getContext());
                break;
            default:
                break;
        }
    }

    /**
     * 监听搜索按钮并执行搜索事件
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if ( (actionId == EditorInfo.IME_ACTION_SEARCH)
                || ((event != null) && (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) )
        {
            value = mSearchText.getText().toString().trim();
            if ( !TextUtils.isEmpty(value)
                    && !value.equals(getContext().getString(R.string.str_vin_query_hint)) )
            {
                if ( value.length() != 17 )
                {
                    showToast(R.string.str_vin_query_hint);
                } else
                {
                    loadDialog.showDialog();
                    new DBAsyncTask< CarInfoBean >(getContext(), ShowCarInfoActivity.this).execute();
                }
            } else
            {
                showToast(R.string.str_vin_query_hint);
            }
            return true;
        }

        return false;
    }

    /**
     * 存储数据的回调方法
     */
    @Override
    public void dataCallBack(int operId, List< CarInfoBean > result)
    {
        if ( (result != null) && (result.size() > 0) )
        {
            carInfo = result.get(0);
            initWebView();
            loadDialog.dismiss();
        } else
        {
            OperationFactManager.getManager().getCarInfoByVinWS(this, value, this);
        }
    }

    /**
     * 存储数据的回调方法
     */
    @Override
    public List< CarInfoBean > doDBOperation(DbHelper helper, int operId) throws CEDbException
    {
        return CarInfoUtil.getUtil(getApplicationContext()).getCarInfo(value);
    }

    /**
     * 存储数据的回调方法
     */
    @Override
    public void errorCallBack(int operId, CEDbException e)
    {
        loadDialog.dismiss();

    }

    /**
     * 请求数据的回调方法
     */
    @Override
    public void onSuccess(Bundle bundle)
    {
        carInfo = bundle.getParcelable(Oper.BUNDLE_EXTRA_DATA);
        if ( carInfo != null )
        {
            initWebView();
        } else
        {
            ZLToast.getToast().showToast(this, getString(R.string.str_carcheck_vin_error));
        }
        loadDialog.dismiss();
    }

    /**
     * 请求数据的回调方法
     */
    @Override
    public void onDataError(String errorMsg, String result)
    {
        ZLToast.getToast().showToast(this, getString(R.string.str_carcheck_vin_error));
        loadDialog.dismiss();
    }

    /**
     * 请求数据的回调方法
     */
    @Override
    public void onFailure(OperResponse response)
    {
        ZLToast.getToast().showToast(this, getString(R.string.str_carcheck_vin_error));
        loadDialog.dismiss();
    }

    /**
     * 请求数据的回调方法
     */
    @Override
    public void onRequestError(HttpException error, String msg)
    {
        ZLToast.getToast().showToast(this, getString(R.string.str_carcheck_net_error));
        loadDialog.dismiss();
    }


    /**
     * webview的回调方法
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress)
    {

    }

    /**
     * webview的回调方法
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {

    }

    /**
     * webview的回调方法
     */
    @Override
    public void onPageFinished(WebView view, String url)
    {

    }

    /**
     * webview的回调方法
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {

    }

    /**
     * webview的回调方法
     */
    @Override
    public void addMoreJavaScriptInterface(WebView webView)
    {
        /**
         * 向JavaScript中传递车辆信息字符串
         */
        mWebView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface
            public String loadValueData()
            {
                if ( !TextUtils.isEmpty(mData) )
                {
                    return mData;
                } else
                {
                    return "";
                }
            }
        }, "loadValue");

        /**
         * 向JavaScript中传递车辆VIN码字符串
         */
        mWebView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface
            public String loadKeyData()
            {
                return "VIN码: " + vin;
            }
        }, "loadKey");

    }

    /**
     * 将车辆信息拼成list集合
     *
     * @return
     */
    private List< String[] > initListData()
    {
        List< String[] > lists = new ArrayList< String[] >();
        String[] item = new String[2];
        item[0] = "产品名称";
        item[1] = carInfo.name;
        lists.add(item);

        item = new String[2];
        item[0] = "品牌";
        item[1] = carInfo.brand;
        lists.add(item);

        item = new String[2];
        item[0] = "制造年份";
        item[1] = carInfo.productionDate;
        lists.add(item);

        item = new String[2];
        item[0] = "产品型号";
        item[1] = carInfo.model;
        lists.add(item);

        item = new String[2];
        item[0] = "发动机型号";
        item[1] = carInfo.engineType;
        lists.add(item);

        item = new String[2];
        item[0] = "排量(cc)";
        item[1] = carInfo.displacement;
        lists.add(item);

        item = new String[2];
        item[0] = "功率(KW)";
        item[1] = carInfo.power;
        lists.add(item);

        item = new String[2];
        item[0] = "产品类型";
        item[1] = carInfo.type;
        lists.add(item);

        item = new String[2];
        item[0] = "额定质量";
        item[1] = carInfo.reatedQuality;
        lists.add(item);

        item = new String[2];
        item[0] = "总质量";
        item[1] = carInfo.totalQuality;
        lists.add(item);

        item = new String[2];
        item[0] = "装备质量";
        item[1] = carInfo.equipmentQuality;
        lists.add(item);

        item = new String[2];
        item[0] = "燃料种类";
        item[1] = carInfo.combustionType;
        lists.add(item);

        item = new String[2];
        item[0] = "排放依据标准";
        item[1] = carInfo.emissionStandards;
        lists.add(item);

        item = new String[2];
        item[0] = "轴数";
        item[1] = carInfo.shaftNum;
        lists.add(item);

        item = new String[2];
        item[0] = "轴距";
        item[1] = carInfo.shaftdistance;
        lists.add(item);

        item = new String[2];
        item[0] = "轴荷";
        item[1] = carInfo.shaftLoad;
        lists.add(item);

        item = new String[2];
        item[0] = "弹簧片数";
        item[1] = carInfo.springNum;
        lists.add(item);

        item = new String[2];
        item[0] = "轮胎数";
        item[1] = carInfo.tireNum;
        lists.add(item);

        item = new String[2];
        item[0] = "轮胎规格";
        item[1] = carInfo.tireSpecifications;
        lists.add(item);

        item = new String[2];
        item[0] = "接近离去角";
        item[1] = carInfo.departureAngle;
        lists.add(item);

        item = new String[2];
        item[0] = "前悬后悬";
        item[1] = carInfo.beforeAfterHanging;
        lists.add(item);

        item = new String[2];
        item[0] = "前轮距";
        item[1] = carInfo.beforeWheelTrack;
        lists.add(item);

        item = new String[2];
        item[0] = "后轮距";
        item[1] = carInfo.afterWheelTrack;
        lists.add(item);

        item = new String[2];
        item[0] = "整车长";
        item[1] = carInfo.carLong;
        lists.add(item);

        item = new String[2];
        item[0] = "整车宽";
        item[1] = carInfo.carWidth;
        lists.add(item);

        item = new String[2];
        item[0] = "整车高";
        item[1] = carInfo.carHigh;
        lists.add(item);

        item = new String[2];
        item[0] = "货箱长";
        item[1] = carInfo.crateLong;
        lists.add(item);

        item = new String[2];
        item[0] = "货箱宽";
        item[1] = carInfo.crateWidth;
        lists.add(item);

        item = new String[2];
        item[0] = "货箱高";
        item[1] = carInfo.crateHight;
        lists.add(item);

        item = new String[2];
        item[0] = "最高车速";
        item[1] = carInfo.maxSpeed;
        lists.add(item);

        item = new String[2];
        item[0] = "额定载客量";
        item[1] = carInfo.carrying;
        lists.add(item);

        item = new String[2];
        item[0] = "驾驶室准乘人数";
        item[1] = carInfo.cabCarring;
        lists.add(item);

        item = new String[2];
        item[0] = "转向形式";
        item[1] = carInfo.turnToType;
        lists.add(item);

        item = new String[2];
        item[0] = "准拖挂车总质量";
        item[1] = carInfo.trailerTotalQuality;
        lists.add(item);

        item = new String[2];
        item[0] = "载质量利用系数";
        item[1] = carInfo.loadQualityFactor;
        lists.add(item);

        item = new String[2];
        item[0] = "半挂车鞍座最大承载质量";
        item[1] = carInfo.semiSaddleBearingQuelity;
        lists.add(item);

        item = new String[2];
        item[0] = "发动机生产商";
        item[1] = carInfo.engineProducers;
        lists.add(item);
        return lists;
    }

    @Override
    public boolean overrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);
        return true;
    }
}

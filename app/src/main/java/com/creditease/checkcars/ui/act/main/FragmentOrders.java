package com.creditease.checkcars.ui.act.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.db.CarOrderUtil;
import com.creditease.checkcars.data.db.base.DBAsyncTask;
import com.creditease.checkcars.data.db.base.DBFindCallBack;
import com.creditease.checkcars.data.db.base.DbHelper;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.msgpush.MessageHandler;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.tools.Util;
import com.creditease.checkcars.ui.act.CarOrderSearchActivity;
import com.creditease.checkcars.ui.act.QRCodeActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.adapter.CarOrderAdapter;
import com.creditease.checkcars.ui.adapter.ViewPagerAdapter;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.XListView;
import com.creditease.checkcars.ui.widget.XListView.IXListViewListener;
import com.creditease.utilframe.exception.HttpException;

/**
 * 订单
 *
 * @author 子龍
 * @function
 * @date 2015年7月8日
 * @company CREDITEASE
 */
public class FragmentOrders extends BaseFragment implements OnClickListener,
        DBFindCallBack< CarOrder >, ViewPager.OnPageChangeListener, OnCheckedChangeListener
{
    public static final String TAG = "orders";
    public static final String PARAM_ORDER_STATUS = "_param_car_status";
    private static final int PAGE_SIZE = 20;
    private static FragmentOrders fragment;
    private OrdersUpdateBroadcastReceive br;
    // 标题栏title
    private TextView titleTV;
    // 标题栏图片
    private ImageView titleImageV;
    // 标题栏搜索按钮
    private ImageView titleSearchTV;
    private ViewPager mViewPager;

    private List< View > views = new ArrayList< View >();
    // 待验车、未支付、已支付Radiobutton
    private RadioButton RBServicing, RBPaying, RBPayed;
    private RadioGroup mRadioGroup;
    // 存放待验车、未支付、已支付内容为空时的文本控件
    private List< TextView > infoTvs = new ArrayList< TextView >(3);
    // 存放待验车、未支付、已支付列表控件
    private List< XListView > listViews = new ArrayList< XListView >(3);
    // 存放待验车、未支付、已支付列表适配器
    private List< CarOrderAdapter > adapterList = new ArrayList< CarOrderAdapter >(3);
    // 存放待验车、未支付、已支付列表数据
    private Map< String, ArrayList< CarOrder > > ordersMap = new HashMap< String, ArrayList< CarOrder > >(3);
    private String appraiserId;// 获取uuid
    private String newestModifyTime = "0";
    private boolean isFirstLoadLocalData = true;// 是否第一次加载本地数据
    private LoadingGifDialog loadDialog;
    private boolean isLoadingOrders = false;
    private int pageNo = 1;// 公用当前页数
    private int unCompleteOrder = 1;// 待验车当前页数
    private int unpayeOrder = 1;// 未支付当前页数
    private int payedOrder = 1;// 已支付页数
    private boolean isLoadingMoreOrders = false;
    private String currentVer;
    private CURRENTPAGER mPager = CURRENTPAGER.FIRST;
    // 网络请求返回监听器
    private RequestListener listener = new RequestListener()
    {
        @Override
        public void onDataError(String errorMsg, String result)
        {
            isLoadingOrders = false;
            isFirstLoadLocalData = false;
            disimissLoadingDialog();
            stopRefresh();
            ZLToast.getToast().showToast(getContext(), errorMsg);
        }

        @Override
        public void onFailure(OperResponse response)
        {
            isLoadingOrders = false;
            isFirstLoadLocalData = false;
            disimissLoadingDialog();
            stopRefresh();
            ZLToast.getToast().showToast(getContext(), response.respmsg);
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            isLoadingOrders = false;
            isFirstLoadLocalData = false;
            disimissLoadingDialog();
            stopRefresh();
            ZLToast.getToast().showToast(getContext(), R.string.net_error);
        }

        @Override
        public void onSuccess(Bundle bundle)
        {
            isFirstLoadLocalData = false;
            stopRefresh();
            ArrayList< CarOrder > list = bundle.getParcelableArrayList(Oper.BUNDLE_EXTRA_DATA);
            if ( (list != null) && (list.size() > 0) )
            {
                new DBAsyncTask< CarOrder >(getContext(), FragmentOrders.this).execute();
            } else
            {
                if ( isLoadingOrders )
                {
                    isLoadingOrders = false;
                }
                disimissLoadingDialog();
            }
        }
    };

    public static FragmentOrders getFragment()
    {
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if ( fragment == null )
        {
            fragment = this;
        }
        registBroadcast();
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_orders;
    }

    /**
     * 初始化View
     */
    @Override
    protected void initView(View v)
    {
        loadDialog = new LoadingGifDialog(getContext());
        titleTV = ( TextView ) v.findViewById(R.id.header_tv_title);
        titleTV.setText(R.string.str_carorders_title);
        titleSearchTV = ( ImageView ) v.findViewById(R.id.header_imgv_search);
        titleSearchTV.setImageResource(R.drawable.btn_order_searcher_selector);
        titleImageV = ( ImageView ) v.findViewById(R.id.header_imgv_btn);
        titleImageV.setImageResource(R.drawable.btn_qrcode_selector);
        mRadioGroup = ( RadioGroup ) v.findViewById(R.id.fragment_orders_radiogroup);
        RBServicing = ( RadioButton ) v.findViewById(R.id.fragment_orders_rb_servicing);
        RBPaying = ( RadioButton ) v.findViewById(R.id.fragment_orders_rb_paying);
        RBPayed = ( RadioButton ) v.findViewById(R.id.fragment_orders_rb_payed);
        clear();
        for ( int i = 0; i < 3; i++ )
        {
            View view = getView(R.layout.viewpager_orderlist);
            views.add(view);
            XListView listView = ( XListView ) view.findViewById(R.id.viewpager_orderlist_xlv);
            CarOrderAdapter adapter = new CarOrderAdapter(getContext());
            listView.setAdapter(adapter);
            adapterList.add(adapter);
            listView.setPullRefreshEnable(true);
            listView.setPullLoadEnable(true);
            listView.setXListViewListener(new IXListViewListener()
            {

                @Override
                public void onLoadMore()
                {
                    loadMoreWS();
                }

                @Override
                public void onRefresh()
                {
                    pageNo = 1;
                    loadOrdersWS();
                }
            });
            ordersMap.put("" + i, new ArrayList< CarOrder >());
            listViews.add(listView);
            TextView tv = ( TextView ) view.findViewById(R.id.viewpager_orderlist_tv_info);
            tv.setVisibility(View.GONE);
            infoTvs.add(tv);
        }

        mViewPager = ( ViewPager ) v.findViewById(R.id.fragment_orders_viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), views, new ArrayList< String >());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }

    /**
     * 初始化数据
     * <p/>
     * 下午6:36:31
     */
    @Override
    protected void initData()
    {
        currentVer = Util.getAppVersionName(getContext());
        appraiserId = SharePrefenceManager.getAppraiserId(getContext());
        newestModifyTime = SharePrefenceManager.getOrdersNewestModifyTime(getContext(), appraiserId);
        // 加载本地订单数据
        new DBAsyncTask< CarOrder >(getContext(), this).execute();
        loadDialog.showDialog();
        // loadDialog.setText("加载中...");
        // TDialog.Builder.builderDialog(loadDialog).showDialog();
    }

    @Override
    protected void initEvents()
    {
        titleImageV.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        titleSearchTV.setOnClickListener(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if ( !isFirstLoadLocalData )
        {
            loadOrdersWS();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unRegistBroadcast();
        clear();
        System.gc();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    /**
     * 清空所有集合中的数据
     */
    private void clear()
    {
        adapterList.clear();
        views.clear();
        ordersMap.clear();
        infoTvs.clear();
        listViews.clear();
    }

    /**
     * 加载更多订单
     */
    public synchronized void loadMoreWS()
    {
        if ( isLoadingMoreOrders )
        {
            return;
        }
        isLoadingMoreOrders = true;
        if ( mPager == CURRENTPAGER.FIRST )
        {
            unCompleteOrder++;
            pageNo = unCompleteOrder;
        } else if ( mPager == CURRENTPAGER.SECOND )
        {
            unpayeOrder++;
            pageNo = unpayeOrder;
        } else if ( mPager == CURRENTPAGER.THIRD )
        {
            payedOrder++;
            pageNo = payedOrder;
        }
        new DBAsyncTask< CarOrder >(getContext(), FragmentOrders.this).execute();
    }

    /**
     * 加载订单
     * <p/>
     * 下午6:28:25
     */
    public synchronized void loadOrdersWS()
    {
        if ( isLoadingOrders )
        {
            return;
        }
        isLoadingOrders = true;
        appraiserId = SharePrefenceManager.getAppraiserId(getContext());
        newestModifyTime = SharePrefenceManager.getOrdersNewestModifyTime(getContext(), appraiserId);
        long modifyTime = TextUtils.isEmpty(newestModifyTime) ? 0 : Long.parseLong(newestModifyTime);
        OperationFactManager.getManager().getCarOrders(getContext(), modifyTime, CarOrder.STATUS_ALL,
                1, 200, listener);
    }

    @Override
    public List< CarOrder > doDBOperation(DbHelper helper, int operId) throws CEDbException
    {
        List< CarOrder > carOrders = null;
        int[] status = null;
        if ( isLoadingMoreOrders )
        {
            if ( mPager == CURRENTPAGER.FIRST )
            {
                status =
                        new int[]{CarOrder.STATUS_REVISIT, CarOrder.STATUS_CANCELED, CarOrder.STATUS_ASSIGNED,
                                CarOrder.STATUS_CHECKING};
            } else if ( mPager == CURRENTPAGER.SECOND )
            {
                status =
                        new int[]{CarOrder.STATUS_FINISH_SERVICE, CarOrder.STATUS_PAY_PREPARED,
                                CarOrder.STATUS_PAYING};
            } else if ( mPager == CURRENTPAGER.THIRD )
            {
                status = new int[]{CarOrder.STATUS_PAY_SUCCESS};
            }
            carOrders =
                    CarOrderUtil.getUtil(getContext())
                            .getCarOrderList(appraiserId, PAGE_SIZE, pageNo, status);
        } else
        {
            carOrders = new ArrayList< CarOrder >();
            status =
                    new int[]{CarOrder.STATUS_REVISIT, CarOrder.STATUS_CANCELED, CarOrder.STATUS_ASSIGNED,
                            CarOrder.STATUS_CHECKING};
            List< CarOrder > checkCarOrders =
                    CarOrderUtil.getUtil(getContext())
                            .getCarOrderList(appraiserId, PAGE_SIZE, pageNo, status);
            status =
                    new int[]{CarOrder.STATUS_FINISH_SERVICE, CarOrder.STATUS_PAY_PREPARED,
                            CarOrder.STATUS_PAYING};
            List< CarOrder > unPayOrders =
                    CarOrderUtil.getUtil(getContext())
                            .getCarOrderList(appraiserId, PAGE_SIZE, pageNo, status);
            status = new int[]{CarOrder.STATUS_PAY_SUCCESS};
            List< CarOrder > payedOrders =
                    CarOrderUtil.getUtil(getContext())
                            .getCarOrderList(appraiserId, PAGE_SIZE, pageNo, status);
            if ( (checkCarOrders != null) && (checkCarOrders.size() > 0) )
            {
                carOrders.addAll(checkCarOrders);
            }
            if ( (unPayOrders != null) && (unPayOrders.size() > 0) )
            {
                carOrders.addAll(unPayOrders);
            }
            if ( (payedOrders != null) && (payedOrders.size() > 0) )
            {
                carOrders.addAll(payedOrders);
            }
        }

        return carOrders;
    }

    @Override
    public void dataCallBack(int operId, List< CarOrder > result)
    {
        // 上拉刷新和下拉加载更多区分开
        if ( isLoadingMoreOrders )
        {
            if ( (result != null) && (result.size() > 0) )
            {
                updateMoreData(result);
            }
            isLoadingMoreOrders = false;
            // 停止加载更多
            stopLoadMore();
        } else
        {
            if ( (result != null) && (result.size() > 0) )
            {
                newestModifyTime = result.get(0).modifyTime;
                updateData(result);
            } else
            {
                newestModifyTime = "0";
            }
            isLoadingOrders = false;
            // 判断是否是版本升级，如果是则强制刷新数据
            String ver = SharePrefenceManager.getAppVersion(getContext());
            if ( "".equals(ver) || !currentVer.equals(ver) )
            {
                SharePrefenceManager.setAppVersion(getContext(), currentVer);
                newestModifyTime = "0";
            }
            SharePrefenceManager.setOrdersNewestModifyTime(getContext(), newestModifyTime, appraiserId);
            if ( isFirstLoadLocalData )
            {
                loadOrdersWS();// 加载订单
            } else
            {
                disimissLoadingDialog();
            }
        }
    }

    @Override
    public void errorCallBack(int operId, CEDbException e)
    {
        // 停止加载更多
        if ( isLoadingMoreOrders )
        {
            isLoadingMoreOrders = false;
            stopLoadMore();
        }
        isLoadingOrders = false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch ( checkedId )
        {
            case R.id.fragment_orders_rb_servicing:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.fragment_orders_rb_paying:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.fragment_orders_rb_payed:
                mViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.header_imgv_btn:
                BaseActivity.launch(getContext(), QRCodeActivity.class, null);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.header_imgv_search:
                BaseActivity.launch(getContext(), CarOrderSearchActivity.class, null);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {
    }

    /**
     * viewpager的viewchange监听方法
     */
    @Override
    public void onPageScrollStateChanged(int arg0)
    {
    }

    @Override
    public void onPageSelected(int pageIndex)
    {
        switch ( pageIndex )
        {
            case 0:
                RBServicing.setChecked(true);
                mPager = CURRENTPAGER.FIRST;
                break;
            case 1:
                RBPaying.setChecked(true);
                mPager = CURRENTPAGER.SECOND;
                break;
            case 2:
                RBPayed.setChecked(true);
                mPager = CURRENTPAGER.THIRD;
                break;
            default:
                break;
        }
    }

    /**
     * 注册广播
     */
    private void registBroadcast()
    {
        br = new OrdersUpdateBroadcastReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MessageHandler.ACTION_BROADCAST_ORDERS_UPDATE); // 为BroadcastReceiver指定action，即要监听的消息名字。
        getContext().registerReceiver(br, intentFilter);
    }

    /**
     * 注销广播
     */
    private void unRegistBroadcast()
    {
        getContext().unregisterReceiver(br);
    }

    /**
     * 停止加载更多
     */
    private void stopLoadMore()
    {
        for ( XListView listv : listViews )
        {
            listv.stopLoadMore();
        }
    }

    /**
     * 停止刷新
     * <p/>
     * 下午6:36:06
     */
    private void stopRefresh()
    {
        for ( XListView listv : listViews )
        {
            if ( listv.isRefresh() )
            {
                listv.stopRefresh();
            }
        }
    }

    /**
     * 刷新时更新view数据
     *
     * @param result 下午6:46:45
     */
    private void updateData(List< CarOrder > result)
    {
        if ( (result == null) || (result.size() == 0) || ordersMap.isEmpty() )
        {
            return;
        }
        ArrayList< CarOrder > servicingOrderList = ordersMap.get("0");
        ArrayList< CarOrder > payingOrderList = ordersMap.get("1");
        ArrayList< CarOrder > payedOrderList = ordersMap.get("2");
        if ( (servicingOrderList == null) || (payingOrderList == null) || (payedOrderList == null) )
        {
            return;
        }
        servicingOrderList.clear();
        payingOrderList.clear();
        payedOrderList.clear();
        for ( CarOrder carOrder : result )
        {
            switch ( carOrder.status )
            {
                case CarOrder.STATUS_REVISIT:
                case CarOrder.STATUS_CANCELED:
                case CarOrder.STATUS_ASSIGNED:
                case CarOrder.STATUS_CHECKING:
                    servicingOrderList.add(carOrder);
                    break;
                case CarOrder.STATUS_FINISH_SERVICE:
                case CarOrder.STATUS_PAY_PREPARED:
                case CarOrder.STATUS_PAYING:
                    payingOrderList.add(carOrder);
                    break;
                case CarOrder.STATUS_PAY_SUCCESS:
                    payedOrderList.add(carOrder);
                    break;
                default:
                    break;
            }
        }
        TextView tv0 = infoTvs.get(0);
        if ( servicingOrderList.size() == 0 )
        {
            tv0.setVisibility(View.VISIBLE);
            tv0.setText("暂无订单");
        } else
        {
            tv0.setVisibility(View.GONE);
        }
        TextView tv1 = infoTvs.get(1);
        if ( payingOrderList.size() == 0 )
        {
            tv1.setVisibility(View.VISIBLE);
            tv1.setText("暂无待支付订单");
        } else
        {
            tv1.setVisibility(View.GONE);
        }
        TextView tv2 = infoTvs.get(2);
        if ( payedOrderList.size() == 0 )
        {
            tv2.setVisibility(View.VISIBLE);
            tv2.setText("暂无已支付订单");
        } else
        {
            tv2.setVisibility(View.GONE);
        }
        for ( int i = 0, len = adapterList.size(); i < len; i++ )
        {
            CarOrderAdapter adapter = adapterList.get(i);
            adapter.updateDataAndNotify(ordersMap.get("" + i));
        }
    }

    /**
     * 更新下拉加载更多时的页面
     *
     * @param result
     */
    private void updateMoreData(List< CarOrder > result)
    {
        if ( (result == null) || (result.size() == 0) )
        {
            return;
        }
        CarOrderAdapter adapter = null;
        if ( mPager == CURRENTPAGER.FIRST )
        {
            adapter = adapterList.get(0);
            // 处理数据和无数据提示同时显示的bug
            TextView tv0 = infoTvs.get(0);
            if ( adapter.getCount() == 0 )
            {
                tv0.setVisibility(View.VISIBLE);
                tv0.setText("暂无订单");
            } else
            {
                tv0.setVisibility(View.GONE);
            }
        } else if ( mPager == CURRENTPAGER.SECOND )
        {
            adapter = adapterList.get(1);
            // 处理数据和无数据提示同时显示的bug
            TextView tv1 = infoTvs.get(1);
            if ( adapter.getCount() == 0 )
            {
                tv1.setVisibility(View.VISIBLE);
                tv1.setText("暂无待支付订单");
            } else
            {
                tv1.setVisibility(View.GONE);
            }
        } else if ( mPager == CURRENTPAGER.THIRD )
        {
            adapter = adapterList.get(2);
            // 处理数据和无数据提示同时显示的bug
            TextView tv2 = infoTvs.get(2);
            if ( adapter.getCount() == 0 )
            {
                tv2.setVisibility(View.VISIBLE);
                tv2.setText("暂无已支付订单");
            } else
            {
                tv2.setVisibility(View.GONE);
            }
        }

        if ( adapter != null )
        {
            adapter.updateMoreData(result);
        }
    }

    private void disimissLoadingDialog()
    {
        if ( loadDialog != null )
        {
            loadDialog.dismiss();
        }
    }

    /**
     * 枚举 表示待验车、未支付、已支付类别
     */
    private enum CURRENTPAGER
    {
        FIRST, SECOND, THIRD
    }

    /**
     * 内部类，广播接收类
     */
    private class OrdersUpdateBroadcastReceive extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            // 判断是否接到订单更新消息
            if ( MessageHandler.ACTION_BROADCAST_ORDERS_UPDATE.equals(action) )
            {
                // Log.d("PushIntentService", TAG + ":handler=broadcast");
                int type = intent.getIntExtra("type", 0);
                // 要获取最新的订单，所以pageNo置为1
                pageNo = 1;
                if ( type == 0 )
                {
                    // 处理内容
                    // Log.d("PushIntentService", TAG
                    // + ":handler=broadcast=loadOrdersWS ");
                    loadOrdersWS();
                } else if ( type == 1 )
                {
                    // Log.d("PushIntentService", TAG
                    // + ":handler=broadcast=update DB ");
                    new DBAsyncTask< CarOrder >(getContext(), FragmentOrders.this).execute();
                }
            }
        }
    }
}

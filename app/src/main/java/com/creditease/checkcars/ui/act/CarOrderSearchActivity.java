package com.creditease.checkcars.ui.act;

import java.util.List;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.db.CarOrderUtil;
import com.creditease.checkcars.data.db.base.DBAsyncTask;
import com.creditease.checkcars.data.db.base.DBFindCallBack;
import com.creditease.checkcars.data.db.base.DbHelper;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.adapter.CarOrderAdapter;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.CleanableEditText;
import com.creditease.checkcars.ui.widget.XListView;
import com.creditease.checkcars.ui.widget.XListView.IXListViewListener;

public class CarOrderSearchActivity extends BaseActivity implements OnClickListener,
        DBFindCallBack< CarOrder >, OnEditorActionListener, IXListViewListener
{

    /**
     * 分页搜索,每页显示的条目数
     */
    private final static int PAGE_SIZE = 20;
    /**
     * 输入框
     */
    private CleanableEditText mSearchText;
    /**
     * 取消按钮，退出搜索页
     */
    private TextView mCancel;
    /**
     * 显示搜索结果
     */
    private XListView mListView;
    /**
     * 搜索为空时显示的提示
     */
    private TextView mEmptyView;
    /**
     * 加载loading
     */
    private LoadingGifDialog loadDialog;
    /**
     * listview的是适配器
     */
    private CarOrderAdapter mAdapter;
    /**
     * 分页搜索，当前页
     */
    private int pageNo = 1;

    /**
     * 装载输入框的字符串
     */
    private String value;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.car_order_search);
        mSearchText = ( CleanableEditText ) findViewById(R.id.car_order_search_edit);
        mCancel = ( TextView ) findViewById(R.id.car_order_search_cancel);
        mListView = ( XListView ) findViewById(R.id.car_order_search_list);
        mEmptyView = ( TextView ) findViewById(R.id.car_order_search_empty);
    }

    @Override
    protected void initData()
    {
        loadDialog = new LoadingGifDialog(getContext());
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(true);
        mAdapter = new CarOrderAdapter(getContext());
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents()
    {
        mCancel.setOnClickListener(this);
        mSearchText.setOnEditorActionListener(this);
        mListView.setXListViewListener(this);
    }


    /**
     * 监听搜索按钮并执行搜索事件
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if ( (actionId == EditorInfo.IME_ACTION_SEARCH)
                || ((event != null) && (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) )
        {
            value = mSearchText.getText().toString().trim();
            if ( !TextUtils.isEmpty(value)
                    && !value.equals(getContext().getString(R.string.car_search_order_hint)) )
            {
                pageNo = 1;
                loadDialog.showDialog();
                /**
                 * 清空原有数据
                 */
                mAdapter.clearData();
                new DBAsyncTask< CarOrder >(getContext(), CarOrderSearchActivity.this).execute();
            } else
            {
                showToast(R.string.car_search_order_hint);
            }
            return true;
        }

        return false;
    }


    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.car_order_search_cancel:
                closeOneAct(getContext());
                break;

            default:
                break;
        }
    }

    @Override
    public void dataCallBack(int operId, List< CarOrder > result)
    {
        loadDialog.dismiss();
        if ( (result != null) && (result.size() > 0) )
        {
            mListView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mAdapter.updateMoreData(result);
        } else
        {
            /**
             * pageNo == 1时显示空提示，说明没有搜索到数据
             */
            if ( pageNo == 1 )
            {
                mListView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            } else
            {
                showToast(R.string.car_search_order_nomore);
            }
        }
    }

    @Override
    public List< CarOrder > doDBOperation(DbHelper helper, int operId) throws CEDbException
    {
        return CarOrderUtil.getUtil(getApplicationContext()).searchCarOrderList(value, PAGE_SIZE,
                pageNo);
    }

    @Override
    public void errorCallBack(int operId, CEDbException e)
    {
        loadDialog.dismiss();
    }

    @Override
    public void onLoadMore()
    {
        pageNo++;
        new DBAsyncTask< CarOrder >(getContext(), CarOrderSearchActivity.this).execute();
    }

    @Override
    public void onRefresh()
    {

    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }
}

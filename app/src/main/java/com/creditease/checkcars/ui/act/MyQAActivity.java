package com.creditease.checkcars.ui.act;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.QuestionAnswer;
import com.creditease.checkcars.data.db.QuestionAnswerUtil;
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
import com.creditease.checkcars.ui.adapter.QuestionAnswerAdapter;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.XListView;
import com.creditease.checkcars.ui.widget.XListView.IXListViewListener;
import com.creditease.utilframe.exception.HttpException;

/**
 * @author zgb
 */
public class MyQAActivity extends BaseActivity implements DBFindCallBack< QuestionAnswer >,
        RequestListener, OnClickListener
{

    public static final String TAG = "qa";
    /**
     * 每页请求的数据个数
     */
    private static final int PAGE_SIZE = 100;
    /**
     * title栏上的返回键，设置为GONE
     */
    private ImageView mBackView;
    /**
     * title栏上的标题
     */
    private TextView mBasicTitle;
    /**
     * 问答列表控件
     */
    private XListView mListView;
    /**
     * 问答列表为空时的控件
     */
    private TextView mEmptyView;
    /**
     * 问答列表的适配器
     */
    private QuestionAnswerAdapter mAdapter;
    /**
     * 记录最后一次从服务器端获取数据的时间 以达到增量请求数据的效果
     */
    private String newestModifyTime;
    /**
     * 用户uuid
     */
    private String appraiserId;
    /**
     * 加载数据的loading的dialog
     */
    private LoadingGifDialog loadDialog;
    /**
     * 是否是第一次进入页面
     */
    private boolean isFirst = true;
    /**
     * 请求服务器的当前页数
     */
    private int current_page = 1;

    /**
     * 初始化View
     */
    @Override
    protected void initViews()
    {
        setContentView(R.layout.question_answer_main);
        mBackView = ( ImageView ) findViewById(R.id.header_imgv_back);
        mBasicTitle = ( TextView ) findViewById(R.id.header_tv_title);
        mBasicTitle.setText(R.string.questtion_main_title);
        mListView = ( XListView ) findViewById(R.id.question_answer_listview);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(new IXListViewListener()
        {

            @Override
            public void onLoadMore()
            {
            }

            @Override
            public void onRefresh()
            {
                current_page = 1;
                updateQuestionAnswer();
            }
        });
        mEmptyView = ( TextView ) findViewById(R.id.question_answer_empty);
        loadDialog = new LoadingGifDialog(getContext());
//    loadDialog.setText("加载中...");
//    TDialog.Builder.builderDialog(
        loadDialog.showDialog();
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData()
    {
        mAdapter = new QuestionAnswerAdapter(getContext(), mListView);
        mListView.setAdapter(mAdapter);
        mListView.addHeaderView(initHeaderView());
        mListView.setEmptyView(mEmptyView);
        // 获取师傅uuid
        appraiserId = SharePrefenceManager.getAppraiserId(getContext());
        // 从本地数据库获取问答数据
        new DBAsyncTask< QuestionAnswer >(getContext(), this).execute();
    }

    /**
     * 初始化事件
     */
    @Override
    protected void initEvents()
    {
        mBackView.setOnClickListener(this);
    }


    @Override
    public void onStart()
    {
        super.onStart();
        if ( mAdapter != null )
        {
            mAdapter.dismissPopupWindow();
        }
        if ( !isFirst )
        {
            updateQuestionAnswer();
        }
    }

    /**
     * 从服务器端获取数据
     */
    private void updateQuestionAnswer()
    {
        newestModifyTime = SharePrefenceManager.getQuestionAnswerTime(getContext(), appraiserId);
        long modifyTime = TextUtils.isEmpty(newestModifyTime) ? 0 : Long.parseLong(newestModifyTime);
        // 获取所有已审核通过的问题
        OperationFactManager.getManager().getQuestionAnswerData(getContext(), 0, current_page,
                PAGE_SIZE, String.valueOf(modifyTime), this);

    }

    /**
     * 在listview的头部加上20px的一块UI 根据UI效果图来做的，因为第一行的分割线与其他行的分割线宽度不一样
     *
     * @return
     */
    private RelativeLayout initHeaderView()
    {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());

        LayoutParams layoutParams =
                new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 20);
        TextView mDivider = new TextView(getContext());
        LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 20);
        mDivider.setLayoutParams(params);
        mDivider.setBackgroundColor(getResources().getColor(R.color.color_mine_interval));
        mDivider.setId(R.id.mDivider);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.addView(mDivider, layoutParams);
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

    @Override
    public List< QuestionAnswer > doDBOperation(DbHelper helper, int operId) throws CEDbException
    {
        return QuestionAnswerUtil.getUtil(getApplicationContext()).getQuestionAnswerList();
    }

    @Override
    public void dataCallBack(int operId, List< QuestionAnswer > result)
    {
        if ( (result != null) && (result.size() > 0) )
        {
            newestModifyTime = String.valueOf(result.get(0).modifyTime);
            mAdapter.updateDataAndNotify(result);
        } else
        {
            newestModifyTime = "0";
        }
        SharePrefenceManager.setQuestionAnswerTime(getContext(), newestModifyTime, appraiserId);
        if ( isFirst )
        {
            updateQuestionAnswer();
            isFirst = false;
        } else
        {
            loadDialog.dismiss();
            mListView.stopRefresh();
        }
    }

    @Override
    public void errorCallBack(int operId, CEDbException e)
    {
        loadDialog.dismiss();
        mListView.stopRefresh();
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        mListView.stopRefresh();
        loadDialog.dismiss();
        ArrayList< QuestionAnswer > list = bundle.getParcelableArrayList(Oper.BUNDLE_EXTRA_DATA);
        if ( (list != null) && (list.size() > 0) )
        {
            new DBAsyncTask< QuestionAnswer >(getContext(), this).execute();
        }
    }

    @Override
    public void onFailure(OperResponse response)
    {
        mListView.stopRefresh();
        loadDialog.dismiss();
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        mListView.stopRefresh();
        loadDialog.dismiss();
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        mListView.stopRefresh();
        loadDialog.dismiss();
    }


    @Override
    public BaseActivity getContext()
    {
        return this;
    }

}

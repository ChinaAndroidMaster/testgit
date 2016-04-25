package com.creditease.checkcars.ui.act.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.bean.MineTopicsCore;
import com.creditease.checkcars.data.db.AppraiserUtil;
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
import com.creditease.checkcars.tools.LocalUtils;
import com.creditease.checkcars.tools.Utils;
import com.creditease.checkcars.ui.act.MineServiceAssessActivity;
import com.creditease.checkcars.ui.act.MineSettingActivity;
import com.creditease.checkcars.ui.act.MineStateActivity;
import com.creditease.checkcars.ui.act.UserBasicDataActivity;
import com.creditease.checkcars.ui.act.WebViewActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.widget.CircleImageView;
import com.creditease.checkcars.ui.widget.TopicItemLayout;
import com.creditease.utilframe.BitmapUtils;
import com.creditease.utilframe.exception.HttpException;

public class FragmentMine extends BaseFragment implements OnClickListener, RequestListener
{
    public static final String TAG = "mine";
    private static final int CURRNT_PAGE = 1;
    private static final int PAGE_SIZE = 100;
    private static final int OPER_TYPE = 0;
    private CircleImageView mCircleImageView;// 圆形头像
    private TextView mUserProfession;// 专业等级
    private TextView mUserName;// 用户名称
    private TextView mUserGood;// 用户好评
    private RelativeLayout mUserStateLayout;// 用户状态布局
    private TextView mIncome;// 收入View
    private TextView mIncomeSort;// 收入排行榜View
    private TextView mOrderCount;// 接单量View
    private TextView mOrderCountSort;// 接单量排行榜View
    private TextView mTopicTtile;// 最新评论title
    private LinearLayout mTopicitemLayout;// 加入评论条布局
    @SuppressWarnings( "unused" )
    private RelativeLayout mTopicLayout;// 评论布局
    private RelativeLayout mMarketLayout;// 我的销售
    private RelativeLayout mAllcoverLayout;// 质保销售
    private ImageView mMineSetting;// 设置按钮
    private TextView mUserState;// 用户状态textView

    /**
     * 设置图片辅助类
     */
    private BitmapUtils bitmapUtils;
    /**
     * 用户uuid
     */
    private String appraiserId;

    private Appraiser appraiser;

    private String state;// 设置师傅状态的参数

    private String newestModifyTime = "0";// 获取本地数据最后一条记录的时间

    private boolean isTopicFirst = true;// 是否是第一次加载服务评价

    private boolean isBasicDataFirst = true;// 是否是第一次更新基础数据

    private String salerId;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg)
        {
            switch ( msg.what )
            {
                case MineStateActivity.MSG_WHAT_MESSAGE:
                    String address = LocalUtils.getUtils(getContext()).address;
                    if ( !TextUtils.isEmpty(address) )
                    {
                        if ( getActivity() == null )
                        {
                            return;
                        }
                        state +=
                                "," + getActivity().getResources().getString(R.string.str_mine_level_state3)
                                        + address;
                    } else
                    {
                        mHandler.sendEmptyMessageDelayed(MineStateActivity.MSG_WHAT_MESSAGE, 5 * 1000);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private DBFindCallBack< Appraiser > mBasicDataBack = new DBFindCallBack< Appraiser >()
    {

        @Override
        public void dataCallBack(int operId, List< Appraiser > result)
        {
            if ( (result != null) && (result.size() > 0) )
            {
                Appraiser appraiser = result.get(0);
                setValue(appraiser);
            }
            if ( isBasicDataFirst )
            {
                updateBasicData();// 更新用户和基础数据
                isBasicDataFirst = false;
            }

        }

        @Override
        public List< Appraiser > doDBOperation(DbHelper helper, int operId) throws CEDbException
        {
            return AppraiserUtil.getUtil(getContext()).getAppraiserByUserId(appraiserId);
        }

        @Override
        public void errorCallBack(int operId, CEDbException e)
        {

        }
    };

    private DBFindCallBack< MineTopicsCore > mTopicBack = new DBFindCallBack< MineTopicsCore >()
    {

        @Override
        public void dataCallBack(int operId, List< MineTopicsCore > result)
        {
            if ( getContext() == null )
            {
                return;
            }
            mTopicitemLayout.removeAllViews();
            if ( (result != null) && (result.size() > 0) )
            {
                newestModifyTime = String.valueOf(result.get(0).modifyTime);
                int size = result.size();
                if ( size > 1 )
                {
                    size = 1;
                }
                for ( int i = 0; i < size; i++ )
                {
                    MineTopicsCore topic = result.get(i);
                    TopicItemLayout itemLayout = new TopicItemLayout(getContext());
                    itemLayout.show(topic.headPic, topic.nickName,
                            Utils.timestamps2Date(topic.createTime, "yyyy-MM-dd"), topic.remark, topic.score);
                    mTopicitemLayout.addView(itemLayout);
                }
            } else
            {
                newestModifyTime = "0";
                setEmptyTopicItem();
            }
            SharePrefenceManager.setMainTopicTime(getContext(), newestModifyTime, appraiserId);
            if ( isTopicFirst )
            {
                updateTopics();// 加载服务评价
                isTopicFirst = false;
            }
        }

        @Override
        public List< MineTopicsCore > doDBOperation(DbHelper helper, int operId) throws CEDbException
        {
            return MineTopicUtil.getUtil(getContext()).getTopicListByToId(appraiserId);
        }

        @Override
        public void errorCallBack(int operId, CEDbException e)
        {

        }
    };


    @Override
    protected int getLayoutId()
    {
        return R.layout.frame_mine;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View v)
    {
        mCircleImageView = ( CircleImageView ) v.findViewById(R.id.mine_main_circleImageview);
        mUserName = ( TextView ) v.findViewById(R.id.mine_main_username);
        mMineSetting = ( ImageView ) v.findViewById(R.id.mine_main_setting);
        mUserProfession = ( TextView ) v.findViewById(R.id.mine_main_userProfession);
        mUserGood = ( TextView ) v.findViewById(R.id.mine_main_userGood);
        mUserState = ( TextView ) v.findViewById(R.id.mine_main_userstate);
        mUserStateLayout = ( RelativeLayout ) v.findViewById(R.id.mine_main_userStateLayout);
        mTopicTtile = ( TextView ) v.findViewById(R.id.mine_main_newtopic_title);
        mTopicitemLayout = ( LinearLayout ) v.findViewById(R.id.mine_main_topicItemLayout);
        mIncome = ( TextView ) v.findViewById(R.id.mine_main_income);
        mIncomeSort = ( TextView ) v.findViewById(R.id.mine_main_incomeSort);
        mOrderCount = ( TextView ) v.findViewById(R.id.mine_main_works);
        mOrderCountSort = ( TextView ) v.findViewById(R.id.mine_main_workssort);
        mMarketLayout = ( RelativeLayout ) v.findViewById(R.id.mine_main_market_layout);
        mAllcoverLayout = ( RelativeLayout ) v.findViewById(R.id.mine_main_allcover_layout);
        bitmapUtils = new BitmapUtils(getContext());
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.mine_defaultimage);
        bitmapUtils.configDefaultLoadingImage(R.drawable.mine_defaultimage);
    }

    @Override
    protected void initData()
    {
        salerId = SharePrefenceManager.getSalerId(getContext());
        if ( TextUtils.isEmpty(salerId) )
        {
            mAllcoverLayout.setVisibility(View.GONE);
        } else
        {
            mAllcoverLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvents()
    {
        mCircleImageView.setOnClickListener(this);
        mMineSetting.setOnClickListener(this);
        mUserStateLayout.setOnClickListener(this);
        mTopicitemLayout.setOnClickListener(this);
        mMarketLayout.setOnClickListener(this);
        mAllcoverLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.mine_main_circleImageview:
                BaseActivity.launch(getActivity(), UserBasicDataActivity.class, null);
                break;
            case R.id.mine_main_userStateLayout:
                BaseActivity.launch(getActivity(), MineStateActivity.class, null);
                break;
            case R.id.mine_main_setting:
                BaseActivity.launch(getActivity(), MineSettingActivity.class, null);
                break;
            case R.id.mine_main_topicItemLayout:
                BaseActivity.launch(getActivity(), MineServiceAssessActivity.class, null);
                break;
            case R.id.mine_main_market_layout:
                if ( (appraiser == null) || (TextUtils.isEmpty(appraiser.salerHomePageUrl))
                        || !appraiser.salerHomePageUrl.startsWith("http") )
                {
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra(WebViewActivity.PARAM_TITLE, "检车销售");
                intent.putExtra(WebViewActivity.PARAM_URL, appraiser.salerHomePageUrl);
                BaseActivity.launch(getContext(), WebViewActivity.class, intent);
                getContext().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.mine_main_allcover_layout:
                Intent intent2 = new Intent();
                intent2.putExtra(WebViewActivity.PARAM_TITLE, "质保销售");
                intent2.putExtra(WebViewActivity.PARAM_URL, Config.ALLCOVER_HOME_URL + salerId);
                BaseActivity.launch(getContext(), WebViewActivity.class, intent2);
                break;
            default:
                break;
        }
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // 获取本地保存的uuid
        appraiserId = SharePrefenceManager.getAppraiserId(getContext());
        // 获取本地的基础数据
        new DBAsyncTask< Appraiser >(getContext(), mBasicDataBack).execute();
        // 获取本地的服务评价数据
        new DBAsyncTask< MineTopicsCore >(getContext(), mTopicBack).execute();
        // 每次进入页面都要更新数据
        isTopicFirst = true;
        isBasicDataFirst = true;
    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        String userData = bundle.getString("userdata");
        if ( !TextUtils.isEmpty(userData) )
        {
            if ( userData.equals("topics") )
            {
                ArrayList< MineTopicsCore > list = bundle.getParcelableArrayList(Oper.BUNDLE_EXTRA_DATA);
                if ( (list != null) && (list.size() > 0) )
                {
                    /**
                     * 这里返回的才是最新评论
                     */
                    mTopicTtile.setText(getString(R.string.str_mine_topic_title) + "(" + list.size() + ")");
                    new DBAsyncTask< MineTopicsCore >(getContext(), mTopicBack).execute();
                }
            } else if ( userData.equals("userdata") )
            {
                new DBAsyncTask< Appraiser >(getContext(), mBasicDataBack).execute();
            }
        }
    }

    @Override
    public void onFailure(OperResponse response)
    {
        setEmptyTopicItem();
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        setEmptyTopicItem();
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
    }

    /**
     * 设置无任何评论或者加载错误时显示的提示语
     */
    private void setEmptyTopicItem()
    {
        mTopicitemLayout.removeAllViews();
        TopicItemLayout itemLayout = new TopicItemLayout(getContext());
        itemLayout.emptyShow();
        mTopicitemLayout.addView(itemLayout);
    }

    /**
     * 将获取到的数据设置到页面控件中
     *
     * @param appraiser
     */
    private void setValue(Appraiser appraiser)
    {
        if ( getActivity() == null )
        {
            return;
        }
        this.appraiser = appraiser;
        String headPic = SharePrefenceManager.getHeadPic(getContext(), appraiserId);
        if ( !TextUtils.isEmpty(headPic) )
        {
            bitmapUtils.display(mCircleImageView, headPic);
        }
        mUserName.setText(appraiser.trueName);
        mUserProfession
                .setText(appraiser.level + getActivity().getString(R.string.str_mine_level_unit));
        if ( !TextUtils.isEmpty(appraiser.goodRate) && !appraiser.goodRate.equals("0%") )
        {
            mUserGood.setText(appraiser.goodRate);
        } else
        {
            mUserGood.setText("100%");
        }
        if ( appraiser.isProvideService == 0 )
        {
            state = getString(R.string.str_mine_level_state1);
        } else if ( appraiser.isProvideService == 1 )
        {
            state = getString(R.string.str_mine_level_state2);
        }
        String address = LocalUtils.getUtils(getContext()).address;
        if ( !TextUtils.isEmpty(address) )
        {
            state += "," + getString(R.string.str_mine_level_state3) + address;
        } else
        {
            mHandler.sendEmptyMessage(MineStateActivity.MSG_WHAT_MESSAGE);
        }
        mUserState.setText(state);
        if ( !TextUtils.isEmpty(appraiser.incomeAmount) )
        {
            mIncome.setText(appraiser.incomeAmount + getString(R.string.str_mine_myincome_unit));
        } else
        {
            mIncome.setText("0" + getString(R.string.str_mine_myincome_unit));
        }
        mIncomeSort.setText(getString(R.string.str_mine_sort_title) + appraiser.incomeOrder
                + getString(R.string.str_mine_myincome_unit1));
        mOrderCount.setText(appraiser.finishNum + getString(R.string.str_mine_myincome_unit2));
        mOrderCountSort.setText(getString(R.string.str_mine_sort_title) + appraiser.finishOrder
                + getString(R.string.str_mine_myincome_unit1));
    }

    /**
     * 从服务端更新接触数据
     */
    private void updateBasicData()
    {
        if ( !TextUtils.isEmpty(appraiserId) )
        {
            OperationFactManager.getManager().getUserBasicData(getContext(), appraiserId, this);
        }
    }

    /**
     * 从服务端获取最新服务评价
     */
    private void updateTopics()
    {
        newestModifyTime = SharePrefenceManager.getMainTopicTime(getContext(), appraiserId);
        long modifyTime = TextUtils.isEmpty(newestModifyTime) ? 0 : Long.parseLong(newestModifyTime);
        OperationFactManager.getManager().getTopicsList(getContext(), CURRNT_PAGE, PAGE_SIZE,
                appraiserId, modifyTime, OPER_TYPE, 0, this);
    }

    // private SpannableString generateSpnableText(String value, String unit) {
    // String str = value + " " + unit;
    // SpannableString ss = new SpannableString(str);
    // ss.setSpan(new AbsoluteSizeSpan(18, true), 0, str.length() - 1,
    // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    // ss.setSpan(new AbsoluteSizeSpan(12, true), str.length() - 1, str.length(),
    // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    // return ss;
    // }
}

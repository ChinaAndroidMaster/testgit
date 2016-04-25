package com.creditease.checkcars.ui.adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.bean.QAnswer;
import com.creditease.checkcars.data.bean.QuestionAnswer;
import com.creditease.checkcars.data.bean.UserInfo;
import com.creditease.checkcars.data.db.AppraiserUtil;
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
import com.creditease.checkcars.tools.JsonUtils;
import com.creditease.checkcars.tools.Utils;
import com.creditease.checkcars.ui.act.ImagePagerActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.widget.NoScrollGridView;
import com.creditease.checkcars.ui.widget.NoScrollListView;
import com.creditease.checkcars.ui.widget.XListView;
import com.creditease.utilframe.exception.HttpException;
import com.google.gson.reflect.TypeToken;

/**
 * @author zgb
 */
@SuppressLint( {"InflateParams", "ClickableViewAccessibility"} )
public class QuestionAnswerAdapter extends BaseAdapter implements RequestListener,
        DBFindCallBack< Appraiser >
{
    // 表示回复来自师傅客户端
    private static final int ANSWER_FROM_FATHER = 0;
    private LayoutInflater mInflater;
    private List< QuestionAnswer > mLists = new ArrayList< QuestionAnswer >();
    private Activity mActivity;
    /**
     * 图片适配器
     */
    private WorkPhotoAdapter mImageAdapter;
    /**
     * 回答列表适配器
     */
    private AnswerAdapter mAnswerAdapter;
    /**
     * 回复窗体
     */
    private PopupWindow mPopupWindow;
    /**
     * 异步弹出软键盘并计算绝对坐标
     */
    private Handler mHandler;
    /**
     * 软键盘
     */
    private InputMethodManager imm;
    /**
     *
     */
    private XListView mXListView;
    /**
     * 师傅uuid，唯一标识
     */
    private String appraiserId;
    /**
     * 师傅实体类
     */
    private Appraiser mAppraiser;
    private Window mRootWindow;
    private View mRootView;
    private EditText mEditText;
    private TextView mSendView;
    private int distance;

    public QuestionAnswerAdapter(Activity activity, XListView mXListView)
    {
        mActivity = activity;
        this.mXListView = mXListView;
        // 获取本地保存的uuid
        appraiserId = SharePrefenceManager.getAppraiserId(mActivity);
        mInflater = LayoutInflater.from(activity);
        mHandler = new Handler();
        mRootWindow = mActivity.getWindow();
        mRootView = mRootWindow.getDecorView().findViewById(android.R.id.content);
        // 获取保存在本地的师傅基本信息
        new DBAsyncTask< Appraiser >(mActivity, this).execute();
    }

    @Override
    public int getCount()
    {
        return mLists.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        QuestionAnswer item = mLists.get(position);
        ViewHolder holder = null;
        if ( convertView == null )
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.question_answer_item, null);
            holder.mRelativeLayout =
                    ( RelativeLayout ) convertView.findViewById(R.id.ques_answer_item_layout);
            holder.mQuestion = ( TextView ) convertView.findViewById(R.id.question_answer_item_title);
            holder.mGridView =
                    ( NoScrollGridView ) convertView.findViewById(R.id.question_answer_item_gridView);
            holder.mDateTime = ( TextView ) convertView.findViewById(R.id.question_answer_item_datetime);
            holder.mAnswerView =
                    ( ImageView ) convertView.findViewById(R.id.question_answer_item_answerButton);
            holder.mListView =
                    ( NoScrollListView ) convertView.findViewById(R.id.question_answer_item_noscorlllistview);
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        // 设置问题title
        if ( !TextUtils.isEmpty(item.title) )
        {
            holder.mQuestion.setText(item.title);
        }
        // 设置问题发布时间
        String dateTime = Utils.timestamps2Date(item.createTime, "yyyy-MM-dd hh:mm");
        if ( !TextUtils.isEmpty(dateTime) )
        {
            holder.mDateTime.setText(dateTime);
        }

        // 设置图片集合
        String photoPaths = item.imgValue;
        if ( !TextUtils.isEmpty(photoPaths) )
        {
            holder.mGridView.setVisibility(View.VISIBLE);
            mImageAdapter = new WorkPhotoAdapter(mActivity, 1);
            holder.mGridView.setAdapter(mImageAdapter);
            ArrayList< String > photoLists = new ArrayList< String >();
            String[] array = TextUtils.split(photoPaths, ";");
            // 处理url为空的情况,去掉为空的部分
            List< String > imageUrls = new ArrayList< String >();
            for ( String url : array )
            {
                if ( !TextUtils.isEmpty(url) )
                {
                    imageUrls.add(url);
                }
            }
            if ( imageUrls.size() > 0 )
            {
                photoLists.addAll(imageUrls);
            }
            mImageAdapter.updateDataAndNotify(photoLists);
            holder.mGridView.setOnItemClickListener(new OnLookImageClick(photoLists));
        } else
        {
            holder.mGridView.setVisibility(View.GONE);
        }
        // 将json字符串转换成list集合
        Type type = new TypeToken< List< QAnswer > >()
        {
        }.getType();
        item.answers = JsonUtils.parsonJson2Obj(item.qanswerJson, type);
        // 设置回复集合
        if ( (item.answers != null) && (item.answers.size() > 0) )
        {
            holder.mListView.setVisibility(View.VISIBLE);
            mAnswerAdapter = new AnswerAdapter(mActivity, item.answerId);
            holder.mListView.setAdapter(mAnswerAdapter);
            mAnswerAdapter.updateDataAndNotify(item.answers);
        } else
        {
            holder.mListView.setVisibility(View.GONE);
        }

        AnswerClick answerClick = new AnswerClick(holder.mRelativeLayout, position);
        holder.mAnswerView.setOnClickListener(answerClick);
        return convertView;
    }

    @Override
    public List< Appraiser > doDBOperation(DbHelper helper, int operId) throws CEDbException
    {
        return AppraiserUtil.getUtil(mActivity).getAppraiserByUserId(appraiserId);
    }

    @Override
    public void dataCallBack(int operId, List< Appraiser > result)
    {
        if ( (result != null) && (result.size() > 0) )
        {
            mAppraiser = result.get(0);
        }
    }

    @Override
    public void errorCallBack(int operId, CEDbException e)
    {

    }

    @Override
    public void onSuccess(Bundle bundle)
    {
        OperResponse resp = bundle.getParcelable(Oper.BUNDLE_EXTRA_RESPONSE);
        String code = resp.respcode;
        if ( TextUtils.equals(code, "0000") )
        {
            // showToast(R.string.str_mine_save_success);
        }
    }

    @Override
    public void onFailure(OperResponse response)
    {

    }

    @Override
    public void onDataError(String errorMsg, String result)
    {

    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {

    }

    /**
     * 弹出软件盘并计算出软键盘的顶部的绝对Y坐标，以及点击的item的底部的绝对Y坐标
     *
     * @param relativeLayout
     * @param position
     */
    private void popupInputMethodWindow(final RelativeLayout relativeLayout, final int position)
    {
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // 弹出软件盘
                imm =
                        ( InputMethodManager ) relativeLayout.getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener()
                        {
                            int oldY = 0;

                            @Override
                            public void onGlobalLayout()
                            {
                                if ( imm.isActive() && (mPopupWindow != null) && mPopupWindow.isShowing() )
                                {
                                    // 计算软件盘的高度
                                    Rect r = new Rect();
                                    View view = mRootWindow.getDecorView();
                                    view.getWindowVisibleDisplayFrame(r);
                                    int keyboardY = r.bottom - dip2px(mActivity, 30);
                                    // 获取被点击的item的左顶点的坐标
                                    int[] location = new int[2];
                                    relativeLayout.getLocationOnScreen(location);
                                    // 计算出被点击的item的底部顶点的Y坐标
                                    int anchorY = location[1] + relativeLayout.getMeasuredHeight();
                                    // 因为每次点击，
                                    if ( oldY == 0 )
                                    {
                                        oldY = anchorY;
                                    } else
                                    {
                                        anchorY = Math.max(anchorY, oldY);
                                    }
                                    // 计算出要移动的距离onGlobalLayout方法都会被执行两次，因此item的锚点取两次值中的最大的那个值才是准确的
                                    distance = anchorY - keyboardY;
                                    if ( distance > 0 )
                                    {
                                        mXListView.postDelayed(new Runnable()
                                        {

                                            @Override
                                            public void run()
                                            {
                                                // 时间设置太小导致移动距离不准确
                                                mXListView.smoothScrollBy(distance, 800);
                                            }
                                        }, 0);
                                    }
                                }
                            }
                        });
            }
        }, 0);
    }

    @SuppressWarnings( "deprecation" )
    private void showPopupWindow(final RelativeLayout relativeLayout, final int position)
    {
        /**
         * 获取popView
         */
        View popView = mInflater.inflate(R.layout.answer_popupwindow, null);
        mEditText = ( EditText ) popView.findViewById(R.id.answer_pop_answerEdit);
        mSendView = ( TextView ) popView.findViewById(R.id.answer_pop_sendAnswer);

        mEditText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String content = mEditText.getText().toString().trim();
                if ( !TextUtils.isEmpty(content) )
                {
                    mSendView.setBackgroundResource(R.drawable.edittext_normal_send);
                    mSendView.setTextColor(mActivity.getResources().getColor(R.color.white));
                } else
                {
                    mSendView.setBackgroundResource(R.drawable.edittext_normal);
                    mSendView.setTextColor(mActivity.getResources().getColor(R.color.color_mine_profession));
                }
            }
        });
        mPopupWindow =
                new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
        /**
         * 监听回退按键
         */
        mEditText.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                    return true;
                }
                return false;
            }
        });

        /**
         * 发送回复
         */
        mSendView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String str = mEditText.getText().toString().trim();
                if ( TextUtils.isEmpty(str) )
                {
                    showToast(R.string.answer_send_content);
                    return;
                }
                String coreId = SharePrefenceManager.getUserId(mActivity);
                mPopupWindow.dismiss();
                // 更新页面
                QuestionAnswer item = mLists.get(position);
                // 更新时间
                item.modifyTime = System.currentTimeMillis();
                // 生成一个新的answer
                QAnswer answer = getQAnswer(str, mAppraiser, coreId);
                item.answers.add(answer);
                item.qanswerJson = JsonUtils.requestObjectBean(item.answers);
                notifyDataSetChanged();
                try
                {
                    // 更新本地的数据
                    QuestionAnswerUtil.getUtil(mActivity).saveOrUpdateAnswer(item, item.uuid);
                    // 上传到服务器
                    OperationFactManager.getManager().updateQuestionAnswerData(mActivity, item.uuid, coreId,
                            0, str, QuestionAnswerAdapter.this);
                } catch ( CEDbException e )
                {
                    e.printStackTrace();
                }
            }
        });
        // 使其聚焦，要想监听菜单里控件的事件就必须调用此方法
        mPopupWindow.setFocusable(true);
        // 设置允许在外点击消失
        mPopupWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置软键盘不会挡着popupWindow
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 显示popupWindow
        mPopupWindow.showAtLocation(relativeLayout, Gravity.BOTTOM, 0, 0);
        // 监听菜单的关闭事件
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {

            }
        });

        // 监听触屏事件
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                return false;
            }
        });
    }

    /**
     * generate new answer
     *
     * @param conent
     * @param appraiser
     * @param coreId
     * @return
     */
    private QAnswer getQAnswer(String conent, Appraiser appraiser, String coreId)
    {
        QAnswer answer = new QAnswer();
        // 回复内容
        answer.content = conent;
        answer.createTime = System.currentTimeMillis();
        // 表示回复来自师傅客户端
        answer.position = ANSWER_FROM_FATHER;
        // 用户的id，coreId，不是uuid
        answer.fromId = coreId;
        answer.uuid = appraiserId;
        if ( mAppraiser != null )
        {
            UserInfo userInfo = new UserInfo();
            userInfo.phoneNumber = mAppraiser.phoneNumber;
            userInfo.headPic = mAppraiser.headPic;
            userInfo.trueName = mAppraiser.trueName;
            answer.userInfo = userInfo;
        }
        answer.userInfoJson = JsonUtils.requestObjectBean(answer);
        return answer;
    }

    /**
     * 关闭popupWindow
     */
    public void dismissPopupWindow()
    {
        if ( (mPopupWindow != null) && mPopupWindow.isShowing() )
        {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 短暂显示Toast提示(来自res)
     **/
    public void showToast(int resId)
    {
        ZLToast.getToast().showToast(mActivity, resId);
    }

    /**
     * 数据发生变化时更新方法
     *
     * @param list
     */
    public void updateDataAndNotify(List< QuestionAnswer > list)
    {
        if ( list != null )
        {
            mLists.clear();
            mLists.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return ( int ) ((dpValue * scale) + 0.5f);
    }

    class ViewHolder
    {
        public TextView mQuestion;// 问题
        public TextView mDateTime;// 问题发布时间
        public NoScrollGridView mGridView;// 图片网格
        public ImageView mAnswerView;// 回复按钮控件
        public NoScrollListView mListView;// 回复列表
        public RelativeLayout mRelativeLayout;// 外层布局
    }

    /**
     * 回复点击事件
     */
    class AnswerClick implements View.OnClickListener
    {
        private RelativeLayout mRelativeLayout;
        private int position;

        public AnswerClick(RelativeLayout relativeLayout, int position)
        {
            mRelativeLayout = relativeLayout;
            this.position = position;
        }

        @Override
        public void onClick(View v)
        {
            showPopupWindow(mRelativeLayout, position);
            popupInputMethodWindow(mRelativeLayout, position);
        }
    }

    class OnLookImageClick implements AdapterView.OnItemClickListener
    {
        private ArrayList< String > imageList;

        public OnLookImageClick(ArrayList< String > list)
        {
            imageList = list;
        }

        @Override
        public void onItemClick(AdapterView< ? > parent, View view, int position, long id)
        {
            Intent intent = new Intent(mActivity, ImagePagerActivity.class);
            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageList);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            mActivity.startActivity(intent);
        }

    }

}

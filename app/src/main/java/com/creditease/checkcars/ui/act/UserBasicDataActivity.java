package com.creditease.checkcars.ui.act;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.Appraiser;
import com.creditease.checkcars.data.bean.Remark;
import com.creditease.checkcars.data.db.AppraiserUtil;
import com.creditease.checkcars.data.db.base.DBAsyncTask;
import com.creditease.checkcars.data.db.base.DBFindCallBack;
import com.creditease.checkcars.data.db.base.DbHelper;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.tools.JsonUtils;
import com.creditease.checkcars.tools.ShareUtils;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.adapter.WorkPhotoAdapter;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.widget.NoScrollGridView;
import com.creditease.checkcars.ui.widget.RoundAngleImageView;
import com.creditease.utilframe.BitmapUtils;
import com.creditease.utilframe.exception.HttpException;
import com.umeng.analytics.MobclickAgent;

public class UserBasicDataActivity extends BaseActivity implements OnClickListener,
        RequestListener, DBFindCallBack< Appraiser >
{

    public static final String KEY_SELECT_PHOTO_ACTION = "action";

    public static final int REQUEST_CAMERA_CODE_PHOTO_0 = 0;

    public static final int REQUEST_CAMERA_CODE_PHOTO_1 = 1;

    public static final int REQUEST_CAMERA_CODE_PHOTO_2 = 2;

    public static final int REQUEST_CAMERA_CODE_PHOTO_3 = 3;
    /**
     * 返回
     */
    private ImageView mBackView;
    /**
     * 分享
     */
    private ImageView mShareView;
    /**
     * 标题
     */
    private TextView mBasicTitle;
    /**
     * 圆角头像
     */
    private RoundAngleImageView mRoundedImageView;
    /**
     * 真实姓名
     */
    private TextView mTrueName;
    /**
     * 擅长车系描述
     */
    private TextView mBestCar;
    /**
     * 师傅宣言布局
     */
    private RelativeLayout mFatherLayout;
    /**
     * 师傅宣言
     */
    private TextView mFatherWord;
    /**
     * 车价区间外层布局
     */
    private RelativeLayout mIntervalPriceLayout;
    /**
     * 车价区间最低车价
     */
    private TextView mStartPrice;
    /**
     * 车价区间最高车价
     */
    private TextView mEndPrice;
    /**
     * 工作照控件
     */
    private NoScrollGridView mGridView;
    /**
     * 用户uuid
     */
    private String appraiserId;
    /**
     * 用户基本资料uuid
     */
    private String coreId;

    private LoadingGifDialog loadDialog;
    /**
     * 设置图片辅助类
     */
    private BitmapUtils bitmapUtils;

    /**
     * 师傅基础数据对象
     */
    private Appraiser mAppraiser;

    /**
     * 工作照适配器
     */
    private WorkPhotoAdapter mAdapter;

    /**
     * 工作照图片地址集合
     */
    private List< String > mList = new ArrayList< String >();

    /**
     * 初始化View
     */
    @Override
    protected void initViews()
    {
        setContentView(R.layout.mine_basic_data);
        loadDialog = new LoadingGifDialog(getContext());
        mBackView = ( ImageView ) findViewById(R.id.header_imgv_back);
        mBasicTitle = ( TextView ) findViewById(R.id.header_tv_title);
        mShareView = ( ImageView ) findViewById(R.id.header_imgv_btn);
        mShareView.setClickable(true);
        mShareView.setVisibility(View.VISIBLE);
        mShareView.setImageResource(R.drawable.image_bg_selector);
        mRoundedImageView = ( RoundAngleImageView ) findViewById(R.id.mine_basic_roundedImageview);
        mTrueName = ( TextView ) findViewById(R.id.mine_basic_trueName);
        mBestCar = ( TextView ) findViewById(R.id.mine_basic_bestcar);
        mIntervalPriceLayout = ( RelativeLayout ) findViewById(R.id.mine_basic_pirceintervalLayout);
        mFatherLayout = ( RelativeLayout ) findViewById(R.id.mine_basic_fatherLayout);
        mFatherWord = ( TextView ) findViewById(R.id.mine_basic_fatherword);
        mStartPrice = ( TextView ) findViewById(R.id.mine_basic_startprice);
        mEndPrice = ( TextView ) findViewById(R.id.mine_basic_endprice);
        mBasicTitle.setText(R.string.str_mine_basic_title);
        mGridView = ( NoScrollGridView ) findViewById(R.id.mine_basic_gridView);
        bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.mine_image_default);
        bitmapUtils.configDefaultLoadingImage(R.drawable.mine_image_default);
        loadDialog.showDialog();
    }


    @Override
    protected void initData()
    {

    }

    @Override
    protected void initEvents()
    {
        mBackView.setOnClickListener(this);
        mShareView.setOnClickListener(this);
        mFatherLayout.setOnClickListener(this);
        mAdapter = new WorkPhotoAdapter(getContext(), 0);
        mGridView.setAdapter(mAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ( (resultCode == RESULT_OK) && (data != null) )
        {
            String savePath = data.getStringExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_SAVEPATH);
            int action = data.getIntExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION, 0);
            if ( savePath == null )
            {
                return;
            }
            if ( action == REQUEST_CAMERA_CODE_PHOTO_0 )
            {
                if ( data.getExtras() != null )
                {
                    Bitmap bitmap = data.getExtras().getParcelable("imagedata");
                    if ( bitmap != null )
                    {
                        mRoundedImageView.setImageBitmap(bitmap);
                    }
                    OperationFactManager.getManager().uploadHeadPic(getContext(), bitmap, coreId, this);
                }
            }
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
            case R.id.header_imgv_btn:
                shareCard();
                break;
            case R.id.mine_basic_fatherLayout:
                startActivity(UserDataSaveActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        appraiserId = SharePrefenceManager.getAppraiserId(getContext());
        String headPic = SharePrefenceManager.getHeadPic(getContext(), appraiserId);
        if ( !TextUtils.isEmpty(headPic) )
        {
            bitmapUtils.display(mRoundedImageView, headPic);
        }
        // 加载本地个人名片数据
        new DBAsyncTask< Appraiser >(getContext(), this).execute();
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
            coreId = mAppraiser.coreId;
            if ( !TextUtils.isEmpty(mAppraiser.trueName) )
            {
                mTrueName.setText(mAppraiser.trueName);
            }
            if ( !TextUtils.isEmpty(mAppraiser.remark) )
            {
                Remark remark = JsonUtils.parserJsonStringToObject(mAppraiser.remark, Remark.class);
                if ( (remark != null) && !TextUtils.isEmpty(remark.specialty) )
                {
                    mBestCar.setText(remark.specialty);
                }
            }
            if ( !TextUtils.isEmpty(mAppraiser.declaration) )
            {
                mFatherWord.setText(mAppraiser.declaration);
            }
            try
            {
                if ( !TextUtils.isEmpty(mAppraiser.minPrice) && !TextUtils.isEmpty(mAppraiser.maxPrice) )
                {
                    int startPrice = Integer.parseInt(mAppraiser.minPrice);
                    int endPrice = Integer.parseInt(mAppraiser.maxPrice);
                    if ( (startPrice > 0) && (endPrice > 0) && (startPrice < endPrice) )
                    {
                        mStartPrice.setText(mAppraiser.minPrice);
                        mEndPrice.setText(mAppraiser.maxPrice);
                    } else
                    {
                        mIntervalPriceLayout.setVisibility(View.GONE);
                    }
                } else
                {
                    mIntervalPriceLayout.setVisibility(View.GONE);
                }
            } catch ( NumberFormatException e )
            {
                e.printStackTrace();
            }
            String workPhoto = mAppraiser.worksImgs;
            if ( !TextUtils.isEmpty(workPhoto) )
            {
                mList.clear();
                String[] array = TextUtils.split(workPhoto, ";");
                // 处理url为空的情况,去掉为空的部分
                List< String > imageUrls = new ArrayList< String >();
                for ( String url : array )
                {
                    if ( !TextUtils.isEmpty(url) )
                    {
                        imageUrls.add(url);
                    }
                }
                mList.addAll(imageUrls);
                mAdapter.updateDataAndNotify(mList);
                mGridView.setVisibility(View.VISIBLE);
            } else
            {
                mGridView.setVisibility(View.GONE);
            }
        }
        loadDialog.dismiss();
    }

    @Override
    public void errorCallBack(int operId, CEDbException e)
    {
        loadDialog.dismiss();
    }

    // @SuppressWarnings("unused")
    // private void gotoPhoto(int requestCode) {
    // Intent intent = new Intent(getContext(), SelectPhotoActivity.class);
    // intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION, requestCode);
    // intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_CUT, 1);
    // startActivityForResult(intent, requestCode);
    // }

    /**
     * 分享名片
     */
    private void shareCard()
    {
        if ( mAppraiser == null )
        {
            return;
        }
        String imageUrl = SharePrefenceManager.getHeadPic(getContext(), appraiserId);
        String content =
                TextUtils.isEmpty(mAppraiser.declaration) ? getResources().getString(
                        R.string.str_mine_basic_title) : mAppraiser.declaration;
        String title = mAppraiser.trueName;
        ShareUtils.getUtils(getApplicationContext()).showShare(imageUrl, appraiserId, content, title);
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

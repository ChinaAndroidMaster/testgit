/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.act;

import java.util.Date;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.creditease.checkcars.R;
import com.creditease.checkcars.tools.PhotoUtils;
import com.creditease.checkcars.tools.TimeUtils;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 子龍
 * @function
 * @date 2015年5月19日
 * @company CREDITEASE
 */
public class SelectPhotoActivity extends BaseActivity implements OnClickListener
{
    // private static final String TAG = SelectPhotoActivity.class.getSimpleName();
    public static final String KEY_SELECT_PHOTO_ACTION = "action";
    public static final String KEY_SELECT_PHOTO_SAVEPATH = "savepath";
    public static final String KEY_SELECT_PHOTO_CUT = "cut";
    public static int intent_action = 0;
    public static int intent_headPic = 0;
    private Button btnTakePhoto;
    private Button btnSelectPhoto;
    private Button btnCancel;
    private String mTakePicturePath;

    @Override
    protected void initViews()
    {
        setContentView(R.layout.activity_select_photo);
        intent_action = getIntent().getIntExtra(KEY_SELECT_PHOTO_ACTION, 0);
        intent_headPic = getIntent().getIntExtra(KEY_SELECT_PHOTO_CUT, 0);
        btnTakePhoto = ( Button ) findViewById(R.id.Button_take_photo);
        btnSelectPhoto = ( Button ) findViewById(R.id.Button_select_photo);
        btnCancel = ( Button ) findViewById(R.id.Button_cancel);
    }

    @Override
    protected void initData()
    {
    }

    @Override
    protected void initEvents()
    {
        btnTakePhoto.setOnClickListener(this);
        btnSelectPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private String getAbsoluteImagePath(Uri uri)
    {
        String[] proj = {MediaColumns.DATA};
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(uri, proj, null, null, null);
        if ( cursor != null )
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
        {
            return uri.getPath();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Log.i(TAG,
        // "SelectPhotoActivity onActivityResult 1 "
        // + System.currentTimeMillis());
        super.onActivityResult(requestCode, resultCode, data);
        switch ( requestCode )
        {
            case PhotoUtils.REQUEST_IMAGE_CODE:
                if ( (data == null) || (data.getData() == null) )
                {
                    finish();
                    return;
                }
                if ( resultCode == RESULT_OK )
                {
                    Uri uri = data.getData();
                    String uriPath = getAbsoluteImagePath(uri);
                    Intent certIntent = new Intent();
                    certIntent.putExtra(KEY_SELECT_PHOTO_ACTION, intent_action);
                    certIntent.putExtra(KEY_SELECT_PHOTO_SAVEPATH, uriPath);
                    setResult(resultCode, certIntent);
                }
                finish();
                break;
            case PhotoUtils.REQUEST_CAMERA_CODE:
                if ( resultCode == RESULT_OK )
                {
                    Intent certIntent = new Intent();
                    certIntent.putExtra(KEY_SELECT_PHOTO_ACTION, intent_action);
                    certIntent.putExtra(KEY_SELECT_PHOTO_SAVEPATH, mTakePicturePath);
                    setResult(resultCode, certIntent);
                }
                finish();
                break;
            case PhotoUtils.REQUEST_IMAGE_CUT_CODE:
                if ( resultCode == RESULT_OK )
                {
                    Intent certIntent = new Intent();
                    certIntent.putExtra(KEY_SELECT_PHOTO_ACTION, intent_action);
                    certIntent.putExtra(KEY_SELECT_PHOTO_SAVEPATH, "cutImage");
                    if ( data.getParcelableExtra("data") != null )
                    {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("imagedata", data.getParcelableExtra("data"));
                        certIntent.putExtras(bundle);
                    }
                    setResult(resultCode, certIntent);
                }
                finish();
                break;
            case PhotoUtils.REQUEST_CAMERA_CUT_CODE:
                PhotoUtils.cropImageUri(getContext(), PhotoUtils.REQUEST_CUTCAMERA_CUT_CODE);
                break;
            case PhotoUtils.REQUEST_CUTCAMERA_CUT_CODE:
                if ( resultCode == RESULT_OK )
                {
                    Intent certIntent = new Intent();
                    certIntent.putExtra(KEY_SELECT_PHOTO_ACTION, intent_action);
                    certIntent.putExtra(KEY_SELECT_PHOTO_SAVEPATH, mTakePicturePath);
                    if ( data.getParcelableExtra("data") != null )
                    {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("imagedata", data.getParcelableExtra("data"));
                        certIntent.putExtras(bundle);
                    }
                    setResult(resultCode, certIntent);
                }
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch ( id )
        {
            case R.id.Button_take_photo:
                if ( intent_headPic == 0 )
                {
                    mTakePicturePath =
                            PhotoUtils.takePicture(this,
                                    "temp_" + TimeUtils.timeToString(new Date(), "yyyyMMddHHmmss") + ".jpg",
                                    PhotoUtils.REQUEST_CAMERA_CODE);
                } else
                {
                    String imagePath =
                            "temp_" + TimeUtils.timeToString(new Date(), "yyyyMMddHHmmss") + ".jpg";
                    mTakePicturePath =
                            PhotoUtils.startCamearPicCut(this, imagePath, PhotoUtils.REQUEST_CAMERA_CUT_CODE);
                }
                break;
            case R.id.Button_select_photo:
                if ( intent_headPic == 0 )
                {
                    PhotoUtils.selectPhoto(this, PhotoUtils.REQUEST_IMAGE_CODE);
                } else
                {
                    PhotoUtils.selectPhotoCut(this, PhotoUtils.REQUEST_IMAGE_CUT_CODE);
                }
                break;
            case R.id.Button_cancel:
                finish();
                break;
        }
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
    public BaseActivity getContext()
    {
        return this;
    }

}

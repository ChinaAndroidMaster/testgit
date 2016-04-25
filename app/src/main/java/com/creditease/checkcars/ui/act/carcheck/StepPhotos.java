package com.creditease.checkcars.ui.act.carcheck;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.Car;
import com.creditease.checkcars.data.bean.CarImg;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.ui.act.SelectPhotoActivity;
import com.creditease.checkcars.ui.adapter.CarPhotosAdapter;

/**
 * @author 子龍
 * @function
 * @date 2015年11月6日
 * @company CREDITEASE
 */
public class StepPhotos extends BaseStep
{
    private GridView photosGridView;
    private CarPhotosAdapter carPhotosAdapter;
    private Car car;

    public StepPhotos(CarCheckActivity activity, View contentRootView, CarReport report)
    {
        super(activity, contentRootView, report);
        car = report.reportCar;
        initViewData();
    }

    @Override
    public void initViews()
    {
        photosGridView = ( GridView ) findViewById(R.id.car_imgs_gridview);
        carPhotosAdapter = new CarPhotosAdapter(mContext);
        photosGridView.setAdapter(carPhotosAdapter);

    }

    @Override
    public void initEvents()
    {
        photosGridView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView< ? > parent, View view, int position, long id)
            {
                Object obj = parent.getAdapter().getItem(position);
                if ( obj instanceof CarImg )
                {
                    CarImg img = ( CarImg ) obj;
                    Intent intent;
                    switch ( img.imgType )
                    {
                        case CarImg.TYPE_IMG_FRONT:
                            intent = new Intent(mContext, SelectPhotoActivity.class);
                            intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_FRONT);
                            startActivityForResult(intent,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_FRONT);
                            break;
                        case CarImg.TYPE_IMG_BACK:
                            intent = new Intent(mContext, SelectPhotoActivity.class);
                            intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_BACK);
                            startActivityForResult(intent,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_BACK);
                            break;
                        case CarImg.TYPE_IMG_BRAND:
                            intent = new Intent(mContext, SelectPhotoActivity.class);
                            intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_BRAND);
                            startActivityForResult(intent,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_BRAND);
                            break;
                        case CarImg.TYPE_IMG_ENGINE:
                            intent = new Intent(mContext, SelectPhotoActivity.class);
                            intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_ENGINE);
                            startActivityForResult(intent,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_ENGINE);
                            break;
                        case CarImg.TYPE_IMG_CCR:
                            intent = new Intent(mContext, SelectPhotoActivity.class);
                            intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_CCR);
                            startActivityForResult(intent,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_CCR);
                            break;
                        case CarImg.TYPE_IMG_PANEL:
                            intent = new Intent(mContext, SelectPhotoActivity.class);
                            intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_PANEL);
                            startActivityForResult(intent,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_PANEL);
                            break;
                        case CarImg.TYPE_IMG_TRUNK:
                            intent = new Intent(mContext, SelectPhotoActivity.class);
                            intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_TRUNK);
                            startActivityForResult(intent,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_TRUNK);
                            break;
                        case CarImg.TYPE_IMG_UNDERPAN:
                            intent = new Intent(mContext, SelectPhotoActivity.class);
                            intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_UNDERPAN);
                            startActivityForResult(intent,
                                    ReportControl.ImageControl.REQUEST_CAMERA_CODE_BASE_CAR_UNDERPAN);
                            break;
                    }
                }
            }
        });
    }

    protected void initViewData()
    {
        if ( car != null )
        {
            carPhotosAdapter.updateDataAndNotify(car.imgList);
        }
    }

    public void notifyDataSetChanged()
    {
        carPhotosAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean validate()
    {
        // TODO
        return false;
    }

    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub

    }

}

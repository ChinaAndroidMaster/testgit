package com.creditease.checkcars.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CarImg;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.db.CarReportUtil;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.tools.Utils;
import com.creditease.checkcars.ui.act.CarReportActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.act.carcheck.CarCheckActivity;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.utilframe.BitmapUtils;
import com.creditease.utilframe.exception.HttpException;

@SuppressLint( "InflateParams" )
public class CarPreReportAdapter extends BaseAdapter
{

    private LayoutInflater mInflater;
    private BitmapUtils utils;
    private List< CarReport > mList = new ArrayList< CarReport >();
    private Context context;
    private CarOrder order;
    private LoadingGifDialog loadDialog;
    public CarPreReportAdapter(Context context)
    {
        this.context = context;
        loadDialog = new LoadingGifDialog(context);
        mInflater = LayoutInflater.from(context);
        utils = new BitmapUtils(context);
        utils.configDefaultLoadFailedImage(R.drawable.mine_image_default);
        utils.configDefaultLoadingImage(R.drawable.mine_image_default);
    }

    @Override
    public int getCount()
    {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        CarReport item = mList.get(position);
        ViewHolder holder;
        if ( convertView == null )
        {
            convertView = mInflater.inflate(R.layout.car_pre_report_item, null);
            holder = new ViewHolder();
            holder.carIcon = ( ImageView ) convertView.findViewById(R.id.car_pre_report_item_icon);
            holder.carName = ( TextView ) convertView.findViewById(R.id.car_pre_report_item_name);
            holder.carVin = ( TextView ) convertView.findViewById(R.id.car_pre_report_item_vin);
            holder.carTime = ( TextView ) convertView.findViewById(R.id.car_pre_report_item_time);
            holder.carExam = ( TextView ) convertView.findViewById(R.id.car_pre_report_item_exam);
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        if ( item != null )
        {
            if ( item.reportCar != null )
            {
                CarImg img = item.reportCar.getCarImg(CarImg.TYPE_IMG_FRONT);
                if ( (img != null) && !TextUtils.isEmpty(img.imgValue) )
                {
                    utils.display(holder.carIcon, img.imgValue);
                } else
                {
                    utils.display(holder.carIcon, "");
                }
            } else
            {
                utils.display(holder.carIcon, "");
            }
            if ( (item.reportCar != null) && !TextUtils.isEmpty(item.reportCar.carBrand) )
            {
                holder.carName.setText(item.reportCar.carBrand);
            } else
            {
                holder.carName.setText("未知车辆");
            }

            if ( (item.reportCar != null) && !TextUtils.isEmpty(item.reportCar.carVin) )
            {
                holder.carVin.setText(context.getString(R.string.str_car_pre_carvin)
                        + item.reportCar.carVin);
            } else
            {
                holder.carVin.setText(R.string.str_car_pre_carvin);
            }
            if ( item.startTime != 0 )
            {
                String dateTime = Utils.timestamps2Date(item.startTime, "yyyy-MM-dd hh:mm");
                holder.carTime.setText(context.getString(R.string.str_car_pre_start_time) + dateTime);
            }
            if ( item.status == CarReport.STATUS_REFUSE )
            {
                holder.carExam.setText(R.string.report_modify);
            } else if ( item.status == CarReport.STATUS_INIT )
            {
                holder.carExam.setText(R.string.str_car_pre_continue_check);
            } else
            {
                holder.carExam.setText(R.string.report_review);
            }
            holder.carExam.setOnClickListener(new CheckReportClickListener(item));
        }
        return convertView;
    }

    /**
     * 修改报告
     *
     * @param order
     * @param cr    下午12:15:33
     */
    private void modifyReport(final CarOrder order, final CarReport cr)
    {
        try
        {
            CarReport creport = CarReportUtil.getUtil(context).getReportByClientUuid(cr.clientUuid);
            if ( creport == null )
            {
                /**
                 * 服务器获取报告报数
                 */
                OperationFactManager.getManager().getReportByUuidWS(context, cr.uuid,
                        new RequestListener()
                        {

                            @Override
                            public void onDataError(String errorMsg, String result)
                            {
                                loadDialog.dismiss();
                                ZLToast.getToast().showToast(context, "加载失败，请检查您的网络连接");
                            }

                            @Override
                            public void onFailure(OperResponse response)
                            {
                                loadDialog.dismiss();
                                ZLToast.getToast().showToast(context, "加载失败，" + response.respmsg);
                            }

                            @Override
                            public void onRequestError(HttpException error, String msg)
                            {
                                loadDialog.dismiss();
                                ZLToast.getToast().showToast(context, "加载失败，请检查您的网络连接");
                            }

                            @Override
                            public void onSuccess(Bundle bundle)
                            {
                                loadDialog.dismiss();
                                Intent intent = new Intent();
                                Bundle b = new Bundle();
                                b.putParcelable(CarCheckActivity.PARAM_ORDER, order);
                                b.putParcelable(CarCheckActivity.PARAM_REPORT, cr);
                                intent.putExtras(b);
                                BaseActivity.launch(context, CarCheckActivity.class, intent);
                                (( Activity ) context).overridePendingTransition(R.anim.push_left_in,
                                        R.anim.push_left_out);
                            }
                        });
                loadDialog.showDialog();
                return;
            }

        } catch ( CEDbException e )
        {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putParcelable(CarCheckActivity.PARAM_ORDER, order);
        b.putParcelable(CarCheckActivity.PARAM_REPORT, cr);
        intent.putExtras(b);
        BaseActivity.launch(context, CarCheckActivity.class, intent);
        (( Activity ) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void updateDataAndNotify(CarOrder order)
    {
        if ( mList == null )
        {
            return;
        }
        mList.clear();
        this.order = order;
        mList.addAll(order.reportList);
        notifyDataSetChanged();
    }

    class ViewHolder
    {
        public ImageView carIcon;
        public TextView carName;
        public TextView carVin;
        public TextView carTime;
        public TextView carExam;
    }

    class CheckReportClickListener implements View.OnClickListener
    {

        private CarReport item;

        public CheckReportClickListener(CarReport item)
        {
            this.item = item;
        }

        @Override
        public void onClick(View v)
        {
            if ( item.status == CarReport.STATUS_REFUSE )
            {
                // modify report
                modifyReport(order, item);
            } else if ( item.status == CarReport.STATUS_INIT )
            {
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putParcelable(CarCheckActivity.PARAM_ORDER, order);
                b.putString(CarCheckActivity.PARAM_CILENT_UUID, item.clientUuid);
                intent.putExtras(b);
                BaseActivity.launch(context, CarCheckActivity.class, intent);
                (( Activity ) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            } else
            {
                String url = item.viewUrl;
                Intent intent = new Intent();
                intent.putExtra(CarReportActivity.PARAM_REPORT_URL, url);
                BaseActivity.launch(context, CarReportActivity.class, intent);
                (( Activity ) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        }

    }
}

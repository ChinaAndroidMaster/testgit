package com.creditease.checkcars.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.alipay.AlipayConfig;
import com.creditease.checkcars.data.bean.CarOrder;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.bean.OrderPay;
import com.creditease.checkcars.data.bean.PayBean;
import com.creditease.checkcars.data.db.CarOrderUtil;
import com.creditease.checkcars.data.db.CarReportUtil;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.OrderPayInfoBean.PayInfoResult;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.tools.CommitOrderEventUtils;
import com.creditease.checkcars.tools.ImageUtils;
import com.creditease.checkcars.ui.act.CarReportActivity;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.act.carcheck.CarCheckActivity;
import com.creditease.checkcars.ui.act.main.FragmentOrders;
import com.creditease.checkcars.ui.act.main.MainActivity;
import com.creditease.checkcars.ui.act.pay.PayActivity;
import com.creditease.checkcars.ui.dialog.CommitReportDialog;
import com.creditease.checkcars.ui.dialog.FeeInfoDialog;
import com.creditease.checkcars.ui.dialog.LoadingGifDialog;
import com.creditease.checkcars.ui.dialog.PayDialog;
import com.creditease.checkcars.ui.dialog.PayDialog.IFinishPay;
import com.creditease.checkcars.ui.dialog.base.TDialog;
import com.creditease.utilframe.exception.HttpException;

/**
 * 订单适配器
 *
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
@SuppressLint( "InflateParams" )
public class CarOrderAdapter extends BaseAdapter
{

    private List< CarOrder > list = new ArrayList< CarOrder >();
    private LayoutInflater mInflater;
    private Context mContext;
    private ImageUtils utils;
    private LoadingGifDialog loadDialog;
    private CarOrder finishOrder, payOrder, feeOrder;
    private String paying;
    private String finishPayStr;
    private String serviceType0;
    private Drawable passedDrawable;
    private Drawable unpassDrawable;
    private RequestListener queryPayInfoListener2 = new RequestListener()
    {

        @Override
        public void onDataError(String errorMsg, String result)
        {
            nullFeeOrder();
            dismissLoadD();
            ZLToast.getToast().showToast(mContext, "加载失败");
        }

        @Override
        public void onFailure(OperResponse response)
        {
            dismissLoadD();
            ZLToast.getToast().showToast(mContext, "加载失败");
            nullFeeOrder();
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            dismissLoadD();
            ZLToast.getToast().showToast(mContext, "加载失败,请检查您的网络连接");
            nullFeeOrder();
        }

        @Override
        public void onSuccess(Bundle bundle)
        {
            dismissLoadD();
            PayInfoResult re = ( PayInfoResult ) bundle.getSerializable(Oper.BUNDLE_EXTRA_DATA);
            FeeInfoDialog dialog = new FeeInfoDialog(mContext);
            dialog.setData(getServiceType(feeOrder), re);
            TDialog.Builder.builderDialog(dialog).showDialog();
            notifyDataSetChanged();
            nullFeeOrder();
        }
    };
    private RequestListener finishPayListener = new RequestListener()
    {

        @Override
        public void onDataError(String errorMsg, String result)
        {
            nullPayOrder();
            dismissLoadD();
            ZLToast.getToast().showToast(mContext, errorMsg);
        }

        @Override
        public void onFailure(OperResponse response)
        {
            nullPayOrder();
            dismissLoadD();
            ZLToast.getToast().showToast(mContext, response != null ? response.respmsg : "操作失败");
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            nullPayOrder();
            dismissLoadD();
            ZLToast.getToast().showToast(mContext, "加载失败,请检查您的网络连接");
        }

        @Override
        public void onSuccess(Bundle bundle)
        {
            if ( payOrder != null )
            {
                payOrder.status = CarOrder.STATUS_PAY_SUCCESS;
                String amount = payOrder.tradeAmount;
                double p = Double.parseDouble(amount);
                if ( p == 0 )
                {
                    payOrder.isCashTransfer = CarOrder.ACCOUNT_TRANSFER_DONE;
                } else
                {
                    pay(payOrder);
                }
            }
            FragmentOrders.getFragment().loadOrdersWS();
            dismissLoadD();
            notifyDataSetChanged();
            nullPayOrder();
        }
    };
    private RequestListener queryPayInfoListener = new RequestListener()
    {

        @Override
        public void onDataError(String errorMsg, String result)
        {
            nullPayOrder();
            dismissLoadD();
            ZLToast.getToast().showToast(mContext, "加载失败");
        }

        @Override
        public void onFailure(OperResponse response)
        {
            nullPayOrder();
            dismissLoadD();
            ZLToast.getToast().showToast(mContext, "加载失败");
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            nullPayOrder();
            dismissLoadD();
            ZLToast.getToast().showToast(mContext, "加载失败,请检查您的网络连接");
        }

        @Override
        public void onSuccess(Bundle bundle)
        {
            dismissLoadD();
            PayInfoResult re = ( PayInfoResult ) bundle.getSerializable(Oper.BUNDLE_EXTRA_DATA);
            final String orderId = re.orderId;
            PayDialog dialog = new PayDialog(mContext);
            dialog.setData(re.tradeAmount + "");

            dialog.setFinishPay(new IFinishPay()
            {

                @Override
                public void cancelPay()
                {
                    nullPayOrder();
                }

                @Override
                public void finishPay()
                {
                    OperationFactManager.getManager().finishOrderPayWS(mContext, orderId, 0,
                            finishPayListener);
                    showLoadingDialog();
                }
            });
            TDialog.Builder.builderDialog(dialog).showDialog();
            notifyDataSetChanged();
        }
    };
    private RequestListener listener = new RequestListener()
    {

        @Override
        public void onDataError(String errorMsg, String result)
        {
            dismissLoadD();
            nullFinishOrder();
        }

        @Override
        public void onFailure(OperResponse response)
        {
            dismissLoadD();
            nullFinishOrder();
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            dismissLoadD();
            nullFinishOrder();
        }

        @Override
        public void onSuccess(Bundle bundle)
        {
            finishOrder.status = CarOrder.STATUS_FINISH_SERVICE;
            try
            {
                CarOrderUtil.getUtil(mContext).updateOrder(finishOrder);
            } catch ( CEDbException e )
            {
            }
            notifyDataSetChanged();
            (( MainActivity ) mContext).loadOrders();
            // 支付提示-微信接口
            OperationFactManager.getManager().weixinPayNoti(mContext, finishOrder, null);
            // TODO
            ZLToast.getToast().showToast(mContext, "完成服务，请提醒用户支付");
            dismissLoadD();
            nullFinishOrder();
        }
    };

    public CarOrderAdapter(Context context)
    {
        mContext = context;
        mInflater = ( LayoutInflater ) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        utils = ImageUtils.getUtils(context);
        utils.configDefaultLoadFailedImage(R.drawable.defalut_head);
        utils.configDefaultLoadingImage(R.drawable.defalut_head);
        // loadDialog = new LoadingGifDialog(context);
        paying = mContext.getResources().getString(R.string.str_carorders_btn_finished);
        serviceType0 = mContext.getResources().getString(R.string.str_carorder_type_0);
        finishPayStr = context.getResources().getString(R.string.str_carorders_btn_payed);
        passedDrawable = context.getResources().getDrawable(R.drawable.report_passed);
        unpassDrawable = context.getResources().getDrawable(R.drawable.report_unpass);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if ( convertView == null )
        {
            convertView = mInflater.inflate(R.layout.listitem_carorder, null);
            holder = new ViewHolder();
            holder.orderNum = ( TextView ) convertView.findViewById(R.id.item_caroder_ordernum);
            holder.headImgV = ( ImageView ) convertView.findViewById(R.id.item_caroder_imgv_head);
            holder.nameTV = ( TextView ) convertView.findViewById(R.id.item_caroder_name);
            holder.payNumberTV = ( TextView ) convertView.findViewById(R.id.item_caroder_paynumber);
            holder.phoneTV = ( TextView ) convertView.findViewById(R.id.item_caroder_phonenumber);
            holder.timeTV = ( TextView ) convertView.findViewById(R.id.item_caroder_time);
            holder.addressTV = ( TextView ) convertView.findViewById(R.id.item_caroder_address);
            holder.typeTV = ( TextView ) convertView.findViewById(R.id.item_caroder_type);
            holder.carBrandTV = ( TextView ) convertView.findViewById(R.id.item_caroder_carbrand);
            holder.payTV = ( TextView ) convertView.findViewById(R.id.item_caroder_paymoney);
            holder.descripTV = ( TextView ) convertView.findViewById(R.id.item_caroder_descrip);
            holder.checkBtn = ( Button ) convertView.findViewById(R.id.item_caroder_btn_start);
            holder.finishBtn = ( Button ) convertView.findViewById(R.id.item_caroder_btn_finish);
            holder.orderStateTV = ( TextView ) convertView.findViewById(R.id.item_caroder_state);
            holder.reportsLayout =
                    ( LinearLayout ) convertView.findViewById(R.id.item_caroder_reports_layout);
            convertView.setTag(holder);
        } else
        {
            holder = ( ViewHolder ) convertView.getTag();
        }
        final CarOrder order = list.get(position);
        final boolean isBusiness = !TextUtils.isEmpty(order.businessId);
        String num = TextUtils.isEmpty(order.orderNum) ? "" : order.orderNum;
        if ( TextUtils.isEmpty(order.orderNum) )
        {
            holder.orderNum.setVisibility(View.GONE);
        }
        holder.orderNum.setText(mContext.getString(R.string.car_ordernum_title) + num);
        holder.nameTV.setText(order.trueName);
        holder.phoneTV.setText(order.phoneNumber);
        // TODO 预约时间
        utils.display(holder.headImgV, order.customer.headPic);
        holder.timeTV.setText(TextUtils.isEmpty(order.assignTime) ? order.orderTime : order.assignTime);
        boolean haveBrand = !(TextUtils.isEmpty(order.carBrand) || "不限".equals(order.carBrand));
        boolean haveSer = !(TextUtils.isEmpty(order.carSeries) || "不限".equals(order.carSeries));
        String carBrand = haveBrand ? order.carBrand : "不限";
        String carSer = haveSer ? order.carSeries : "不限";
        holder.carBrandTV.setVisibility(View.VISIBLE);
        if ( haveBrand && haveSer )
        {
            holder.carBrandTV.setText(carBrand + "-" + carSer);
        } else if ( haveBrand )
        {
            holder.carBrandTV.setText(carBrand);
        } else if ( haveSer )
        {
            holder.carBrandTV.setText(carSer);
        } else
        {
            holder.carBrandTV.setVisibility(View.GONE);
        }
        boolean fee = TextUtils.isEmpty(order.tradeAmount);
        holder.payTV.setText("服务费用" + (fee ? "" : (getPayNumber(order.tradeAmount) + "元")));
        holder.addressTV.setText(order.address);
        if ( !TextUtils.isEmpty(order.description) )
        {
            holder.descripTV.setVisibility(View.VISIBLE);
            holder.descripTV.setText("注:" + order.description);
        } else
        {
            holder.descripTV.setVisibility(View.GONE);
        }
        holder.finishBtn.setClickable(false);
        if ( isBusiness )
        {
            holder.typeTV.setTextColor(0xffec7016);
        } else
        {
            holder.typeTV.setTextColor(0xff888888);
        }
        holder.typeTV.setText(getServiceType2(order));
        holder.orderStateTV.setVisibility(View.GONE);
        switch ( order.status )
        {
            case CarOrder.STATUS_REVISIT:
                holder.finishBtn.setVisibility(View.GONE);
                // holder.checkBtn
                // .setBackgroundResource(R.color.color_bg_btn_next_unable);
                holder.checkBtn.setText(R.string.str_carorders_btn_assign_p);
                holder.checkBtn.setTextColor(mContext.getResources().getColor(R.color.color_text_gray));
                holder.checkBtn.setBackgroundResource(R.drawable.bg_btn_order_unable);
                holder.checkBtn.setPadding(15, 5, 15, 5);
                break;
            case CarOrder.STATUS_ASSIGNED:
            case CarOrder.STATUS_CHECKING:
                if ( (order.showReportList != null) && (order.showReportList.size() > 0) )
                {
                    holder.finishBtn.setClickable(true);
                    holder.finishBtn.setVisibility(View.VISIBLE);
                    holder.finishBtn.setBackgroundResource(R.drawable.bg_btn_able);
                } else
                {
                    holder.finishBtn.setClickable(false);
                    holder.finishBtn.setVisibility(View.GONE);
                }
                holder.payNumberTV.setVisibility(View.GONE);
                holder.checkBtn.setText(R.string.str_carorders_btn_evaluate);
                holder.checkBtn.setTextColor(mContext.getResources()
                        .getColor(R.color.color_text_item_order));
                holder.checkBtn.setBackgroundResource(R.drawable.bg_btn_order_selector);
                holder.checkBtn.setPadding(15, 5, 15, 5);
                break;
            case CarOrder.STATUS_PAY_SUCCESS:
                holder.carBrandTV.setVisibility(View.GONE);
                holder.payNumberTV.setVisibility(View.GONE);
                // holder.checkBtn
                // .setBackgroundResource(R.color.color_bg_btn_next_unable);
                // holder.finishBtn.setTextColor(0xffffffff);
                switch ( order.payType )
                {
                    case OrderPay.TYPE_PAY_CASH:

                        holder.checkBtn.setText(finishPayStr + "（现金）");

                        if ( order.isCashTransfer == CarOrder.ACCOUNT_TRANSFER_UNDO )
                        {
                            holder.finishBtn.setVisibility(View.VISIBLE);
                            holder.finishBtn.setBackgroundResource(R.drawable.bg_btn_pay);
                            holder.finishBtn.setText(R.string.pay);
                        } else if ( order.isCashTransfer == CarOrder.ACCOUNT_TRANSFER_DONE )
                        {
                            holder.orderStateTV.setVisibility(View.VISIBLE);
                            holder.finishBtn.setBackgroundResource(R.drawable.bg_btn_unable);
                            holder.finishBtn.setVisibility(View.GONE);
                        }
                        break;
                    case OrderPay.TYPE_PAY_WEIXIN:
                        holder.orderStateTV.setVisibility(View.VISIBLE);
                        holder.finishBtn.setVisibility(View.GONE);
                        holder.checkBtn.setText(finishPayStr + "（微信）");
                        break;
                    default:
                        holder.finishBtn.setVisibility(View.GONE);
                        holder.checkBtn.setText(finishPayStr);
                        break;
                }

                holder.checkBtn.setTextColor(mContext.getResources().getColor(R.color.color_text_gray));
                holder.checkBtn.setBackgroundResource(R.drawable.bg_btn_order_unable);
                holder.checkBtn.setPadding(15, 5, 15, 5);
                break;
            case CarOrder.STATUS_FINISH_SERVICE:
            case CarOrder.STATUS_PAY_PREPARED:
            case CarOrder.STATUS_PAYING:
                holder.carBrandTV.setVisibility(View.GONE);
                holder.finishBtn.setVisibility(View.GONE);
                holder.payNumberTV.setVisibility(View.VISIBLE);
                // holder.checkBtn
                // .setBackgroundResource(R.color.color_bg_btn_next_pay);
                if ( !TextUtils.isEmpty(order.tradeAmount) )
                {
                    String pay = getPayNumber(order.tradeAmount);
                    holder.payNumberTV.setText(paying + pay);
                } else
                {
                    holder.payNumberTV.setText(paying);
                }
                if ( isBusiness )
                {
                    holder.checkBtn.setText("公司结算");
                    holder.checkBtn.setTextColor(mContext.getResources().getColor(R.color.color_text_gray));
                    holder.checkBtn.setBackgroundResource(R.drawable.bg_btn_order_unable);
                    holder.checkBtn.setPadding(15, 5, 15, 5);
                    // holder.checkBtn.setClickable(false);
                } else
                {
                    holder.checkBtn.setText(R.string.str_dialog_payinfo_title);
                    holder.checkBtn.setTextColor(mContext.getResources().getColor(
                            R.color.color_text_item_order));
                    holder.checkBtn.setBackgroundResource(R.drawable.bg_btn_paydetail_selector);
                    holder.checkBtn.setPadding(15, 5, 15, 5);
                    // holder.checkBtn.setClickable(true);
                }

                break;
            case CarOrder.STATUS_CANCELED:
                holder.carBrandTV.setVisibility(View.GONE);
                holder.finishBtn.setVisibility(View.GONE);
                holder.payNumberTV.setVisibility(View.GONE);
                // holder.checkBtn
                // .setBackgroundResource(R.color.color_bg_btn_next_unable);
                holder.checkBtn.setText(R.string.str_carorders_btn_canceled);
                holder.checkBtn.setTextColor(mContext.getResources().getColor(R.color.color_text_gray));
                holder.checkBtn.setBackgroundResource(R.drawable.bg_btn_order_unable);
                holder.checkBtn.setPadding(15, 5, 15, 5);
                break;
            case CarOrder.STATUS_OBSOLETE:
                holder.finishBtn.setVisibility(View.GONE);
                holder.payNumberTV.setVisibility(View.GONE);
                // holder.checkBtn
                // .setBackgroundResource(R.color.color_bg_btn_next_unable);
                holder.checkBtn.setText(R.string.str_carorders_btn_canceled);
                holder.checkBtn.setBackgroundResource(R.drawable.bg_btn_order_selector);
                holder.checkBtn.setPadding(15, 5, 15, 5);
                break;
            default:
                holder.finishBtn.setVisibility(View.GONE);
                holder.payNumberTV.setVisibility(View.GONE);
                holder.carBrandTV.setVisibility(View.GONE);
                break;
        }
        holder.payNumberTV.setVisibility(View.GONE);
        holder.phoneTV.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                telPhone(order.phoneNumber);
                // 记录电话呼出时间
                CommitOrderEventUtils.getUtil(mContext.getApplicationContext()).recordTime(order.uuid, 0,
                        12, null);
            }
        });
        holder.payTV.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                feeOrder = order;
                // 待支付状态的订单
                OperationFactManager.getManager().getOrderPayInfo(mContext, order, queryPayInfoListener2);
                showLoadingDialog();
            }
        });
        holder.checkBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {

                if ( (order.status == CarOrder.STATUS_FINISH_SERVICE)
                        || (order.status == CarOrder.STATUS_PAY_PREPARED)
                        || (order.status == CarOrder.STATUS_PAYING) )
                {
                    if ( isBusiness )
                    {
                        return;
                    }
                    // 待支付状态的订单
                    payOrder = order;
                    OperationFactManager.getManager().getOrderPayInfo(mContext, order, queryPayInfoListener);
                    showLoadingDialog();
                    return;
                }
                if ( (order.status != CarOrder.STATUS_ASSIGNED)
                        && (order.status != CarOrder.STATUS_CHECKING) )
                {
                    // 非已分配&非检车状态
                    return;
                }
                CarReport localInitReport = null;
                try
                {
                    localInitReport = CarReportUtil.getUtil(mContext).getInitReport(order.uuid);
                } catch ( CEDbException e )
                {
                    e.printStackTrace();
                }
                CarReport initReport =
                        ((order.reportList != null) && (order.reportList.size() > 0)) ? order.reportList.get(0)
                                : null;
                initReport =
                        ((initReport != null) && (initReport.status != CarReport.STATUS_INIT)) ? null
                                : initReport;

                if ( (initReport == null) && (localInitReport == null) )
                {
                    if ( (order.reportList != null) && (order.reportList.size() > 0) )
                    {
                        // 检测下一辆车
                        startCheckAnotherCar(order);
                    } else
                    {
                        startCheckFirstCar(order);
                    }
                } else if ( localInitReport != null )
                {
                    continueCheckCar(order, localInitReport);
                } else if ( initReport != null )
                {
                    continueCheckCar(order, initReport);
                }
            }
        });
        holder.finishBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if ( (order.status == CarOrder.STATUS_PAY_SUCCESS)
                        && (order.payType == OrderPay.TYPE_PAY_CASH) )
                {
                    if ( order.isCashTransfer == CarOrder.ACCOUNT_TRANSFER_UNDO )
                    {
                        // 完成支付
                        pay(order);
                    }
                } else
                {
                    finishOrder(order);
                }
            }
        });
        if ( (order.showReportList != null) && (order.showReportList.size() > 0) )
        {
            holder.reportsLayout.setVisibility(View.VISIBLE);
            holder.reportsLayout.removeAllViews();
            for ( final CarReport rep : order.showReportList )
            {
                if ( rep == null )
                {
                    continue;
                }
                View view = getView(R.layout.listitem_carorder_item_report);
                // final TextView tv = new TextView(mContext);
                TextView carTypeTV =
                        ( TextView ) view.findViewById(R.id.listitem_carorder_item_report_cartype_tv);
                TextView reportStateTV =
                        ( TextView ) view.findViewById(R.id.listitem_carorder_item_report_state_tv);
                final TextView reportBtnTV =
                        ( TextView ) view.findViewById(R.id.listitem_carorder_item_report_btn_tv);
                carTypeTV.setText(rep.reportCar == null ? "检测报告：" : rep.reportCar.carBrand + " : ");

                if ( rep.status == CarReport.STATUS_PASSED )
                {
                    reportStateTV.setText(R.string.report_passed);
                    reportBtnTV.setText(R.string.report_review);
                    reportStateTV.setCompoundDrawablesWithIntrinsicBounds(null, null, passedDrawable, null);
                } else if ( rep.status == CarReport.STATUS_FINISH )
                {
                    reportStateTV.setText(R.string.report_undo);
                    reportBtnTV.setText(R.string.report_review);
                    reportStateTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else if ( rep.status == CarReport.STATUS_REFUSE )
                {
                    reportStateTV.setText(R.string.report_unpass);
                    reportBtnTV.setText(R.string.report_modify);
                    reportStateTV.setCompoundDrawablesWithIntrinsicBounds(null, null, unpassDrawable, null);
                }

                holder.reportsLayout.addView(view);
                // holder.reportsLayout.addView(getLineView());
                reportBtnTV.setTag(rep.viewUrl);
                reportBtnTV.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View arg0)
                    {
                        if ( rep.status == CarReport.STATUS_REFUSE )
                        {
                            // modify report
                            modifyReport(order, rep);
                        } else
                        {
                            String url = ( String ) reportBtnTV.getTag();
                            Intent intent = new Intent();
                            intent.putExtra(CarReportActivity.PARAM_REPORT_URL, url);
                            BaseActivity.launch(mContext, CarReportActivity.class, intent);
                            (( Activity ) mContext).overridePendingTransition(R.anim.push_left_in,
                                    R.anim.push_left_out);
                        }
                    }
                });
                // i++;
            }
        } else
        {
            holder.reportsLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    private View getView(int layoutId)
    {
        return mInflater.inflate(layoutId, null);
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
            CarReport creport = CarReportUtil.getUtil(mContext).getReportByClientUuid(cr.clientUuid);
            if ( creport == null )
            {
                OperationFactManager.getManager().getReportByUuidWS(mContext, cr.uuid,
                        new RequestListener()
                        {

                            @Override
                            public void onDataError(String errorMsg, String result)
                            {
                                loadDialog.dismiss();
                                ZLToast.getToast().showToast(mContext, "加载失败，请检查您的网络连接");
                            }

                            @Override
                            public void onFailure(OperResponse response)
                            {
                                loadDialog.dismiss();
                                ZLToast.getToast().showToast(mContext, "加载失败，" + response.respmsg);
                            }

                            @Override
                            public void onRequestError(HttpException error, String msg)
                            {
                                loadDialog.dismiss();
                                ZLToast.getToast().showToast(mContext, "加载失败，请检查您的网络连接");
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
                                BaseActivity.launch(mContext, CarCheckActivity.class, intent);
                                (( Activity ) mContext).overridePendingTransition(R.anim.push_left_in,
                                        R.anim.push_left_out);
                            }
                        });
                showLoadingDialog();
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
        BaseActivity.launch(mContext, CarCheckActivity.class, intent);
        (( Activity ) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void nullFeeOrder()
    {
        feeOrder = null;
    }

    private void nullFinishOrder()
    {
        finishOrder = null;
    }

    private void nullPayOrder()
    {
        payOrder = null;
    }

    public void showLoadingDialog()
    {
        // loadDialog.setText("正在加载支付详情...");
        // TDialog.Builder.builderDialog(loadDialog).showDialog();
        loadDialog = new LoadingGifDialog(mContext);
        loadDialog.showDialog();
    }

    public void showUploadingDialog()
    {
        // loadDialog.setText(R.string.text_order_uploading);
        // TDialog.Builder.builderDialog(loadDialog).showDialog();
        loadDialog = new LoadingGifDialog(mContext);
        loadDialog.showDialog();
    }

    /**
     * 打电话
     *
     * @param phone
     */
    public void telPhone(String phone)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 设置适配器数据
     *
     * @param list 上午11:15:05
     */
    public void updateDataAndNotify(List< CarOrder > list)
    {
        if ( list != null )
        {
            for ( CarOrder carOrder : list )
            {
                if ( carOrder.showReportList == null )
                {
                    carOrder.showReportList = new ArrayList< CarReport >();
                }
                carOrder.showReportList.clear();
                if ( (carOrder.reportList != null) && (carOrder.reportList.size() > 0) )
                {
                    for ( int i = 0, len = carOrder.reportList.size(); i < len; i++ )
                    {
                        CarReport cr = carOrder.reportList.get(i);
                        if ( cr.status == CarReport.STATUS_INIT )
                        {
                            carOrder.reportList.remove(i);
                            carOrder.reportList.add(0, cr);
                        } else
                        {
                            carOrder.showReportList.add(cr);
                        }
                    }
                }
            }
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 设置适配器数据
     *
     * @param list 上午11:15:05
     */
    public void updateMoreData(List< CarOrder > list)
    {
        if ( list != null )
        {

            for ( CarOrder carOrder : list )
            {
                if ( carOrder.showReportList == null )
                {
                    carOrder.showReportList = new ArrayList< CarReport >();
                }
                carOrder.showReportList.clear();
                if ( (carOrder.reportList != null) && (carOrder.reportList.size() > 0) )
                {
                    for ( int i = 0, len = carOrder.reportList.size(); i < len; i++ )
                    {
                        CarReport cr = carOrder.reportList.get(i);
                        if ( cr.status == CarReport.STATUS_INIT )
                        {
                            carOrder.reportList.remove(i);
                            carOrder.reportList.add(0, cr);
                        } else
                        {
                            carOrder.showReportList.add(cr);
                        }
                    }
                }
            }
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clearData()
    {
        list.clear();
        notifyDataSetChanged();
    }

    public void startCheckAnotherCar(final CarOrder order)
    {
        if ( ((order.serviceType == CarOrder.TYPE_SERVICE_0) && order.p_name.contains("辆"))
                || (order.quantity <= 1) )
        {
            ZLToast.getToast().showToast(mContext, "本订单只支持一单一辆");
            return;
        }
        final CommitReportDialog dialog = new CommitReportDialog(mContext);
        dialog.setContent(R.string.add_another_report);
        dialog.setOnOKBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
                addReport(order, null);
            }
        }).setOnCancelBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
            }
        });
        TDialog.Builder.builderDialog(dialog).showDialog();
    }

    private void addReport(CarOrder order, CarReport initReport)
    {
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putParcelable(CarCheckActivity.PARAM_ORDER, order);
        if ( initReport != null )
        {
            b.putString(CarCheckActivity.PARAM_CILENT_UUID, initReport.clientUuid);
        }
        intent.putExtras(b);
        BaseActivity.launch(mContext, CarCheckActivity.class, intent);
        (( Activity ) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 继续检车
     *
     * @param order
     * @param initReport 下午12:05:00
     */
    public void continueCheckCar(CarOrder order, CarReport initReport)
    {
        addReport(order, initReport);
    }

    /**
     * 开始检测第一辆车
     *
     * @param order 下午12:05:08
     */
    public void startCheckFirstCar(final CarOrder order)
    {
        final CommitReportDialog dialog = new CommitReportDialog(mContext);
        dialog.setContent(R.string.add_report);
        dialog.setOnOKBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
                addReport(order, null);
            }
        }).setOnCancelBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
            }
        });
        TDialog.Builder.builderDialog(dialog).showDialog();
    }

    public void dismissLoadD()
    {
        loadDialog.dismiss();
    }

    /**
     * 转账支付
     *
     * @param order 下午4:10:32
     */
    private void pay(CarOrder order)
    {
        String phone = SharePrefenceManager.getUserLoginName(mContext);
        PayBean payBean =
                new PayBean(order.orderNum, "师傅看车转账支付", "看车服务现金收款转账支付", order.tradeAmount, phone,
                        AlipayConfig.SELLER, order.uuid, PayBean.TYPE_ORDER_CARCHECK);
        // PayBean payBean =
        // new PayBean(order.orderNum, "师傅看车转账支付", "看车服务现金收款转账支付","0.01", phone,
        // AlipayConfig.SELLER, order.uuid, PayBean.TYPE_ORDER_CARCHECK);
        // PayBean payBean =
        // new PayBean(order.orderNum, "师傅看车转账支付", "看车服务现金收款转账支付", order.tradeAmount, phone);
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putParcelable(PayActivity.PARAM_BEAN_PAY, payBean);
        intent.putExtras(b);
        BaseActivity.launch(mContext, PayActivity.class, intent);
        (( Activity ) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 完成订单服务
     *
     * @param order 下午2:59:56
     */
    private void finishOrder(final CarOrder order)
    {
        final CommitReportDialog dialog = new CommitReportDialog(mContext);
        dialog.setContent(R.string.finish_order_ornot);
        dialog.setOnOKBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
                finishOrder = order;
                OperationFactManager.getManager().finishCarOrder(mContext, order.uuid,
                        CarOrder.STATUS_FINISH_SERVICE, System.currentTimeMillis() + "", listener);
                showUploadingDialog();
            }
        }).setOnCancelBtnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                dialog.dismiss();
            }
        });
        TDialog.Builder.builderDialog(dialog).showDialog();

    }

    @SuppressWarnings( "unused" )
    private View getLineView()
    {
        View lineView = new View(mContext);
        LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 2);
        lp.setMargins(0, 20, 0, 0);
        lineView.setLayoutParams(lp);
        lineView.setBackgroundColor(0xfff2f2f2);
        return lineView;
    }

    private String getPayNumber(String tradeAmount)
    {
        String pay = "";
        if ( !TextUtils.isEmpty(tradeAmount) )
        {
            int index = tradeAmount.indexOf(".");
            int len = tradeAmount.length();

            if ( ((index + 2) < len) && (index > 0) )
            {
                pay = tradeAmount.subSequence(0, index + 2).toString();
            } else
            {
                pay = tradeAmount;
            }

        }
        return pay;
    }

    private String getServiceType(CarOrder order)
    {
        if ( order == null )
        {
            return "检测服务：";
        }
        StringBuilder fee = new StringBuilder();
        if ( order.serviceType == CarOrder.TYPE_SERVICE_0 )
        {

            // fee.append(serviceType0);
            if ( order.p_name != null )
            {
                if ( order.p_name.contains("辆") )
                {
                    fee.append("按辆检测服务：");
                } else if ( order.p_name.contains("时") )
                {
                    fee.append("包时检测服务：");
                }
            }
        } else if ( order.serviceType == CarOrder.TYPE_SERVICE_1 )
        {
            fee.append(mContext.getResources().getString(R.string.str_carorder_type_1) + "：");
        } else
        {
            fee.append("检测服务：");
        }
        return fee.toString();
    }

    private String getServiceType2(CarOrder order)
    {
        if ( order == null )
        {
            return "检测服务";
        }
        StringBuilder fee = new StringBuilder();
        // boolean isBussiness = !TextUtils.isEmpty(order.businessId);
        // if (isBussiness) {
        // fee.append("企业订单—");
        // }
        if ( order.serviceType == CarOrder.TYPE_SERVICE_0 )
        {

            // fee.append(serviceType0);
            if ( order.p_name != null )
            {
                if ( order.p_name.contains("辆") )
                {
                    fee.append(serviceType0 + "—按辆套餐");
                } else if ( order.p_name.contains("4时") )
                {
                    fee.append(serviceType0 + "—4小时套餐");
                } else if ( order.p_name.contains("8时") )
                {
                    fee.append(serviceType0 + "—8小时套餐");
                }
            }
        } else if ( order.serviceType == CarOrder.TYPE_SERVICE_1 )
        {
            fee.append(mContext.getResources().getString(R.string.str_carorder_type_1));
        } else if ( order.serviceType == CarOrder.TYPE_SERVICE_B )
        {
            fee.append("企业订单—");
            if ( !TextUtils.isEmpty(order.p_name) )
            {
                fee.append(order.p_name);
            } else
            {
                fee.append("检测服务");
            }
        } else
        {
            fee.append("检测服务");
        }
        return fee.toString();
    }

    class ViewHolder
    {
        TextView orderNum;
        ImageView headImgV;
        TextView nameTV;
        TextView payNumberTV;
        TextView phoneTV;
        TextView timeTV;
        TextView addressTV;
        TextView typeTV;
        TextView descripTV;
        TextView carBrandTV;
        TextView payTV;
        Button checkBtn;
        Button finishBtn;
        LinearLayout reportsLayout;
        TextView orderStateTV;

    }

}

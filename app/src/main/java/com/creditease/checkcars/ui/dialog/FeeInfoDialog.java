/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.net.oper.bean.OrderPayInfoBean.PayInfoResult;
import com.creditease.checkcars.ui.dialog.base.BDialog;

/**
 * @author 子龍
 * @function
 * @date 2015年6月4日
 * @company CREDITEASE
 */
public class FeeInfoDialog extends BDialog implements OnClickListener
{


    private ImageView closeTV;
    private TextView serviceTypeTV, originalFeeTV, privilegeActivitiesTV, privilegeOldUserTV,
            privilegeChanelTV, privilegeOtherTV, totalTV;

    // private RelativeLayout activitiesLayout, oldUserLayout, chanelLayout, otherLayout;


    public FeeInfoDialog(Context context)
    {
        super(context);

    }

    @Override
    public int getDialogViewLayoutId()
    {
        return R.layout.dialog_feeinfo;
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

    @Override
    public void initDialogView()
    {
        closeTV = ( ImageView ) view.findViewById(R.id.dialog_feeinfo_close);
        serviceTypeTV = ( TextView ) view.findViewById(R.id.dialog_feeinfo_servicetype_tv);
        originalFeeTV = ( TextView ) view.findViewById(R.id.dialog_feeinfo_original_fee_tv);
        privilegeActivitiesTV = ( TextView ) view.findViewById(R.id.dialog_feeinfo_privilege_activity_tv);
        privilegeOldUserTV = ( TextView ) view.findViewById(R.id.dialog_feeinfo_privilege_olduser_tv);
        privilegeChanelTV = ( TextView ) view.findViewById(R.id.dialog_feeinfo_privilege_channel_tv);
        privilegeOtherTV = ( TextView ) view.findViewById(R.id.dialog_feeinfo_privilege_other_tv);
        totalTV = ( TextView ) view.findViewById(R.id.dialog_feeinfo_total_tv);
        // activitiesLayout = (RelativeLayout) view.findViewById(R.id.dialog_feeinfo_layout_activities);
        // oldUserLayout = (RelativeLayout) view.findViewById(R.id.dialog_feeinfo_layout_olduser);
        // chanelLayout = (RelativeLayout) view.findViewById(R.id.dialog_feeinfo_layout_channel);
        // otherLayout = (RelativeLayout) view.findViewById(R.id.dialog_feeinfo_layout_other);
        closeTV.setOnClickListener(this);
    }

    @Override
    public boolean isCanceledOnKeyBack()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCanceledOnTouchOutside()
    {
        return false;
    }

    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.dialog_feeinfo_close:
                dismiss();
                break;

            default:
                break;
        }

    }

    /**
     * 设置数据
     *
     * @param carNum    检车数量
     * @param couponNum 优惠券
     * @param value     需收取费用 下午7:42:18
     */
    public void setData(String serviceType, PayInfoResult bean)
    {
        serviceTypeTV.setText(serviceType);
        if ( bean == null )
        {
            return;
        }
        originalFeeTV.setText(getPayNumber(String.valueOf(bean.orignAmount)) + "元");
        double fee = (bean.discountAmount - bean.channelDiscount - bean.oldCusAmount) + bean.otherPay;
        privilegeActivitiesTV.setText(getPayNumber((fee == 0 ? "" : "-") + String.valueOf(fee)) + "元");
        privilegeOldUserTV.setText(getPayNumber((bean.oldCusAmount == 0 ? "" : "-")
                + String.valueOf(bean.oldCusAmount))
                + "元");
        privilegeChanelTV.setText(getPayNumber((bean.channelDiscount == 0 ? "" : "-")
                + String.valueOf(bean.channelDiscount))
                + "元");
        privilegeOtherTV.setText((bean.otherPay > 0 ? "+" : "")
                + getPayNumber(String.valueOf(bean.otherPay)) + "元");
        totalTV.setText(getPayNumber(String.valueOf(bean.tradeAmount)) + "元");
    }

}

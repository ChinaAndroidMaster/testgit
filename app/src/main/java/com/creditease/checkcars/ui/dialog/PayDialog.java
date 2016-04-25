/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.dialog.base.BDialog;

/**
 * @author 子龍
 * @function
 * @date 2015年6月4日
 * @company CREDITEASE
 */
public class PayDialog extends BDialog implements OnClickListener
{

    private TextView payTV;
    private Button sureBtn, cancelBtn;
    private IFinishPay finishPayImp;

    public PayDialog(Context context)
    {
        super(context);

    }

    @Override
    public int getDialogViewLayoutId()
    {
        return R.layout.dialog_pay;
    }

    @Override
    public void initDialogView()
    {
        payTV = ( TextView ) view.findViewById(R.id.dialog_pay_fee_tv);
        sureBtn = ( Button ) view.findViewById(R.id.dialog_pay_btn_true);
        cancelBtn = ( Button ) view.findViewById(R.id.dialog_pay_btn_cancel);

        sureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
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
            case R.id.dialog_pay_btn_cancel:
                if ( finishPayImp != null )
                {
                    finishPayImp.cancelPay();
                }
                break;
            case R.id.dialog_pay_btn_true:
                if ( finishPayImp != null )
                {
                    finishPayImp.finishPay();
                }
                break;

            default:
                break;
        }
        dismiss();
    }

    /**
     * 设置数据
     *
     * @param carNum    检车数量
     * @param couponNum 优惠券
     * @param value     需收取费用 下午7:42:18
     */
    public void setData(String value)
    {
        if ( !TextUtils.isEmpty(value) )
        {
            int index = value.indexOf(".");
            int len = value.length();
            String pay;
            if ( ((index + 2) < len) && (index > 0) )
            {
                pay = value.subSequence(0, index + 2).toString();
            } else
            {
                pay = value;
            }
            payTV.setText(pay);
        } else
        {
            payTV.setText(0);
        }
    }

    public void setFinishPay(IFinishPay finishPayImp)
    {
        this.finishPayImp = finishPayImp;
    }

    public interface IFinishPay
    {
        public void cancelPay();

        public void finishPay();
    }

}

package com.creditease.checkcars.ui.act.pay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.alipay.AlipayMessageHandler;
import com.creditease.checkcars.alipay.AlipayUtil;
import com.creditease.checkcars.alipay.IPayCallBack;
import com.creditease.checkcars.data.bean.PayBean;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.PayNotifyBean;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.utilframe.exception.HttpException;

/**
 * 支付宝支付
 *
 * @author 子龍
 * @function
 * @date 2015年11月17日
 * @company CREDITEASE
 */
public class PayActivity extends BaseActivity implements OnClickListener, IPayCallBack,
        RequestListener
{

    public static final String PARAM_BEAN_PAY = "_param_paybean";
    private PayBean payBean;

    /**
     * 标题
     */
    private TextView titleTV;

    /**
     * 返回图标
     */
    private ImageView backImgV;

    private TextView subjectNameTV;
    private TextView orderNumberTV;
    private TextView subjectDescribTV;
    private TextView subjectPriceTV;
    private TextView phoneNumbTV;
    private TextView sellerTV;
    private TextView paySuccessTV;
    private Button payBtn;
    private int notifyTime = 0;


    @Override
    protected void initViews()
    {
        setContentView(R.layout.act_pay);
        titleTV = ( TextView ) findViewById(R.id.header_tv_title);
        backImgV = ( ImageView ) findViewById(R.id.header_imgv_back);
        subjectNameTV = ( TextView ) findViewById(R.id.act_pay_tv_subject_name);
        orderNumberTV = ( TextView ) findViewById(R.id.act_pay_tv_ordernumber);
        subjectDescribTV = ( TextView ) findViewById(R.id.act_pay_tv_subject_describ);
        subjectPriceTV = ( TextView ) findViewById(R.id.act_pay_tv_subject_price);
        phoneNumbTV = ( TextView ) findViewById(R.id.act_pay_tv_payer_phone);
        sellerTV = ( TextView ) findViewById(R.id.act_pay_tv_sellerid);
        payBtn = ( Button ) findViewById(R.id.act_pay_btn_pay);
        paySuccessTV = ( TextView ) findViewById(R.id.act_pay_success_layout);
    }

    @Override
    protected void initData()
    {
        // 初始化支付handler
        AlipayMessageHandler.buildHandler(getContext());
        payBean = getIntent().getParcelableExtra(PARAM_BEAN_PAY);
        titleTV.setText("转账支付");
        if ( payBean != null )
        {
            subjectNameTV.setText(payBean.subjectName);
            orderNumberTV.setText(payBean.orderNmber);
            subjectDescribTV.setText(payBean.subjectCont);
            subjectPriceTV.setText(payBean.subjectPrice);
            sellerTV.setText("师傅看车平台");
            phoneNumbTV.setText(payBean.payerPhone);
        }
    }

    @Override
    protected void initEvents()
    {
        payBtn.setOnClickListener(this);
        backImgV.setOnClickListener(this);

    }

    @Override
    public BaseActivity getContext()
    {
        return this;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch ( id )
        {
            case R.id.header_imgv_back:
                onBackPressed();
                break;
            case R.id.act_pay_btn_pay:
                AlipayUtil.getPayUtil().pay(getContext(), payBean, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void paySuccess(String result)
    {
        paySuccessTV.setVisibility(View.VISIBLE);
        payBtn.setBackgroundResource(R.drawable.bg_btn_unable);
        payBtn.setText(R.string.pay_done);
        payBtn.setTextColor(getResources().getColor(R.color.white));
        payBtn.setEnabled(false);
        notifyPayResultWS();
    }

    public void notifyPayResultWS()
    {
        if ( notifyTime > 3 )
        {
            return;
        }
        OperationFactManager.getManager().alipyPayResultNotify(getApplicationContext(),
                PayNotifyBean.STATUS_PAY_SUCCESS, payBean.orderNmber, payBean.orderType, payBean.sellerId,
                payBean.subjectPrice, this);
        notifyTime++;
    }

    @Override
    public void payFailed(String resultStatus, String result)
    {
        // Log.e("AlipayResult", "resultStatus====" + resultStatus + "    \nresult====" + result);
    }

    @Override
    public void onDataError(String errorMsg, String result)
    {
        notifyPayResultWS();
    }

    @Override
    public void onFailure(OperResponse response)
    {
        notifyPayResultWS();
    }

    @Override
    public void onRequestError(HttpException error, String msg)
    {
        notifyPayResultWS();
    }

    @Override
    public void onSuccess(Bundle bundle)
    {

    }

}

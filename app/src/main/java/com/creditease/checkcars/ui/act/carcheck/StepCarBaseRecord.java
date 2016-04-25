package com.creditease.checkcars.ui.act.carcheck;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.Car;
import com.creditease.checkcars.data.bean.CarInfoBean;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.data.db.CarInfoUtil;
import com.creditease.checkcars.data.db.base.DBAsyncTask;
import com.creditease.checkcars.data.db.base.DBFindCallBack;
import com.creditease.checkcars.data.db.base.DbHelper;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.Oper.RequestListener;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.manager.OperationFactManager;
import com.creditease.checkcars.ui.act.ShowCarInfoActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.dialog.DateSelectorDialog;
import com.creditease.checkcars.ui.dialog.DateSelectorDialog.DateSelectorCallback;
import com.creditease.checkcars.ui.dialog.base.TDialog;
import com.creditease.checkcars.ui.ppwindow.base.PullDownWindow;
import com.creditease.checkcars.ui.ppwindow.base.PullDownWindow.InitViewDataPort;
import com.creditease.checkcars.ui.ppwindow.base.PullDownWindow.PullOnItemClickListener;
import com.creditease.checkcars.ui.ppwindow.defaultp.DefaultObj;
import com.creditease.checkcars.ui.ppwindow.defaultp.DefaultPopWindow;
import com.creditease.checkcars.ui.widget.CleanableEditText;
import com.creditease.utilframe.exception.HttpException;

/**
 * @author 子龍
 * @function
 * @date 2015年11月5日
 * @company CREDITEASE
 */
public class StepCarBaseRecord extends BaseStep implements OnClickListener, DateSelectorCallback
{

    /**
     * 时间选择
     */
    public static final int ID_PRODUCT_DATE = 1;// 出厂日期
    public static final int ID_YEAR_DATE = 2;// 上牌日期

    /**************
     * 车录入
     *****************/
    private ScrollView sv;
    private CleanableEditText carET, mileageET, vinET;
    private TextView productionDateTV, yearTV, emissionStandardsTV;
    private DateSelectorDialog productDateDialog, yearDialog;
    private TextView userGuideTV, factoryCertificatesTV, registDataTV, ticketsTV;
    private CleanableEditText carconfigET;
    private RadioGroup carTypeRG;
    private RadioButton importRB, domesticRB;
    private Button carETQuery;

    private ArrayList< DefaultObj > list = new ArrayList< DefaultObj >(6);
    private ArrayList< DefaultObj > answerlist = new ArrayList< DefaultObj >(3);
    private Car car;
    private CarInfoBean bean;
    private String orderId;
    private RequestListener carInfoListener = new RequestListener()
    {

        @Override
        public void onDataError(String errorMsg, String result)
        {
            ZLToast.getToast().showToast(mContext, mContext.getString(R.string.str_carcheck_vin_error));
            carETQuery.setClickable(false);
            carETQuery.setBackgroundResource(R.drawable.btn_car_query_disable);
            loadDialog.dismiss();
        }

        @Override
        public void onFailure(OperResponse response)
        {
            ZLToast.getToast().showToast(mContext, mContext.getString(R.string.str_carcheck_vin_error));
            carETQuery.setClickable(false);
            carETQuery.setBackgroundResource(R.drawable.btn_car_query_disable);
            loadDialog.dismiss();
        }

        @Override
        public void onRequestError(HttpException error, String msg)
        {
            ZLToast.getToast().showToast(mContext, mContext.getString(R.string.str_carcheck_net_error));
            carETQuery.setClickable(false);
            carETQuery.setBackgroundResource(R.drawable.btn_car_query_disable);
            loadDialog.dismiss();
        }

        @Override
        public void onSuccess(Bundle bundle)
        {
            bean = bundle.getParcelable(Oper.BUNDLE_EXTRA_DATA);
            if ( bean != null )
            {
                emissionStandardsTV.setText(bean.emissionStandards);
                car.engineType = bean.engineType;
                car.environmentStandard = bean.emissionStandards;
                carET.setText(bean.name);
                productionDateTV.setText(bean.productionDate);
                carETQuery.setClickable(true);
                carETQuery.setBackgroundResource(R.drawable.btn_car_query_able);
            } else
            {
                emissionStandardsTV.setText("");
                ZLToast.getToast().showToast(mContext, mContext.getString(R.string.str_carcheck_vin_error));
                carETQuery.setClickable(false);
                carETQuery.setBackgroundResource(R.drawable.btn_car_query_disable);
            }
            loadDialog.dismiss();
        }
    };

    private DBFindCallBack< CarInfoBean > carInfoDbCallBack = new DBFindCallBack< CarInfoBean >()
    {

        @Override
        public void dataCallBack(int operId, List< CarInfoBean > result)
        {
            if ( (result != null) && (result.size() > 0) )
            {
                bean = result.get(0);
                carETQuery.setClickable(true);
                carETQuery.setBackgroundResource(R.drawable.btn_car_query_able);
            }
        }

        @Override
        public List< CarInfoBean > doDBOperation(DbHelper helper, int operId) throws CEDbException
        {
            return CarInfoUtil.getUtil(mContext.getApplicationContext()).getCarInfo(car.carVin);
        }

        @Override
        public void errorCallBack(int operId, CEDbException e)
        {
        }
    };


    public StepCarBaseRecord(CarCheckActivity activity, View contentRootView, CarReport report,
                             String orderId)
    {
        super(activity, contentRootView, report);
        this.orderId = orderId;
        car = report.reportCar;
        initViewData();
        initData();
    }


    @Override
    public void initViews()
    {
        /******************** 车录入 *******************/
        sv = ( ScrollView ) findViewById(R.id.car_record_scrollv);
        carET = ( CleanableEditText ) findViewById(R.id.car_record_car_et);
        yearTV = ( TextView ) findViewById(R.id.car_record_year_et);
        mileageET = ( CleanableEditText ) findViewById(R.id.car_record_mileage_et);
        vinET = ( CleanableEditText ) findViewById(R.id.car_record_vin_et);
        productionDateTV = ( TextView ) findViewById(R.id.car_record_productdate_et);
        emissionStandardsTV = ( TextView ) findViewById(R.id.car_record_emissionStandards_tv);
        carconfigET = ( CleanableEditText ) findViewById(R.id.car_record_yearType_et);
        userGuideTV = ( TextView ) findViewById(R.id.car_record_userguide_tv);
        factoryCertificatesTV = ( TextView ) findViewById(R.id.car_record_factorycertificates_tv);
        registDataTV = ( TextView ) findViewById(R.id.car_record_registdata_tv);
        ticketsTV = ( TextView ) findViewById(R.id.car_record_fourTwoOne_tv);
        carTypeRG = ( RadioGroup ) findViewById(R.id.car_record_carType_rg);
        importRB = ( RadioButton ) findViewById(R.id.car_record_carType_rb_import);
        domesticRB = ( RadioButton ) findViewById(R.id.car_record_carType_rb_domestic);
        carETQuery = ( Button ) findViewById(R.id.car_record_vin_et_query);
        productDateDialog = new DateSelectorDialog(mContext, ID_PRODUCT_DATE);
        yearDialog = new DateSelectorDialog(mContext, ID_YEAR_DATE);
    }

    protected void initViewData()
    {
        if ( car != null )
        {
            carET.setText(TextUtils.isEmpty(car.carBrand) ? "" : car.carBrand);
            mileageET.setText(TextUtils.isEmpty(car.mileage) ? "" : car.mileage);
            vinET.setText(TextUtils.isEmpty(car.carVin) ? "" : car.carVin);
            // 如果vin不为空则查询本地数据库，为展示车辆信息做准备
            if ( !TextUtils.isEmpty(car.carVin) )
            {
                new DBAsyncTask< CarInfoBean >(mContext, carInfoDbCallBack).execute();
            }
            if ( !TextUtils.isEmpty(car.carYear) )
            {
                yearTV.setText(car.carYear);
                yearDialog.setDate(car.carYear);
            }

            if ( !TextUtils.isEmpty(car.outTime) )
            {
                productionDateTV.setText(car.outTime);
                productDateDialog.setDate(car.outTime);
            }
            emissionStandardsTV.setText(TextUtils.isEmpty(car.environmentStandard) ? ""
                    : car.environmentStandard);
            carconfigET.setText(TextUtils.isEmpty(car.yearType) ? "" : car.yearType);

            userGuideTV.setText(TextUtils.isEmpty(car.userManual) ? "" : car.userManual);
            factoryCertificatesTV.setText(TextUtils.isEmpty(car.factoryCertificate) ? ""
                    : car.factoryCertificate);
            registDataTV.setText(TextUtils.isEmpty(car.registrationInformation) ? ""
                    : car.registrationInformation);
            ticketsTV.setText(TextUtils.isEmpty(car.fourTwoOne) ? "" : car.fourTwoOne);
            if ( car.carType == Car.CARTYPE_IMPORT )
            {
                importRB.setChecked(true);
            } else if ( car.carType == Car.CARTYPE_DOMESTIC )
            {
                domesticRB.setChecked(true);
            }
        }
        setTextWatcher();
    }

    protected void initData()
    {
        list.clear();
        // 国Ⅰ国Ⅱ 国Ⅲ 国Ⅳ 国V
        list.add(new DefaultObj("待查"));
        list.add(new DefaultObj("国Ⅱ"));
        list.add(new DefaultObj("国Ⅲ"));
        list.add(new DefaultObj("国Ⅳ"));
        list.add(new DefaultObj("国V"));

        // 全/待查/缺
        answerlist.clear();
        answerlist.add(new DefaultObj("全"));
        answerlist.add(new DefaultObj("待查"));
        answerlist.add(new DefaultObj("缺"));
    }

    @Override
    public void initEvents()
    {
        /************** 车录入 *****************/
        productionDateTV.setOnClickListener(this);
        carETQuery.setOnClickListener(this);
        yearTV.setOnClickListener(this);
        emissionStandardsTV.setOnClickListener(this);
        userGuideTV.setOnClickListener(this);
        factoryCertificatesTV.setOnClickListener(this);
        registDataTV.setOnClickListener(this);
        ticketsTV.setOnClickListener(this);
        productDateDialog.setDateCallback(this);
        productDateDialog.showParts(true, true, true);
        yearDialog.setDateCallback(this);
        yearDialog.showParts(true, true, true);
        carTypeRG.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch ( checkedId )
                {
                    case R.id.car_record_carType_rb_import:
                        car.carType = Car.CARTYPE_IMPORT;
                        break;
                    case R.id.car_record_carType_rb_domestic:
                        car.carType = Car.CARTYPE_DOMESTIC;
                        break;
                    default:
                        break;
                }
                mContext.saveReport();
            }
        });

    }

    /**
     * 设置text监听
     * <p/>
     * 下午5:28:13
     */
    private void setTextWatcher()
    {
        carET.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void afterTextChanged(Editable s)
            {
                mContext.saveReport();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });
        mileageET.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void afterTextChanged(Editable s)
            {
                mContext.saveReport();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });
        vinET.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void afterTextChanged(Editable s)
            {
                String vin = vinET.getText().toString().trim();
                if ( vin.length() == 17 )
                {
                    OperationFactManager.getManager().getCarInfoByVinWS(mContext, vin, carInfoListener);
                    // 仅仅是要更新车辆的vin码
                    OperationFactManager.getManager().addCarReport(mContext, orderId, report.clientUuid, vin,
                            null);
                    loadDialog.showDialog();
                }
                mContext.saveReport();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });

        carconfigET.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void afterTextChanged(Editable s)
            {
                mContext.saveReport();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
        });


    }

    @Override
    public boolean validate()
    {
        /**************** 车基本信息 ***************************/
        if ( TextUtils.isEmpty(car.carVin) )
        {
            showToast(R.string.str_carrecord_marked_vin);
            vinET.requestFocus();
            return false;
        }
        if ( car.carVin.length() < 17 )
        {
            showToast(R.string.str_carrecord_marked_vin_error);
            vinET.requestFocus();
            return false;
        }
        if ( TextUtils.isEmpty(car.carBrand) )
        {
            showToast(R.string.str_carrecord_marked_car);
            carET.requestFocus();
            return false;
        }
        if ( TextUtils.isEmpty(car.yearType) )
        {
            showToast(R.string.str_carrecord_marked_yeartype);
            carconfigET.requestFocus();
            return false;
        }
        if ( TextUtils.isEmpty(car.outTime) )
        {
            showToast(R.string.str_carrecord_marked_outdate);
            productionDateTV.requestFocus();
            return false;
        }
        if ( TextUtils.isEmpty(car.carYear) )
        {
            showToast(R.string.str_carrecord_marked_year);
            yearTV.requestFocus();
            return false;
        }
        if ( TextUtils.isEmpty(car.mileage) )
        {
            showToast(R.string.str_carrecord_marked_mode);
            mileageET.requestFocus();
            return false;
        }

        if ( TextUtils.isEmpty(car.environmentStandard) )
        {
            showToast(R.string.str_carrecord_marked_environmentStandard);
            return false;
        }
        // 车进出口
        if ( car.isCarTypeNull() )
        {
            showToast(R.string.str_carrecord_marked_cartype);
            sv.fullScroll(View.FOCUS_DOWN);// 滾屏置底
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch ( id )
        {
            case R.id.car_record_productdate_et:
                mContext.hideSoftKeyboard();
                TDialog.Builder.builderDialog(productDateDialog).showDialog();
                break;
            case R.id.car_record_year_et:
                mContext.hideSoftKeyboard();
                TDialog.Builder.builderDialog(yearDialog).showDialog();
                break;
            case R.id.car_record_vin_et_query:
                Intent intent = new Intent(mContext, ShowCarInfoActivity.class);
                if ( bean != null )
                {
                    // 区分从检车跳转还是车辆查询处跳转
                    intent.putExtra("type", ShowCarInfoActivity.FROM_CAR_CHECK);
                    Bundle bundle = new Bundle();
                    // bundle.putString("vin", vinET.getText().toString().trim());
                    bundle.putSerializable("carInfo", bean);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.car_record_emissionStandards_tv:
                // popwindow 待查 国Ⅰ国Ⅱ 国Ⅲ 国Ⅳ 国V
                showPPWindow(emissionStandardsTV, list);
                break;
            case R.id.car_record_userguide_tv:
                sv.fullScroll(View.FOCUS_DOWN);
                showPPWindow(userGuideTV, answerlist);
                break;
            case R.id.car_record_factorycertificates_tv:
                sv.fullScroll(View.FOCUS_DOWN);
                showPPWindow(factoryCertificatesTV, answerlist);
                break;
            case R.id.car_record_registdata_tv:
                sv.fullScroll(View.FOCUS_DOWN);
                showPPWindow(registDataTV, answerlist);
                break;
            case R.id.car_record_fourTwoOne_tv:
                sv.fullScroll(View.FOCUS_DOWN);
                showPPWindow(ticketsTV, answerlist);
                break;
        }
    }

    /**
     * 弹窗选择
     *
     * @param view
     * @param list 下午5:31:01
     */
    private void showPPWindow(final TextView view, final ArrayList< DefaultObj > list)
    {
        DefaultPopWindow ppWindow =
                new DefaultPopWindow(mContext, view, view, 374,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT, new InitViewDataPort< DefaultObj >()
                {

                    @Override
                    public void initViewData(PullDownWindow< DefaultObj > window)
                    {
                        if ( (list != null) && (list.size() > 0) )
                        {
                            window.setData(list);
                        }
                    }
                });
        ppWindow.setPullOnItemClickListener(new PullOnItemClickListener< DefaultObj >()
        {

            @Override
            public void onPullItemClick(PullDownWindow< DefaultObj > window, AdapterView< ? > parent,
                                        int position)
            {
                DefaultObj obj = ( DefaultObj ) parent.getAdapter().getItem(position);
                if ( obj != null )
                {
                    view.setText(obj.name);
                    mContext.saveReport();
                }
            }
        });
        ppWindow.showAsDropDown(view);
    }

    @Override
    public void callbackDate(int id, int year, int monthOfYear, int dayOfMonth)
    {
        switch ( id )
        {
            case ID_PRODUCT_DATE:
                if ( year == -1 )
                {
                    productionDateTV.setText("待查");
                } else
                {
                    productionDateTV.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                }
                break;
            case ID_YEAR_DATE:
                if ( year == -1 )
                {
                    yearTV.setText("待查");
                } else
                {
                    yearTV.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                }
                break;

            default:
                break;
        }
        mContext.saveReport();
    }


    /**
     * 初始化车
     * <p/>
     * 上午11:51:38
     */
    public Car initCar()
    {
        car.carBrand = carET.getText().toString().trim();
        car.outTime = productionDateTV.getText().toString().trim();
        car.carYear = yearTV.getText().toString().trim();
        car.mileage = mileageET.getText().toString().trim();
        car.carVin = vinET.getText().toString().trim();
        car.yearType = carconfigET.getText().toString().trim();
        car.fourTwoOne = ticketsTV.getText().toString().trim();
        car.environmentStandard = emissionStandardsTV.getText().toString().trim();
        car.userManual = userGuideTV.getText().toString().trim();
        car.factoryCertificate = factoryCertificatesTV.getText().toString().trim();
        car.registrationInformation = registDataTV.getText().toString().trim();
        return car;
    }


    /**
     * get car
     *
     * @return 下午4:53:41
     */
    public Car getCar()
    {
        return car;
    }


    @Override
    public void destroy()
    {
        list.clear();
        answerlist.clear();
    }


}

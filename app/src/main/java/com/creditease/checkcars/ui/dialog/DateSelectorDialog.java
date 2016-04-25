package com.creditease.checkcars.ui.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.dialog.base.BDialog;

@SuppressLint( "HandlerLeak" )
public class DateSelectorDialog extends BDialog implements OnClickListener
{

    private DatePicker picker;
    private DateSelectorCallback callback;
    private int id;
    private Time time;
    private Button okBtn, cancelBtn;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if ( msg.what == 0 )
            {
                picker.setEnabled(true);
            }
        }
    };

    public DateSelectorDialog(Context context, int id)
    {
        super(context);
        this.id = id;
    }

    @Override
    public int getDialogViewLayoutId()
    {
        return R.layout.dialog_dateselect;
    }

    @Override
    public void initDialogView()
    {
        okBtn = ( Button ) view.findViewById(R.id.dialog_dateselect_btn_ok);
        cancelBtn = ( Button ) view.findViewById(R.id.dialog_dateselect_btn_cancel);
        picker = ( DatePicker ) view.findViewById(R.id.dialog_dateselect_datePicker);
        picker.setEnabled(false);
        time = new Time();
        time.setToNow();
        picker.init(time.year, time.month, time.monthDay, null);
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

    }

    @Override
    public boolean isCanceledOnKeyBack()
    {
        return false;
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
            case R.id.dialog_dateselect_btn_ok:
                if ( callback != null )
                {
                    callback.callbackDate(id, picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
                }
                dismiss();
                break;
            case R.id.dialog_dateselect_btn_cancel:
                if ( callback != null )
                {
                    callback.callbackDate(id, -1, -1, -1);
                }

                dismiss();
                break;

            default:
                break;
        }
    }

    public void setDate(String date)
    {
        if ( date.equals("待查") )
        {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        try
        {
            Date d = dateFormat.parse(date);
            time = new Time();
            time.set(d.getTime());
            picker.updateDate(time.year, time.month, time.monthDay);
        } catch ( ParseException e )
        {
            // TODO Auto-gen
        }

    }

    public void setDate(Time time)
    {
        this.time = time;
        picker.updateDate(time.year, time.month, time.monthDay);
    }

    public void setDateCallback(DateSelectorCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void showDialog()
    {
        super.showDialog();
        handler.obtainMessage(0).sendToTarget();
        // if (callback != null) {
        // callback.callbackDate(id, time.year, time.month, time.monthDay);
        // }
    }

    public void showParts(boolean year, boolean month, boolean dayOfMonth)
    {
        picker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        ViewGroup vg = ( ViewGroup ) picker.getChildAt(0);
        ViewGroup vg2 = ( ViewGroup ) vg.getChildAt(0);
        if ( !dayOfMonth )
        {
            vg2.getChildAt(2).setVisibility(View.GONE);
        }
        if ( !month )
        {
            vg2.getChildAt(1).setVisibility(View.GONE);
        }
        if ( !year )
        {
            vg2.getChildAt(0).setVisibility(View.GONE);
        }
    }

    /**
     * 日期回调
     *
     * @author 子龍
     * @function
     * @date 2015年3月12日
     * @company CREDITEASE
     */
    public interface DateSelectorCallback
    {
        public void callbackDate(int id, int year, int monthOfYear, int dayOfMonth);
    }

}

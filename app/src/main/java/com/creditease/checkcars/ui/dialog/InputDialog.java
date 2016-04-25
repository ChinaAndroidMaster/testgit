/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.dialog;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CCItem;
import com.creditease.checkcars.ui.dialog.base.BDialog;
import com.creditease.checkcars.ui.widget.CleanableEditText;

/**
 * @author 子龍
 * @function
 * @date 2015年3月17日
 * @company CREDITEASE
 */
public class InputDialog extends BDialog implements android.view.View.OnClickListener
{

    public CCItem item;
    public boolean isValue = false;
    public boolean isPositive = false;//是否点击确认按钮
    private CleanableEditText inputET;
    private TextView titleTV;
    private Button okBtn;
    private ContentCallback callback;
    private DismissDialogCallBack dismissCallback;
    private ImageView closeImgV;
    private int inputType = -1;
    private boolean isNumber = false;

    public InputDialog(Context context, CCItem item)
    {
        super(context);
        this.item = item;
        if ( item != null )
        {
            setText(item.remark);
        }

    }

    public InputDialog(Context context, CCItem item, boolean isMain, boolean isNumber)
    {
        super(context);
        this.item = item;
        isValue = isMain;
        if ( item != null )
        {
            setText(item.attrValue);
        }
        this.isNumber = isNumber;
        setInputET();
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
    }

    public ContentCallback getCallback()
    {
        return callback;
    }

    public void setCallback(ContentCallback callback)
    {
        this.callback = callback;
    }

    public DismissDialogCallBack getDismissCallback()
    {
        return dismissCallback;
    }

    public void setDismissCallback(DismissDialogCallBack dismissCallback)
    {
        this.dismissCallback = dismissCallback;
    }

    @Override
    public int getDialogViewLayoutId()
    {
        return R.layout.dialog_input;
    }

    @Override
    public void initDialogView()
    {
        closeImgV = ( ImageView ) view.findViewById(R.id.dialog_input_btn_close);
        titleTV = ( TextView ) view.findViewById(R.id.dialog_input_title);
        inputET = ( CleanableEditText ) view.findViewById(R.id.dialog_input_et);
        okBtn = ( Button ) view.findViewById(R.id.dialog_input_btn);
        okBtn.setOnClickListener(this);
        closeImgV.setOnClickListener(this);
        if ( isNumber )
        {
            inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            inputET.setInputType(inputType);
        } else
        {
            inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
            inputET.setInputType(inputType);
        }
        inputET.setEnabled(true);
        inputET.setFocusable(true);
        inputET.setOnClickListener(new android.view.View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                inputET.requestFocus();
            }
        });
        inputET.requestFocus();
    }

    @Override
    public boolean isCanceledOnKeyBack()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCanceledOnTouchOutside()
    {
        return true;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch ( id )
        {
            case R.id.dialog_input_btn:
                String content = inputET.getText().toString().trim();
                if ( item != null )
                {
                    if ( isValue )
                    {
                        item.attrValue = content;
                    } else
                    {
                        item.remark = content;
                    }
                }
                isPositive = true;
                if ( callback != null )
                {
                    callback.contentCallback(content);
                }
                break;
            case R.id.dialog_input_btn_close:
                break;
            default:
                break;
        }

        dismiss();
    }

    private void setInputET()
    {
        if ( isNumber )
        {
            inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            inputET.setInputType(inputType);
        } else
        {
            inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
            inputET.setInputType(inputType);
        }
    }

    public void setText(String text)
    {
        inputET.setText(text);
    }

    public void setTitle(String title)
    {
        titleTV.setText(title);
    }

    @Override
    public void dismissCallback()
    {
        String content = inputET.getText().toString().trim();
        if ( dismissCallback != null )
        {
            dismissCallback.dismissDialog(content, isPositive);
        }
        isPositive = false;
    }

    public interface ContentCallback
    {
        public void contentCallback(String content);
    }

    public interface DismissDialogCallBack
    {
        public void dismissDialog(String content, boolean isPositive);
    }
}

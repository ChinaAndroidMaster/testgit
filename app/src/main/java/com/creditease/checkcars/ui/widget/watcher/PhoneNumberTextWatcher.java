/**
 * Copyright © 2014 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.widget.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author noah.zheng
 */
public class PhoneNumberTextWatcher implements TextWatcher
{
    private final String ch = " ";
    private boolean mIsChange = true;
    private EditText mEtPhone;
    private int keyDel;
    private String a;
    private String a0;
    private int isAppent = 0;

    public PhoneNumberTextWatcher(EditText etPhone)
    {
        mEtPhone = etPhone;
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        a0 = s.toString();
    }

    public boolean isChange()
    {
        return mIsChange;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        mIsChange = true;
        boolean flag = true;
        // if (s.length() > 19) {
        // mEtPhone.setText(a0);
        // mEtPhone.setSelection(mEtPhone.getText().length());
        // return;
        // }
        String eachBlock[] = s.toString().split(ch);
        for ( String element : eachBlock )
        {
            if ( eachBlock[0].length() > 3 )
            {
                flag = false;
                break;
            }
            if ( element.length() > 4 )
            {
                flag = false;
                break;
            }
        }
        if ( a0.length() > s.toString().length() )
        {
            keyDel = 1;
        }
        if ( flag )
        {
            if ( keyDel == 0 )
            {

                if ( (mEtPhone.getText().length() == 3) || (mEtPhone.getText().length() == 8) )
                {

                    if ( s.toString().split(ch).length <= 3 )
                    {
                        isAppent = 1;
                        mEtPhone.setText(s + ch);
                        isAppent = 0;
                        mEtPhone.setSelection(mEtPhone.getText().length());
                        a = mEtPhone.getText().toString();
                        return;
                    }
                }
                if ( isAppent == 0 )
                {
                    String str = s.toString();
                    if ( (str.length() > 0) && (str.lastIndexOf(ch) == (str.length() - 1)) )
                    {
                        str = str.substring(0, str.lastIndexOf(ch));
                        keyDel = 1;
                        mEtPhone.setText(str);
                        keyDel = 0;
                        mEtPhone.setSelection(mEtPhone.getText().length());
                        a = mEtPhone.getText().toString();
                        return;
                    }
                }
            } else
            {
                String str = s.toString();
                if ( (str.length() > 0) && (str.lastIndexOf(ch) == (str.length() - 1)) )
                {
                    str = str.substring(0, str.lastIndexOf(ch));
                    keyDel = 1;
                    mEtPhone.setText(str);
                    keyDel = 0;
                    mEtPhone.setSelection(mEtPhone.getText().length());
                    a = mEtPhone.getText().toString();
                    return;
                } else
                {
                    a = mEtPhone.getText().toString();
                    keyDel = 0;
                }
            }

        } else
        {
            if ( keyDel == 0 )
            {
                String str = s.toString();
                str = str.replace(" ", "");
                if ( (str.length() == 3) || (str.length() < 7) )
                {
                    str = str.substring(0, 3) + ch + str.substring(3, str.length());
                } else if ( str.length() >= 7 )
                {
                    str =
                            str.substring(0, 3) + ch + str.substring(3, 7) + ch + str.substring(7, str.length());
                }
                int selection = start;
                if ( (start == 3) || (start == 8) )
                {
                    selection += 2;
                } else
                {
                    selection += 1;
                }
                a = str;
                keyDel = 1;
                mEtPhone.setText(a);
                keyDel = 0;
                if ( selection < 0 )
                {
                    selection = 0;
                }
                mEtPhone.setSelection(selection > mEtPhone.length() ? mEtPhone.length() : selection);
            } else
            {
                a = mEtPhone.getText().toString();
                keyDel = 0;
            }
        }

    }

}

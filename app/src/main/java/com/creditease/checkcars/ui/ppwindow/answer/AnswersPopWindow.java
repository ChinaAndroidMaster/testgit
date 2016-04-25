package com.creditease.checkcars.ui.ppwindow.answer;

import java.util.List;

import android.app.Activity;
import android.view.View;

import com.creditease.checkcars.data.bean.Answer;
import com.creditease.checkcars.ui.ppwindow.base.PullDownWindow;

/**
 * @author 子龍
 * @date 2014年6月18日
 * @company 宜信
 */
public class AnswersPopWindow extends PullDownWindow< Answer >
{

    public AnswersPopWindow(Activity _activity, View _parentView, View _ShowlocationView, int width,
                            int h, InitViewDataPort< Answer > port)
    {
        super(_activity, _parentView, _ShowlocationView, h, port, true);
    }

    @Override
    public View getView()
    {
        return null;
    }

    @Override
    public void setData(List< Answer > data)
    {
        AnswerListAdapter adapter = new AnswerListAdapter(mContext, data);
        super.setAdapter(adapter);
    }

    @Override
    public void setSelectPosition(int selectedPosition)
    {
        super.setSelectPosition(selectedPosition);
    }

}

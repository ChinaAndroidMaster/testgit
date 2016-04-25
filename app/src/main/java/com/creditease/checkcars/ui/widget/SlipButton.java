package com.creditease.checkcars.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.creditease.checkcars.R;


@SuppressLint( "ClickableViewAccessibility" )
public class SlipButton extends View
{


    private boolean isChecked;
    private OnChangedListener onChangedListener;
    private Bitmap bg_on, bg_off;
    public SlipButton(Context context)
    {
        super(context);
        init();
    }


    public SlipButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SlipButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {// 初始化
        bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.slip_on_bg);
        bg_off = BitmapFactory.decodeResource(getResources(), R.drawable.slip_off_bg);
    }

    @SuppressLint( "DrawAllocation" )
    @Override
    protected void onDraw(Canvas canvas)
    {// 绘图函数
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        if ( !isChecked )// 滑动到前半段与后半段的背景不同,在此做判断
        {
            canvas.drawBitmap(bg_off, matrix, paint);// 画出关闭时的背景
        } else
        {
            canvas.drawBitmap(bg_on, matrix, paint);// 画出打开时的背景
        }
        // if (isChecked) {
        // canvas.drawBitmap(bg_on, matrix, paint);
        // isChecked = !isChecked;
        // }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch ( event.getAction() )
        {
            case MotionEvent.ACTION_DOWN:// 按下
                if ( (event.getX() > bg_on.getWidth()) || (event.getY() > bg_on.getHeight()) )
                {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:// 松开
                if ( event.getX() >= (bg_on.getWidth() / 2) )
                {
                    isChecked = true;
                } else
                {
                    isChecked = false;
                }
                if ( onChangedListener != null )
                {
                    onChangedListener.OnChanged(isChecked);
                }
                break;
            default:
        }
        invalidate();// 重画控件
        return true;
    }

    public boolean isCheck()
    {
        return this.isChecked;
    }

    public void setCheck(boolean isChecked)
    {
        this.isChecked = isChecked;
        // if (onChangedListener != null) {
        // onChangedListener.OnChanged(isChecked);
        // }
        invalidate();// 重画控件
    }

    public void setOnChangedListener(OnChangedListener l)
    {// 设置监听器,当状态修改的时候
        onChangedListener = l;
    }

    public interface OnChangedListener
    {
        void OnChanged(boolean checkState);
    }
}

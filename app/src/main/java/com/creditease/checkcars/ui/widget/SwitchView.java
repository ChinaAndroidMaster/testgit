package com.creditease.checkcars.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint( "ClickableViewAccessibility" )
public class SwitchView extends ImageView
{

    private static final int STATUS_DEFAULT = 0;
    private static final int STATUS_CHECKED = 1;
    private static final int STATUS_UNCHECKED = 2;
    public int checked = STATUS_DEFAULT;
    int imageWidth;
    private int centerX = -1;
    private OnCheckedChangeListener listener;

    public SwitchView(Context context)
    {
        super(context);
    }

    public SwitchView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    private void initLocation()
    {
        if ( centerX == -1 )
        {
            centerX = getWidth() / 2;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        initLocation();
        int action = event.getAction();
        switch ( action )
        {
            case MotionEvent.ACTION_UP:
                float pX = event.getX();
                float pY = event.getY();
                updateCheckedStatus(pX, pY);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * @param checked 下午2:25:58
     */
    public void setChecked(boolean checked)
    {
        this.checked = checked ? STATUS_CHECKED : STATUS_UNCHECKED;
        // if (checked) {
        // setImageResource(R.drawable.cb_carche_item_true);
        // } else {
        // setImageResource(R.drawable.cb_carche_item_false);
        // }
    }

    private void setChecked(int checked)
    {
        if ( listener != null )
        {
            listener.onCheckedChanged(checked == STATUS_CHECKED);
        }
        // if (checked == STATUS_CHECKED) {
        // setImageResource(R.drawable.cb_carche_item_true);
        // } else {
        // setImageResource(R.drawable.cb_carche_item_false);
        // }
    }

    // private boolean isTouchInView(float x, float y) {
    // return x >= left && x <= right && y >= top && y <= bottom;
    // }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener)
    {
        this.listener = listener;
    }

    public void updateCheckedStatus(float x, float y)
    {
        // if (isTouchInView(x, y)) {
        checked = x <= centerX ? STATUS_CHECKED : STATUS_UNCHECKED;
        setChecked(checked);

        // }
    }

    public interface OnCheckedChangeListener
    {
        public void onCheckedChanged(boolean checked);
    }

}

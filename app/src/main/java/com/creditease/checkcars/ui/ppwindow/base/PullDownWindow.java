package com.creditease.checkcars.ui.ppwindow.base;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;

import com.creditease.checkcars.R;
import com.creditease.checkcars.tools.Util;

/**
 * @author 子龍
 * @date 2014年6月18日
 * @company 宜信
 */
@SuppressLint( "InflateParams" )
public abstract class PullDownWindow< T > implements InitViewPort
{

    protected Context mContext = null;
    protected ListView listView = null;
    protected BaseListAdapter< T > pullListAdapter = null;
    protected BaseAdapter adapter = null;
    protected PullOnItemClickListener< T > onItemClickListener;
    private BasePopupWindow popWindow;
    private View parentView = null;
    private int[] location = new int[2];
    private int width;
    private InitViewDataPort< T > initViewDataP;
    private OnPopDismissListener listener;
    private View view;

    public PullDownWindow(Activity _activity)
    {
        mContext = _activity;
        initView();
    }

    /**
     * @param _activity         调用此方法的Activity
     * @param _parentView       用来显示的PopupWindow的View
     * @param _ShowlocationView 基于那个控件显示
     * @param _data             数据源
     */
    public PullDownWindow(Activity _activity, View _parentView, View _ShowlocationView,
                          ArrayList< T > _data, int h, boolean useDefault)
    {
        mContext = _activity;
        parentView = _parentView;
        // _ShowlocationView.getLocationOnScreen(location);
        if ( parentView != null )
        {
            parentView.getLocationOnScreen(location);
            width = parentView.getWidth();
        }
        if ( useDefault )
        {
            initDefaultPopuWindow(h);
        }
    }

    public PullDownWindow(Activity _activity, View _parentView, View _ShowlocationView,
                          boolean useDefault)
    {
        mContext = _activity;
        parentView = _parentView;
        //
        if ( parentView != null )
        {
            parentView.getLocationOnScreen(location);
            width = parentView.getWidth();
        }
        if ( useDefault )
        {
            initDefaultPopuWindow(-1);
        }
    }

    public PullDownWindow(Activity _activity, View _parentView, View _ShowlocationView, int h,
                          InitViewDataPort< T > port)
    {
        mContext = _activity;
        parentView = _parentView;
        if ( _ShowlocationView != null )
        {
            _ShowlocationView.getLocationOnScreen(location);
        }
        // parentView.getLocationOnScreen(location);
        if ( parentView != null )
        {
            width = parentView.getWidth();
        }
        initViewDataP = port;

        initView();

    }

    public PullDownWindow(Activity _activity, View _parentView, View _ShowlocationView, int h,
                          InitViewDataPort< T > port, boolean useDefault)
    {
        mContext = _activity;
        parentView = _parentView;
        // _ShowlocationView.getLocationOnScreen(location);
        if ( parentView != null )
        {
            parentView.getLocationOnScreen(location);
        }
        if ( parentView != null )
        {
            width = parentView.getWidth();
        }
        initViewDataP = port;
        if ( useDefault )
        {
            initDefaultPopuWindow(h);
        }
    }

    public PullDownWindow(Activity _activity, View _parentView, View _ShowlocationView, int width,
                          int h, InitViewDataPort< T > port)
    {
        mContext = _activity;
        parentView = _parentView;
        // _ShowlocationView.getLocationOnScreen(location);
        if ( parentView != null )
        {
            parentView.getLocationOnScreen(location);
        }
        this.width = width;
        initViewDataP = port;
        initView();
    }

    /**
     * PopupWindow消失
     */
    public void dismiss()
    {
        popWindow.dismiss();
    }

    @Override
    public abstract View getView();

    /**
     * 初始化PopupWindow
     */
    @SuppressWarnings( "deprecation" )
    private void initDefaultPopuWindow(int height)
    {
        View loginwindow =
                LayoutInflater.from(mContext).inflate(R.layout.popupwindow_default_listv, null);
        listView = ( ListView ) loginwindow;
        initViewDataP.initViewData(this);
        popWindow =
                new BasePopupWindow(loginwindow, width,
                        height == -1 ? android.view.ViewGroup.LayoutParams.WRAP_CONTENT : height);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        // popupWindow.setAnimationStyle(R.style.MenuAnimation);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView< ? > parent, View arg1, int position, long arg3)
            {
                onItemClickListener.onPullItemClick(PullDownWindow.this, parent, position);
                setSelectPosition(position);
                pullListAdapter.notifyDataSetChanged();
                dismiss();
            }
        });
        popWindow.setOnDismissListener(new OnDismissListener()
        {

            @Override
            public void onDismiss()
            {
                if ( listener != null )
                {
                    listener.onDismiss();
                }
            }
        });
    }

    @SuppressWarnings( "deprecation" )
    public void initView()
    {
        view = getView();
        int height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        if ( initViewDataP != null )
        {
            initViewDataP.initViewData(this);
        } else
        {
            width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            height = Util.dip2px(mContext, 280);
        }
        popWindow = new BasePopupWindow(view, width, height);
        // selectPopupWindow = new PopupWindow(loginwindow, width,
        // LayoutParams.WRAP_CONTENT, true);
        // setWindowHeight(height);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOnDismissListener(new OnDismissListener()
        {

            @Override
            public void onDismiss()
            {
                if ( listener != null )
                {
                    listener.onDismiss();
                }
            }
        });

    }

    public void setAdapter(BaseListAdapter< T > adapter)
    {
        if ( listView == null )
        {
            return;
        }
        pullListAdapter = adapter;
        listView.setAdapter(pullListAdapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView< ? > arg0, View arg1, int position, long arg3)
            {
                onItemClickListener.onPullItemClick(PullDownWindow.this, arg0, position);
                setSelectPosition(position);
                dismiss();
            }
        });

    }

    public void setSelectPosition(int position)
    {
        pullListAdapter.selectPositon = position;
    }

    public abstract void setData(List< T > data);

    public void setOnPopDismissListener(OnPopDismissListener listener)
    {
        this.listener = listener;
    }

    public void setPullOnItemClickListener(PullOnItemClickListener< T > onPullItemClickListener)
    {
        this.onItemClickListener = onPullItemClickListener;
    }

    public void setWindowHeight(int height)
    {
        popWindow.setHeight(height);
    }

    public void showAsDropDown(View view)
    {

        popWindow.showAsDropDown(view);
    }

    public void showAtLocation(View parent, int gravity, int x, int y)
    {
        if ( adapter != null )
        {
            adapter.notifyDataSetChanged();
        } else if ( pullListAdapter != null )
        {
            pullListAdapter.notifyDataSetChanged();
        }
        popWindow.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 显示PopupWindow窗口
     *
     * @param popupwindow
     */
    public void showPopupWindw()
    {
        popWindow.showAtLocation(parentView, Gravity.TOP, 0, location[1]);
    }

    /**
     * 显示在parent的中心
     *
     * @param parent
     */
    public void showPopupWindwCenter()
    {
        popWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    /**
     * 显示PopupWindow窗口
     *
     * @param popupwindow
     */
    public void showPopupWindwTopCenter()
    {
        popWindow.showViewTopCenter(parentView);
    }

    public interface InitViewDataPort< T >
    {
        public void initViewData(PullDownWindow< T > window);
    }

    public interface OnPopDismissListener
    {
        public void onDismiss();
    }

    public interface PullOnItemClickListener< T >
    {
        public void onPullItemClick(PullDownWindow< T > window, AdapterView< ? > parent, int position);
    }
}

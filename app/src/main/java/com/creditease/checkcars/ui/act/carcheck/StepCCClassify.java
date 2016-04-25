package com.creditease.checkcars.ui.act.carcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.CCClassify;
import com.creditease.checkcars.data.bean.CCItem;
import com.creditease.checkcars.data.bean.CarReport;
import com.creditease.checkcars.ui.act.SelectPhotoActivity;
import com.creditease.checkcars.ui.act.base.ZLToast;
import com.creditease.checkcars.ui.adapter.CarCheckAdapter;
import com.creditease.checkcars.ui.adapter.CarCheckAdapter.IPhoto;
import com.creditease.checkcars.ui.adapter.ImageAdapter;
import com.creditease.checkcars.ui.dialog.InputDialog;
import com.creditease.checkcars.ui.dialog.base.TDialog;
import com.creditease.checkcars.ui.widget.CEHorizontalListView;
import com.creditease.checkcars.ui.widget.SlipButton;
import com.creditease.checkcars.ui.widget.SlipButton.OnChangedListener;

/**
 * @author 子龍
 * @function
 * @date 2015年11月6日
 * @company CREDITEASE
 */
public class StepCCClassify extends BaseStep implements IPhoto
{

    public ArrayList< CCClassify > classifyList = new ArrayList< CCClassify >();// 检车大项
    private int viewLayoutId;
    private List< View > viewList = new ArrayList< View >();
    private Map< String, CarCheckAdapter > adapterMap = new HashMap< String, CarCheckAdapter >();
    private Map< String, ImageAdapter > imgAdapterMap = new HashMap< String, ImageAdapter >();
    // 拍照的检测大项
    private CCClassify classify;


    public StepCCClassify(CarCheckActivity activity, View contentRootView, int viewLayoutId,
                          CarReport report)
    {
        super(activity, contentRootView, report);
        this.viewLayoutId = viewLayoutId;
        if ( report.cRoot.children != null )
        {
            classifyList.clear();
            classifyList.addAll(report.cRoot.children);
        }
        initViewData();
    }

    @Override
    public void initViews()
    {
        if ( rootView == null )
        {
            return;
        }
    }


    @Override
    public void initEvents()
    {
        if ( rootView == null )
        {
            return;
        }
    }

    protected void initViewData()
    {
        viewList.clear();
        for ( int i = 0, len = classifyList.size(); i < len; i++ )
        {
            View view = getView(viewLayoutId);
            initChildViews(view, classifyList.get(i), i);
            viewList.add(view);
        }
    }

    /**
     * get view list
     *
     * @return 下午4:44:26
     */
    public List< View > getViewList()
    {
        return viewList;
    }

    /**
     * init child view
     *
     * @param view
     * @param ccc  下午4:45:06
     */
    private void initChildViews(View view, final CCClassify ccc, final int index)
    {
        ListView listview = ( ListView ) view.findViewById(R.id.page_listv);
        final SlipButton slipBtn = ( SlipButton ) view.findViewById(R.id.page_slipButton);
        RelativeLayout switchLayout = ( RelativeLayout ) view.findViewById(R.id.page_layout_switch);
        final CarCheckAdapter adapter = new CarCheckAdapter(mContext);
        adapter.setIPhoto(this);
        adapter.updateDataAndNotify(ccc);
        adapter.setListView(listview);
        listview.setAdapter(adapter);
        adapterMap.put(ccc.toString(), adapter);
        final CEHorizontalListView hListView =
                ( CEHorizontalListView ) view.findViewById(R.id.page_hlistv_imgs);
        hListView.setViewPager(mContext.mViewPager);
        ImageAdapter imgAdapter = new ImageAdapter(ccc, mContext);
        hListView.setAdapter(imgAdapter);
        imgAdapterMap.put(ccc.toString(), imgAdapter);

        hListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView< ? > parent, View view, int position, long id)
            {
                if ( position == 0 )
                {

                    Intent intent = new Intent(mContext, SelectPhotoActivity.class);
                    intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                            ReportControl.ImageControl.REQUEST_CAMERA_CODE_CHECKITEM);
                    startActivityForResult(intent, ReportControl.ImageControl.REQUEST_CAMERA_CODE_CHECKITEM);
                    takePhoto(ccc, ccc.mainItem);
                } else
                {
                    mContext.reviewPics(ccc, ccc.mainItem, position - 1);
                }
            }
        });
        // 是否检测大项开关
        if ( (ccc.mainItem != null) && (ccc.mainItem.answerRequired == CCItem.ANSWER_REQUIRED_NEED) )
        {
            switchLayout.setVisibility(View.GONE);
            slipBtn.setVisibility(View.GONE);
        } else
        {
            switchLayout.setVisibility(View.VISIBLE);
            // 如果报告已存在，则再次进入时需要同步isFill和doSwitch的状态
            if ( ccc.mainItem != null )
            {
                if ( ccc.mainItem.isFill == CCItem.SWITCH_ON )
                {
                    ccc.doSwitch = CCClassify.SWITCH_YES;
                    adapter.setAble(true);
                } else if ( ccc.mainItem.isFill == CCItem.SWITCH_OFF )
                {
                    ccc.doSwitch = CCClassify.SWITCH_NO;
                    adapter.setAble(false);
                }
            }
            slipBtn.setCheck(ccc.doSwitch == CCClassify.SWITCH_YES);
        }
        slipBtn.setOnChangedListener(new OnChangedListener()
        {

            @Override
            public void OnChanged(boolean checkState)
            {
                if ( ccc.mainItem == null )
                {
                    return;
                }

                ccc.doSwitch = checkState ? CCClassify.SWITCH_YES : CCClassify.SWITCH_NO;
                mContext.saveReport();
                adapter.setAble(checkState);
                if ( !checkState )
                {
                    ccc.mainItem.isFill = CCItem.SWITCH_OFF;
                    if ( index != 0 )
                    {
                        InputDialog dialog = new InputDialog(mContext, ccc.mainItem, true, false);
                        dialog.setTitle("理由");
                        TDialog.Builder.builderDialog(dialog).showDialog();
                        dialog.setDismissCallback(new InputDialog.DismissDialogCallBack()
                        {
                            @Override
                            public void dismissDialog(String content, boolean isPositive)
                            {
                                if ( TextUtils.isEmpty(content) )
                                {
                                    slipBtn.setCheck(true);
                                    ZLToast.getToast().showToast(mContext, "请填写未检测的理由!");
                                    ccc.doSwitch = CCClassify.SWITCH_YES;
                                    ccc.mainItem.isFill = CCItem.SWITCH_ON;
                                    adapter.setAble(true);
                                    mContext.saveReport();
                                }
                            }
                        });
                    }
                } else
                {
                    ccc.mainItem.isFill = CCItem.SWITCH_ON;
                }
            }
        });

    }


    @Override
    public boolean validate()
    {
        int i = 1;
        for ( CCClassify c : classifyList )
        {
            ++i;
            if ( c.doSwitch == CCClassify.SWITCH_NO )
            {
                if ( !TextUtils.equals(c.name, "重点检测") && (c.mainItem != null)
                        && TextUtils.isEmpty(c.mainItem.attrValue) )
                {
                    ZLToast.getToast().showToast(mContext, "请填写未检测的理由!");
                    return false;
                }
                continue;
            }
            CarCheckAdapter adapter = adapterMap.get(c.toString());
            if ( adapter == null )
            {
                continue;
            }
            if ( !adapter.isCheckedAll() )
            {
                ZLToast.getToast().showToast(mContext, "请完成车辆检测项检测");
                mContext.mViewPager.setCurrentItem(i);
                return false;
            }
            if ( !adapter.isCatchPhotoInProblemItems() )
            {
                mContext.mViewPager.setCurrentItem(i);
                return false;
            }
        }
        return true;
    }

    /**
     * 获取检测列表
     *
     * @return 下午4:50:41
     */
    public ArrayList< CCClassify > getCCClassifyList()
    {
        return classifyList;
    }

    /**
     * 更新adapter
     * <p/>
     * 下午3:58:09
     */
    public void notifyDataSetChanged()
    {
        if ( classify == null )
        {
            return;
        }
        imgAdapterMap.get(classify.toString()).notifyDataSetChanged();
        adapterMap.get(classify.toString()).notifyDataSetChanged();
    }

    @Override
    public void takePhoto(CCClassify classify, CCItem item)
    {
        this.classify = classify;
        mContext.takePhoto(classify, item);
    }

    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub

    }

}

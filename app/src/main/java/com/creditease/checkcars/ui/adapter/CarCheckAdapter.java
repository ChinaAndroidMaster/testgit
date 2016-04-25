/**
 * Copyright © 2015 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.data.bean.Answer;
import com.creditease.checkcars.data.bean.CCClassify;
import com.creditease.checkcars.data.bean.CCItem;
import com.creditease.checkcars.ui.act.SelectPhotoActivity;
import com.creditease.checkcars.ui.act.carcheck.CarCheckActivity;
import com.creditease.checkcars.ui.act.carcheck.ReportControl;
import com.creditease.checkcars.ui.dialog.CommitReportDialog;
import com.creditease.checkcars.ui.dialog.InputDialog;
import com.creditease.checkcars.ui.dialog.InputDialog.ContentCallback;
import com.creditease.checkcars.ui.dialog.base.TDialog;
import com.creditease.checkcars.ui.ppwindow.answer.AnswersPopWindow;
import com.creditease.checkcars.ui.ppwindow.base.PullDownWindow;
import com.creditease.checkcars.ui.ppwindow.base.PullDownWindow.InitViewDataPort;
import com.creditease.checkcars.ui.ppwindow.base.PullDownWindow.PullOnItemClickListener;
import com.creditease.checkcars.ui.widget.HorizontalListView;

/**
 * @author 子龍
 * @function
 * @date 2015年3月11日
 * @company CREDITEASE
 */
@SuppressLint( {"SimpleDateFormat", "InflateParams"} )
public class CarCheckAdapter extends BaseAdapter
{

    protected LayoutInflater mInflater;
    private CarCheckActivity mContext;
    // private CheckedProgressCallback callback;
    private CCClassify ccClassify;
    private List< CCItem > problems = new ArrayList< CCItem >();
    private IPhoto iPhoto;
    private ListView listview;
    private boolean isUnable = false;

    public CarCheckAdapter(CarCheckActivity context)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setAble(boolean isAble)
    {
        isUnable = !isAble;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return ccClassify.attrs.size();
    }

    @Override
    public Object getItem(int position)
    {
        return ccClassify.attrs.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolderChild viewHolderChild = null;
        if ( convertView == null )
        {
            convertView = mInflater.inflate(R.layout.listitem_carcheck, null);
            viewHolderChild = new ViewHolderChild();
            viewHolderChild.nameTV = ( TextView ) convertView.findViewById(R.id.listitem_carcheck_name_tv);
            viewHolderChild.switchs = ( TextView ) convertView.findViewById(R.id.listitem_carcheck_answer);
            viewHolderChild.photo =
                    ( RelativeLayout ) convertView.findViewById(R.id.listitem_carcheck_tphoto);
            viewHolderChild.unfinishIcon =
                    ( ImageView ) convertView.findViewById(R.id.listitem_carcheck_unfinish_icon);
            viewHolderChild.commentBtn =
                    ( Button ) convertView.findViewById(R.id.listitem_carcheck_comment_btn);
            viewHolderChild.remarkTV = ( TextView ) convertView.findViewById(R.id.listitem_carcheck_remark);
            // 问题项图片集
            viewHolderChild.hlv =
                    ( HorizontalListView ) convertView.findViewById(R.id.listitem_carcheck_hlv);
            convertView.setTag(viewHolderChild);
        } else
        {
            viewHolderChild = ( ViewHolderChild ) convertView.getTag();
        }

        if ( ccClassify != null )
        {
            // viewHolderChild.commentBtn.setText(ccClassify.mainItem.attrValue);
            final CCItem item = ccClassify.attrs.get(position);
            if ( position == (getCount() - 1) )
            {
                viewHolderChild.commentBtn.setVisibility(View.VISIBLE);
            } else
            {
                viewHolderChild.commentBtn.setVisibility(View.GONE);
            }
            viewHolderChild.commentBtn.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    InputDialog dialog = new InputDialog(mContext, ccClassify.mainItem, true, false);
                    dialog.setTitle("点评");
                    TDialog.Builder.builderDialog(dialog).showDialog();
                }
            });
            viewHolderChild.switchs.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View arg0)
                {
                    if ( !item.isChecked() )
                    {
                        item.checked();
                    }
                    notifyDataSetChanged();
                }
            });
            final ViewHolderChild vhc = viewHolderChild;
            viewHolderChild.switchs.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    if ( item.answerType == CCItem.ANSWER_TYPE_SELECT )
                    {
                        showPPWindow(vhc, item);
                    } else if ( item.answerType == CCItem.ANSWER_TYPE_INPUT )
                    {
                        InputDialog dialog = new InputDialog(mContext, item, true, true);
                        dialog.setTitle("输入数值");
                        dialog.setCallback(new ContentCallback()
                        {

                            @Override
                            public void contentCallback(String content)
                            {
                                vhc.switchs.setText(item.attrValue);
                                notifyDataSetChanged();
                            }
                        });
                        TDialog.Builder.builderDialog(dialog).showDialog();
                    }
                }
            });

            viewHolderChild.photo.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View arg0)
                {
                    takePhoto(ccClassify, item);
                }
            });
            if ( !TextUtils.isEmpty(item.remark) )
            {
                viewHolderChild.remarkTV.setText(R.string.str_carcheck_item_remarked);
                viewHolderChild.remarkTV.setBackgroundResource(R.drawable.bg_btn_remark_done);
            } else
            {
                viewHolderChild.remarkTV.setText(R.string.str_carcheck_item_remark);
                viewHolderChild.remarkTV.setBackgroundResource(R.drawable.bg_btn_remark_undo);
            }
            viewHolderChild.remarkTV.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    InputDialog dialog = new InputDialog(mContext, item);
                    dialog.setTitle("备注");
                    dialog.setCallback(new ContentCallback()
                    {

                        @Override
                        public void contentCallback(String content)
                        {
                            notifyDataSetChanged();
                        }
                    });
                    TDialog.Builder.builderDialog(dialog).showDialog();
                }
            });
            String name = TextUtils.isEmpty(item.name) ? item.attrName : item.name;
            viewHolderChild.nameTV.setText((position + 1) + " . " + name
                    + ((item.answerRequired == CCItem.ANSWER_REQUIRED_NEED) ? "" : "(选填)"));
            viewHolderChild.unfinishIcon.setVisibility(View.GONE);
            viewHolderChild.switchs.setHint(R.string.str_carcheck_default);
            viewHolderChild.switchs.setHintTextColor(0xffaaaaaa);
            if ( (!TextUtils.isEmpty(item.attrValue)) && (item.attrValue != CCItem.VALUE_DEFAULT) )
            {
                if ( item.answerType == CCItem.ANSWER_TYPE_INPUT )
                {
                    viewHolderChild.switchs.setText(item.attrValue);
                } else if ( item.answerType == CCItem.ANSWER_TYPE_SELECT )
                {
                    if ( (item.answer != null) && TextUtils.isEmpty(item.attrValueName) )
                    {
                        item.attrValueName = item.answer.name;
                    }
                    if ( !TextUtils.isEmpty(item.attrValueName) )
                    {
                        viewHolderChild.switchs.setText(item.attrValueName);
                    }
                }
                if ( (item.attrValue.equals(CCItem.VALUE_YES) || ((item.answer != null) && (item.answer.weight != 4)))
                        && (item.requiredType == CCItem.TYPE_REQUIRED_IMAGE) )
                {
                    if ( TextUtils.isEmpty(item.imgValue) )
                    {
                        addProblemItem(item);
                        viewHolderChild.unfinishIcon.setVisibility(View.VISIBLE);
                    } else
                    {
                        viewHolderChild.unfinishIcon.setVisibility(View.GONE);
                    }
                }
            } else
            {
                viewHolderChild.switchs.setText("");
                if ( item.answerType == CCItem.ANSWER_TYPE_INPUT )
                {
                    viewHolderChild.switchs.setHint("输入数值");
                } else if ( item.answerType == CCItem.ANSWER_TYPE_SELECT )
                {
                    viewHolderChild.switchs.setHint(R.string.str_carcheck_default);
                }
            }
            if ( !TextUtils.isEmpty(item.imgValue)
                    && ((item.picPathList == null) || (item.picPathList.size() == 0)) )
            {
                if ( item.picPathList == null )
                {
                    item.picPathList = new ArrayList< String >(3);
                }
                String[] values = item.imgValue.split(";");
                for ( String path : values )
                {
                    item.picPathList.add(path);
                }
            }
            if ( (item.picPathList != null) && (item.picPathList.size() > 0) )
            {
                viewHolderChild.hlv.setVisibility(View.VISIBLE);
                ItemImageAdapter adapter = ( ItemImageAdapter ) viewHolderChild.hlv.getAdapter();
                if ( adapter == null )
                {
                    adapter = new ItemImageAdapter(item.picPathList, mContext);
                    viewHolderChild.hlv.setAdapter(adapter);
                } else
                {
                    adapter.notifyDataSetChanged();
                }
            } else
            {
                viewHolderChild.hlv.setVisibility(View.GONE);
            }
            viewHolderChild.hlv.setOnItemClickListener(new OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView< ? > parent, View view, int position, long id)
                {
                    // 图片预览
                    mContext.reviewPics(ccClassify, item, position);
                }
            });
        }


        if ( isUnable )
        {
            viewHolderChild.nameTV.setTextColor(0xffbbbbbb);
            viewHolderChild.remarkTV.setTextColor(0xffbbbbbb);
            viewHolderChild.commentBtn.setTextColor(0xffbbbbbb);
            viewHolderChild.switchs.setTextColor(0xffbbbbbb);
            viewHolderChild.remarkTV.setBackgroundResource(R.drawable.bg_btn_unable_checkitem);
            viewHolderChild.commentBtn.setBackgroundResource(R.drawable.bg_btn_unable_checkitem);
            viewHolderChild.photo.setBackgroundResource(R.drawable.bg_btn_unable_checkitem);
        } else
        {
            viewHolderChild.nameTV.setTextColor(0xff666666);
            viewHolderChild.remarkTV.setTextColor(0xffffffff);
            viewHolderChild.commentBtn.setTextColor(0xffffffff);
            viewHolderChild.switchs.setTextColor(0xff666666);
            // viewHolderChild.remarkTV.setBackgroundResource(R.drawable.bg_btn_unable_checkitem);
            viewHolderChild.commentBtn.setBackgroundResource(R.drawable.bg_btn_orderitem);
            viewHolderChild.photo.setBackgroundResource(R.drawable.bg_btn_checkitem_photo);
        }
        return convertView;
    }

    /**
     * 问题项目集合
     *
     * @param item 下午12:27:58
     */
    public void addProblemItem(CCItem item)
    {
        if ( !problems.contains(item)
                && (item.getAttrValue() || ((item.answer != null) && (item.answer.weight != 4)))
                && (item.requiredType == CCItem.TYPE_REQUIRED_IMAGE) && TextUtils.isEmpty(item.imgValue) )
        {
            problems.add(item);
        } else if ( problems.contains(item)
                && (!item.getAttrValue() || ((item.answer != null) && (item.answer.weight == 4))) )
        {
            problems.remove(item);
        }
    }

    /**
     * 问题项目是否已经拍照
     *
     * @return 下午12:27:44
     */
    public boolean isCatchPhotoInProblemItems()
    {
        boolean bool = (problems.size() == 0);
        if ( !bool )
        {
            CCItem item = problems.get(0);
            final int index = ccClassify.attrs.indexOf(item);
            listview.setSelection(index);
            final CommitReportDialog dialog = new CommitReportDialog(mContext);
            dialog.setContent("检测有问题项未拍照");
            dialog.setOnOKBtnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            }).setOnCancelBtnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });
            TDialog.Builder.builderDialog(dialog).showDialog();
        }
        return bool;
    }

    public boolean isCheckedAll()
    {
        if ( ccClassify.progress < getCount() )
        {
            for ( int i = 0, len = getCount(); i < len; i++ )
            {
                CCItem item = ccClassify.attrs.get(i);
                if ( item.answerRequired == CCItem.ANSWER_REQUIRED_UNNEED )
                {
                    continue;
                }
                if ( TextUtils.isEmpty(item.attrValue) || (item.attrValue == CCItem.VALUE_DEFAULT) )
                {
                    listview.setSelection(i);
                    return false;
                }
            }

        }
        return true;
    }

    public void setIPhoto(IPhoto iPhoto)
    {
        this.iPhoto = iPhoto;
    }

    // public void setCheckedProgressCallback(CheckedProgressCallback callback)
    // {
    // this.callback = callback;
    // }

    public void setListView(ListView listview)
    {
        this.listview = listview;
    }

    private void showPPWindow(final ViewHolderChild vhc, final CCItem item)
    {
        AnswersPopWindow ppWindow =
                new AnswersPopWindow(mContext, vhc.switchs, vhc.switchs, 374,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT, new InitViewDataPort< Answer >()
                {

                    @Override
                    public void initViewData(PullDownWindow< Answer > window)
                    {
                        if ( (item.answerList != null) && (item.answerList.size() > 0) )
                        {
                            window.setData(item.answerList);
                            int position;
                            if ( item.answer == null )
                            {
                                position = -1;
                            } else
                            {
                                position = item.answerList.indexOf(item.answer);
                            }
                            window.setSelectPosition(position);
                        }
                    }
                });
        ppWindow.setPullOnItemClickListener(new PullOnItemClickListener< Answer >()
        {

            @Override
            public void onPullItemClick(PullDownWindow< Answer > window, AdapterView< ? > parent, int position)
            {
                if ( TextUtils.isEmpty(item.attrValue) || CCItem.VALUE_DEFAULT.equals(item.attrValue) )
                {
                    ccClassify.progress++;
                }
                Answer ans = ( Answer ) parent.getAdapter().getItem(position);
                item.valueAttr(ans);
                addProblemItem(item);
                mContext.reportControl.saveReport();
                vhc.switchs.setText(ans.name);
                notifyDataSetChanged();

            }
        });
        ppWindow.showAsDropDown(vhc.switchs);
    }

    private void takePhoto(CCClassify classify, CCItem item)
    {
        if ( iPhoto != null )
        {
            iPhoto.takePhoto(classify, item);
        }
        Intent intent = new Intent(mContext, SelectPhotoActivity.class);
        intent.putExtra(SelectPhotoActivity.KEY_SELECT_PHOTO_ACTION,
                ReportControl.ImageControl.REQUEST_CAMERA_CODE_CHECKITEM);
        mContext.startActivityForResult(intent,
                ReportControl.ImageControl.REQUEST_CAMERA_CODE_CHECKITEM);

    }

    public void updateDataAndNotify(CCClassify ccClassify)
    {
        this.ccClassify = ccClassify;
        if ( ccClassify.attrs == null )
        {
            return;
        }
        CCItem c = new CCItem("主属性");
        if ( (ccClassify.attrs.size() > 0) && ccClassify.attrs.contains(c) )
        {
            int index = ccClassify.attrs.indexOf(c);
            ccClassify.mainItem = ccClassify.attrs.get(index);
            // if (ccClassify.mainItem != null
            // && !TextUtils.isEmpty(ccClassify.mainItem.imgValue)) {
            // String[] imgValue = ccClassify.mainItem.imgValue.split(";");
            // for (int i = 0; i < imgValue.length; i++) {
            // ccClassify.mainItem.picPathList.add(imgValue[i]);
            // }
            // }
            ccClassify.attrs.remove(index);
        }
        notifyDataSetChanged();
    }

    public void updateImageCont()
    {

    }

    public void updateProblems()
    {
        for ( int i = 0; i < getCount(); i++ )
        {
            int size = problems.size();
            if ( size > 0 )
            {
                for ( int j = size - 1; j >= 0; j-- )
                {
                    CCItem item = problems.get(j);
                    if ( !TextUtils.isEmpty(item.imgValue)
                            || (item.requiredType != CCItem.TYPE_REQUIRED_IMAGE)
                            || ((item.answer != null) && (item.answer.weight == 4)) || !item.getAttrValue() )
                    {
                        problems.remove(j);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface CheckedProgressCallback
    {
        public void progressCallback(boolean isFinished);
    }

    public interface IPhoto
    {
        public void takePhoto(CCClassify classify, CCItem item);
    }

    class ViewHolderChild
    {

        TextView nameTV;
        TextView switchs;
        RelativeLayout photo;
        ImageView unfinishIcon;
        Button commentBtn;
        TextView remarkTV;
        HorizontalListView hlv;

    }

    class CETextWatcher implements TextWatcher
    {
        public CETextWatcher()
        {
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if ( (s != null) && !TextUtils.isEmpty(s) )
            {
                ccClassify.mainItem.attrValue = s.toString();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }
    }
}

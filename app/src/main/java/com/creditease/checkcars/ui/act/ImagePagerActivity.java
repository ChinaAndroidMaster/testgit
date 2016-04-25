package com.creditease.checkcars.ui.act;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.creditease.checkcars.R;
import com.creditease.checkcars.ui.act.base.BaseActivity;
import com.creditease.checkcars.ui.widget.HackyViewPager;
import com.creditease.checkcars.ui.widget.ImageDetailFragment;

/**
 * 图片查看器
 *
 * @author zgb
 */
public class ImagePagerActivity extends BaseActivity
{
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    private static final String STATE_POSITION = "STATE_POSITION";
    private HackyViewPager mPager;
    private int pagerPosition;

    private TextView indicator;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        ArrayList< String > urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

        mPager = ( HackyViewPager ) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = ( TextView ) findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener()
        {

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }

            @Override
            public void onPageSelected(int arg0)
            {
                CharSequence text =
                        getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if ( savedInstanceState != null )
        {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initData()
    {

    }


    @Override
    protected void initEvents()
    {

    }


    @Override
    public BaseActivity getContext()
    {
        return this;
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter
    {

        public ArrayList< String > fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList< String > fileList)
        {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount()
        {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position)
        {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }

    }
}

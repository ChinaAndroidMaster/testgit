package com.creditease.checkcars.ui.adapter;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter
{
    private List< View > views;
    private List< String > title;

    public ViewPagerAdapter(Context context, List< View > list, List< String > title)
    {
        views = list;
        this.title = title;
    }

    @Override
    public void destroyItem(View container, int position, Object object)
    {
        (( ViewPager ) container).removeView(( View ) object);
    }

    @Override
    public void finishUpdate(View container)
    {
    }

    @Override
    public int getCount()
    {
        return views.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return title.get(position);
    }

    @Override
    public Object instantiateItem(View container, int position)
    {
        // ((ViewPager) container).addView(mListViews.get(position),0);
        // return mListViews.get(arg1);
        (( ViewPager ) container).addView(views.get(position));
        return views.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader)
    {
    }

    @Override
    public Parcelable saveState()
    {
        return null;
    }

    @Override
    public void startUpdate(View container)
    {
    }

}

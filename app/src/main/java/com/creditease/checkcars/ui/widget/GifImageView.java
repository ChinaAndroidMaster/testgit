package com.creditease.checkcars.ui.widget;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

@SuppressLint( "NewApi" )
public class GifImageView extends ImageView
{
    private Movie mMovie;
    private long mMovieStart;

    public GifImageView(Context context)
    {
        super(context);
    }

    public GifImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public GifImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    public void init(InputStream draw)
    {
        if ( draw != null )
        {
            mMovie = Movie.decodeStream(draw);
        }
        // API 11 之后不能使用硬件渲染
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
        {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        long now = android.os.SystemClock.uptimeMillis();
        if ( mMovieStart == 0 )
        { // first time
            mMovieStart = now;
        }
        if ( mMovie != null )
        {
            int dur = mMovie.duration();
            if ( dur == 0 )
            {
                dur = 1000;
            }
            int relTime = ( int ) ((now - mMovieStart) % dur);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        if ( mMovie != null )
        {
            mMovie = null;
        }
        super.onDetachedFromWindow();
    }
}
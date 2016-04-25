package com.creditease.checkcars.ui.widget;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.creditease.checkcars.R;

/**
 * @author zgb
 */
public class ImageDetailFragment extends Fragment
{
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    private RequestQueue mQueue;

    public static ImageDetailFragment newInstance(String imageUrl)
    {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if ( !TextUtils.isEmpty(mImageUrl) )
        {
            progressBar.setVisibility(View.VISIBLE);
            ImageRequest imageRequest = new ImageRequest(mImageUrl, new Response.Listener< Bitmap >()
            {

                @Override
                public void onResponse(Bitmap bitmap)
                {
                    mImageView.setImageBitmap(bitmap);
                    progressBar.setVisibility(View.GONE);
                    mAttacher.update();
                }

            }, 0, 0, Config.RGB_565, new Response.ErrorListener()
            {

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    progressBar.setVisibility(View.GONE);
                    mImageView.setImageResource(R.drawable.answer_big_error);
                }
            });
            mQueue.add(imageRequest);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        mQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = ( ImageView ) v.findViewById(com.creditease.checkcars.R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener()
        {

            @Override
            public void onPhotoTap(View view, float arg1, float arg2)
            {
                if ( getActivity() != null )
                {
                    getActivity().finish();
                }
            }
        });

        progressBar = ( ProgressBar ) v.findViewById(R.id.loading);
        return v;
    }
}

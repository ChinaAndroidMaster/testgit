/**
 * Copyright © 2013 CreditEase. All rights reserved.
 */
package com.creditease.checkcars.net.uploadimg;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.creditease.utilframe.http.client.multipart.HttpMultipartMode;
import com.creditease.utilframe.http.client.multipart.MultipartEntity;

/**
 * @author noah.zheng
 */
public class CredetEaseMultipartEntity extends MultipartEntity
{

    private final ProgressListener listener;

    public CredetEaseMultipartEntity(final HttpMultipartMode mode, final ProgressListener listener)
    {
        super(mode);
        this.listener = listener;
    }

    public CredetEaseMultipartEntity(HttpMultipartMode mode, final String boundary,
                                     final Charset charset, final ProgressListener listener)
    {
        super(mode, boundary, charset);
        this.listener = listener;
    }

    public CredetEaseMultipartEntity(final ProgressListener listener)
    {
        super();
        this.listener = listener;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException
    {
        super.writeTo(new CountingOutputStream(outstream, listener));
    }

    public static interface ProgressListener
    {
        void transferred(long num);
    }

    public static class CountingOutputStream extends FilterOutputStream
    {

        private final ProgressListener listener;
        private long transferred;

        public CountingOutputStream(final OutputStream out, final ProgressListener listener)
        {
            super(out);
            this.listener = listener;
            transferred = 0;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException
        {
            out.write(b, off, len);
            transferred += len;
            if ( listener != null )
            {
                listener.transferred(transferred);
            }
        }

        @Override
        public void write(int b) throws IOException
        {
            out.write(b);
            transferred++;
            if ( listener != null )
            {
                listener.transferred(transferred);
            }
        }
    }
}

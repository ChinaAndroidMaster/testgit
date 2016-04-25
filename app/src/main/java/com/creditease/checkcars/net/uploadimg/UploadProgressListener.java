package com.creditease.checkcars.net.uploadimg;

public interface UploadProgressListener
{

    public void onProgress(String uploadId, long count, long progress);

}

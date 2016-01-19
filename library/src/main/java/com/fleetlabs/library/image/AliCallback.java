package com.fleetlabs.library.image;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public abstract class AliCallback implements OSSProgressCallback<PutObjectRequest>, OSSCompletedCallback<PutObjectRequest, PutObjectResult> {

    @Override
    public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
    }

    @Override
    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
    }

    @Override
    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
    }
}

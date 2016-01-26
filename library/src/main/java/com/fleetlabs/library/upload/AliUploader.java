package com.fleetlabs.library.upload;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.net.URI;
import java.util.HashMap;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class AliUploader implements Uploader {

    private OSS oss;
    private String endpoint;
    private String bucket;

    @Override
    public void init(Context context, HashMap<String, String> config) {
        endpoint = config.get("endpoint");
        bucket = config.get("bucket");
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                config.get("accessKeyId"), config.get("accessKeySecret"));
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(context, endpoint, credentialProvider, conf);
    }

    @Override
    public void upload(String path, final String name, HashMap<String, String> otherParameters, final UploadCallback callback) {
        // 构造上传请求
        if(otherParameters != null && otherParameters.containsKey("bucket")) {
            bucket = otherParameters.get("bucket");
        }

        PutObjectRequest put = new PutObjectRequest(bucket, name, path);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {

            @Override
            public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
                callback.onProgress(l * 100d / l1);
            }
        });

        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                callback.onSuccess("http://" + bucket + "." + URI.create(endpoint).getHost() + "/" + name);
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                callback.onFailure(e1 == null ? new Exception("上传失败") : e1);
            }
        });
    }
}

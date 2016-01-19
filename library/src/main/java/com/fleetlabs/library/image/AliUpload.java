package com.fleetlabs.library.image;

import com.alibaba.oss.sample.MultipartUploadSamples;
import com.alibaba.oss.sample.PutObjectSamples;
import com.alibaba.sdk.android.oss.OSS;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class AliUpload {

    private OSS oss;
    private String bucket;

    public AliUpload(OSS oss, String bucket) {
        this.oss = oss;
        this.bucket = bucket;
    }

    public void upload(String uploadFilePath, String fileName) {
        uploadSimple(oss, uploadFilePath, bucket, fileName);
    }

    public void uploadWithCallback(String uploadFilePath, String fileName, AliCallback aliImageCallback) {
        uploadSimpleWithCallback(oss, uploadFilePath, bucket, fileName, aliImageCallback);
    }

    public void uploadMultipart(String uploadFilePath, String fileName) {
        multipartUpload(oss, uploadFilePath, bucket, fileName);
    }

    private void uploadSimple(final OSS oss, final String uploadFilePath, final String bucket, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new PutObjectSamples(oss, bucket, fileName, uploadFilePath).asyncPutObjectFromLocalFile();
            }
        }).start();
    }

    private void uploadSimpleWithCallback(final OSS oss, final String uploadFilePath, final String bucket, final String fileName, final AliCallback aliImageCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new PutObjectSamples(oss, bucket, fileName, uploadFilePath, aliImageCallback).asyncPutObjectFromLocalFile();
            }
        }).start();
    }

    private void multipartUpload(final OSS oss, final String uploadFilePath, final String bucket, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new MultipartUploadSamples(oss, bucket, fileName, uploadFilePath).multipartUpload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

package com.fleetlabs.library.utils;

import android.content.Context;

import com.alibaba.oss.sample.MultipartUploadSamples;
import com.alibaba.oss.sample.PutObjectSamples;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.fleetlabs.library.model.OSSType;

import java.io.File;

/**
 * Created by Aaron.Wu on 2016/1/18.
 */
public class ImageUploadUtil {

    public static void upload(Context context, OSSType type, String endpoint, String accessKeyId, String accessKeySecret, String uploadFilePath, String bucket, String fileName) {
        switch (type) {
            case ALI_OSS:
                OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
                ClientConfiguration conf = new ClientConfiguration();
                conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                OSSLog.enableLog();
                final OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);
                File file = new File(uploadFilePath);
                double length = file.length() / 1024 / 1024d;
                //if file size greater than 4M, use multipart upload way.
                if (length < 4) {
                    uploadSimple(oss, uploadFilePath, bucket, fileName);
                } else {
                    multipartUpload(oss, uploadFilePath, bucket, fileName);
                }
                break;
            case QI_NIU_CLOUD:

                break;
        }
    }

    private static void uploadSimple(final OSS oss, final String uploadFilePath, final String bucket, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new PutObjectSamples(oss, bucket, fileName, uploadFilePath).asyncPutObjectFromLocalFile();
            }
        }).start();
    }

    private static void multipartUpload(final OSS oss, final String uploadFilePath, final String bucket, final String fileName) {
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

package com.fleetlabs.library.image;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class AliManager {

    private static Context mContext;

    private static final AliManager INSTANCE = new AliManager();

    protected static AliManager get(Context context) {
        mContext = context;
        return INSTANCE;
    }

    public AliUpload init(String endpoint, String accessKeyId, String accessKeySecret, String bucket) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        final OSS oss = new OSSClient(mContext, endpoint, credentialProvider, conf);
        return new AliUpload(oss, bucket);
    }


}

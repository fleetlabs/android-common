package com.fleetlabs.common;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.fleetlabs.library.model.OSSType;
import com.fleetlabs.library.utils.ImageLoadUtil;
import com.fleetlabs.library.utils.ImageUploadUtil;

public class ALIImageUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnMultipartUpload;
    private Button btnMultipart;
    private Button btnSimpleUpload;
    private Button btnSimpleLoad;
    private ImageView ivTest;

    private static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    private static final String accessKeyId = "LVDUaIkwk9ul7mL7";
    private static final String accessKeySecret = "l03usWb8e5LG96VXJXQi8PYYoxeLqN";
    private static final String bucket = "kuitest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aliimage_upload);

        btnSimpleUpload = (Button) findViewById(R.id.btnSimpleUpload);
        btnSimpleLoad = (Button) findViewById(R.id.btnSimpleLoad);
        btnMultipartUpload = (Button) findViewById(R.id.btnMultipartUpload);
        btnMultipart = (Button) findViewById(R.id.btnMultipart);
        ivTest = (ImageView) findViewById(R.id.ivTest);
        btnMultipartUpload.setOnClickListener(this);
        btnMultipart.setOnClickListener(this);
        btnSimpleUpload.setOnClickListener(this);
        btnSimpleLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSimpleUpload:
                ImageUploadUtil.upload(this, OSSType.ALI_OSS, endpoint, accessKeyId, accessKeySecret,
                        Environment.getExternalStorageDirectory().toString() + "/1.jpg", bucket, "11.png");
                break;
            case R.id.btnSimpleLoad:
                OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
                ClientConfiguration conf = new ClientConfiguration();
                conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                OSSLog.enableLog();
                OSS oss = new OSSClient(this, endpoint, credentialProvider, conf);
                try {
                    String image1 = oss.presignConstrainedObjectURL(bucket, "11.png", 30 * 60);
                    ImageLoadUtil.load(this, image1, ivTest);
                } catch (ClientException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnMultipartUpload:
                ImageUploadUtil.upload(this, OSSType.ALI_OSS, endpoint, accessKeyId, accessKeySecret,
                        Environment.getExternalStorageDirectory().toString() + "/2.jpg", bucket, "22.png");
                break;
            case R.id.btnMultipart:
                OSSCredentialProvider credentialProvider2 = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
                ClientConfiguration conf2 = new ClientConfiguration();
                conf2.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                conf2.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                conf2.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                conf2.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                OSSLog.enableLog();
                OSS oss2 = new OSSClient(this, endpoint, credentialProvider2, conf2);
                try {
                    String image2 = oss2.presignConstrainedObjectURL(bucket, "22.png", 30 * 60);
                    ImageLoadUtil.load(this, image2, ivTest);
                } catch (ClientException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

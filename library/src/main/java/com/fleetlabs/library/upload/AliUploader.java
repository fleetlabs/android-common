package com.fleetlabs.library.upload;

import android.content.Context;
import android.util.Base64;
/*
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
*/
import com.fleetlabs.library.utils.UrlSafeBase64;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class AliUploader implements Uploader {
    /*
    private OSS oss;
    private String endpoint;
    private String bucket;
    */

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;

    @Override
    public void init(Context context, HashMap<String, String> config) {
        accessKeyId = config.get("accessKeyId");
        accessKeySecret = config.get("accessKeySecret");
        endpoint = config.get("endpoint");

        /*
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
        */
    }

    @Override
    public void upload(String path, final String name, HashMap<String, String> otherParameters, final UploadCallback callback) {
        // 客户端hack
        String policy = "expiration\":\"2115-01-27T10:56:19Z\",\"conditions\":[[\"content-length-range\", 0, 1048576000]]}";
        policy = Base64.encodeToString(policy.getBytes(), Base64.DEFAULT);
        String signature = "";
        try {
            signature = Base64.encodeToString(HmacSHA1Encrypt(policy, accessKeySecret), Base64.DEFAULT);
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure(e);
            }
            e.printStackTrace();

            return;
        }

        HttpUploader httpUploader = new HttpUploader();

        HashMap<String, String> config = new HashMap<>();
        config.put("endpoint", endpoint);
        httpUploader.init(null, config);

        otherParameters = new HashMap<>();
        otherParameters.put("OSSAccessKeyId", accessKeyId);
        //otherParameters.put("policy", policy);
        otherParameters.put("key", "upload/"+name);
        //otherParameters.put("Signature", signature);
        otherParameters.put("success_action_status", "200");

        httpUploader.upload(path, "file", otherParameters, callback);


        // 构造上传请求
        /*
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
        */
    }

    private static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        String MAC_NAME = "HmacSHA1";
        String ENCODING = "UTF-8";

        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }
}

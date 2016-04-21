package com.fleetlabs.library.upload;

import android.content.Context;

import com.fleetlabs.library.utils.UrlSafeBase64;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class QiNiuUploader implements Uploader {

    public static final String TOKEN_PARAM = "token";
    public static final String KEY_PARAM = "key";

    private Context mContext;
    private String keyUrl;
    private String AccessKey;
    private String SecretKey;
    private String bucket;

    @Override
    public void init(Context context, HashMap<String, String> config) {
        mContext = context;

        if (config.containsKey("key_url")) {
            keyUrl = config.get("key_url");
        }

        if (config.containsKey("AccessKey")) {
            AccessKey = config.get("AccessKey");
        }

        if (config.containsKey("SecretKey")) {
            SecretKey = config.get("SecretKey");
        }

        if (config.containsKey("bucket")) {
            bucket = config.get("bucket");
        }
    }

    @Override
    public void upload(final String path, final String name, HashMap<String, String> otherParameters, final UploadCallback callback) {
        //Use User Token
        if (otherParameters != null && otherParameters.containsKey(TOKEN_PARAM)) {
            upload2(otherParameters, path, name, callback);
            return;
        }

        String b = bucket;

        if (otherParameters != null && otherParameters.containsKey("bucket")) {
            b = otherParameters.get("bucket");
        }

        // Generate token at client, Not recommend
        if (AccessKey != null && SecretKey != null) {
            try {
                // 1 构造上传策略
                JSONObject _json = new JSONObject();
                long _dataline = System.currentTimeMillis() / 1000 + 3600;
                _json.put("deadline", _dataline);// 有效时间为一个小时
                _json.put("scope", b);
                String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
                        .toString().getBytes());
                byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, SecretKey);
                String _encodedSign = UrlSafeBase64.encodeToString(_sign);
                String _uploadToken = AccessKey + ':' + _encodedSign + ':'
                        + _encodedPutPolicy;
                HashMap<String, String> config = new HashMap<>();
                config.put(TOKEN_PARAM, _uploadToken);
                upload2(config, path, name, callback);
                return;
            } catch (Exception e) {
                callback.onFailure(e);
                return;
            }
        }
        // Get Token from Server
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient mOkHttpClient = new OkHttpClient();
                final Request request = new Request.Builder().url(keyUrl).build();
                final com.squareup.okhttp.Call call = mOkHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        callback.onFailure(new Exception("授权获取失败"));
                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {
                        String uploadToken = response.body().string();
                        try {
                            String token = new JSONObject(uploadToken).get("uptoken").toString();
                            HashMap<String, String> config = new HashMap<>();
                            config.put(TOKEN_PARAM, token);
                            upload2(config, path, name, callback);
                        } catch (JSONException e) {
                            callback.onFailure(new Exception("授权获取失败"));
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public String upload(String path, String name, HashMap<String, String> otherParameters) {
        HttpUploader httpUploader = new HttpUploader();

        //Use User Token
        if (otherParameters != null && otherParameters.containsKey(TOKEN_PARAM)) {
            HashMap<String, String> config = new HashMap<>();
            config.put("endpoint", "http://upload.qiniu.com/");
            httpUploader.init(mContext, config);

            return httpUploader.upload(path, name, otherParameters);
        } else {
            try {
                // 1 构造上传策略
                JSONObject _json = new JSONObject();
                long _dataline = System.currentTimeMillis() / 1000 + 3600;
                _json.put("deadline", _dataline);// 有效时间为一个小时
                _json.put("scope", bucket);
                String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
                        .toString().getBytes());
                byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, SecretKey);
                String _encodedSign = UrlSafeBase64.encodeToString(_sign);
                String _uploadToken = AccessKey + ':' + _encodedSign + ':'
                        + _encodedPutPolicy;
                HashMap<String, String> config = new HashMap<>();
                config.put(TOKEN_PARAM, _uploadToken);

                config.put("endpoint", "http://upload.qiniu.com/");
                httpUploader.init(mContext, config);

                return httpUploader.upload(path, name, otherParameters);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void upload2(HashMap<String, String> otherParameters, String path, String name, final UploadCallback callback) {

        HttpUploader httpUploader = new HttpUploader();

        HashMap<String, String> config = new HashMap<>();
        config.put("endpoint", "http://upload.qiniu.com/");
        httpUploader.init(mContext, config);

        httpUploader.upload(path, name, otherParameters, callback);
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

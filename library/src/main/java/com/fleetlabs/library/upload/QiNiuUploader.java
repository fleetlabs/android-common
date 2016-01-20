package com.fleetlabs.library.upload;

import android.content.Context;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class QiNiuUploader implements Uploader {

    private Context mContext;
    private UploadManager uploadManager;
    private String keyUrl;

    @Override
    public void init(Context context, HashMap<String, String> config) {
        mContext = context;
        keyUrl = config.get("key_url");

    }

    @Override
    public void upload(final String path, final String name, String bucket, final UploadCallback callback) {
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
                            upload2(token, path, name, callback);
                        } catch (JSONException e) {
                            callback.onFailure(new Exception("授权获取失败"));
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    private void upload2(String uploadToken, String path, String name, final UploadCallback callback) {
        if (this.uploadManager == null) {
            this.uploadManager = new UploadManager();
        }
        File uploadFile = new File(path);
        UploadOptions uploadOptions = new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                callback.onProgress(percent);
            }
        }, null);

        this.uploadManager.put(uploadFile, name, uploadToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo respInfo, JSONObject jsonData) {
                        if (respInfo.isOK()) {
                            callback.onSuccess(key);
                        } else {
                            callback.onFailure(new Exception());
                        }
                    }
                }, uploadOptions);
    }
}

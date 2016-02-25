package com.fleetlabs.library.upload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by alvinzeng on 1/26/16.
 */
public class HttpUploader implements Uploader {
    private String endpoint;

    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());

    @Override
    public void init(Context context, HashMap<String, String> config) {
        endpoint = config.get("endpoint");
    }

    @Override
    public void upload(final String path, final String name, final HashMap<String, String> otherParameters, final UploadCallback callback) {

        if (otherParameters != null && otherParameters.containsKey("endpoint")) {
            endpoint = otherParameters.get("endpoint");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpRequest httpRequest = HttpRequest.post(endpoint).useCaches(false).chunk(0);
                    if (otherParameters != null) {
                        Iterator iter = otherParameters.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            String key = entry.getKey().toString();
                            String val = entry.getValue().toString();

                            httpRequest.part(key, val);
                        }
                    }

                    httpRequest.progress(new HttpRequest.UploadProgress() {
                        @Override
                        public void onUpload(final long uploaded, final long total) {
                            MAIN_THREAD.post(new Runnable() {
                                @Override
                                public void run() {
                                    int percent = (int) (uploaded * 100d / total);
                                    if (percent >= 100) {
                                        percent = 99;
                                    }

                                    callback.onProgress((double) percent);
                                }
                            });
                        }
                    }).part("file", name, new File(path));

                    if (httpRequest.ok()) {
                        final String response = httpRequest.body();

                        MAIN_THREAD.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onProgress(100.0);
                                callback.onSuccess(response);
                            }
                        });
                    } else {
                        MAIN_THREAD.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(new Exception("Fail to get response"));
                            }
                        });
                    }
                } catch (final Exception exc) {
                    MAIN_THREAD.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(exc);
                        }
                    });
                }
            }
        }).start();
    }
}

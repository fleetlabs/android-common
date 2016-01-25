package com.fleetlabs.library.upload;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public interface Uploader {
    void init(Context context, HashMap<String, String> config);
    void upload(String path, String name, UploadCallback callback);
}

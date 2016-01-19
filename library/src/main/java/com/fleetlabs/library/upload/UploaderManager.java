package com.fleetlabs.library.upload;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class UploaderManager {

    private static final UploaderManager INSTANCE = new UploaderManager();

    private Uploader uploader;

    public static UploaderManager getInstance() {
        return INSTANCE;
    }

    /**
     * @param context
     * @param type
     * @param config
     */
    public void initConfig(Context context, OSSType type, HashMap<String, String> config) {
        switch (type) {
            case Ali:
                uploader = new AliUploader();
                break;
            case QiNiu:
                uploader = new QiNiuUploader();
        }
        uploader.init(context, config);
    }

    public void upload(String path, String name, String bucket, UploadCallback callback) {
        uploader.upload(path, name, bucket, callback);
    }

}

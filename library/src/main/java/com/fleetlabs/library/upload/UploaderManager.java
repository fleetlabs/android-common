package com.fleetlabs.library.upload;

import android.content.Context;

import com.fleetlabs.library.upload.queue.FileUploadTaskQueue;

import java.util.HashMap;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class UploaderManager {

    private static final UploaderManager INSTANCE = new UploaderManager();

    private Context mContext;
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
        mContext = context;
        switch (type) {
            case Ali:
                uploader = new AliUploader();
                break;
            case QiNiu:
                uploader = new QiNiuUploader();
                break;
            case Http:
                uploader = new HttpUploader();
        }
        uploader.init(context, config);
    }

    //Add Your custom Uploader
    public void setUploader(Uploader uploader) {
        this.uploader = uploader;
    }

    public void upload(String path, String name, HashMap<String, String> otherParameters, UploadCallback callback) {
        uploader.upload(path, name, otherParameters, callback);
    }

    public void upload(String path, String name, UploadCallback callback) {
        uploader.upload(path, name, null, callback);
    }

    public FileUploadTaskQueue createQueue() {
        FileUploadTaskQueue queue = FileUploadTaskQueue.create(mContext);
        return queue;
    }

}

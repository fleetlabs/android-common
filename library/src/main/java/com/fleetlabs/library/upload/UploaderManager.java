package com.fleetlabs.library.upload;

import android.content.Context;

import com.fleetlabs.library.upload.queue.ImageUploadTask;
import com.fleetlabs.library.upload.queue.ImageUploadTaskQueue;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

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
        }
        uploader.init(context, config);
    }

    public void upload(String path, String name, String bucket, UploadCallback callback) {
        uploader.upload(path, name, bucket, callback);
    }

    public void uploadMultipart(List<String> pathList, List<String> nameList, String bucket, ImageUploadTask.Callback callback) {
        ImageUploadTaskQueue queue = ImageUploadTaskQueue.create(mContext, new Gson());
        for (int i = 0; i < pathList.size(); i++) {
            queue.add(new ImageUploadTask(pathList.get(i), nameList.get(i), bucket));
        }
    }


}

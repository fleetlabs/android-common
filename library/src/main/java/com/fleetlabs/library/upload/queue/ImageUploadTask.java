package com.fleetlabs.library.upload.queue;

import com.fleetlabs.library.upload.UploadCallback;
import com.fleetlabs.library.upload.UploaderManager;
import com.squareup.tape.Task;

public class ImageUploadTask implements Task<ImageUploadTask.Callback> {

    private static final long serialVersionUID = 126142781146165256L;

    public interface Callback {
        void onSuccess(String url);

        void onFailure();
    }

    private String path;
    private String name;
    private String bucket;

    public ImageUploadTask(String path, String name, String bucket) {
        this.path = path;
        this.name = name;
        this.bucket = bucket;
    }

    @Override
    public void execute(final Callback callback) {
        UploadCallback uploadCallback = new UploadCallback() {
            @Override
            public void onSuccess(String url) {
                callback.onSuccess(url);
            }

            @Override
            public void onFailure(Exception exc) {
                callback.onFailure();
            }
        };
        UploaderManager.getInstance().upload(path, name, bucket, uploadCallback);
    }
}

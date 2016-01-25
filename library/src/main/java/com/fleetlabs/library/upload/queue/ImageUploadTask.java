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
    private UploadCallback mCallback;

    public ImageUploadTask(String path, String name, UploadCallback callback) {
        this.path = path;
        this.name = name;
        this.mCallback = callback;
    }

    @Override
    public void execute(final Callback callback) {
        UploaderManager.getInstance().upload(path, name, new UploadCallback() {
            @Override
            public void onSuccess(String url) {
                callback.onSuccess(url);
            }

            @Override
            public void onProgress(double percent) {
                mCallback.onProgress(percent);
            }

            @Override
            public void onFailure(Exception exc) {
                callback.onFailure();
            }
        });
    }
}

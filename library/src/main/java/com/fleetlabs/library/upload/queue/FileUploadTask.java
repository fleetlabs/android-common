package com.fleetlabs.library.upload.queue;

import com.fleetlabs.library.upload.UploadCallback;
import com.fleetlabs.library.upload.UploaderManager;
import com.squareup.tape.Task;

import java.util.HashMap;
import java.util.Map;

public class FileUploadTask implements Task<FileUploadTask.Callback> {

    private static final long serialVersionUID = 126142781146165256L;

    public interface Callback {
        void onSuccess(String url);
        void onFailure();
    }

    private String path;
    private String name;
    private HashMap<String ,String> otherParameters;
    private UploadCallback uploadCallback;

    public FileUploadTask(String path, String name, UploadCallback callback) {
        this.path = path;
        this.name = name;
        uploadCallback = callback;
    }

    public void setOtherParameters(HashMap<String, String> otherParameters) {
        this.otherParameters = otherParameters;
    }

    @Override
    public void execute(final Callback callback) {
        UploaderManager.getInstance().upload(path, name, otherParameters, new UploadCallback() {
            @Override
            public void onSuccess(String url) {
                callback.onSuccess(url);
                uploadCallback.onSuccess(url);
            }

            @Override
            public void onProgress(double percent) {
                uploadCallback.onProgress(percent);
            }

            @Override
            public void onFailure(Exception exc) {
                callback.onFailure();
                uploadCallback.onFailure(exc);
            }
        });
    }
}

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
    private static Map<String, UploadCallback> map = new HashMap<>();

    public FileUploadTask(String path, String name, UploadCallback callback) {
        this.path = path;
        this.name = name;
        map.put(name, callback);
    }

    @Override
    public void execute(final Callback callback) {
        UploaderManager.getInstance().upload(path, name, new UploadCallback() {
            @Override
            public void onSuccess(String url) {
                callback.onSuccess(url);
                map.get(name).onSuccess(url);
            }

            @Override
            public void onProgress(double percent) {
                map.get(name).onProgress(percent);
            }

            @Override
            public void onFailure(Exception exc) {
                map.get(name).onFailure(exc);
            }
        });
    }
}

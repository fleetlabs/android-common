package com.fleetlabs.library.upload.queue;

import com.fleetlabs.library.upload.UploadCallback;
import com.fleetlabs.library.upload.UploaderManager;
import com.squareup.tape.Task;

import java.util.HashMap;
import java.util.Map;

public class FileUploadTask implements Task<FileUploadTask.Callback> {

    private static final long serialVersionUID = 126142781146165256L;

    public interface Callback {
        void onSuccess();
        void onFailure();
    }

    public enum TaskStatus{
        READY, UPLOADING, SUCCESS, FAIL
    }

    private String path;
    private String name;
    private HashMap<String ,String> otherParameters;
    private UploadCallback uploadCallback;
    private TaskStatus taskStatus = TaskStatus.READY;
    private double currentPercent;

    public FileUploadTask(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public FileUploadTask(String path, String name, UploadCallback callback) {
        this.path = path;
        this.name = name;
        uploadCallback = callback;
    }

    public void setOtherParameters(HashMap<String, String> otherParameters) {
        this.otherParameters = otherParameters;
    }

    public void setUploadCallback(UploadCallback uploadCallback) {
        this.uploadCallback = uploadCallback;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public double getCurrentPercent() {
        return currentPercent;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void execute(final Callback callback) {
        taskStatus = TaskStatus.UPLOADING;
        UploaderManager.getInstance().upload(path, name, otherParameters, new UploadCallback() {
            @Override
            public void onSuccess(String url) {
                taskStatus = TaskStatus.SUCCESS;

                callback.onSuccess();
                if(uploadCallback != null) {
                    uploadCallback.onSuccess(url);
                }
            }

            @Override
            public void onProgress(double percent) {
                currentPercent = percent;
                if(uploadCallback != null) {
                    uploadCallback.onProgress(percent);
                }
            }

            @Override
            public void onFailure(Exception exc) {
                taskStatus = TaskStatus.FAIL;
                callback.onFailure();
                if(uploadCallback != null) {
                    uploadCallback.onFailure(exc);
                }
            }
        });
    }
}

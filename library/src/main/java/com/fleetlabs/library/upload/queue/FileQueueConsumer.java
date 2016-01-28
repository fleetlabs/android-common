package com.fleetlabs.library.upload.queue;

import android.util.Log;

/**
 * Created by alvinzeng on 1/26/16.
 */
public class FileQueueConsumer implements FileUploadTask.Callback {

    FileUploadTaskQueue taskQueue;
    private boolean running;

    public FileQueueConsumer(FileUploadTaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void start() {
        executeNext();
    }

    private synchronized void executeNext() {
        if (running) return; // Only one task at a time.

        FileUploadTask task = taskQueue.peek();
        if (task != null) {
            running = true;
            task.execute(this);
        } else {
            Log.d("FileQueueConsumer", "Queue is empty");
        }
    }

    @Override
    public void onSuccess() {
        running = false;
        taskQueue.remove();
        executeNext();
    }

    @Override
    public void onFailure() {
        running = false;
        taskQueue.remove();
        executeNext();
    }
}
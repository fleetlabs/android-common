package com.fleetlabs.library.upload.queue;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.squareup.tape.TaskQueue;

/**
 * Created by Aaron.Wu on 2016/1/22.
 */
public class FileQueueService extends Service implements FileUploadTask.Callback {
    private static TaskQueue<FileUploadTask> taskQueue;
    private boolean running;

    @Override
    public void onCreate() {
        super.onCreate();
        //queue = FileUploadTaskQueue.create(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executeNext();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setQueue() {

    }

    public void executeNext() {
        if (running) return; // Only one task at a time.
        FileUploadTask task = taskQueue.peek();
        if (task != null) {
            task.execute(this);
            running = true;
            return;
        }
        stopSelf(); // We're done for now.
    }

    @Override
    public void onSuccess(String url) {
        running = false;
        taskQueue.remove();
        executeNext();
    }

    @Override
    public void onFailure() {

    }

    public static void setQueue(FileUploadTaskQueue queue) {
        taskQueue = queue;
    }
}
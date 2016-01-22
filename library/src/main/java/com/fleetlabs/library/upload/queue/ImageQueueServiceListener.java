package com.fleetlabs.library.upload.queue;

import android.content.Context;
import android.content.Intent;

import com.squareup.tape.ObjectQueue;

/**
 * Created by Aaron.Wu on 2016/1/22.
 */
public class ImageQueueServiceListener implements ObjectQueue.Listener<ImageUploadTask> {
    private final Context context;

    public ImageQueueServiceListener(Context context) {
        this.context = context;
    }

    @Override
    public void onAdd(ObjectQueue<ImageUploadTask> queue, ImageUploadTask task) {
        context.startService(new Intent(context, ImageQueueService.class));
    }

    @Override
    public void onRemove(ObjectQueue<ImageUploadTask> queue) {
    }
}
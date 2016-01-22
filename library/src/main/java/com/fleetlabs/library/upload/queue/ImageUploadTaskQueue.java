// Copyright 2012 Square, Inc.
package com.fleetlabs.library.upload.queue;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;

import java.io.File;
import java.io.IOException;

public class ImageUploadTaskQueue extends TaskQueue<ImageUploadTask> {
    private static final String FILENAME = "image_upload_task_queue";

    private final Context context;

    private ImageUploadTaskQueue(ObjectQueue<ImageUploadTask> delegate, Context context) {
        super(delegate);
        this.context = context;

        if (size() > 0) {
            startService();
        }
    }

    private void startService() {
        context.startService(new Intent(context, ImageQueueService.class));
    }

    @Override
    public void add(ImageUploadTask entry) {
        super.add(entry);
        startService();
    }

    @Override
    public void remove() {
        super.remove();
    }

    public static ImageUploadTaskQueue create(Context context, Gson gson) {
        FileObjectQueue.Converter<ImageUploadTask> converter = new GsonConverter<ImageUploadTask>(gson, ImageUploadTask.class);
        File queueFile = new File(context.getFilesDir(), FILENAME);
        FileObjectQueue<ImageUploadTask> delegate;
        try {
            delegate = new FileObjectQueue<ImageUploadTask>(queueFile, converter);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file queue.", e);
        }
        return new ImageUploadTaskQueue(delegate, context);
    }
}

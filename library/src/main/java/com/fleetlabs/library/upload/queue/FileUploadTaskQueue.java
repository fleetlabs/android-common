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

public class FileUploadTaskQueue extends TaskQueue<FileUploadTask> {
    private static final String FILENAME = "image_upload_task_queue";

    private final Context context;

    private FileUploadTaskQueue(ObjectQueue<FileUploadTask> delegate, Context context) {
        super(delegate);
        this.context = context;

        if (size() > 0) {
            startService();
        }
    }

    private void startService() {
        Intent intent = new Intent(context, FileQueueService.class);
        context.startService(intent);
        //FileQueueService.getInstance().executeNext();
    }

    @Override
    public void add(FileUploadTask entry) {
        super.add(entry);
        startService();
    }

    @Override
    public void remove() {
        super.remove();
    }

    public static FileUploadTaskQueue create(Context context) {
        FileObjectQueue.Converter<FileUploadTask> converter = new GsonConverter<FileUploadTask>(new Gson(), FileUploadTask.class);
        File queueFile = new File(context.getFilesDir(), FILENAME);
        FileObjectQueue<FileUploadTask> delegate;
        try {
            delegate = new FileObjectQueue<FileUploadTask>(queueFile, converter);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file queue.", e);
        }
        FileUploadTaskQueue queue = new FileUploadTaskQueue(delegate, context);
        FileQueueService.setQueue(queue);
        return queue;
    }
}

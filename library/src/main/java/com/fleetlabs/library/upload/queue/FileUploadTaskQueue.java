// Copyright 2012 Square, Inc.
package com.fleetlabs.library.upload.queue;

import android.content.Context;

import com.squareup.tape.InMemoryObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;

public class FileUploadTaskQueue extends TaskQueue<FileUploadTask> {

    FileQueueConsumer fileQueueConsumer;

    private FileUploadTaskQueue(ObjectQueue<FileUploadTask> delegate) {
        super(delegate);
    }

    @Override
    public void add(FileUploadTask entry) {
        super.add(entry);
        startConsumer();
    }

    private void startConsumer() {
        if (fileQueueConsumer == null) {
            fileQueueConsumer = new FileQueueConsumer(this);
        }
        fileQueueConsumer.start();
    }

    public static FileUploadTaskQueue create(Context context) {
        return new FileUploadTaskQueue(new InMemoryObjectQueue<FileUploadTask>());
    }

    @Override
    public void remove() {
        super.remove();
    }
}

package com.fleetlabs.library.imagepicker;

/**
 * Created by Aaron.Wu on 2016/1/21.
 */
public abstract class ImagePickerCallback {
    public abstract void onSuccess(String path);

    public abstract void onFailed(Exception e);
}

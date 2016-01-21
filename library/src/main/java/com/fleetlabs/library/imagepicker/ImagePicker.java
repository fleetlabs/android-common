package com.fleetlabs.library.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.fleetlabs.library.utils.FileUtil;

/**
 * Created by Aaron.Wu on 2016/1/21.
 */
public class ImagePicker {

    public static final int GET_IMAGE_RESULT_CODE = 0x001;
    public static final int TAKE_PHOTO_RESULT_CODE = 0x002;

    private static Activity mActivity;
    private static ImagePickerCallback mCallback;

    public static void getImageFromCamera(Activity activity, ImagePickerCallback callback) {
        mActivity = activity;
        mCallback = callback;
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            mActivity.startActivityForResult(intent, TAKE_PHOTO_RESULT_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getImageFromGallery(Activity activity, ImagePickerCallback callback) {
        mActivity = activity;
        mCallback = callback;
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            mActivity.startActivityForResult(intent, GET_IMAGE_RESULT_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setOnActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode != mActivity.RESULT_OK) {
            mCallback.onFailed(new Exception("获取图片失败"));
            return;
        }
        switch (requestCode) {
            case TAKE_PHOTO_RESULT_CODE:
                Uri uri = result.getData();
                if (uri == null) {
                    Bundle bundle = result.getExtras();
                    if (bundle != null) {
                        Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                        FileUtil.initDir(mActivity.getPackageName());
                        String path = FileUtil.savePhotoToSDCard(photo, FileUtil.TEMP_FOLDER, String.valueOf(System.currentTimeMillis()));
                        mCallback.onSuccess(path);
                    } else {
                        mCallback.onFailed(new Exception("获取图片失败"));
                    }
                } else {
                    mCallback.onFailed(new Exception("获取图片失败"));
                }
                break;
            case GET_IMAGE_RESULT_CODE:
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = mActivity.getContentResolver().query(result.getData(), proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                FileUtil.initDir(mActivity.getPackageName());
                String tempPath = FileUtil.saveImage(path, FileUtil.TEMP_FOLDER);
                mCallback.onSuccess(tempPath);
                break;
        }
        mActivity = null;
        mCallback = null;
    }
}

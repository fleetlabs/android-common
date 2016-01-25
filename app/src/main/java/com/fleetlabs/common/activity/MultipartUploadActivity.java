package com.fleetlabs.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fleetlabs.common.R;
import com.fleetlabs.library.imagepicker.ImagePicker;
import com.fleetlabs.library.imagepicker.ImagePickerCallback;
import com.fleetlabs.library.upload.UploadCallback;
import com.fleetlabs.library.upload.UploaderManager;
import com.fleetlabs.library.upload.queue.ImageUploadTask;
import com.fleetlabs.library.upload.queue.ImageUploadTaskQueue;
import com.fleetlabs.library.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class MultipartUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private Button btnImagePickerCamera;
    private Button btnImagePickerGallery;
    private Button btnMultipartImageUpload;
    private ImageView ivImage;

    private List<String> pathList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multipart_upload);
        mContext = this;

        btnImagePickerCamera = (Button) findViewById(R.id.btnImagePickerCamera);
        btnImagePickerGallery = (Button) findViewById(R.id.btnImagePickerGallery);
        btnMultipartImageUpload = (Button) findViewById(R.id.btnMultipartImageUpload);
        ivImage = (ImageView) findViewById(R.id.ivImage);

        btnImagePickerCamera.setOnClickListener(this);
        btnImagePickerGallery.setOnClickListener(this);
        btnMultipartImageUpload.setOnClickListener(this);

        pathList.add(Environment.getExternalStorageDirectory().toString() + "/1.jpg");
        pathList.add(Environment.getExternalStorageDirectory().toString() + "/2.jpg");
        pathList.add(Environment.getExternalStorageDirectory().toString() + "/3.jpg");
        pathList.add(Environment.getExternalStorageDirectory().toString() + "/4.jpg");
        pathList.add(Environment.getExternalStorageDirectory().toString() + "/5.jpg");
        nameList.add("1");
        nameList.add("2");
        nameList.add("3");
        nameList.add("4");
        nameList.add("5");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        ImagePicker.setOnActivityResult(requestCode, resultCode, result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnImagePickerCamera:
                ImagePicker.getImageFromCamera(this, new ImagePickerCallback() {

                    @Override
                    public void onSuccess(String path) {
                        Log.i("TAG", "onSuccess" + path);
                        pathList.add(path);
                        ImageUtil.load(mContext, path, ivImage);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.i("TAG", "onFailed:" + e.getMessage());
                    }
                });
                break;
            case R.id.btnImagePickerGallery:
                ImagePicker.getImageFromGallery(this, new ImagePickerCallback() {

                    @Override
                    public void onSuccess(String path) {
                        Log.i("TAG", "onSuccess" + path);
                        pathList.add(path);
                        ImageUtil.load(mContext, path, ivImage);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.i("TAG", "onFailed:" + e.getMessage());
                    }
                });
                break;
            case R.id.btnMultipartImageUpload:
                /*UploaderManager.getInstance().uploadMultipart(pathList, nameList, "kuitest", new ImageUploadTask.Callback() {
                    @Override
                    public void onSuccess(String url) {
                        Log.i("TAG", "onSuccess:" + url);
                    }

                    @Override
                    public void onProgress(int total, int current, double percent) {
                        Log.i("TAG", "onProgress:" + "total:" + total + ";current:" + current + ";percent:" + percent);
                    }

                    @Override
                    public void onFailure() {
                        Log.i("TAG", "onFailure:");
                    }
                });*/

                ImageUploadTaskQueue queue = UploaderManager.getInstance().createQueue();
                for (int i = 0; i < pathList.size(); i++) {
                    queue.add(new ImageUploadTask(pathList.get(i), nameList.get(i), new UploadCallback() {
                        @Override
                        public void onSuccess(String url) {
                            Log.i("TAG", "onSuccess" + url);
                        }

                        @Override
                        public void onProgress(double percent) {
                            Log.i("TAG", "onProgress" + percent);
                        }

                        @Override
                        public void onFailure(Exception exc) {
                            Log.i("TAG", "onProgress");
                        }
                    }));
                }
                break;
        }
    }
}

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
import com.fleetlabs.library.upload.queue.FileUploadTask;
import com.fleetlabs.library.upload.queue.FileUploadTaskQueue;
import com.fleetlabs.library.utils.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
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

                FileUploadTaskQueue queue = UploaderManager.getInstance().createQueue();
                for (int i = 0; i < pathList.size(); i++) {
                    FileUploadTask task = new FileUploadTask(pathList.get(i), "file", new UploadCallback() {
                        @Override
                        public void onSuccess(String url) {
                            Log.i("TAG", "MultipartUploadActivity:onSuccess" + url);
                        }

                        @Override
                        public void onProgress(double percent) {
                            Log.i("TAG", "MultipartUploadActivity:onProgress" + percent);
                        }

                        @Override
                        public void onFailure(Exception exc) {
                            Log.i("TAG", "MultipartUploadActivity:onFailure");
                        }
                    });

                    HashMap<String , String> map = new HashMap<>();
                    map.put("test", "dddddd");
                    task.setOtherParameters(map);

                    queue.add(task);
                }
                break;
        }
    }
}

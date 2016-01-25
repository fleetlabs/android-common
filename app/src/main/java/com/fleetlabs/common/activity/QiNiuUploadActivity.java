package com.fleetlabs.common.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fleetlabs.common.R;
import com.fleetlabs.library.upload.UploadCallback;
import com.fleetlabs.library.upload.UploaderManager;

public class QiNiuUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnQNSimpleUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qi_niu_upload);

        btnQNSimpleUpload = (Button) findViewById(R.id.btnQNSimpleUpload);
        btnQNSimpleUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnQNSimpleUpload:
                String path = Environment.getExternalStorageDirectory().toString() + "/1.jpg";

                UploaderManager.getInstance().upload(path, "123123123456456456123123123", new UploadCallback() {
                    @Override
                    public void onSuccess(String url) {
                        Log.i("TAG", "onSuccess:" + url);
                    }

                    @Override
                    public void onFailure(Exception exc) {
                        Log.i("TAG", "onFailure");
                    }

                    @Override
                    public void onProgress(double precent) {
                        Log.i("TAG", "onProgress" + precent);
                    }
                });
                break;
        }
    }
}

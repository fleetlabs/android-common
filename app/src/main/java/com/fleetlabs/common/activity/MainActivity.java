package com.fleetlabs.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fleetlabs.common.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAliUpload;
    private Button btnQiNiuUpload;
    private Button btnMultipartUpload;
    private Button btnEditViewWithDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAliUpload = (Button) findViewById(R.id.btnAliUpload);
        btnQiNiuUpload = (Button) findViewById(R.id.btnQiNiuUpload);
        btnMultipartUpload = (Button) findViewById(R.id.btnMultipartUpload);
        btnEditViewWithDelete = (Button) findViewById(R.id.btnEditViewWithDelete);
        btnAliUpload.setOnClickListener(this);
        btnQiNiuUpload.setOnClickListener(this);
        btnMultipartUpload.setOnClickListener(this);
        btnEditViewWithDelete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAliUpload:
                Intent intentAli = new Intent(this, AliImageUploadActivity.class);
                startActivity(intentAli);
                break;
            case R.id.btnQiNiuUpload:
                Intent intentQiNiu = new Intent(this, QiNiuUploadActivity.class);
                startActivity(intentQiNiu);
                break;
            case R.id.btnMultipartUpload:
                Intent multipartUploadIntent = new Intent(this, MultipartUploadActivity.class);
                startActivity(multipartUploadIntent);
                break;
            case R.id.btnEditViewWithDelete:
                Intent EditViewWithDeleteIntent = new Intent(this, EditViewWithDeleteActivity.class);
                startActivity(EditViewWithDeleteIntent);
                break;
            default:break;
        }
    }
}

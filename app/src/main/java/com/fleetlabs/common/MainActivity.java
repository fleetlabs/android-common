package com.fleetlabs.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAliUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAliUpload = (Button) findViewById(R.id.btnAliUpload);
        btnAliUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAliUpload:
                Intent intent = new Intent(this, AliImageUploadActivity.class);
                startActivity(intent);
                break;
        }
    }
}

package com.fleetlabs.common.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fleetlabs.common.R;
import com.fleetlabs.library.view.EditViewWithDelete;

/**
 * Created by Richard.Lai on 2016/1/26.
 */
public class EditViewWithDeleteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view_with_delete);
        View btnSubmit = findViewById(R.id.btnSubmit);
        final EditViewWithDelete evwdEditText = (EditViewWithDelete) findViewById(R.id.evwdEditText);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Log.i("TAG","evwdEditText = " + evwdEditText.getText());
            }
        });
    }
}

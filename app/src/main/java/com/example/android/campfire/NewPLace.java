package com.example.android.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewPLace extends AppCompatActivity {

    private Button newPlaceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);
        newPlaceBtn = findViewById(R.id.newPlaceConfirm);
        newPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = getIntent();
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}

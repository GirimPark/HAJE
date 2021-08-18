package com.cookandroid.haje;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class DriverCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_call);



        //호출하기 버튼 눌렀을 때
        ImageButton callDriverButton = findViewById(R.id.btnNext);
        callDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
                intent.putExtra("name", "mike");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}


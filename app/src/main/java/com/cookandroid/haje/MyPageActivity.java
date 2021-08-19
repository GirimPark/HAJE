package com.cookandroid.haje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MyPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ImageButton BtnShowRide =findViewById(R.id.btn_show_ride);

        BtnShowRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("내역페이지", "접근 o");
                Intent intent = new Intent(MyPageActivity.this, ShowRideActivity.class);
                startActivity(intent);
            }
        });
    }
}
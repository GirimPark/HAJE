package com.cookandroid.haje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class MyPage2 extends AppCompatActivity {
    FirebaseFirestore db;

    TextView tv_rideDate;
    TextView tv_rideTime;
    TextView tv_ridePlace;
    TextView tv_arriveTime;
    TextView tv_destination;

    Intent idIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page2);

        tv_rideDate = findViewById(R.id.tv_rideDate);
        tv_rideTime = findViewById(R.id.tv_rideTime);
        tv_ridePlace = findViewById(R.id.tv_ridePlace);
        tv_arriveTime = findViewById(R.id.tv_arriveTime);
        tv_destination = findViewById(R.id.tv_destination);

        db = FirebaseFirestore.getInstance();
        idIntent = getIntent();
        String uuid = idIntent.getStringExtra("uuid");

        db.collection("breakdown").document(uuid).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        tv_rideDate.setText(task.getResult().get("date").toString());
                        tv_rideTime.setText(task.getResult().get("startTime").toString());
                        tv_ridePlace.setText(task.getResult().get("departure").toString());
                        tv_arriveTime.setText(task.getResult().get("endTime").toString());
                        tv_destination.setText(task.getResult().get("destination").toString());
                    }
                    else{
                        Log.d("db접근 실패", task.getException().getMessage());
                    }

                });
    }
}
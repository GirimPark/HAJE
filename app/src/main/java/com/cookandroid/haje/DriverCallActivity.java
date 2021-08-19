package com.cookandroid.haje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import net.daum.mf.map.api.MapView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class DriverCallActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    EditText StartPoint;
    EditText EndPoint;
    TextView CarType;
    TextView guardian_number;
    TextView point;
    TextView paypoint;
    ImageButton btnNext;

    Intent emailIntent;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_call);


        db = FirebaseFirestore.getInstance();

        emailIntent = getIntent();
        email = emailIntent.getStringExtra("email");


        StartPoint = findViewById(R.id.StartPoint);
        EndPoint = findViewById(R.id.EndPoint);
        CarType = findViewById(R.id.CarType);
        guardian_number = findViewById(R.id.guardian_number);
        point = findViewById(R.id.point);
        paypoint = findViewById(R.id.paypoint);
        btnNext = findViewById(R.id.btnNext);


        setData(db, email); //  차 기종, 보호자 번호 받아오면 됨
        point.setText("50,000");
        paypoint.setText("20,000");


        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                putPoint(db, StartPoint.getText().toString(), EndPoint.getText().toString());
                Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
                intent.putExtra("name", "mike");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }


    public void setData(FirebaseFirestore db, String email){  //파이어스토어에서 데이터 받아오는 함수
        db.collection("user").document(email).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        //Log.d("db접근 password 받아오기", task.getResult().get("password").toString());
                        String car = task.getResult().get("car").toString();
                        String guardian_num = task.getResult().get("guardian_number").toString();

                        CarType.setText(car);
                        guardian_number.setText(guardian_num);
                    }
                    else{
                        Log.d("db접근 실패", task.getException().getMessage());
                    }
                });
    }


    public void putPoint(FirebaseFirestore db, String startPoint, String endPoint){
        if(startPoint.isEmpty() || endPoint.isEmpty()){
            Toast.makeText(this, "출발지와 도착지를 입력해주세요", Toast.LENGTH_LONG).show();
        }

        // 내역 객체 만들기
        Date now = new Date();
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);

        String id = new UUIDgeneration().getUUID();
        String date = dayFormat.format(now);
        String startTime = timeFormat.format(now);
        String endTime = timeFormat.format(now);
        String user_email = email;
        String departure = startPoint;
        String destination = endPoint;
        int price = 20000;
        boolean safe_message = true;

        Breakdown breakdown = new Breakdown(id, date, startTime, endTime,
                user_email, departure, destination, price, safe_message);
        db.collection("breakdown").document(id).set(breakdown)
        .addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d("db접근 내역 넣기 성공", breakdown.getId());
            }
            else{
                Log.d("db접근 내역 넣기 실패", task.getException().getMessage());
            }
        });


    }
}


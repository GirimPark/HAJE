package com.cookandroid.haje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    ImageButton btnSearchStartPoint;
    ImageButton btnSearchEndPoint;

    Breakdown breakdown;

    Intent callIntent;
    String email;

    double startX, startY, endX, endY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_call);


        db = FirebaseFirestore.getInstance();

        callIntent = getIntent();
        email = callIntent.getStringExtra("email");


        StartPoint = findViewById(R.id.StartPoint);
        EndPoint = findViewById(R.id.EndPoint);
        CarType = findViewById(R.id.CarType);
        guardian_number = findViewById(R.id.guardian_number);
        point = findViewById(R.id.point);
        paypoint = findViewById(R.id.paypoint);
        btnNext = findViewById(R.id.btnNext);
        btnSearchStartPoint = findViewById(R.id.btnSearchStartPoint);
        btnSearchEndPoint = findViewById(R.id.btnSearchEndPoint);


        setData(db, email); //  ??? ??????, ????????? ?????? ???????????? ???
        point.setText("50,000");
        paypoint.setText("20,000");


        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(getApplicationContext());
                geoCoding(geocoder);
                breakdown = setPoint(StartPoint.getText().toString(), EndPoint.getText().toString());
                Log.d("???????????? ?????????", breakdown.getUser_email());
                Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
                intent.putExtra("name", "mike");
                intent.putExtra("breakdown", breakdown);
                intent.putExtra("startX", startX);
                intent.putExtra("startY", startY);
                intent.putExtra("endX", endX);
                intent.putExtra("endY", endY);
                callIntent.putExtra("breakdown", breakdown);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


        btnSearchStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                startActivityForResult(searchIntent, 102);
            }
        });

        btnSearchEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                startActivityForResult(searchIntent, 103);
            }
        });

    }


    public void geoCoding(Geocoder geocoder){

        List<Address> startList = null;
        List<Address> endList = null;

        String startAddress = StartPoint.getText().toString();
        String endAddress = EndPoint.getText().toString();

        try {
            startList = geocoder.getFromLocationName(startAddress, 10);
            endList = geocoder.getFromLocationName(endAddress, 10);
        } catch (IOException e) {
            Log.d("???????????? ??????", e.getMessage());
        }

        if(startList != null){
            if(startList.size() == 0){
                Toast.makeText(getApplicationContext(), "????????? ??????????????????", Toast.LENGTH_LONG).show();
            }
            else{
                Address temp = startList.get(0);
                startX = temp.getLongitude();
                startY = temp.getLatitude();

                temp = endList.get(0);
                endX = temp.getLongitude();
                endY = temp.getLatitude();
            }
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case 102:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        StartPoint.setText(data);
                    }
                }
                break;

            case 103:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        EndPoint.setText(data);
                    }
                }
                break;
        }
    }



    public void setData(FirebaseFirestore db, String email){  //???????????????????????? ????????? ???????????? ??????
        db.collection("user").document(email).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        //Log.d("db?????? password ????????????", task.getResult().get("password").toString());
                        String car = task.getResult().get("car").toString();
                        String guardian_num = task.getResult().get("guardian_number").toString();

                        CarType.setText(car);
                        guardian_number.setText(guardian_num);
                    }
                    else{
                        Log.d("db?????? ??????", task.getException().getMessage());
                    }
                });
    }


    public Breakdown setPoint(String startPoint, String endPoint){
        if(startPoint.isEmpty() || endPoint.isEmpty()){
            Toast.makeText(this, "???????????? ???????????? ??????????????????", Toast.LENGTH_LONG).show();
        }

        // ?????? ?????? ?????????
        Date now = new Date();
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);

        String id = new UUIDgeneration().getUUID();
        String date = dayFormat.format(now);
        String startTime = timeFormat.format(now);  //  ???????????? ?????? ??? ????????? ???
        String endTime = timeFormat.format(now);    //  ???????????? ?????? ??? ????????? ???
        String user_email = email;
        String departure = startPoint;
        String destination = endPoint;
        int price = 20000;
        boolean safe_message = true;

        breakdown = new Breakdown(id, date, startTime, endTime,
                user_email, departure, destination, price, safe_message);

        return breakdown;
//        db.collection("breakdown").document(id).set(breakdown)
//        .addOnCompleteListener(task -> {
//            if(task.isSuccessful()){
//                Log.d("db?????? ?????? ?????? ??????", breakdown.getId());
//            }
//            else{
//                Log.d("db?????? ?????? ?????? ??????", task.getException().getMessage());
//            }
//        });


    }
}


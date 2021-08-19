package com.cookandroid.haje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SignupActivity3 extends AppCompatActivity {
    EditText PassName;
    EditText PassBirth;
    EditText PassBirth2;
    EditText PassNumber;
    EditText PassSecurity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);


        ImageButton btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                genderauth();
            }

        });
    }


    private void genderauth() {

        PassName = findViewById(R.id.PassName);
        PassBirth = findViewById(R.id.PassBirth);
        PassBirth2 = findViewById(R.id.PassBirth2);
        PassNumber = findViewById(R.id.PassNumber);
        PassSecurity = findViewById(R.id.PassSecurity);

        if (PassName.getText().toString().isEmpty() ||
                PassBirth.getText().toString().isEmpty() ||
                PassBirth2.getText().toString().isEmpty() ||
                PassNumber.getText().toString().isEmpty() ||
                PassSecurity.getText().toString().isEmpty()) {
            Toast.makeText(SignupActivity3.this, "비어있는 정보를 입력해주세요", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(SignupActivity3.this, "여성인증 완료되었습니다.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();

        }


    }


}





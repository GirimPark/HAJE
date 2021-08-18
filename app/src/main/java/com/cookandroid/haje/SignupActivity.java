package com.cookandroid.haje;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    int user_id;

    EditText inputPhoneNum;
    EditText inputID;
    EditText inputName;
    EditText inputPW;
    EditText inputPW2;
    ImageButton btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnNext = findViewById(R.id.btnNext);
        auth = FirebaseAuth.getInstance();

        // "다음"버튼 클릭시 정보 입력 되었는지 확인 후 firebase에 정보 저장
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                confirmAndCreate();
            }

        });
    }

    private void confirmAndCreate(){

        inputPhoneNum = findViewById(R.id.inputPhoneNum);
        inputID = findViewById(R.id.inputID);
        inputName = findViewById(R.id.inputName);
        inputPW = findViewById(R.id.inputPW);
        inputPW2 = findViewById(R.id.inputPW2);

        // 정보 확인 *수정 : 존재하는 계정인지 등 정보 중복 확인해야함
        if(inputPhoneNum.getText().toString().isEmpty() ||
                inputID.getText().toString().isEmpty() ||
                inputName.getText().toString().isEmpty() ||
                inputPW.getText().toString().isEmpty() ||
                inputPW2.getText().toString().isEmpty()) {
            Toast.makeText(this, "비어있는 정보를 입력해주세요", Toast.LENGTH_LONG).show();
        }
        else if(!inputPW.getText().toString().equals(inputPW2.getText().toString())){
            Toast.makeText(this, "비밀번호 입력이 일치하지 않습니다", Toast.LENGTH_LONG).show();
        }
        else{   // 정보 입력 모두 되었으면 저장
            auth.createUserWithEmailAndPassword(
                    inputID.getText().toString(),
                    inputPW.getText().toString()
            ).addOnCompleteListener(task -> {

               if(task.isSuccessful()){ // 회원가입 성공

                   String number = inputPhoneNum.getText().toString();
                   String email = inputID.getText().toString();
                   String name = inputName.getText().toString();
                   String password = inputPW.getText().toString();

                   db = FirebaseFirestore.getInstance();


                   User user = new User(number,
                           email, name,
                           password, true,
                           "car", "111");


                   db.collection("user").document(user.email).set(user)
                   .addOnCompleteListener(subtask -> {
                       if(subtask.isSuccessful()){
                           Toast.makeText(this, "회원 정보 삽입 성공", Toast.LENGTH_LONG).show();
                       }
                       else{
                           Toast.makeText(this, subtask.getException().getMessage(), Toast.LENGTH_LONG).show();
                       }
                   });

                   Toast.makeText(this, "회원가입 성공", Toast.LENGTH_LONG).show();
                   Intent intent = new Intent(getApplicationContext(), SignupActivity2.class);
                   startActivity(intent);

                   getDBUser(db, user);
               }

               else{    //  회원가입 실패
                   Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
               }

            });

        }
    }

    public int getDBUser(FirebaseFirestore db, User user){
        db.collection("user").document(user.email).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d("db접근 password 받아오기", task.getResult().get("password").toString());
                    }
                    else{
                        Log.d("db접근 실패", task.getException().getMessage());
                    }
                });

        return user_id;
    }

}

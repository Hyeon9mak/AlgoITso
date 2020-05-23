package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity{
    private ServiceApi service;
    private EditText editEmail;
    private EditText editPasswd;
    private EditText editName;
    private EditText editPhone;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        ImageButton btn_back = (ImageButton) findViewById(R.id.back_btn);
        Button btn_join = (Button)findViewById(R.id.join_btn);
        editEmail = (EditText) findViewById(R.id.edit_Email);
        editPasswd = (EditText) findViewById(R.id.edit_Passwd);
        editName = (EditText) findViewById(R.id.edit_Name);
        editPhone = (EditText) findViewById(R.id.edit_Phone);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        progressBar = (ProgressBar) findViewById(R.id.join_progress);

        btn_back.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.back_btn:
                        Intent intent_back = new Intent(getApplicationContext(), InitActivity.class);
                        startActivity(intent_back);
                        finish();
                        break;
                }
            }
        });
        btn_join.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()) {
                    case R.id.join_btn:
                        attemptJoin();
                }
            }
        });
    }

    private void attemptJoin(){
        editEmail.setError(null);
        editPasswd.setError(null);
        editName.setError(null);
        editPhone.setError(null);

        String email = editEmail.getText().toString();
        String password = editPasswd.getText().toString();
        String name = editName.getText().toString();
        String phone = editPhone.getText().toString();

        boolean cancel = false;
        View focusview = null;

        //패스워드 유효성
        if (password.isEmpty()){
            editEmail.setError("비밀번호를 입력해주세요");
            focusview = editEmail;
            cancel = true;
        }
        else if(!isPasswordValid(password)){
            editPasswd.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusview = editPasswd;
            cancel = true;
        }

        //이메일 유효성
        if(email.isEmpty()){
            editEmail.setError("이메일을 입력해주세요");
            focusview = editEmail;
            cancel = true;
        }
        else if (!isEmailValid(email)){
            editEmail.setError("@를 포함한 유효한 이메일을 입력해주세요.");
            focusview = editEmail;
            cancel = true;
        }

        //이름 유효성 검사
        if(name.isEmpty()){
            editName.setError("이름을 입력해주세요.");
            focusview = editName;
            cancel = true;
        }

        //번호 유효성 검사
        if(phone.isEmpty()){
            editPhone.setError("전화번호를 입력해주세요.");
            focusview = editPhone;
            cancel = true;
        }

        if(cancel){
            focusview.requestFocus();
        }
        else{
            startJoin(new JoinData(name, email, password, phone));
            showProgress(true);
        }
    }
    private void startJoin(JoinData data){
        service.userJoin(data).enqueue(new Callback<JoinResponse>(){
            @Override
            public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                JoinResponse result = response.body();
                Toast.makeText(JoinActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);

                if(result.getCode()==200){
                    Intent intent_join = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent_join); //데이터베이스 필요
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JoinResponse> call, Throwable t) {
                Toast.makeText(JoinActivity.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
                showProgress(false);
            }
        });
    }
    private boolean isEmailValid(String email){ return email.contains("@"); };

    private  boolean isPasswordValid(String password){ return password.length() >= 6; }

    private void showProgress(boolean show){
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}

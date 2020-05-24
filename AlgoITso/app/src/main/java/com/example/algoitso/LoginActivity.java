package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import com.example.algoitso.SharedPreference;
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

public class LoginActivity extends AppCompatActivity{
    private EditText editEmail;
    private EditText editPasswd;
    private ProgressBar progressBar;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText) findViewById(R.id.edit_Email);
        editPasswd = (EditText) findViewById(R.id.edit_Passwd);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        ImageButton btn_back = (ImageButton) findViewById(R.id.back_btn);
        Button btn_login = (Button) findViewById(R.id.login_btn);
        Button btn_join = (Button) findViewById(R.id.join_btn);

        service = RetrofitClient.getClient().create(ServiceApi.class);

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

        btn_login.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()) {
                    case R.id.login_btn:
                        attemptLogin();
                }
            }
        });

        btn_join.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()) {
                    case R.id.join_btn:
                        Intent intent_join = new Intent(getApplicationContext(), JoinActivity.class);
                        startActivity(intent_join); //데이터베이스 필요
                        finish();
                        break;
                }
            }
        });
    }

    private void attemptLogin(){
        editEmail.setError(null);
        editPasswd.setError(null);

        String email = editEmail.getText().toString();
        String password = editPasswd.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //패스워드 유효성
        if(password.isEmpty()){
            editEmail.setError("비밀번호를 입력해주세요.");
            focusView = editEmail;
            cancel = true;
        }
        else if(!isPasswordValid(password)){
            editPasswd.setError("6자 잉상의 비밀번호를 입력해주세요.");
            focusView = editPasswd;
            cancel = true;
        }

        //이메일 유효성
        if(email.isEmpty()){
            editEmail.setError("이메일을 입력해주세요.");
            focusView = editEmail;
            cancel = true;
        }
        else if(!isEmailValid(email)){
            editEmail.setError("@를 포함한 유효한 이메일을 입력해주세요.");
            focusView = editEmail;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }
        else{
            startLogin(new LoginData(email, password));
            showProgress(true);
        }
    }
    private void startLogin(final LoginData data){
        service.userLogin(data).enqueue(new Callback<LoginResponse>(){
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);

                if(result.getCode()==200) {
                    Intent intent_login = new Intent(getApplicationContext(), MainActivity.class);
                    SharedPreference.setAttribute(getApplicationContext(), "useremail", data.userEmail);
                    startActivity(intent_login);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "로그인 에러 발생",  Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
                showProgress(false);
            }
        });
    }

    private boolean isEmailValid(String email){ return email.contains("@"); }

    private boolean isPasswordValid(String password){ return password.length() >= 6; }

    private void showProgress(boolean show){
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
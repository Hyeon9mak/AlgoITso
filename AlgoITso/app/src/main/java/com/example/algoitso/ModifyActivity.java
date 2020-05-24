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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyActivity extends AppCompatActivity {

    private TextView textEmail;
    private TextView textName;
    private EditText editPasswd;
    private EditText editPhone;
    private ServiceApi service;
    private ProgressBar progressBar;
    ImageButton btn_back;
    Button btn_modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        progressBar = (ProgressBar) findViewById(R.id.modify_progress);
        textEmail = (TextView) findViewById(R.id.text_Email);
        textName = (TextView) findViewById(R.id.text_Name);
        editPasswd = (EditText) findViewById(R.id.edit_Passwd);
        editPhone = (EditText) findViewById(R.id.edit_Phone);
        btn_back = (ImageButton) findViewById(R.id.back_btn) ;
        btn_modify = (Button) findViewById(R.id.modify_btn);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        String Shared_ID = SharedPreference.getAttribute(getApplicationContext(),"useremail");
        String Shared_Name = SharedPreference.getAttribute(getApplicationContext(), "username");
        String Shared_Phone = SharedPreference.getAttribute(getApplicationContext(), "userphone");
        textEmail.setText(Shared_ID);
        textName.setText(Shared_Name);
        editPhone.setText(Shared_Phone);

        btn_back.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.back_btn:
                        Intent intent_back = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent_back);
                        finish();
                        break;
                }
            }
        });

        btn_modify.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()) {
                    case R.id.modify_btn:
                        attemptModify();
                }
            }
        });
    }

    private void attemptModify(){
        editPasswd.setError(null);
        editPhone.setError(null);

        String email = textEmail.getText().toString();
        String password = editPasswd.getText().toString();
        String phone = editPhone.getText().toString();

        boolean cancel = false;
        View focusview = null;

        //패스워드 유효성
        if (password.isEmpty()){
            editPasswd.setError("수정할 비밀번호를 입력해주세요");
            focusview = editPasswd;     //!
            cancel = true;
        }
        else if(!isPasswordValid(password)){
            editPasswd.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusview = editPasswd;
            cancel = true;
        }
        else if (!compareCurrentPassword(password)) {
            editPasswd.setError("기존 비밀번호와 다르게 입력해주세요.");
            focusview = editPasswd;
            cancel = true;
        }
        if(phone.isEmpty()){
            editPhone.setError("수정할 전화번호를 입력해주세요.");
            focusview = editPhone;
            cancel = true;
        }

        if(cancel){
            focusview.requestFocus();
        }
        else{
            startModify(new ModifyData(password, phone, email));
            showProgress(true);
        }

    }

    private void startModify(final ModifyData data){
        service.userModify(data).enqueue(new Callback<ModifyResponse>(){
            @Override
            public void onResponse(Call<ModifyResponse> call, Response<ModifyResponse> response) {
                ModifyResponse result = response.body();
                Toast.makeText(ModifyActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);

                if(result.getCode()==200){
                    SharedPreference.setAttribute(getApplicationContext(), "userpwd", data.getModifyPwd());
                    SharedPreference.setAttribute(getApplicationContext(), "userphone", data.getModifyPhone());
                    Intent intent_modify = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent_modify); //데이터베이스 필요
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ModifyResponse> call, Throwable t) {
                Toast.makeText(ModifyActivity.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
                showProgress(false);
            }
        });
    }

    private boolean compareCurrentPassword(String password){
        String Shared_Pwd = SharedPreference.getAttribute(getApplicationContext(), "userpwd");
        if(password.equals(Shared_Pwd))
            return false;
        else
            return true;
    }

    private  boolean isPasswordValid(String password){ return password.length() >= 6; }

    private void showProgress(boolean show){
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
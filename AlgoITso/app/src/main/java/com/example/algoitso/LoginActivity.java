package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageButton btn_back = (ImageButton) findViewById(R.id.back_btn);
        Button btn_login = (Button) findViewById(R.id.login_btn);
        Button btn_join = (Button) findViewById(R.id.join_btn);

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
                        Intent intent_login = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent_login);
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
                        Intent intent_join = new Intent(getApplicationContext(), JoinActivity.class);
                        startActivity(intent_join); //데이터베이스 필요
                        finish();
                        break;
                }
            }
        });


    }
}

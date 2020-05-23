package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitActivity extends AppCompatActivity implements Button.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        Button btn_login = (Button) findViewById(R.id.login_btn);
        btn_login.setOnClickListener(this);
        Button btn_join = (Button) findViewById(R.id.join_btn);
        btn_join.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:
                Intent intent_login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent_login);
                finish();
                break;
            case R.id.join_btn:
                Intent intent_join = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent_join);
                finish();
                break;

        }
    }
}

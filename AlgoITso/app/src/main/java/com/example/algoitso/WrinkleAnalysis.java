package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class WrinkleAnalysis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrinkle_analysis);

        ImageButton btn_back = (ImageButton) findViewById(R.id.back_btn);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_back = new Intent(getApplicationContext(), Selectanalysis.class);
                startActivity(intent_back);
                finish();
            }
        });
    }
}
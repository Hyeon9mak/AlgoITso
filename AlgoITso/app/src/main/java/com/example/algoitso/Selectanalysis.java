package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Selectanalysis extends AppCompatActivity {

    String selectServerUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectanalysis);


        ImageButton btn_back = (ImageButton) findViewById(R.id.back_btn);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_back = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_back);
                finish();
            }
        });
        ImageButton btn_pore = (ImageButton) findViewById(R.id.pore_btn);
        btn_pore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_pore = new Intent(getApplicationContext(), PoreAnalysis.class);
                startActivity(intent_pore);
                finish();
            }
        });

        ImageButton btn_wrinkle= (ImageButton) findViewById(R.id.wrinkle_btn);
        btn_wrinkle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_wrinkle = new Intent(getApplicationContext(), WrinkleAnalysis.class);
                startActivity(intent_wrinkle);
                finish();
            }
        });
    }
}
package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements ImageButton.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btn_info = (ImageButton) findViewById(R.id.info_btn);
        btn_info.setOnClickListener(this);
        ImageButton btn_menu = (ImageButton) findViewById(R.id.menu_btn);
        btn_menu.setOnClickListener(this);
        ImageButton btn_pore = (ImageButton) findViewById(R.id.pore_btn);
        btn_pore.setOnClickListener(this);
        ImageButton btn_wrinkle = (ImageButton) findViewById(R.id.wrinkle_btn);
        btn_wrinkle.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.info_btn: //인포 따로
                break;
            case R.id.pore_btn:
                Intent intent_pore = new Intent(getApplicationContext(), CameraTwo.class);
                startActivity(intent_pore);
                finish();
                break;
            case R.id.wrinkle_btn:
                Intent intent_wrinkle = new Intent(getApplicationContext(), CameraTwo.class);
                startActivity(intent_wrinkle);
                finish();
                break;

        }
    }
}

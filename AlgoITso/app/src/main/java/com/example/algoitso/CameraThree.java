package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CameraThree extends AppCompatActivity implements ImageButton.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_three);

        ImageButton btn_menu_cam = (ImageButton) findViewById(R.id.menu_cam_btn);
        btn_menu_cam.setOnClickListener(this);
        ImageButton btn_righttoleft = (ImageButton) findViewById(R.id.righttoleft_btn);
        btn_righttoleft.setOnClickListener(this);
        ImageButton btn_convert = (ImageButton) findViewById(R.id.convert_btn);
        btn_convert.setOnClickListener(this);
        ImageButton btn_camera = (ImageButton) findViewById(R.id.camera_btn);
        btn_camera.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_cam_btn:
                Intent intent_menu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_menu);
                finish();
                break;
            case R.id.righttoleft_btn:
                Intent intent_righttoleft = new Intent(getApplicationContext(), CameraTwo.class);
                startActivity(intent_righttoleft);
                finish();
                break;
            case R.id.convert_btn:
                //카메라 전환
                break;
            case R.id.camera_btn:
                //카메라 촬영
                break;

        }
    }
}

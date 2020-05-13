package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CameraTwo extends AppCompatActivity implements ImageButton.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_two);

        ImageButton btn_menu_cam = (ImageButton) findViewById(R.id.menu_cam_btn);
        btn_menu_cam.setOnClickListener(this);
        ImageButton btn_lefttoright = (ImageButton) findViewById(R.id.lefttoright_btn);
        btn_lefttoright.setOnClickListener(this);
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
            case R.id.lefttoright_btn:
                Intent intent_lefttoright = new Intent(getApplicationContext(), CameraThree.class);
                startActivity(intent_lefttoright);
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

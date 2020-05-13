package com.example.algoitso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements ImageButton.OnClickListener{

    DrawerLayout drawerLayout;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);    //기존 타이틀 지우기
        actionBar.setDisplayHomeAsUpEnabled(true);  //사이드 메뉴 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.side_menu);   //사이드 메뉴 버튼 이미지 지정

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navi_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if (id == R.id.side_logout) {
                    //로그아웃 기능
                    Toast.makeText(context, title , Toast.LENGTH_SHORT).show();
                } else if (id == R.id.side_editinfo) {
                    //회원 정보 수정 기능
                    Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                } else if (id == R.id.side_album) {
                    //앨범 기능
                    Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                } else if (id == R.id.side_analysis) {
                    //분석 기능
                    Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        ImageButton btn_pore = (ImageButton) findViewById(R.id.pore_btn);
        btn_pore.setOnClickListener(this);
        ImageButton btn_wrinkle = (ImageButton) findViewById(R.id.wrinkle_btn);
        btn_wrinkle.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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

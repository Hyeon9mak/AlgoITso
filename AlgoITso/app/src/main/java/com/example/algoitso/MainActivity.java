package com.example.algoitso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
                    Intent intent_logout = new Intent(getApplicationContext(), InitActivity.class);
                    //SharedPreference.removeAttribute(getApplicationContext(),"useremail");
                    startActivity(intent_logout);
                    finish();
                } else if (id == R.id.side_editinfo) {
                    //회원 정보 수정 기능
                    Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                } else if (id == R.id.side_album) {
                    //앨범 기능
                    Intent intent_album = new Intent();
                    intent_album.setType("image/*");
                    intent_album.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent_album,1);
                    finish();
                } else if (id == R.id.side_analysis) {
                    //분석 기능
                    Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        ImageButton btn_take_pic = (ImageButton) findViewById(R.id.take_pic_btn);
        btn_take_pic.setOnClickListener(this);
        ImageButton btn_gallery = (ImageButton) findViewById(R.id.gallery_btn);
        btn_gallery.setOnClickListener(this);

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
            case R.id.take_pic_btn:
                Intent intent_take_pic = new Intent(getApplicationContext(), LaunchActivity.class);
                startActivity(intent_take_pic);
                finish();
                break;
            case R.id.gallery_btn:
                Intent intent_gallery = new Intent(getApplicationContext(), ProcessActivity.class);
                startActivity(intent_gallery);
                finish();
                break;

        }
    }
}
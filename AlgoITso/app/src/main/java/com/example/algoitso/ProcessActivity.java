package com.example.algoitso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class ProcessActivity extends AppCompatActivity {
    public Intent useintent;
    public Bitmap img;
    private static final int REQUEST_CODE = 0;
    private ImageView imageView;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        flag = 0;
        imageView = findViewById(R.id.imageView);

        ImageButton btn_back = (ImageButton) findViewById(R.id.back_btn);
        btn_back.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_back = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_back);
                finish();
            }
        });
        Button btn_retry = (Button) findViewById(R.id.retry_btn);
        btn_retry.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_retry = new Intent(getApplicationContext(), LaunchActivity.class);
                startActivity(intent_retry);
                finish();
            }
        });
        Button use_btn = (Button) findViewById(R.id.use_btn);
        use_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(flag == 0){
                    Toast.makeText(getApplicationContext(), "사진을 먼저 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(useintent);
                    finish();
                }
            }

        });
        Button take_btn = (Button) findViewById(R.id.take_btn);
        take_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_take = new Intent();
                intent_take.setType("image/*");
                flag = 1;

                intent_take.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_take, REQUEST_CODE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    // 이미지 이름
                    System.out.println("------------ ProcessActivity Image -----------");
                    useintent = new Intent(this, SelectActivity.class);
                    String RealName = getRealNameFromURI((data.getData()));
                    System.out.println(RealName);
                    useintent.putExtra("selectImageName", RealName);
                    System.out.println("------------ ProcessActivity Image -----------");
                    img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);

                }catch(Exception e)
                {
                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 선택된 이미지 파일명 가져오기
    private String getRealNameFromURI(Uri returnUri) {
        String result;

        Cursor returnCursor =
                getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,
         * and display it.
         */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }
}

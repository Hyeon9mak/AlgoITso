package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PoreAnalysis extends AppCompatActivity {
    String poreselectFileName = "";
    int serverResponseCode = 0;
    String poreselectServerUri = null;
    Bitmap bmImg;
    ImageView pore_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pore_analysis);
        pore_img = (ImageView) findViewById(R.id.pore_img);
        ImageButton btn_back = (ImageButton) findViewById(R.id.back_btn);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_back = new Intent(getApplicationContext(), Selectanalysis.class);
                startActivity(intent_back);
                finish();
            }
        });







        String User_ID = SharedPreference.getAttribute(getApplicationContext(),"useremail");;
        poreselectServerUri = "http://ec2-18-223-44-123.us-east-2.compute.amazonaws.com:3000/select/" + User_ID + "/pore";
        Thread th = new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
                poreselectFile(poreselectServerUri);
            }
        };
        th.start();
        try{
            th.join();
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println(poreselectServerUri);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            pore_img.setImageBitmap(bmImg);

/*

코드 추가
            result_img.setImageBitmap(bmImg);
            relative_select.setVisibility(View.INVISIBLE);
            relative_result.setVisibility(View.VISIBLE);

 */

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public int poreselectFile(String sourceUri) {
        HttpURLConnection conn = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        try {
            // open a URL connection to the Servlet
            URL url = new URL(sourceUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
//
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();


            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);



            if (serverResponseCode == 200) {
                bmImg = BitmapFactory.decodeStream(conn.getInputStream());
            }

        } catch (MalformedURLException ex) {

            ex.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(PoreAnalysis.this, "MalformedURLException",
                            Toast.LENGTH_SHORT).show();
                }
            });

            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            e.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(PoreAnalysis.this, "Got Exception : see logcat ",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
        }
        return serverResponseCode;

    } // End else block
}
package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SelectActivity extends AppCompatActivity {

    final String uploadFilePath = "/mnt/sdcard/Pictures/";
    String uploadFileName = "";
    int serverResponseCode = 0;
    String upLoadServerUri = null;
    Bitmap bmImg;

    RelativeLayout relative_select;
    RelativeLayout relative_result;

    ImageButton btn_back;
    ImageView result_img;
    Button btn_retry;
    Button btn_sel_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        relative_result = (RelativeLayout) findViewById(R.id.relative_result);
        relative_select = (RelativeLayout) findViewById(R.id.relative_select);
        result_img = (ImageView) findViewById(R.id.result_img);
        btn_back = (ImageButton) findViewById(R.id.back_btn);
        btn_retry = (Button) findViewById(R.id.retry_btn);
        btn_sel_pic = (Button) findViewById(R.id.sel_pic_btn);

        Intent intent = getIntent();
        uploadFileName=intent.getStringExtra("selectImageName");
        System.out.println("------------ SelectActivity Image ------------");
        System.out.println(uploadFileName);
        System.out.println("------------ SelectActivity Image ------------");
        ImageButton btn_pore = (ImageButton) findViewById(R.id.pore_btn);
        ImageButton btn_wrinkle = (ImageButton) findViewById(R.id.wrinkle_btn);




        btn_back.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_back = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_back);
                finish();
            }
        });
        btn_retry.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_retry = new Intent(getApplicationContext(), LaunchActivity.class);
                startActivity(intent_retry);
                finish();
            }
        });
        btn_sel_pic.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_sel_pic = new Intent(getApplicationContext(), ProcessActivity.class);
                startActivity(intent_sel_pic);
                finish();
            }
        });
        btn_pore.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                String User_ID = SharedPreference.getAttribute(getApplicationContext(),"useremail");;
                upLoadServerUri = "http://ec2-18-223-44-123.us-east-2.compute.amazonaws.com:3000/" + User_ID + "/pore";
                Thread th = new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                            }
                        });
                        uploadFile(uploadFilePath + "" + uploadFileName);
                    }
                };
                th.start();
                try{
                    th.join();

                    result_img.setImageBitmap(bmImg);
                    relative_select.setVisibility(View.INVISIBLE);
                    relative_result.setVisibility(View.VISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_wrinkle.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                String User_ID = SharedPreference.getAttribute(getApplicationContext(),"useremail");;
                upLoadServerUri = "http://ec2-18-223-44-123.us-east-2.compute.amazonaws.com:3000/" + User_ID + "/wrinkle";
                Thread th = new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                            }
                        });
                        uploadFile(uploadFilePath + "" + uploadFileName);
                    }
                };
                th.start();
                try{
                    th.join();

                    result_img.setImageBitmap(bmImg);
                    relative_select.setVisibility(View.INVISIBLE);
                    relative_result.setVisibility(View.VISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {


            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                }
            });

            return 0;

        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"img\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();


                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);



                if (serverResponseCode == 200) {
                    bmImg = BitmapFactory.decodeStream(conn.getInputStream());
                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();


            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SelectActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SelectActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;

        } // End else block
    }
}
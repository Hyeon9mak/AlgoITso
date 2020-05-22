package com.example.algoitso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.sql.Savepoint;
import java.util.Arrays;

public class CameraActivity extends AppCompatActivity {

    public static final String CAMERA_FRONT = "1";
    public static final String CAMERA_BACK = "0";

    private String mCameraId = CAMERA_BACK;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Handler handler;
    private ImageReader imageReader;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession mSession;
    private int deviceRotation;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private SensorManager sensorManager;
    private DeviceOrientation deviceOrientation;
    int DSI_height, DSI_width;
    int i=0;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static{
        ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 0);
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 90);
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 180);
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 270);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        상태바 안보이게
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //화면 켜진 상태 유지
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_camera);

        surfaceView = findViewById(R.id.surfaceView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        deviceOrientation = new DeviceOrientation();

        ImageButton btn_menu_cam = (ImageButton) findViewById(R.id.menu_cam_btn);
        btn_menu_cam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_menu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_menu);
                finish();
            }
        });
        final ImageButton btn_lefttoright = (ImageButton) findViewById(R.id.lefttoright_btn);
        btn_lefttoright.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                i=1-i;
                if(i==0)
                    btn_lefttoright.setImageResource(R.drawable.righttoleft);
                else
                    btn_lefttoright.setImageResource(R.drawable.lefttoright);
            }
        });
        ImageButton btn_convert = (ImageButton) findViewById(R.id.convert_btn);
        btn_convert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                convertCamera();
            }
        });
        ImageButton btn_camera = (ImageButton) findViewById(R.id.camera_btn);
        btn_camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        initSurfaceView();
    }

    private void convertCamera() {
        if(mCameraId.equals(CAMERA_FRONT)){
            mCameraId=CAMERA_BACK;
            cameraDevice.close();
            initCameraAndPreview();
        }
        else if(mCameraId.equals(CAMERA_BACK)){
            mCameraId=CAMERA_FRONT;
            cameraDevice.close();
            initCameraAndPreview();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(deviceOrientation.getEventListener(), accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(deviceOrientation.getEventListener(), magnetometer, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(deviceOrientation.getEventListener());
    }

    public void initSurfaceView(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DSI_height = displayMetrics.heightPixels;
        DSI_width = displayMetrics.widthPixels;

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder Holder) {
                initCameraAndPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder Holder) {
                if(cameraDevice != null){
                    cameraDevice.close();
                    cameraDevice = null;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
    }

    @TargetApi(19)
    public void initCameraAndPreview(){
        HandlerThread handlerThread = new HandlerThread("CAMERA2");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        Handler mainHandler = new Handler(getMainLooper());
        try{
            //String cameraId = ""+ CameraCharacteristics.LENS_FACING_FRONT; //후면 카메라 사용

            CameraManager cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            Size largestPreviewSize = map.getOutputSizes(ImageFormat.JPEG)[0];
            Log.i("LargestSize", largestPreviewSize.getWidth()+" "+largestPreviewSize.getHeight());

            setAspectRatioTextureView(largestPreviewSize.getHeight(), largestPreviewSize.getWidth());

            imageReader = ImageReader.newInstance(largestPreviewSize.getWidth(), largestPreviewSize.getHeight(), ImageFormat.JPEG,7);
            imageReader.setOnImageAvailableListener(onImageAvailableListener, mainHandler);
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                return;
            }
            cameraManager.openCamera(mCameraId,deviceStateCallback,handler);
        } catch (CameraAccessException e){
            Toast.makeText(this, "카메라를 열지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            new SaveImageTask().execute(bitmap);
        }
    };

    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            try{
                takePreview();
            } catch (CameraAccessException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if(cameraDevice != null){
                cameraDevice.close();
                cameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Toast.makeText(CameraActivity.this, "카메라를 열지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    public void takePreview() throws CameraAccessException{
        previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        previewBuilder.addTarget(surfaceHolder.getSurface());
        cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), imageReader.getSurface()),sessionPreviewStateCallback, handler);
    }

    private CameraCaptureSession.StateCallback sessionPreviewStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mSession = session;

            try {
                previewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                previewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                mSession.setRepeatingRequest(previewBuilder.build(), null, handler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Toast.makeText(CameraActivity.this, "카메라 구성 실패", Toast.LENGTH_SHORT).show();
        }
    };

    private CameraCaptureSession.CaptureCallback sessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result){
            mSession = session;
            unlockFocus();
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, CaptureResult partialResult){
            mSession = session;
        }
        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }
    };

    public void takePicture(){

        try{
            CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(imageReader.getSurface());
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

            //화면 회전 안되게 고정시켜 놓은 상태에서는 아래 로직으로 방향을 얻을 수 없기 때문에
            //센서를 사용하는 것으로 변경

            deviceRotation = ORIENTATIONS.get(deviceOrientation.getOrientation());
            Log.d("@@@", deviceRotation+"");

            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, deviceRotation);
            CaptureRequest captureRequest = captureRequestBuilder.build();
            mSession.capture(captureRequest, sessionCaptureCallback, handler);
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception{
        if(bitmap == null) return null;
        if(degrees == 0) return bitmap;

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth()/2, (float)bitmap.getHeight()/2);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    private void unlockFocus(){
        try{
            previewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            previewBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            mSession.capture(previewBuilder.build(), sessionCaptureCallback,
                    handler);
            mSession.setRepeatingRequest(previewBuilder.build(), sessionCaptureCallback,
                    handler);
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    public static final String insertImage(ContentResolver cr,
                                           Bitmap source,
                                           String title,
                                           String description){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "images/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED,System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN,System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;

        try{
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if(source != null){
                OutputStream imageOut = cr.openOutputStream(url);
                try{
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

            } else{
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e){
            if (url != null){
                cr.delete(url, null, null);
                url = null;
            }
        }
        if(url != null){
            stringUrl = url.toString();
        }
        return stringUrl;
    }


    private class SaveImageTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);

            Toast.makeText(CameraActivity.this, "사진을 저장하였습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Bitmap... data){

            Bitmap bitmap = null;
            try{
                bitmap = getRotatedBitmap(data[0], deviceRotation);
            } catch (Exception e){
                e.printStackTrace();
            }
            insertImage(getContentResolver(), bitmap, ""+System.currentTimeMillis(),"");

            return null;
        }
    }

    private void setAspectRatioTextureView(int ResolutionWidth, int ResolutionHeight){
        if(ResolutionWidth > ResolutionHeight){
            int newWidth = DSI_width;
            int newHeight = ((DSI_width*ResolutionWidth)/ResolutionHeight);
            updateTextureViewSize(newWidth,newHeight);
        }
        else{
            int newWidth = DSI_width;
            int newHeight = ((DSI_width*ResolutionHeight)/ResolutionWidth);
            updateTextureViewSize(newWidth,newHeight);
        }
    }

    private void updateTextureViewSize(int viewWidth, int viewHeight){
        Log.d("@@@", "TextureView Width : "+viewWidth+" TextureView Height : "+viewHeight);
        surfaceView.setLayoutParams(new FrameLayout.LayoutParams(viewWidth,viewHeight));
    }

}

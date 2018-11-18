package fr.hes.raynaudmonitoring;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import static fr.hes.raynaudmonitoring.Constants.*;
/**
 * An activity that manages the stream of the phone's native camera
 */
public class CamTestActivity extends Activity {
    private static final String TAG = "CamTestActivity";
    Preview preview;
    Button buttonClick;
    Camera camera;
    Activity act;
    Context ctx;
    boolean isStart;
    boolean isUpdate;
    String id;
    String imageFileName;
    static Date dateSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cam_test);
        checkPermission();
        Intent myIntent = getIntent(); // gets the previously created intent
        isStart = myIntent.getBooleanExtra("isStart", false);
        isUpdate = myIntent.getBooleanExtra("isUpdate", false);
        id = myIntent.getStringExtra("id");


        dateSelected = PicturesFragment.dateSelected;


        preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
        preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.cam_layout)).addView(preview);
        preview.setKeepScreenOn(true);





        buttonClick = findViewById(R.id.btnCapture);

        buttonClick.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                preview.mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });

        buttonClick.setOnLongClickListener(new OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                camera.autoFocus(new Camera.AutoFocusCallback(){
                    @Override
                    public void onAutoFocus(boolean arg0, Camera arg1) {
                        //camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                camera = Camera.open(0);
                camera.startPreview();
                preview.setCamera(camera);
            } catch (RuntimeException ex){
                Toast.makeText(ctx, "Camera not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        if(camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    private void resetCam() {
        camera.startPreview();
        preview.setCamera(camera);
    }

    /**
     * Useless
     */
    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    /**
     * Save the pictures taken in the external storage and write the name of the file in the Database
     */
    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;
            try {


                File outFile = createImageFile();

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);

                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                refreshGallery(outFile);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            /**
             * $
             */
            Intent intent = new Intent();


            if(isUpdate==false){
                if (isStart){
                    intent.putExtra("startName", imageFileName);
                }
                else{
                    intent.putExtra("endName", imageFileName);
                }
                setResult(CAM_REQUEST_START,intent);
                finish();
            }
            else {
                if (isStart){
                    intent.putExtra("startName", imageFileName);
                    intent.putExtra("isStart", true);
                }
                else{
                    intent.putExtra("endName", imageFileName);
                    intent.putExtra("isStart", false);
                }
                setResult(CAM_REQUEST_UPDATE,intent);
                finish();
            }

            return null;

        }

    }
    /**
     * Return true if the date passed as parameters is the date from Yesterday
     * @return a File with a storage directory and a name based on a timestamp
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("dMMMyyyy_mmhhSS").format(new Date());
        imageFileName = "pictures_"+timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName+".jpg");

        return image;









    }
    /**
     * Runtime permission for accessing the camera
     */
    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)
                    ctx, Manifest.permission.CAMERA)) {


            } else
                ActivityCompat.requestPermissions((Activity) ctx,
                        new String[]{Manifest.permission.CAMERA},
                        1);
        }


    }

}

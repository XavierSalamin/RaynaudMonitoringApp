package fr.hes.raynaudmonitoring;



import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import static fr.hes.raynaudmonitoring.Constants.*;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flir.flironesdk.Device;
import com.flir.flironesdk.FlirUsbDevice;
import com.flir.flironesdk.Frame;
import com.flir.flironesdk.FrameProcessor;
import com.flir.flironesdk.LoadedFrame;
import com.flir.flironesdk.RenderedImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;

import fr.hes.raynaudmonitoring.util.SystemUiHider;


/**
 * The main activity who read the stream from the Flir One Pro camera
 */
public class ThermoActivity extends AppCompatActivity implements Device.Delegate, FrameProcessor.Delegate, Device.StreamDelegate, Device.PowerUpdateDelegate{

    public static volatile Device flirOneDevice;
    GLSurfaceView thermalSurfaceView;
    private volatile boolean imageCaptureRequested = false;

    private Button captureButton;
    private ImageView spot1, spot2, spot3;
    TextView spotMeterTextView;





    private FrameProcessor frameProcessor;


    private Device.TuningState currentTuningState = Device.TuningState.Unknown;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        setContentView(R.layout.activity_thermo);

        RenderedImage.ImageType defaultImageType = RenderedImage.ImageType.ThermalRGBA8888Image;

        spotMeterTextView = findViewById(R.id.spotMeterValue);

        spot1 = findViewById(R.id.icon1);
        spot2 = findViewById(R.id.icon2);
        spot3 = findViewById(R.id.icon3);


        spot1.setX(416);
        spot1.setY(402);

        spot2.setX(585);
        spot2.setY(390);

        spot3.setX(720);
        spot3.setY(429);

        //Set the render type of the image (We want thermal image only)
        frameProcessor = new FrameProcessor(this, this, EnumSet.of(RenderedImage.ImageType.ThermalRadiometricKelvinImage), true);
        frameProcessor.setGLOutputMode(defaultImageType);

        thermalSurfaceView = findViewById(R.id.imageView);
        thermalSurfaceView.setPreserveEGLContextOnPause(true);
        thermalSurfaceView.setEGLContextClientVersion(2);
        thermalSurfaceView.setRenderer(frameProcessor);
        thermalSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        thermalSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR | GLSurfaceView.DEBUG_LOG_GL_CALLS);


        final String[] imageTypeNames = new String[]{ "Visible", "Thermal", "MSX" };
        final RenderedImage.ImageType[] imageTypeValues = new RenderedImage.ImageType[]{
                RenderedImage.ImageType.VisibleAlignedRGBA8888Image,
                RenderedImage.ImageType.ThermalRGBA8888Image
        };

        captureButton = findViewById(R.id.btnCaptureThermo );
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flirOneDevice != null) {
                    imageCaptureRequested = true;
                }
            }
        });



    }



    @Override
    public void onTuningStateChanged(Device.TuningState tuningState) {

    }

    @Override
    public void onAutomaticTuningChanged(boolean b) {

    }

    @Override
    public void onDeviceConnected(Device device) {


        flirOneDevice = device;
        flirOneDevice.setPowerUpdateDelegate(this);
        flirOneDevice.startFrameStream(this);



    }

    @Override
    public void onDeviceDisconnected(Device device) {

        flirOneDevice = null;

        finish();

    }
    @Override
    public void onPause(){
        super.onPause();

        thermalSurfaceView.onPause();
        if (flirOneDevice != null){
            flirOneDevice.stopFrameStream();
        }
    }
    @Override
    public void onResume(){
        super.onResume();

        thermalSurfaceView.onResume();

        if (flirOneDevice != null){
            flirOneDevice.startFrameStream(this);
        }
    }
    @Override
    public void onStop() {
        // We must unregister our usb receiver, otherwise we will steal events from other apps
        Device.stopDiscovery();
        flirOneDevice = null;

        super.onStop();
    }

    @Override
    public void onBatteryChargingStateReceived(Device.BatteryChargingState batteryChargingState) {

    }

    @Override
    public void onBatteryPercentageReceived(final byte percentage) {
        final TextView levelTextView = findViewById(R.id.batteryLevelTextView);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                levelTextView.setText(String.valueOf((int) percentage) + "%");
            }
        });

    }

    @Override
    public void onFrameReceived(Frame frame) {
        if (currentTuningState != Device.TuningState.InProgress){
            frameProcessor.processFrame(frame, FrameProcessor.QueuingOption.CLEAR_QUEUED);
            thermalSurfaceView.requestRender();
        }
    }

    @Override
    public void onFrameProcessed(final RenderedImage renderedImage) {
        if (renderedImage.imageType() == RenderedImage.ImageType.ThermalRadiometricKelvinImage){
            // Note: this code is not optimized

            int[] thermalPixels = renderedImage.thermalPixelValues();
            // average the center 9 pixels for the spot meter

            int width = renderedImage.width();
            int height = renderedImage.height();
            int centerPixelIndex = width * (height/2) + (width/2);

            int[] centerPixelIndexes = new int[] {
                    centerPixelIndex, centerPixelIndex-1, centerPixelIndex+1,
                    centerPixelIndex - width,
                    centerPixelIndex - width - 1,
                    centerPixelIndex - width + 1,
                    centerPixelIndex + width,
                    centerPixelIndex + width - 1,
                    centerPixelIndex + width + 1
            };

            double averageTemp = 0;

            for (int i = 0; i < centerPixelIndexes.length; i++){
                // Remember: all primitives are signed, we want the unsigned value,
                // we've used renderedImage.thermalPixelValues() to get unsigned values
                int pixelValue = (thermalPixels[centerPixelIndexes[i]]);
                averageTemp += (((double)pixelValue) - averageTemp) / ((double) i + 1);
            }
            double averageC = (averageTemp / 100) - 273.15;
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            final String spotMeterValue = numberFormat.format(averageC) + "°C";


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //spotMeterTextView.setText(""+spotMeterValue);
                }
            });


        }

        /*
        Capture this image if requested.
        */
        if (this.imageCaptureRequested) {
            imageCaptureRequested = false;
            Toast.makeText(this, "Thermographie enregistrée !", Toast.LENGTH_SHORT).show();
            final Context context = this;
            new Thread(new Runnable() {
                public void run() {

                    //We create the path and the name of the thermal file
                    String timeStamp = new SimpleDateFormat("dMMMyyyy_mmhhSS").format(new Date());
                    imageFileName = "thermal_"+timeStamp;
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = new File(storageDir, imageFileName+".jpg");


                    Intent intent = new Intent();
                    boolean isStart = getIntent().getBooleanExtra("isStart", true);



                    // if(isUpdate==false) {
                    if (isStart) {
                    intent.putExtra("thermStartName", imageFileName);
                    }
                    else {
                    intent.putExtra("thermEndName", imageFileName);
                    }

                    intent.putExtra("isStart", isStart);
                    setResult(FLIR_REQUEST_START, intent);




                    try {

                        renderedImage.getFrame().save(image ,frameProcessor);


                        finish();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }

                    try{


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }



    }

    String imageFileName;

    @Override
    public void onStart(){
        super.onStart();
        if (Device.getSupportedDeviceClasses(this).contains(FlirUsbDevice.class)){
            // getView().findViewById(R.id.pleaseConnect).setVisibility(View.VISIBLE);
        }
        try {
            Device.startDiscovery(this, this);
        }catch(IllegalStateException e){
            // it's okay if we've already started discovery
        }catch (SecurityException e){
            // On some platforms, we need the user to select the app to give us permisison to the USB device.
            Toast.makeText(this, "Please insert FLIR One and select "+getString(R.string.app_name), Toast.LENGTH_LONG).show();
            // There is likely a cleaner way to recover, but for now, exit the activity and
            // wait for user to follow the instructions;
            finish();
        }

    }


}

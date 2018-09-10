package fr.hes.raynaudmonitoring;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flir.flironesdk.FrameProcessor;
import com.flir.flironesdk.LoadedFrame;
import com.flir.flironesdk.RenderedImage;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.NumberFormat;
import java.util.EnumSet;
import java.util.Map;


/**
 * An activity that displays thermography with temperature
 */
public class ThermoPreviewActivity extends AppCompatActivity {


    //Icons
    private ImageView i1;
    private ImageView i2;
    private ImageView i3;
    private ImageView i4;
    private ImageView i5;
    private ImageView i6;
    private ImageView i7;
    private ImageView i8;
    private ImageView i9;
    private ImageView i10;
    //Temperature TextView
    private TextView s1;
    private TextView s2;
    private TextView s3;
    private TextView s4;
    private TextView s5;
    private TextView s6;
    private TextView s7;
    private TextView s8;
    private TextView s9;
    private TextView s10;

    private FrameProcessor frameProcessor;
    volatile RenderedImage msxRenderedImage;

    RelativeLayout relativeLayout;
    volatile Bitmap thermalBitmap;
    File frameFile;
    Map<RenderedImage.ImageType, RenderedImage> renderedImageMap;
    private FrameProcessor.Delegate frameReceiver = new FrameProcessor.Delegate() {
        @Override
        public void onFrameProcessed(final RenderedImage renderedImage) {


            if (renderedImage.imageType() == RenderedImage.ImageType.BlendedMSXRGBA8888Image) {
                msxRenderedImage = renderedImage;
                thermalBitmap = Bitmap.createBitmap(renderedImage.width(), renderedImage.height(), Bitmap.Config.ARGB_8888);
                thermalBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(renderedImage.pixelData()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ((ImageView) findViewById(R.id.editorImageView)).setImageBitmap(thermalBitmap);
                    }
                });
            } else if (renderedImage.imageType() == RenderedImage.ImageType.ThermalRadiometricKelvinImage) {

                /*
                double averageTemp = 0;
                short[] shortPixels = new short[renderedImage.pixelData().length / 2];
                ByteBuffer.wrap(renderedImage.pixelData()).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortPixels);
                for (int i = 0; i < shortPixels.length; i++) {
                    averageTemp += (((int)shortPixels[i]) - averageTemp) / ((double) i + 1);
                }
                final double averageC = (averageTemp / 100) - 273.15;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Average Temperature = " + averageC + "ºC", Toast.LENGTH_LONG).show();
                    }
                });

                */

                // Note: this code is not optimized



                //2ième TextView


               //final int  x = spotMeterIcon.getLeft();
               //final  int  y = spotMeterIcon.getTop();


                //We need to scale the frame of the therm image to the scale of the screen;
                float scalingX = 2.25f;
                float scalingY = 3f;
                //640x480

                int x1 = 185;
                int y1 = 134;

                int x2 = 260;
                int y2 = 130;

                int x3 = 320;
                int y3 = 143;

                //Middle finger mid

                int x4 = 190;
                int y4 = 190;

                int x5 = 260;
                int y5 = 190;

                int x6 = 315;
                int y6 = 200;

                //Finger bottom

                int x7 = 196;
                int y7 = 240;

                int x8 = 260;
                int y8 = 236;

                int x9 = 310;
                int y9 = 250;

                //Center

                int x10 = 240;
                int y10 = 360;

                int margeText = 75;

                i1.setX(x1*scalingX);
                i1.setY(y1*scalingY);


                s1.setX((x1*scalingX)-margeText*2);
                s1.setY((y1*scalingY)-margeText);

                i2.setX(x2*scalingX);
                i2.setY(y2*scalingY);

                s2.setY((y2*scalingY)-margeText);
                s2.setX(x2*scalingX);


                i3.setX(x3*scalingX);
                i3.setY(y3*scalingY);

                s3.setY((y3*scalingY)-margeText);

                s3.setX((x3*scalingX)+margeText/2);


                i4.setX(x4*scalingX);
                i4.setY(y4*scalingY);

                s4.setY((y4*scalingY)-margeText);
                s4.setX((x4*scalingX)-margeText*2);


                i5.setX(x5*scalingX);
                i5.setY(y5*scalingY);
                s5.setY((y5*scalingY)-margeText);
                s5.setX(x5*scalingX);


                i6.setX(x6*scalingX);
                i6.setY((y6*scalingY));
                s6.setY((y6*scalingY)-margeText);
                s6.setX((x6*scalingX)+margeText/2);



                i7.setY((y7*scalingY));
                i7.setX(x7*scalingX);

                s7.setX((x7*scalingX)-margeText*2);
                s7.setY((y7*scalingY)-margeText);


                s8.setX(x8*scalingX);
                i8.setY((y8*scalingY));

                i8.setX(x8*scalingX);
                s8.setY((y8*scalingY)-margeText);

                s9.setX((x9*scalingX)+margeText/2);
                i9.setY((y9*scalingY));

                i9.setX(x9*scalingX);
                s9.setY((y9*scalingY)-margeText);

                s10.setX(x10*scalingX);
                i10.setY((y10*scalingY));

                i10.setX(x10*scalingX);
                s10.setY((y10*scalingY)-margeText);
                final String v1 =         getTemperature(x1, y1, renderedImage);

                final String v2 =         getTemperature(x2, y2, renderedImage);
                final String v3 =         getTemperature(x3, y3, renderedImage);

                final String v4 =         getTemperature(x4, y4, renderedImage);
                final String v5 =         getTemperature(x5, y5, renderedImage);

                final String v6 =         getTemperature(x6, y6, renderedImage);

                final String v7 =         getTemperature(x7, y7, renderedImage);
                final String v8 =         getTemperature(x8, y8, renderedImage);
                final String v9 =         getTemperature(x9, y9, renderedImage);
                final String v10 =         getTemperature(x10, y10, renderedImage);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        s1.setText(""+v1);
                        s2.setText(""+v2);
                        s3.setText(""+v3);
                        s4.setText(""+v4);
                        s5.setText(""+v5);
                        s6.setText(""+v6);
                        s7.setText(""+v7);
                        s8.setText(""+v8);
                        s9.setText(""+v9);
                        s10.setText(""+v10);
                    }
                });
           }

        }
    };

    @SuppressLint("ResourceAsColor")
    private void addSpot(int x, int y, RenderedImage renderedImage) {


        float scaling = 2.25f;
        //Rendered Image 640*480

        ImageView imageView = new ImageView(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
            imageView.setLayoutParams(layoutParams);

        imageView.setImageResource(R.drawable.ic_location_searching_black_24dp);

        imageView.setX(x*scaling);
        imageView.setY(y*scaling);

        final TextView textView = new TextView(this);
        textView.setTextColor(Color.BLACK);
        //TextView

        textView.setX(x*scaling);
        int test = (int)((y*scaling)-60);
        textView.setY(test);
        textView.setBackgroundColor(Color.argb(120,255,255,255));


        final String v1 =         getTemperature(x, y, renderedImage);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(""+v1);
            }
        });

        relativeLayout.addView(textView);
        relativeLayout.addView(imageView);
        textView.setVisibility(View.VISIBLE);

    }
    /**
     *  Gives the average value of the temperature of a pixel at a position (x, y)
     */
    private String getTemperature(int x, int y, RenderedImage renderedImage) {

        int[] thermalPixels = renderedImage.thermalPixelValues();
        final int width = renderedImage.width();
        int index =  x + width*y;

        int[] indexes = new int[]{
                index, index - 1, index + 1,
                index - width,
                index - width - 1,
                index - width + 1,
                index + width,
                index + width - 1,
                index + width + 1
        };

        double averageTemp = 0;
        // average the center 9 pixels for the spot meter

        for (int i = 0; i < indexes.length; i++) {
            // Remember: all primitives are signed, we want the unsigned value,
            // we've used renderedImage.thermalPixelValues() to get unsigned values
            int pixelValue = (thermalPixels[indexes[i]]);
            averageTemp += (((double) pixelValue) - averageTemp) / ((double) i + 1);
        }
        double averageC = (averageTemp / 100) - 273.15;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
         String spotMeterValue = numberFormat.format(averageC) + "°C";
         return  spotMeterValue;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermo_preview);
        i1 =  findViewById(R.id.i1);
        i2 =  findViewById(R.id.i2);
        i3 =  findViewById(R.id.i3);
        i4 =  findViewById(R.id.i4);
        i5 =  findViewById(R.id.i5);
        i6 =  findViewById(R.id.i6);
        i7 =  findViewById(R.id.i7);
        i8 =  findViewById(R.id.i8);
        i9 =  findViewById(R.id.i9);
        i10 =  findViewById(R.id.i10);
        setTitle("Thermogramme");

        Intent intent = getIntent();
        String path;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        relativeLayout = findViewById(R.id.relative_layout);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        s4 = findViewById(R.id.s4);
        s5 = findViewById(R.id.s5);
        s6 = findViewById(R.id.s6);
        s7 = findViewById(R.id.s7);
        s8 = findViewById(R.id.s8);
        s9 = findViewById(R.id.s9);
        s10 = findViewById(R.id.s10);


        frameFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + getIntent().getStringExtra("fileName") + ".jpg");
        final LoadedFrame frame;
        try {
            frame = new LoadedFrame(frameFile);
        }catch (final RuntimeException ex){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            return;
        }


        final Context context = this.getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                frameProcessor = new FrameProcessor(context, frameReceiver,
                        EnumSet.of(frame.getPreviewImageType(), RenderedImage.ImageType.ThermalRadiometricKelvinImage));
                frameProcessor.setImagePalette(frame.getPreviewPalette());
                frameProcessor.processFrame(frame);
            }
        }).start();


    }

    /**
     * Modify the color palette of the thermogram
     * @param v the View of the layout
     */
    public void onImageClick(View v){
        if (msxRenderedImage != null) {
            RenderedImage.Palette currentPalette = msxRenderedImage.palette();
            RenderedImage.Palette[] palettes = RenderedImage.Palette.values();
            int nextPaletteOrdinal = (currentPalette.ordinal() + 1) % palettes.length;
            frameProcessor.setImagePalette(palettes[nextPaletteOrdinal]);
            renderedImageMap = frameProcessor.getProcessedFrames(new LoadedFrame(frameFile));
            msxRenderedImage = renderedImageMap.get(RenderedImage.ImageType.BlendedMSXRGBA8888Image);

            thermalBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(msxRenderedImage.pixelData()));
            ((ImageView) findViewById(R.id.editorImageView)).setImageBitmap(thermalBitmap);
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return false;
    }
}

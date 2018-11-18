package fr.hes.raynaudmonitoring;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import java.io.File;


/**
 * An Activity to preview and analyze pictures
 */
public class PicturesPreviewActivity extends AppCompatActivity {
    public Context context;


    String path;
    private TextView averageValueTextView;
    private TextView averageHSVValueTextView;
    private TextView minmaxValueTextView;
    private TextView minmaxHSVValueTextView;
    int counter = 0;

    float maxH = 0;
    float maxS = 0;
    float maxV = 0;

    float minH = 255;
    float minS = 255;
    float minV = 255;

    int maxR = 0;
    int maxG = 0;
    int maxB = 0;

    int minR = 255;
    int minG = 255;
    int minB = 255;

    int avgR = 0;
    int avgG = 0;
    int avgB = 0;
    int counterRed = 0;
    int counterBlue = 0;
    int counterWhite = 0;
    int counterAll = 0;


    float avgH = 255;
    float avgS = 255;
    int redColors = 0;
    int greenColors = 0;
    int blueColors = 0;
    int pixelCount = 0;
    float avgV = 255;

    boolean blue = false;
    boolean red = false;
    boolean white = false;


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private ImageView mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pictures_preview);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context=this;

        averageValueTextView = findViewById(R.id.average_textview);
        averageHSVValueTextView = findViewById(R.id.averagehsv_textview);
        minmaxValueTextView = findViewById(R.id.minmax_textview);
        minmaxHSVValueTextView = findViewById(R.id.minmaxhsv_textview);
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        //Rotate the image
        Matrix matrix = new Matrix();
        int rotation = getIntent().getIntExtra("rotation", 0);
        matrix.postRotate(90-rotation);
        path = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + getIntent().getStringExtra("fileName") + ".jpg";

        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth(), myBitmap.getHeight(), true);
            final Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            mContentView.setImageBitmap(rotatedBitmap);



            //Analyse image
            mControlsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    counter++;





                        /*
                        averageValueTextView.setText("R : "+  avgR +" ; G : "+ avgG +" ; B : "+ avgB );




                        averageHSVValueTextView.setText("H : "+
                                (int)avgH +" ; S : "+ Math.round(avgS*100) +" ; V : "+Math.round(avgV*100));

                        averageValueTextView.setBackgroundColor(Color.rgb(255,255,255));
                        averageHSVValueTextView.setBackgroundColor(Color.rgb(255,255, 255));
                        minmaxValueTextView.setBackgroundColor(Color.rgb(255,255, 255));


                        minmaxHSVValueTextView.setText("H : "+(int)minH +" - "+ (int)maxH+" S : "+ Math.round(minS*100) +" - "+
                                Math.round(maxS*100)+" V : "+ Math.round(minV*100) +" - "+  Math.round(maxV*100));
                        minmaxHSVValueTextView.setBackgroundColor(Color.rgb(255,255, 255));
                        */

                       mContentView.setImageBitmap(test( rotatedBitmap, 1-0.4f, 1+0.8f));

                       if(blue){
                           Toast.makeText(context, "Phase bleu détectée", Toast.LENGTH_LONG).show();
                       }
                       else if (white){
                           Toast.makeText(context, "Phase blanche détectée", Toast.LENGTH_LONG).show();
                       }
                       else{
                           Toast.makeText(context, "Aucune phase détectée", Toast.LENGTH_LONG).show();
                       }


                }
            });
        }


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }




    /**
     * Get all the pixels of the image and perform a color analysis on each of these pixels
     */
    public Bitmap test(Bitmap someBitmap, float margeMin, float margeMax){
        Bitmap bitmap = someBitmap; //picture tested


        for (int y = 0; y < bitmap.getHeight(); y++)
        {
            for (int x = 0; x < bitmap.getWidth(); x++)
            {
                int pixel = bitmap.getPixel(x, y);
                //Extract RGB value of pixel
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                int A = Color.alpha(pixel);
                //Convert RGB to HSV
                float[] hsv = new float[3];
                Color.RGBToHSV(R, G, B, hsv);
                //Convert RGB to YCbCr
                int Y = (int)(0.299*R+0.587*G+0.114*B);
                int Cb=(int)(128-0.1687*R-0.3313*G+0.500*B);
                int Cr =(int)(128+0.500*R-0.418*G-0.0813*B);


                if((R > 95 && G > 40 && B > 20 && R > G && R > B &&
                    Math.abs(R-G)>15 && A > 15 && Cr > 135&&
                    Cb > 85 && Y > 80 &&
                    Cr <= (1.5862*Cb)+20 &&
                    Cr >=((0.3448*Cb)+76.2069)&&
                    Cr>= ((-4.5652*Cb)+234.5652)&&
                    Cr <= ((-1.15*Cb)+301.75) &&
                    Cr <= ((-2.2857*Cb)+432.85))||(

                    (hsv[0]>=minH*margeMin && hsv[0] <= avgH*margeMax && //Hue between 0 & 50
                    hsv[1] >=minS*margeMin && hsv[1] <=avgS*margeMax && //Saturation between 0.23 & 0.68
                    R > minR*margeMin && G > minG*margeMin && B > minB*margeMin && //Red > 95 Green > 40 Blue > 20
                    R > G && R > B && Math.abs(R-G) > 13 && A > 15)))
                {
                    counterAll++;
                    if(hsv[0]>260){
                        counterBlue++;
                    }
                    if(R>120){
                        counterRed++;
                    }
                    if(B>200&&R>150&&G>120){
                        counterWhite++;
                    }




                   // bitmap.setPixel(x, y, Color.argb(120, 255,  255, 255));
                }
                else{
                    bitmap.setPixel(x, y, Color.argb(120, 0,  0, 0));
                }

            }
            int threshold = 0;
            threshold = counterAll/10;


            if(counterBlue>=threshold)
            {
                blue=true;
            }
            else if(counterWhite>=threshold){
                white=true;
            }
           else{
                blue=false;
                white=false;
                red=false;
            }

        }

        return bitmap;
    }


    /**
     * Get all the pixels of the image and perform a color analysis on each of these pixels
     */
    public void detectPhase(int[][] arrayOfPixel, float margeMin, float margeMax){

        int counterWhitePhase;
        int counterBluePhase = 0;
        int counterRedPhase;


        for (int y = 0; y < arrayOfPixel.length; y++)
        {
            for (int x = 0; x < arrayOfPixel.length; x++)
            {
                //Extract RGB value of pixel
                int R = Color.red(arrayOfPixel[x][y]);
                int G = Color.green(arrayOfPixel[x][y]);
                int B = Color.blue(arrayOfPixel[x][y]);
                int A = Color.alpha(arrayOfPixel[x][y]);
                //Convert RGB to HSV
                float[] hsv = new float[3];
                Color.RGBToHSV(R, G, B, hsv);
                //Convert RGB to YCbCr
                int Y = (int)(0.299*R+0.587*G+0.114*B);
                int Cb=(int)(128-0.1687*R-0.3313*G+0.500*B);
                int Cr =(int)(128+0.500*R-0.418*G-0.0813*B);


                if((R > 121 && G > 54 && B > 31 && R > G && R > B &&
                        Math.abs(R-G)>15 && A > 15 && Cr > 135&&
                        Cb > 121 && Y > 90 &&
                        Cr <= (1.5862*Cb)+20 &&
                        Cr >=((0.3448*Cb)+76.2069)&&
                        Cr>= ((-4.5652*Cb)+234.5652)&&
                        Cr <= ((-1.15*Cb)+301.75) &&
                        Cr <= ((-2.2857*Cb)+432.85))||(

                        (hsv[0]>=minH*margeMin && hsv[0] <= avgH*margeMax && //Hue between 0 & 50
                                hsv[1] >=minS*margeMin && hsv[1] <=avgS*margeMax && //Saturation between 0.23 & 0.68
                                R > minR*margeMin && G > minG*margeMin && B > minB*margeMin && //Red > 95 Green > 40 Blue > 20
                                R > G && R > B && Math.abs(R-G) > 13 && A > 15)))
                {

                    if(counterBluePhase>=arrayOfPixel.length*30)
                        Toast.makeText(context, "Phase bleu détectée", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Aucune phase détectée", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }


    /**
     * @param marge the influence of the skin sampled in the algo
     * @param someBitmap state of the application
     * @return an instance of of {@link DatePickerDialog}
     */
    public void getAverageValueFromCenter(Bitmap someBitmap, int marge){
        Bitmap bitmap = someBitmap; //assign your bitmap here



        int centerX = bitmap.getWidth()/2;
        int centerY = (bitmap.getHeight()/2)+400;




        for (int y = 0; y < bitmap.getHeight(); y++)
        {
            for (int x = 0; x < bitmap.getWidth(); x++)
            {
                //Draw rectangle marge*2 of width and height in the center of the bitmap
                if(x+marge > centerX && x-marge<= centerX){
                    if(y+marge > centerY && y-marge<= centerY){

                        int c = bitmap.getPixel(x, y);
                        pixelCount++;
                        redColors += Color.red(c);
                        greenColors += Color.green(c);
                        blueColors += Color.blue(c);

                        //test max RGB
                        if(Color.red(c)>maxR)
                            maxR=Color.red(c);
                        if(Color.green(c)>maxG)
                            maxG=Color.green(c);
                        if(Color.blue(c)>maxB)
                            maxB=Color.blue(c);

                        //test min RGB
                        if(Color.red(c)<minR)
                            minR=Color.red(c);
                        if(Color.green(c)<minG)
                            minG=Color.green(c);
                        if(Color.blue(c)<minB)
                            minB=Color.blue(c);

                        float[] hsvMax = new float[3];
                        Color.RGBToHSV(Color.red(c), Color.green(c), Color.blue(c), hsvMax);

                        //test max HSV
                        if((int)hsvMax[0]> maxH)
                            maxH=(int)hsvMax[0];
                        if(hsvMax[1]> maxS)
                            maxS=hsvMax[1];
                        if(hsvMax[2]> maxV)
                            maxV=hsvMax[2];

                        float[] hsvMin = new float[3];
                        Color.RGBToHSV(Color.red(c), Color.green(c), Color.blue(c), hsvMin);
                        //test min HSV
                        if((int)hsvMin[0]< minH)
                            minH=(int)hsvMin[0];
                        if(hsvMin[1]< minS)
                            minS=hsvMin[1];
                        if(hsvMin[2]< minV)
                            minV=hsvMin[2];


                    }
                }
            }
        }
        // calculate average of bitmap r,g,b values
         avgR = (redColors/pixelCount);
         avgG = (greenColors/pixelCount);
         avgB = (blueColors/pixelCount);

        //int[] rgb = {red, green, blue};
        float[] hsv = new float[3];
        Color.RGBToHSV(avgR, avgG, avgB, hsv);
        avgH = hsv[0];
        avgS = hsv[1];
        avgV = hsv[2];

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return false;
    }

}
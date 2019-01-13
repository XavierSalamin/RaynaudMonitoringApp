package fr.hes.raynaudmonitoring;




import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static fr.hes.raynaudmonitoring.Constants.*;


public class PicturesFragment extends Fragment {

    private Button updateStartButton;

    private Button updateEndButton;
    private Button thermoButton;
    private Button thermoButtonEnd;


    private ImageView startImageViewThermo;
    private ImageView endImageViewThermo;
    private ImageView startImageView;
    private ImageView endImageView;

    private LinearLayout startPictureLayout;
    private LinearLayout startPictureTakenLayout;
    private LinearLayout endPictureLayout;
    private LinearLayout endPictureTakenLayout;
    private TextView startTextView;
    private TextView painTextView;


    private TextView endTextView;

    private SeekBar seekBarCrisis;

    static String id;
    static int position;

    private boolean isModified;

    public String startFileName, endFileName, startText, endText, thermStartName, thermEndName;
    public int pain;

    //Date members;
    private LinearLayout leftDateButton;
    private LinearLayout rightDateButton;
    public static TextView actualDateTextView;
    int dateCounter;
    static Date dateSelected; //The date returned by the Date Picker element

    public static PicturesFragment newInstance() {
        PicturesFragment fragment = new PicturesFragment();
        return fragment;
    }

    public static Date getDateSelected() {
        return dateSelected;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pictures, container, false);

        startImageView = rootView.findViewById(R.id.start_picture_imageview);
        startImageViewThermo = rootView.findViewById(R.id.start_picture_imageview_thermo);
        endImageView = rootView.findViewById(R.id.end_picture_imageview);
        endImageViewThermo = rootView.findViewById(R.id.end_picture_imageview_thermo);

        startPictureTakenLayout = rootView.findViewById(R.id.start_picture_taken_linearlayout);
        //We hide this layout before the user take a picture
        startPictureTakenLayout.setVisibility(View.GONE);

        endPictureTakenLayout = rootView.findViewById(R.id.end_picture_taken_linearlayout);
        endPictureTakenLayout.setVisibility(View.GONE);

        startTextView = rootView.findViewById(R.id.start_textView);
        endTextView = rootView.findViewById(R.id.end_textView);
        painTextView = rootView.findViewById(R.id.pain_textview);


        seekBarCrisis = rootView.findViewById(R.id.seekBar_crisis);

        thermoButton = rootView.findViewById(R.id.thermal_start_button);
        thermoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ThermoActivity.class);
                intent.putExtra("isStart", true);
                startActivityForResult(intent, FLIR_REQUEST_START);
            }
        });

        thermoButtonEnd = rootView.findViewById(R.id.thermal_end_button);
        thermoButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ThermoActivity.class);
                intent.putExtra("isStart", false);
                startActivityForResult(intent, FLIR_REQUEST_START);
            }
        });

        startPictureLayout = rootView.findViewById(R.id.start_picture_linearlayout);
        endPictureLayout = rootView.findViewById(R.id.end_picture_linearlayout);
        dateSelected=MainFragment.getDateSelected();

        datePickerFactory(rootView);
        endImageView.setRotation(90);
        startImageView.setRotation(90);
        setHasOptionsMenu(true);

        pain=5;
        isModified = false;

        seekBarCrisis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pain = progress;
                painTextView.setText("Douleur : "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setupModify();

        //Start Pictures Preview Activity when we click on the ImageView
        startImageViewThermo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ThermoPreviewActivity.class);
                intent.putExtra("fileName", thermStartName);
                intent.putExtra("rotation", 90);
                id = getActivity().getIntent().getStringExtra("id");
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });


        //Start Pictures Preview Activity when we click on the ImageView
        endImageViewThermo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PicturesPreviewActivity.class);
                intent.putExtra("fileName", thermEndName);
                intent.putExtra("rotation", 90);
                id = getActivity().getIntent().getStringExtra("id");
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        //Start Pictures Preview Activity when we click on the ImageView
        startImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PicturesPreviewActivity.class);
                intent.putExtra("fileName", startFileName);
                id = getActivity().getIntent().getStringExtra("id");
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        //Start Pictures Preview Activity when we click on the ImageView
        endImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PicturesPreviewActivity.class);
                intent.putExtra("fileName", endFileName);
                startActivity(intent);
            }
        });

        //Start Camera OnClick

        startPictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CamTestActivity.class);
                intent.putExtra("isStart", true);
                startActivityForResult( intent,CAM_REQUEST_START);
            }
        });

        //Start Camera OnClick

        endPictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CamTestActivity.class);
                intent.putExtra("isStart", false);
                startActivityForResult( intent,CAM_REQUEST_START);
            }
        });

        updateStartButton = rootView.findViewById(R.id.update_pictures_start_button);
        updateStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CamTestActivity.class);
                intent.putExtra("isStart", true);
                intent.putExtra("isUpdate", true);
                intent.putExtra("id", id);
                startActivityForResult( intent,CAM_REQUEST_UPDATE);
            }
        });
        updateEndButton = rootView.findViewById(R.id.update_pictures_end_button);
        updateEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CamTestActivity.class);
                intent.putExtra("isStart", false);
                intent.putExtra("isUpdate", true);
                intent.putExtra("id", id);
                startActivityForResult( intent,CAM_REQUEST_UPDATE);
            }
        });
        return rootView;


    }

    private void setupModify() {
        Intent myIntent = getActivity().getIntent(); // gets the previously created intent
        String newStartName, newEndName, newStartText, newEndText, newThermStartName, newThermEndName;
        int newPain;

        newStartName = myIntent.getStringExtra("startName");
        newEndName = myIntent.getStringExtra("endName");
        newStartText = myIntent.getStringExtra("startText");
        newEndText = myIntent.getStringExtra("endText");

        newThermStartName = myIntent.getStringExtra("thermStartName");
        newThermEndName = myIntent.getStringExtra("thermEndName");

        boolean newIsModified = myIntent.getBooleanExtra("isModified", false);
        position = myIntent.getIntExtra("position", 0);
        newPain = myIntent.getIntExtra("pain", 5);
        id = myIntent.getStringExtra("id");


        if(newIsModified){
            setPicture(newStartName, startImageView);
            setPicture(newEndName, endImageView);
            setPicture(newThermStartName, startImageViewThermo);
            setPicture(newThermEndName, endImageViewThermo);
            isModified=newIsModified;

            ((AddCrisisActivity)getActivity()).setActionBarTitle(R.string.title_activity_modify_crisis);
            startText = newStartText;
            endText = newEndText;
            thermStartName = newThermStartName;
            thermEndName = newThermEndName;

            pain = newPain;
            startFileName = newStartName;
            endFileName = newEndName;
            seekBarCrisis.setProgress(pain);


            dateSelected=MainFragment.dateSelected;

            startTextView.setText(newStartText);
            endTextView.setText(newEndText);
            DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);

                startPictureTakenLayout.setVisibility(View.VISIBLE);
                startPictureLayout.setVisibility(View.GONE);



            if(endFileName!=null){
                endPictureTakenLayout.setVisibility(View.VISIBLE);
                endPictureLayout.setVisibility(View.GONE);

            }


        }

    }

    public void setPicture(String fileName, ImageView mImageView){
        String path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/"+fileName+".jpg";

        File imgFile = new File(path);
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mImageView.setImageBitmap(myBitmap);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CAM_REQUEST_START) && (resultCode == CAM_REQUEST_START)) {


           if(data.getStringExtra("startName")!=null){

               startFileName = data.getStringExtra("startName");
               startPictureTakenLayout.setVisibility(View.VISIBLE);
               startPictureLayout.setVisibility(View.GONE);
               setPicture(startFileName, startImageView);

               Calendar cal = Calendar.getInstance();
               SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
               String formattedDate = sdf.format(cal.getTime());
               startText =  formattedDate;
               startTextView.setText(formattedDate);


           }

            if(data.getStringExtra("endName")!=null){
               endFileName = data.getStringExtra("endName");
                endPictureTakenLayout.setVisibility(View.VISIBLE);
                endPictureLayout.setVisibility(View.GONE);


                setPicture(endFileName, endImageView);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String formattedDate = sdf.format(cal.getTime());
                endText = formattedDate;
                endTextView.setText(formattedDate);

            }

        }
        if ((requestCode == CAM_REQUEST_UPDATE) && (resultCode == CAM_REQUEST_UPDATE)) {

            boolean isStart = data.getBooleanExtra("isStart", false);

            if(isStart) {
                startFileName = data.getStringExtra("startName");
                setPicture(startFileName, startImageView);
            }
            else{
                endFileName = data.getStringExtra("endName");
                setPicture(endFileName, endImageView);
            }

        }
        //Thermal Activity
        if ((requestCode == FLIR_REQUEST_START ) && (resultCode == FLIR_REQUEST_START)) {

            boolean isStart = data.getBooleanExtra("isStart", false);

            if(isStart) {
                thermStartName = data.getStringExtra("thermStartName");
                setPicture(thermStartName, startImageViewThermo);
            }
            else{
                thermEndName = data.getStringExtra("thermEndName");
                setPicture(thermEndName, endImageViewThermo);
            }
        }

    }

    private void datePickerFactory(View rootView) {

        //Date Picker and Date Manager Section
        leftDateButton = rootView.findViewById(R.id.date_left_button);
        rightDateButton = rootView.findViewById(R.id.date_right_button);
        actualDateTextView = rootView.findViewById(R.id.actual_date_textview);

        final Calendar calendar = Calendar.getInstance();

        dateSelected = MainFragment.getDateSelected();
        DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);

        dateCounter = calendar.get(Calendar.DAY_OF_MONTH);








        actualDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DateManager();
                dialog.show(getActivity().getFragmentManager(), "dialog");

            }
        });


        DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);
        leftDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We substract one day
                calendar.setTime(dateSelected);
                calendar.add(Calendar.DATE, -1);

                dateSelected =calendar.getTime();
                AddTreatmentActivity.setDateSelected(dateSelected); //avoir null pointer exception

                DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);


            }
        });


        rightDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We add one day
                calendar.setTime(dateSelected);
                calendar.add(Calendar.DATE, +1);

                dateSelected =calendar.getTime();

                DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);

            }
        });


    }

    /**
     * This static method is called from the {@link DateManager}
     * update the selected date from the DatePickerDialog and set the TextView
     * @param date date selected from the DatePickerDialog of the {@link DateManager}
     */
    public static void OnDateManagerResult(Date date){
        dateSelected = date;
        if(actualDateTextView==null){

        }
        else
            DateManager.setTextViewFormattedDate(date, actualDateTextView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check:
                //If it's a modification of the treatment
                if(isModified){
                    Crisis crisis = new Crisis(startFileName, endFileName, dateSelected, startText, endText, pain, thermStartName, thermEndName);
                    crisis.setNotes(false);
                    MainFragment.updateCrisis(crisis, id);
                    getActivity().setResult(ADD_PICTURES_CRISIS_REQUEST);
                    getActivity().finish();

                    return true;
                }
                else{
                    Crisis crisis = new Crisis(startFileName, endFileName, dateSelected, startText, endText, pain, thermStartName, thermEndName);
                    crisis.setNotes(false);
                    //MainFragment.addCrisis(crisis);
                    getActivity().setResult(ADD_PICTURES_CRISIS_REQUEST);
                    getActivity().finish();
                    if(startText == null && endText == null ){
                        Toast.makeText(getContext(), "La crise n'a pas été enregistré car elle ne contenait aucune information !", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else{
                        MainFragment.addCrisis(crisis);
                    }
                    return true;
                }


            default:
                return false;
        }
    }
}

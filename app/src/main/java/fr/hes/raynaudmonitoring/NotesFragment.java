package fr.hes.raynaudmonitoring;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import static fr.hes.raynaudmonitoring.Constants.ADD_NOTES_CRISIS_REQUEST;
import static fr.hes.raynaudmonitoring.Constants.ADD_PICTURES_CRISIS_REQUEST;


/**
 * A simple {@link Fragment} subclass from the {@link AddCrisisActivity}.
 */
public class NotesFragment extends Fragment {

    private TimePicker timePickerStart;
    private TimePicker timePickerEnd;

    int hourStart;
    int hourEnd;

    int minuteStart;
    int minuteEnd;
    int position;
    String id;
    int pain;
    private SeekBar seekBarCrisis;
    private TextView painTextView;
    boolean isModified;
    //Date members;
    private LinearLayout leftDateButton;
    private LinearLayout rightDateButton;
    public static TextView actualDateTextView;
    int dateCounter;
    static Date dateSelected; //The date returned by the Date Picker element


    public static NotesFragment newInstance() {

        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    public static Date getDateSelected() {
        return dateSelected;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        timePickerStart = getView().findViewById(R.id.timePicker_start);
        timePickerStart.setIs24HourView(true);
        timePickerEnd = getView().findViewById(R.id.timePicker_end);
        timePickerEnd.setIs24HourView(true);


        timePickerStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDayChanged, int minuteChanged) {
                hourStart = hourOfDayChanged;
                minuteStart = minuteChanged;
            }
        });



        timePickerEnd.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDayChanged, int minuteChanged) {
                hourEnd = hourOfDayChanged;
                minuteEnd = minuteChanged;
            }
        });

        dateSelected=MainFragment.getDateSelected();


        datePickerFactory(getView());
        setHasOptionsMenu(true);

        isModified = false;

        Calendar cal = Calendar.getInstance();
        hourStart = cal.get(Calendar.HOUR_OF_DAY);
        hourEnd= cal.get(Calendar.HOUR_OF_DAY);
        minuteStart = cal.get(Calendar.MINUTE);
        minuteEnd= cal.get(Calendar.MINUTE);

        seekBarCrisis =  getView().findViewById(R.id.seekBar_crisis);
        painTextView = getView().findViewById(R.id.pain_textview);

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
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    /**
     * Get all the data from the Intent of the previous Activity
     */
    private void setupModify() {
        Intent myIntent = getActivity().getIntent(); // gets the previously created intent
        int newHourStart, newHourEnd, newMinuteStart, newMinuteEnd;
        int newPain;

        newHourStart = myIntent.getIntExtra("hourStart", 0 );

        newHourEnd = myIntent.getIntExtra("hourEnd", 0 );

        newMinuteStart = myIntent.getIntExtra("minuteStart", 0 );

        newMinuteEnd = myIntent.getIntExtra("minuteEnd", 0 );


        boolean newIsModified = myIntent.getBooleanExtra("isModified", false);
        position = myIntent.getIntExtra("position", 0);
        newPain = myIntent.getIntExtra("pain", 5);
        id = myIntent.getStringExtra("id");


        if(newIsModified){

            isModified=newIsModified;
            ((AddCrisisActivity)getActivity()).setActionBarTitle(R.string.title_activity_modify_crisis);

            pain = newPain;

            minuteEnd = newMinuteEnd;
            minuteStart= newMinuteStart;

            hourStart = newHourStart;
            hourEnd = newHourEnd;

            seekBarCrisis.setProgress(pain);


            dateSelected=MainFragment.dateSelected;

            timePickerStart.setMinute(newMinuteStart);
            timePickerStart.setHour(newHourStart);
            timePickerEnd.setHour(newHourEnd);
            timePickerEnd.setMinute(newMinuteEnd);


            DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);




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
                    String startText=hourStart+":"+minuteStart;
                    String endText=hourEnd+":"+minuteEnd;
                    Crisis crisis = new Crisis(dateSelected, startText, endText, pain, hourStart, minuteStart, hourEnd, minuteEnd);
                    crisis.setNotes(true);
                    MainFragment.updateCrisis(crisis, id);
                    getActivity().setResult(ADD_NOTES_CRISIS_REQUEST);
                    getActivity().finish();
                    return true;
                }
                else{
                    String startText=hourStart+":"+minuteStart;
                    String endText=hourEnd+":"+minuteEnd;
                    Crisis crisis = new Crisis(dateSelected, startText, endText, pain, hourStart, minuteStart, hourEnd, minuteEnd);
                    crisis.setNotes(true);
                    MainFragment.addCrisis(crisis);
                    getActivity().setResult(ADD_NOTES_CRISIS_REQUEST);
                    getActivity().finish();
                    return true;

                }



            default:
                return false;
        }
    }

}

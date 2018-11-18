package fr.hes.raynaudmonitoring;

import static fr.hes.raynaudmonitoring.Constants.*;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;

import java.util.Calendar;

/**
 * An Activity who add an instance of {@link Reminder} to the DataBase
 * This activity is also used to update a reminders
 */
public class AddRemindersActivity extends AppCompatActivity {

    private String title;
    //Spinner items with 3 title
    private  String[] items;

    private   ArrayAdapter<String> adapter;

    private static final int Time_id = 1;

    private TextView time;
    private int hour;
    private Spinner spinner;
    private int minute;
    private int position;
    private boolean isModified;
    private String id;
    private boolean isChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setTitle(R.string.title_activity_add_reminders);
        time =  findViewById(R.id.selecttime);

        //We set the actual time
        isModified=false;
        isChecked=false;

        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        time.setText(formattedTime(hour, minute));


        //get the spinner from the xml.
        spinner = findViewById(R.id.spinner1);

        //create a list of items for the spinner.
        items = new String[]{"Traitement", "Crise", "RCS"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                title="rien";
            }
        });

        modifySetup();

        //listReminders.add()

    }

    /**
     * Get all the data from the Intent of the previous Activity
     */
    private void modifySetup() {
        //Retrieve Data if we want to modify the activity;
        Intent myIntent = getIntent(); // gets the previously created intent
        int newHour = myIntent.getIntExtra("hour", 0);
        int newMinute= myIntent.getIntExtra("minute", 0);
        String newTitle = myIntent.getStringExtra("title");
        boolean newIsModified = myIntent.getBooleanExtra("isModified", false);
        boolean newIsChecked = myIntent.getBooleanExtra("isChecked", false);
        id = myIntent.getStringExtra("id");
        position = myIntent.getIntExtra("position", 0);



        //If we modify the treatment

        if(newIsModified){
            isModified=newIsModified;
            getSupportActionBar().setTitle(R.string.title_activity_modify_reminders);
            switch (newTitle){
                case "Traitement" :spinner.setSelection(adapter.getPosition("Traitement"));
                break;
                case "Crise" :spinner.setSelection(adapter.getPosition("Crise"));
                    break;
                case "RCS" :spinner.setSelection(adapter.getPosition("RCS"));
                    break;
            }

            time.setText(formattedTime(newHour, newMinute));
            hour = newHour;
            minute = newMinute;
            isChecked = newIsChecked;

        }

    }
    /**
     * Format the time correctly.
     * Adds a 0 to minutes and hours when they are less than 10
     * @return String , for example (04:32)
     */
    public String formattedTime(int hour, int minute) {
        String s; //Returned String
        String h;
        String m;

        //Add 0 digit behind the hour
        if(hour<10)
            h="0";
        else
            h="";

        //Same for the minutes
        if(minute<10)
            m="0";
        else
            m="";
        s = h+hour+":"+m+minute;
        return s;
    }

    /**
     * Adds a close button
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check, menu);

        return true;
    }
    /**
     * Return true if the date passed as parameters is the date from Tomorrow
     * @param item for now the only item available will be action_check
     * @return true when an item is clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check:
                if(isModified){

                    Reminder r = new Reminder(title, hour, minute);
                    r.setChecked(true);
                    switch (title){
                        case "Traitement" :
                            try {
                                if(MainActivity.treatmentRegistredToday()){
                                    MainActivity.setAlarmTreatment(hour, minute, false);
                                }
                                else{
                                    MainActivity.setAlarmTreatment(hour, minute, true);
                                }
                            } catch (CouchbaseLiteException e) {
                                e.printStackTrace();
                            }


                            break;
                        case "Crise" :
                            try {
                                if(MainActivity.crisisRegistredToday()){
                                    MainActivity.setAlarmCrisis(hour, minute, false);
                                }
                                else{
                                    MainActivity.setAlarmCrisis(hour, minute, true);
                                }
                            } catch (CouchbaseLiteException e) {
                                e.printStackTrace();
                            }


                            break;
                        case "RCS" :

                            try {
                                if(MainActivity.rcsRegistredToday()){
                                    MainActivity.setAlarmRcs(hour, minute, false);
                                }
                                else{
                                    MainActivity.setAlarmRcs(hour, minute, true);
                                }
                            } catch (CouchbaseLiteException e) {
                                e.printStackTrace();
                            }

                            break;
                    }
                    RemindersFragment.updateReminders(r, id);

                    setResult(REMINDERS_REQUEST_UPDATE);
                    Toast.makeText(this, "Rappel modifié avec succès !",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                }
                else {
                    Reminder r = new Reminder(title, hour, minute);
                    r.setChecked(true);
                    switch (title){
                        case "Traitement" :
                            try {
                                if(MainActivity.treatmentRegistredToday()){
                                    MainActivity.setAlarmTreatment(hour, minute, false);
                                }
                                else{
                                    MainActivity.setAlarmTreatment(hour, minute, true);
                                }
                            } catch (CouchbaseLiteException e) {
                                e.printStackTrace();
                            }


                            break;
                        case "Crise" :
                            try {
                                if(MainActivity.crisisRegistredToday()){
                                    MainActivity.setAlarmCrisis(hour, minute, false);
                                }
                                else{
                                    MainActivity.setAlarmCrisis(hour, minute, true);
                                }
                            } catch (CouchbaseLiteException e) {
                                e.printStackTrace();
                            }


                            break;
                        case "RCS" :

                            try {
                                if(MainActivity.rcsRegistredToday()){
                                    MainActivity.setAlarmRcs(hour, minute, false);
                                }
                                else{
                                    MainActivity.setAlarmRcs(hour, minute, true);
                                }
                            } catch (CouchbaseLiteException e) {
                                e.printStackTrace();
                            }

                            break;
                    }
                    RemindersFragment.addReminders(r);
                    setResult(REMINDERS_REQUEST_START);
                    finish();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onClickTime(View v) {
        // Show time dialog
        showDialog(Time_id);
    }



    protected Dialog onCreateDialog(int id) {

    // Get the calendar
    Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    switch (id) {
        case Time_id:
        // Open the timepicker dialog
        return new TimePickerDialog(AddRemindersActivity.this,  TimePickerDialog.THEME_HOLO_LIGHT,  time_listener, hour,
        minute, true);
    }
    return null;
    }

    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

    @Override
    public void onTimeSet(TimePicker view, int h, int m) {
        // store the data in one string and set it to text
        time.setText(formattedTime(h, m));
        hour = h;
        minute = m;
    }
    };
}

package fr.hes.raynaudmonitoring;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static fr.hes.raynaudmonitoring.Constants.ADD_TREATMENT_REQUEST;
/**
 * An Activity who add an instance of {@link Treatment} to the DataBase
 * This activity is also used to update a treatment
 */
public class AddTreatmentActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText editDescription;
    private boolean isModified;
    private int hour;
    private int minute;
    private String description;
    private int position; //Position of the treatment in the listView
    private boolean sideEffects;
    private RadioGroup radioGroup;
    private boolean isTextChanged; //If the user as edited the side effects textfield
    private CardView descriptionLayout;
    public static TextView actualDateTextView;
    public static Date dateSelected; //The date returned by the Date Picker element
    private String id; //The id of the treatment we want to modify


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treatment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setTitle(R.string.title_activity_add_treatment);
        timePicker = findViewById(R.id.timePicker_treatment);
        timePicker.setIs24HourView(true);

        isTextChanged = false; //false by default
        isModified=false;

        editDescription = findViewById(R.id.description_edit);
        descriptionLayout = findViewById(R.id.add_treatment_description_layout);
        hour = timePicker.getCurrentHour();

        dateSelected=MainFragment.getDateSelected();


        //Date
        datePickerFactory();

        descriptionLayout.setVisibility(CardView.GONE);
        minute = timePicker.getCurrentMinute();
        description = editDescription.getText().toString();
        radioGroup = findViewById(R.id.radio_treatment);

        DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);
        modifySetup();


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDayChanged, int minuteChanged) {
                hour = hourOfDayChanged;
                minute = minuteChanged;
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case 0: // No Button
                        descriptionLayout.setVisibility(CardView.GONE);
                        sideEffects = false;
                      break;
                    case 1: // Yes Button

                        descriptionLayout.setVisibility(CardView.VISIBLE);
                        sideEffects = true;
                      break;
                }
            }
        });


        editDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isTextChanged=true;
                description = s.toString();
            }
        });



    }

    /**
     * Retrieve data from the {@link MainFragment} of the selected Treatment from the ListView
     * Update the title of the activity, and set all the element of the AddTreatmentActivity
     * from the data retrieved
     * RequiresApi annotation is for the setHour and setMinute method of the timePicker components.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void modifySetup() {
        //Retrieve Data if we want to modify the activity;
        Intent myIntent = getIntent(); // gets the previously created intent
        int newHour = myIntent.getIntExtra("hour", 0);
        int newMinute= myIntent.getIntExtra("minute", 0);
        String newDescription = myIntent.getStringExtra("description");
        boolean newIsModified = myIntent.getBooleanExtra("isModified", false);
        boolean newSideEffects = myIntent.getBooleanExtra("sideEffects", false);
        position = myIntent.getIntExtra("position", 0);
        id = myIntent.getStringExtra("id");


        // End Retrieving data

        //If we modify the treatment

        if(newIsModified){
            isModified=newIsModified;
            getSupportActionBar().setTitle(R.string.title_activity_modify_treatment);
            timePicker.setHour(newHour);
            timePicker.setMinute(newMinute);
            hour = newHour;
            minute = newMinute;
            dateSelected=MainFragment.dateSelected;
            DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);
            if(newSideEffects){
                radioGroup.check(R.id.radio_treatment_yes);//We check the "Yes" item;
                descriptionLayout.setVisibility(CardView.VISIBLE);
                description=newDescription;
                editDescription.setText(newDescription);
                isTextChanged=true;
                sideEffects=true;
            }
        }




    }

    /**
     * Initialize the different button of the Date Picker component
     * and set three listener. Two for the left and right arrow,
     * one for the TextView.
     *
     */
    private void datePickerFactory() {

        //Date Picker and Date Manager Section
        /*
      Date Members
     */
        LinearLayout leftDateButton = findViewById(R.id.date_left_button);
        LinearLayout rightDateButton = findViewById(R.id.date_right_button);
        actualDateTextView = findViewById(R.id.actual_date_textview);



        final Calendar calendar = Calendar.getInstance();

        dateSelected = MainFragment.getDateSelected();
        DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);

        actualDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DateManager();
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        leftDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We substract one day
                calendar.setTime(dateSelected);
                calendar.add(Calendar.DATE, -1);
                dateSelected =calendar.getTime();
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
     * Add or modify Treatment or cancel the activity on option item selected by the user
     * @param item the item selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check:
                //If it's a modification of the treatment
                if(isModified){
                    //If the users say he had side effects but didn't complete de description textfield
                    if(sideEffects && isTextChanged==false){
                        Toast.makeText(getApplicationContext(), "Veuillez remplir la description" , Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(!sideEffects)
                            description="Aucun effet secondaire";


                        Treatment t = new Treatment(hour, minute, description, sideEffects, dateSelected);
                        MainFragment.updateTreatment(t, id);
                        DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);

                        setResult(ADD_TREATMENT_REQUEST);
                        Toast.makeText(this, "Traitement modifié avec succès !",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    }
                }
                //Add Treatment Activity
                else{
                    if(sideEffects && isTextChanged==false){
                        Toast.makeText(getApplicationContext(), "Veuillez remplir la description" , Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(!sideEffects)
                            description="Aucun effet secondaire";

                        Treatment t = new Treatment(hour, minute, description, sideEffects,dateSelected);
                        MainFragment.addTreatment(t);
                        DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);
                        setResult(ADD_TREATMENT_REQUEST);
                        Toast.makeText(this, "Traitement ajouté avec succès !",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    }
                }


            default:
                return super.onOptionsItemSelected(item);
        }
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

    public static Date getDateSelected() {
        return dateSelected;
    }

    public static void setDateSelected(Date dateSelected) {
        AddTreatmentActivity.dateSelected = dateSelected;
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        private static final String CHANNEL_ID = "fr.hes.raynaudmonitoring.channelId";

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent notificationIntent = new Intent(context, MainFragment.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainFragment.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Builder builder = new Notification.Builder(context);

            Notification notification = builder.setContentTitle("Demo App Notification")
                    .setContentText("New Notification From Demo App..")
                    .setTicker("New Message Alert!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent).build();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID);
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "NotificationDemo",
                        IMPORTANCE_DEFAULT
                );
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notification);
        }
    }
}

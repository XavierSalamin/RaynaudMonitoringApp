package fr.hes.raynaudmonitoring;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.BasicAuthenticator;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Endpoint;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Replicator;
import com.couchbase.lite.ReplicatorChange;
import com.couchbase.lite.ReplicatorChangeListener;
import com.couchbase.lite.ReplicatorConfiguration;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.couchbase.lite.URLEndpoint;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static fr.hes.raynaudmonitoring.DatabaseManager.database;

/**
 * The main activity that contains all the fragments
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //FOR FRAGMENTS
    private Fragment fragmentThermo;
    private Fragment fragmentReminders;
    private Fragment fragmentMain;
    private Fragment fragmentSettings;
    static AlarmManager alarmManagerR;
    static AlarmManager alarmManagerC;
    static AlarmManager alarmManagerT;

    static PendingIntent broadcastR;
    static PendingIntent broadcastC;
    static PendingIntent broadcastT;

    String numberPatient;
    String phase;

    static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Notification
        alarmManagerR = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManagerC = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManagerT = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        ctx=this;



        //End Notification

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//Singleton
        try {
            DatabaseManager database = new DatabaseManager(getApplicationContext());
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        try {

            startSync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            retrieveUserData();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }



        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        View headerView = navigationView.getHeaderView(0);
         TextView navNumberPatient =  headerView.findViewById(R.id.number_patient);
         TextView navNumberPhase =  headerView.findViewById(R.id.number_phase);
         if(phase!=null)
        navNumberPhase.setText("Phase de traitement "+phase);
         if(numberPatient!=null)
        navNumberPatient.setText("Patient numéro "+numberPatient);

        // Show First Fragment
        showMainFragment();

        try {
            refreshReminders();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }



    }

    /**
     * Static method to create an Alarm at a specified time
     * @param hour hour when the alarm will start
     * @param minute minute when the alarm will start
     * @param activate if it's activate or not
     */
    public static void setAlarmRcs(int hour, int minute, boolean activate){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 00);


        Intent notificationIntent = new Intent(ctx, AlarmReceiverRcs.class);
        broadcastR = PendingIntent.getBroadcast(ctx, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(activate)
            alarmManagerR.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcastR);
        else
            alarmManagerR.cancel(broadcastR);
    }


    public static void setAlarmCrisis(int hour, int minute, boolean activate){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 00);


        Intent notificationIntent = new Intent(ctx, AlarmReceiverCrisis.class);
        broadcastC = PendingIntent.getBroadcast(ctx, 101, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(activate)
            alarmManagerC.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcastC);
        else
            alarmManagerC.cancel(broadcastC);
    }
    public static void setAlarmTreatment(int hour, int minute, boolean activate){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 00);


        Intent notificationIntent = new Intent(ctx, AlarmReceiverTreatment.class);
        broadcastT = PendingIntent.getBroadcast(ctx, 102, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(activate)
            alarmManagerT.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcastT);
        else
            alarmManagerT.cancel(broadcastT);
    }
    public void retrieveUserData()throws CouchbaseLiteException {
        //We get all the reminders from the database
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("userData")));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");


            phase = all.getString("phase");
            numberPatient = all.getString("patient");
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            this.showMainFragment();
            // Handle the camera action
        } else if (id == R.id.nav_thermo) {
            this.showThermoFragment();

        } else if (id == R.id.nav_reminders) {
            this.showRemindersFragment();

        } else if (id == R.id.nav_settings) {
            this.showSettingsFragment();
        }



        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showMainFragment(){
        if (this.fragmentMain == null) this.fragmentMain = MainFragment.newInstance();
        this.startTransactionFragment(this.fragmentMain);
        getSupportActionBar().setTitle(R.string.title_main_fragment);

    }
    private void showSettingsFragment(){
        if (this.fragmentSettings == null) this.fragmentSettings = Settings.newInstance();
        this.startTransactionFragment(this.fragmentSettings);
        getSupportActionBar().setTitle("Paramètres");

    }
    private void showThermoFragment(){
        if (this.fragmentThermo == null) this.fragmentThermo = ThermoFragment.newInstance();
        this.startTransactionFragment(this.fragmentThermo);
        getSupportActionBar().setTitle(R.string.title_thermo_fragment);
    }

    private void showRemindersFragment(){
        if (this.fragmentReminders == null) this.fragmentReminders = RemindersFragment.newInstance();
        this.startTransactionFragment(this.fragmentReminders);
        getSupportActionBar().setTitle(R.string.title_reminders_fragment);
    }
    // Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, fragment).commit();
        }
    }





    //start bi-directional syncing
    protected void startSync() {


    }


    public void refreshReminders() throws CouchbaseLiteException {
        RemindersFragment.listReminders = new ArrayList<>();
        RemindersFragment.adapter =  new RemindersAdapter(this, R.layout.reminderslist,      RemindersFragment.listReminders);
        RemindersFragment.retrieveDataFromDatabase();

        ArrayList<Reminder> l = RemindersFragment.listReminders;


        for(int position = 0; position < l.size(); position++){
            switch (l.get(position).getTitle()){
                case "Traitement" :
                    if(treatmentRegistredToday()){
                        MainActivity.setAlarmTreatment(l.get(position).getHour(), l.get(position).getMinute(), false);
                    }
                    else{
                        MainActivity.setAlarmTreatment(l.get(position).getHour(), l.get(position).getMinute(), true);
                    }
                    break;
                case "Crise" :
                    if(crisisRegistredToday()){
                        MainActivity.setAlarmCrisis(l.get(position).getHour(), l.get(position).getMinute(), false);
                    }
                    else{
                        MainActivity.setAlarmCrisis(l.get(position).getHour(), l.get(position).getMinute(), true);
                    }

                    break;
                case "RCS" :
                    if(rcsRegistredToday()){
                        MainActivity.setAlarmRcs(l.get(position).getHour(), l.get(position).getMinute(), false);
                    }
                    else{
                        MainActivity.setAlarmRcs(l.get(position).getHour(), l.get(position).getMinute(), true);
                    }

                    break;
            }
        }

    }

    //Say if one RCS was registred today
    public static boolean rcsRegistredToday() throws CouchbaseLiteException {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        //We get all the RCS corresponding to the selected day
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("rcs"))
                        .and(Expression.property("day").equalTo(Expression.intValue(day)))
                        .and(Expression.property("month").equalTo(Expression.intValue(month)))
                        .and(Expression.property("year").equalTo(Expression.intValue(year))));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");

            if(all.getInt("rcs")>=0){
                return true;
            }
        }
        return false;

    }


    //Say if one RCS was registred today
    public static boolean crisisRegistredToday() throws CouchbaseLiteException {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        //We get all the RCS corresponding to the selected day
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("crisis"))
                        .and(Expression.property("day").equalTo(Expression.intValue(day)))
                        .and(Expression.property("month").equalTo(Expression.intValue(month)))
                        .and(Expression.property("year").equalTo(Expression.intValue(year))));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");

            if(all.getString("startName")!=null){
                return true;
            }
        }
        return false;

    }

    //Say if one RCS was registred today
    public static boolean treatmentRegistredToday() throws CouchbaseLiteException {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        //We get all the RCS corresponding to the selected day
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("treatment"))
                        .and(Expression.property("day").equalTo(Expression.intValue(day)))
                        .and(Expression.property("month").equalTo(Expression.intValue(month)))
                        .and(Expression.property("year").equalTo(Expression.intValue(year))));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");

            if(all.getString("description")!=null){
                return true;
            }
        }
        return false;

    }
}

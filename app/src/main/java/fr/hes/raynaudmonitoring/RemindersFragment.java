package fr.hes.raynaudmonitoring;

import static fr.hes.raynaudmonitoring.Constants.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link ThermoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemindersFragment extends Fragment {

private FloatingActionButton fab;
private ListView listView;
public static ArrayList<Reminder> listReminders;
public static RemindersAdapter  adapter;




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ThermoFragment.
     */

    public static RemindersFragment newInstance() {
        RemindersFragment fragment = new RemindersFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_reminders, container, false);
        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getActivity(), AddRemindersActivity.class);
                startActivityForResult(intent, REMINDERS_REQUEST_START);



            }
        });

        listReminders = new ArrayList<>();
        adapter = new RemindersAdapter(this.getActivity(), R.layout.reminderslist, listReminders);
        listView = rootView.findViewById(R.id.reminders_listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent((getActivity()), AddRemindersActivity.class);
                modifyRemindersIntentFactory(position, intent);
                startActivityForResult(intent,REMINDERS_REQUEST_UPDATE );
            }
        });




        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteRemindersDialog(position);
                return true;
            }
        });

        try {
            retrieveDataFromDatabase();

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        // Inflate the layout for this fragment





        return rootView;

    }
    public  void removeReminders(int position) {

        listReminders.remove(position);

    }

    public static void addReminders(Reminder r) {
        try {
            DatabaseManager.addReminder(r);
            retrieveDataFromDatabase();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public static void retrieveDataFromDatabase() throws CouchbaseLiteException {

        listReminders.clear();
        adapter.notifyDataSetChanged();

        //We get all the reminders from the database
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("reminder")));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");

            //for every document discover we create a new reminder
            Reminder r = new Reminder(
            all.getString("title"),
            all.getInt("hour"),
            all.getInt("minute")
            );
            r.setChecked(all.getBoolean("isChecked"));
            //we add the reminder to the list
            listReminders.add(r);



            adapter.notifyDataSetChanged();

        }

    }
    public static void updateReminders(Reminder r, String id) {

        try {
            DatabaseManager.updateReminder(r, id);
            adapter.notifyDataSetChanged();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        try {
            retrieveDataFromDatabase();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }


    }

    private void modifyRemindersIntentFactory(int position, Intent intent) {

        Reminder r = listReminders.get(position);
        //We map each parameters
        intent.putExtra("position", position);
        intent.putExtra("title", r.getTitle());
        intent.putExtra("isModified", true);
        intent.putExtra("hour", r.getHour());
        intent.putExtra("minute", r.getMinute());
        intent.putExtra("isChecked", r.isChecked());
       intent.putExtra("id", DatabaseManager.getReminderId(r));
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REMINDERS_REQUEST_START) && (resultCode == REMINDERS_REQUEST_START)) {
            adapter.notifyDataSetChanged();
        }

    }
    public void deleteRemindersDialog(final int position){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), android.R.style.Theme_Material_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        }
        builder.setTitle("Confirmation")
                .setMessage("Êtes vous sûr de vouloir supprimer ce rappel ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Reminder r = listReminders.get(position);

                        try {
                            DatabaseManager.deleteReminder(r);






                            //Activate alarms
                            switch (listReminders.get(position).getTitle()){
                                case "Traitement" :
                                    MainActivity.setAlarmTreatment(listReminders.get(position).getHour(), listReminders.get(position).getMinute(), false);

                                    break;
                                case "Crise" :

                                    MainActivity.setAlarmCrisis(listReminders.get(position).getHour(), listReminders.get(position).getMinute(), false);

                                    break;
                                case "RCS" :
                                    MainActivity.setAlarmRcs(listReminders.get(position).getHour(), listReminders.get(position).getMinute(), false);
                                    break;
                            }
















                        } catch (CouchbaseLiteException e) {
                            e.printStackTrace();
                        }

                        removeReminders(position);

                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.ic_delete_black_24dp)
                .show();

    }
}

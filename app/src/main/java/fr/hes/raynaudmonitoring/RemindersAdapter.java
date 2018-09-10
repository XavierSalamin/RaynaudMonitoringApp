package fr.hes.raynaudmonitoring;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * an Adapter object acts as a bridge between an AdapterView and the {@link Reminder}
 * The Adapter is also responsible for making a View for each item in the data set.
 */
public class RemindersAdapter extends ArrayAdapter<Reminder>{

   private ArrayList<Reminder> l;
    private Context c;
    int r;
    TextView title;
    TextView subtitle;
    Switch sw;

    RemindersAdapter(Context context, int resource, ArrayList<Reminder> objects) {
        super(context, resource, objects);
        this.l = objects;
        this.c = context;
        this.r = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        @SuppressLint("ViewHolder") View v = inflater.inflate(r, parent, false);
        String el1 = l.get(position).getTitle();
        String el2 = l.get(position).toString();
        boolean el3 = l.get(position).isChecked();
        title = v.findViewById(R.id.reminders_title_textview);
        title.setText(el1);
        subtitle = v.findViewById(R.id.reminders_subtitle_textview);
        subtitle.setText(el2);
        sw= v.findViewById(R.id.switch1);
        sw.setChecked(el3);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    //Activate alarms
                    switch (l.get(position).getTitle()){
                        case "Traitement" :
                            MainActivity.setAlarmTreatment(l.get(position).getHour(), l.get(position).getMinute(), true);
                            Toast.makeText(getContext(), "Rappel "+l.get(position).getTitle()+" à "+l.get(position).toString()+" activé !", Toast.LENGTH_SHORT).show();
                            break;
                        case "Crise" :
                            Toast.makeText(getContext(), "Rappel "+l.get(position).getTitle()+" à "+l.get(position).toString()+" activé !", Toast.LENGTH_SHORT).show();
                            MainActivity.setAlarmCrisis(l.get(position).getHour(), l.get(position).getMinute(), true);
                            break;
                        case "RCS" :
                            Toast.makeText(getContext(), "Rappel "+l.get(position).getTitle()+" à "+l.get(position).toString()+" activé !", Toast.LENGTH_SHORT).show();
                            MainActivity.setAlarmRcs(l.get(position).getHour(), l.get(position).getMinute(), true);
                            break;
                    }

                }
                //If the switch is not checked
                else{
                    Toast.makeText(getContext(), "Rappel désactivé !", Toast.LENGTH_SHORT).show();
                    //Cancel all alarms
                    switch (l.get(position).getTitle()){
                        case "Traitement" : MainActivity.setAlarmTreatment(l.get(position).getHour(), l.get(position).getMinute(), false);
                            break;
                        case "Crise" :      MainActivity.setAlarmCrisis(l.get(position).getHour(), l.get(position).getMinute(), false);
                            break;
                        case "RCS" :         MainActivity.setAlarmRcs(l.get(position).getHour(), l.get(position).getMinute(), false);
                            break;
                    }
                }

                l.get(position).setChecked(isChecked);
                RemindersFragment.updateReminders(l.get(position), DatabaseManager.getReminderId(l.get(position)));
            }
        });
        return v;
    }


}

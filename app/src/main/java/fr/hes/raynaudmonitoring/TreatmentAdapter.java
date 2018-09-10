package fr.hes.raynaudmonitoring;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * an Adapter object acts as a bridge between an AdapterView and the {@link Treatment}
 * The Adapter is also responsible for making a View for each item in the data set.
 */
public class TreatmentAdapter extends ArrayAdapter<Treatment> {


    ArrayList<Treatment> l;
    Context c;
    int r;
    TextView title;
    TextView subtitle;

    /**
     * Constructor of the Treatment Object
     * @param context context of the adapter
     * @param resource layout of the Adapter
     * @param objects arrayList of treatment

     */
    public TreatmentAdapter(Context context, int resource, ArrayList<Treatment> objects) {
        super(context, resource, objects);
        this.l = objects;
        this.c = context;
        this.r = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View v = inflater.inflate(r, parent, false);


        String el1 = l.get(position).toString();
        String el2 = l.get(position).getDescription();
        title = v.findViewById(R.id.treatment_title_textview);
        title.setText(el1);
        subtitle = v.findViewById(R.id.treatment_subtitle_textview);
        subtitle.setText(el2);
        return v;

    }
}

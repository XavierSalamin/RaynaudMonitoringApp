package fr.hes.raynaudmonitoring.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hes.raynaudmonitoring.Crisis;
import fr.hes.raynaudmonitoring.R;
/**
 * an Adapter object acts as a bridge between an AdapterView and the {@link Crisis}
 * The Adapter is also responsible for making a View for each item in the data set.
 */
public class CrisisAdapter extends ArrayAdapter<Crisis> {


    ArrayList<Crisis> l;
    Context c;
    int r;
    TextView title;
    TextView subtitle;

    ImageView iconPictures, iconNotes;


    public CrisisAdapter(Context context, int resource, ArrayList<Crisis> objects) {
        super(context, resource, objects);
        this.l = objects;
        this.c = context;
        this.r = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View v = inflater.inflate(r, parent, false);


        String el1 = l.get(position).getStartText();
        String el2 = l.get(position).getEndText();
        title = v.findViewById(R.id.crisis_title_textview);
        title.setText("Début : "+el1);
        subtitle = v.findViewById(R.id.crisis_subtitle_textview);

        iconNotes = v.findViewById(R.id.notes_icon);
        iconPictures = v.findViewById(R.id.photo_icon);
        //Set icon if it's a notes or a pictures
        if(l.get(position).isNotes()){
            iconNotes.setVisibility(View.VISIBLE);
            iconPictures.setVisibility(View.GONE);
        }
        else{
            iconNotes.setVisibility(View.GONE);
            iconPictures.setVisibility(View.VISIBLE);
        }


        if (el2 != null)
            subtitle.setText("Fin : "+el2);
        else
            subtitle.setText("");
        return v;

    }
}

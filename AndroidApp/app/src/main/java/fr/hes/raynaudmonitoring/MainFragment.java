package fr.hes.raynaudmonitoring;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.hes.raynaudmonitoring.Adapter.CrisisAdapter;

import static fr.hes.raynaudmonitoring.Constants.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain link to {@link AddTreatmentActivity}and {@link AddCrisisActivity}
 * Centralize the data of the users
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private LinearLayout addCrisisLink;
    private LinearLayout addTreatmentLink;

    private CardView crisisCardView;
    private CardView treatmentCardView;


    //Treatment members

    private ListView listViewTreatment;
    private static ArrayList<Treatment> listTreatment;
    private static TreatmentAdapter adapterTreatment;


    //Crisis members

    private ListView listViewCrisis;
    private static ArrayList<Crisis> listCrisis;
    private static CrisisAdapter adapterCrisis;


    //Rcs members
    private static SeekBar seekbarRcs;
    private static EditText editTextCommentary;
    private int rcs;
    private String commentary;
    private TextView rcsTextView;

    //Date members;
    private LinearLayout leftDateButton;
    private LinearLayout rightDateButton;
    public static TextView actualDateTextView;
    int dateCounter;
    boolean isRcsUpdated;
    static Date dateSelected; //The date returned by the Date Picker element

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ThermoFragment.
     */

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        RcsFactory(rootView);
        DatePickerFactory(rootView);
        CrisisFactory(rootView);
        TreatmentFactory(rootView);


        try {
            retrieveDataFromDatabase();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void RcsFactory(View rootView) {


        crisisCardView = rootView.findViewById(R.id.crisis_layout);
        treatmentCardView = rootView.findViewById(R.id.treatment_layout);
        rcsTextView=rootView.findViewById(R.id.rcs_textview);

        editTextCommentary = rootView.findViewById(R.id.plain_text_input_commentary);
        rcs = 0;

        editTextCommentary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                commentary = s.toString();

            }
        });

        //We hide the others Section for more lisibility when the user focus the EditText
        editTextCommentary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    crisisCardView.setVisibility(View.GONE);
                    treatmentCardView.setVisibility(View.GONE);
                } else {
                    crisisCardView.setVisibility(View.VISIBLE);
                    treatmentCardView.setVisibility(View.VISIBLE);
                }
            }
        });


        editTextCommentary.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // the user is done typing.
                                try {
                                    DatabaseManager.addCommentary(commentary, dateSelected);
                                } catch (CouchbaseLiteException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getContext(), "Commentaires rajouté avec succès", Toast.LENGTH_SHORT).show();
                                hide_keyboard();
                                editTextCommentary.clearFocus();
                                return true; // consume.
                            }
                        }

                        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
                            editTextCommentary.clearFocus();
                        return false; // pass on to other listeners.
                    }
                }
        );


        seekbarRcs = rootView.findViewById(R.id.seekBar_rcs);

        seekbarRcs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rcs = progress;
                rcsTextView.setText("RCS : "+ progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    DatabaseManager.addRCS(rcs, dateSelected);
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }

            }

        });


    }




    private void hide_keyboard() {
    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(editTextCommentary.getWindowToken(), 0);
}



    private void CrisisFactory(View rootView) {
        /** Treatment Section */

        //ListView  factory
        listViewCrisis = rootView.findViewById(R.id.crisis_listview);
        listCrisis = new ArrayList<>();
        adapterCrisis = new CrisisAdapter(getActivity(), R.layout.crisislist, listCrisis);
        listViewCrisis.setAdapter(adapterCrisis);


        listViewCrisis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), AddCrisisActivity.class);
                modifyCrisisIntentFactory(position, intent);
                startActivity(intent);

            }
        });

        listViewCrisis.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteCrisisDialog(position);
                return true;
            }
        });


        addCrisisLink = rootView.findViewById(R.id.add_crisis_linear_layout);
        addCrisisLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.startReplication();
                Intent intent = new Intent(getActivity(), AddCrisisActivity.class);
                startActivity(intent);
            }
        });
    }

    private void DatePickerFactory(View rootView) {

        //Date Picker and Date Manager Section
        leftDateButton = rootView.findViewById(R.id.date_left_button);
        rightDateButton = rootView.findViewById(R.id.date_right_button);
        actualDateTextView = rootView.findViewById(R.id.actual_date_textview);

        final Calendar calendar = Calendar.getInstance();

        dateSelected=calendar.getTime();

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
                editTextCommentary.setText("");

                DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);


                seekbarRcs.setProgress(0);
                editTextCommentary.clearFocus();


                try {
                    retrieveDataFromDatabase();
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }

            }
        });


        rightDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We add one day
                calendar.setTime(dateSelected);
                calendar.add(Calendar.DATE, +1);

                dateSelected =calendar.getTime();
                editTextCommentary.setText("");
                DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);


                editTextCommentary.clearFocus();
                seekbarRcs.setProgress(0);
                try {
                    retrieveDataFromDatabase();
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void TreatmentFactory(View rootView){

        /** Treatment Section */

        //ListView  factory
        listViewTreatment = rootView.findViewById(R.id.treatment_listview);
        listTreatment = new ArrayList<>();
        adapterTreatment = new TreatmentAdapter(getActivity(), R.layout.treatmentlist, listTreatment);
        listViewTreatment.setAdapter(adapterTreatment);



        listViewTreatment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), AddTreatmentActivity.class);
                modifyTreatmentIntentFactory(position, intent);
                startActivityForResult(intent, 10002);

            }
        });

        listViewTreatment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteTreatmentDialog(position);
                return true;
            }
        });

        addTreatmentLink = rootView.findViewById(R.id.add_treatment_linear_layout);
        addTreatmentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTreatmentActivity.class);
                startActivityForResult(intent, 10002);
            }
        });
    }

    private void modifyTreatmentIntentFactory(int position, Intent intent) {
        //Savoir sur quel position ou id du list view on clique. Agir en conséquence
        //Passer les data en paramètr de l'intent
        Treatment t = listTreatment.get(position);
        //We map each parameters
        intent.putExtra("position", position);
        intent.putExtra("description", t.getDescription());
        intent.putExtra("isModified", true);
        intent.putExtra("hour", t.getHour());
        intent.putExtra("minute", t.getMinute());
        intent.putExtra("sideEffects", t.isSideEffects());
        intent.putExtra("id", DatabaseManager.getTreatmentId(t));
    }



    private void modifyCrisisIntentFactory(int position, Intent intent) {
        //Savoir sur quel position ou id du list view on clique. Agir en conséquence
        //Passer les data en paramètr de l'intent
        Crisis c = listCrisis.get(position);
        //We map each parameters
        intent.putExtra("startName", c.getStartFileName());
        intent.putExtra("endName", c.getEndFileName());

        intent.putExtra("thermStartName", c.getThermStartName());
        intent.putExtra("thermEndName", c.getThermEndName());
        intent.putExtra("startText", c.getStartText());
        intent.putExtra("isModified", true);
        intent.putExtra("endText", c.getEndText());
        intent.putExtra("position", position);
        intent.putExtra("pain", c.getPain());

        intent.putExtra("hourStart", c.getHourStart());
        intent.putExtra("hourEnd", c.getHourEnd());
        intent.putExtra("minuteStart", c.getMinuteStart());
        intent.putExtra("minuteEnd", c.getMinuteEnd());

        intent.putExtra("id", DatabaseManager.getCrisisId(c));
    }

    public static void addTreatment(Treatment t) {

        try {
            DatabaseManager.addTreatment(t);
            retrieveDataFromDatabase();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        DateManager.setTextViewFormattedDate(t.getDate(), actualDateTextView);

    }



    public static void addCrisis(Crisis c) {

        try {
            DatabaseManager.addCrisis(c);
            retrieveDataFromDatabase();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        DateManager.setTextViewFormattedDate(c.getDate(), actualDateTextView);

    }

    public static void retrieveDataFromDatabase() throws CouchbaseLiteException {


        listTreatment.clear();
        adapterTreatment.notifyDataSetChanged();

        listCrisis.clear();
        adapterCrisis.notifyDataSetChanged();


        //Get the day of the dateSelected members
        Calendar c = Calendar.getInstance();
        c.setTime(dateSelected);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        retrieveQueryTreatment(day, month, year);
        retrieveQueryCrisis(day, month, year);
        retrieveQueryRcs(day, month, year);
        retrieveQueryCommentary(day, month, year);

    }

    private static void retrieveQueryCrisis(int day, int month, int year) throws CouchbaseLiteException {
        //We get all the treatment corresponding to the selected day
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


            listCrisis.add(new Crisis(
                    all.getString("startName"),
                    all.getString("endName"),
                    all.getDate("date"),
                    all.getString("startText"),
                    all.getString("endText"),
                    all.getInt("pain"),
                    all.getString("thermStartName"),
                    all.getString("thermEndName"),
                    all.getInt("hourStart"),
                    all.getInt("minuteStart"),
                    all.getInt("hourEnd"),
                    all.getBoolean("isNotes"),
                    all.getInt("minuteEnd")

            ));
            adapterCrisis.notifyDataSetChanged();

        }

    }

    private static void retrieveQueryTreatment(int day, int month, int year) throws CouchbaseLiteException {
        //We get all the treatment corresponding to the selected day
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


            listTreatment.add(new Treatment(
                    all.getInt("hour"),
                    all.getInt("minute"),
                    all.getString("description"),
                    all.getBoolean("sideEffects"),
                    all.getDate("date")
            ));
            adapterTreatment.notifyDataSetChanged();

        }
    }

    private static void retrieveQueryRcs(int day, int month, int year) throws CouchbaseLiteException {
        //We get all the treatment corresponding to the selected day
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

            seekbarRcs.setProgress(all.getInt("rcs"));
        }
    }

    private static void retrieveQueryCommentary(int day, int month, int year) throws CouchbaseLiteException {
        //We get all the treatment corresponding to the selected day
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("commentary"))
                        .and(Expression.property("day").equalTo(Expression.intValue(day)))
                        .and(Expression.property("month").equalTo(Expression.intValue(month)))
                        .and(Expression.property("year").equalTo(Expression.intValue(year))));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");
            editTextCommentary.setText(all.getString("commentary"));
        }
    }

    public static void updateTreatment(Treatment t, String id) {

        dateSelected = AddTreatmentActivity.getDateSelected();
        DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);

        try {
            DatabaseManager.updateTreatment(t, id);
            adapterTreatment.notifyDataSetChanged();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        try {
            retrieveDataFromDatabase();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

    }

    public static void updateCrisis(Crisis c, String id) {

        dateSelected = PicturesFragment.dateSelected;
        DateManager.setTextViewFormattedDate(dateSelected, actualDateTextView);

        try {
            DatabaseManager.updateCrisis(c, id);
            adapterCrisis.notifyDataSetChanged();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        try {
            retrieveDataFromDatabase();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

    }


    public static void removeTreatment(int position) {
        listTreatment.remove(position);

    }
    public  void removeCrisis(int position) {

        listCrisis.remove(position);

    }
    public void deleteCrisisDialog(final int position){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Confirmation")
                .setMessage("Êtes vous sûr de vouloir supprimer cette crise ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Crisis c= listCrisis.get(position);
                        try {
                            DatabaseManager.deleteCrisis(c);
                        } catch (CouchbaseLiteException e) {
                            e.printStackTrace();
                        }
                        removeCrisis(position);

                        adapterCrisis.notifyDataSetChanged();
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


    public void deleteTreatmentDialog(final int position){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Confirmation")
                .setMessage("Êtes vous sûr de vouloir supprimer ce traitement ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Treatment t = listTreatment.get(position);
                        try {
                            DatabaseManager.deleteTreatment(t);
                        } catch (CouchbaseLiteException e) {
                            e.printStackTrace();
                        }
                        removeTreatment(position);

                        adapterTreatment.notifyDataSetChanged();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_TREATMENT_REQUEST) && (resultCode == ADD_TREATMENT_REQUEST)) {
            dateSelected=AddTreatmentActivity.getDateSelected();
            adapterTreatment.notifyDataSetChanged();
        }
        if ((requestCode == ADD_PICTURES_CRISIS_REQUEST) && (resultCode == ADD_PICTURES_CRISIS_REQUEST)) {
            dateSelected=PicturesFragment.getDateSelected();
        }
        if ((requestCode == ADD_NOTES_CRISIS_REQUEST) && (resultCode == ADD_NOTES_CRISIS_REQUEST)) {
            dateSelected=NotesFragment.getDateSelected();
        }

    }

    public static void OnDateManagerResult(Date date){
        dateSelected = date;
        DateManager.setTextViewFormattedDate(date, actualDateTextView);
        editTextCommentary.setText("");
        try {
            retrieveDataFromDatabase();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public static Date getDateSelected() {
        return dateSelected;
    }
}

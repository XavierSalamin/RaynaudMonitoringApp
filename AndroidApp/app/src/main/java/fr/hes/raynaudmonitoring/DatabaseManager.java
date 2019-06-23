package fr.hes.raynaudmonitoring;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.widget.Toast;

import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Document;
import com.couchbase.lite.Endpoint;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Replicator;
import com.couchbase.lite.ReplicatorConfiguration;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.couchbase.lite.URLEndpoint;
import com.couchbase.lite.internal.support.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.hes.raynaudmonitoring.model.UserRequest;

import static java.time.LocalDate.now;

/**
 * Class that contains all the static methods for adding, modifying, and removing items from the database
 * All data are managed by the Couchbase library
 */
public class DatabaseManager {


    private static Logger log = Logger.getLogger(String.valueOf(DatabaseManager.class));

    public static final String DB_NAME = "staging";
    public static final String IP_CIBLE = "192.168.3.8:4984";
    public static final String USER_REQUEST_TYPE = "user_request";


    public  static  String userProfile ="";
    private static  Context context;
    public static Database database; //Singleton
    public static Replicator replicator;



    public DatabaseManager(Context context) throws CouchbaseLiteException {
        this.context = context;
        final String TAG = "DATABASE";
        // Get the database (and create it if it doesn’t exist).
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        database = new Database(DB_NAME, config);


    }


    public static void startReplication() {
        URI uri = null;
        try {
            uri = new URI("ws://"+ IP_CIBLE +"/"+DB_NAME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Endpoint endpoint = new URLEndpoint(uri);
        ReplicatorConfiguration config = new ReplicatorConfiguration(database, endpoint);
        config.setReplicatorType(ReplicatorConfiguration.ReplicatorType.PUSH);
        replicator = new Replicator(config);
        replicator.start();
    }



    public static void pullData() {
        URI uri = null;
        try {
            uri = new URI("ws://"+ IP_CIBLE +"/"+DB_NAME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Endpoint endpoint = new URLEndpoint(uri);
        ReplicatorConfiguration config = new ReplicatorConfiguration(database, endpoint);
        config.setReplicatorType(ReplicatorConfiguration.ReplicatorType.PULL);
        replicator = new Replicator(config);
        replicator.start();
    }

    public static Database getDatabase() {
        return database;
    }

    /**
     * Delete Treatment from the Database
     * @param t Treatment to be deleted
     * @throws CouchbaseLiteException
     */
    public static void deleteTreatment (Treatment t) throws CouchbaseLiteException {


        String id = getTreatmentId(t);
        Document doc = database.getDocument(id);
        database.delete(doc);


    }
    public static void deleteReminder (Reminder r) throws CouchbaseLiteException {


        String id = getReminderId(r);
        Document doc = database.getDocument(id);
        database.delete(doc);


    }

    public static void deleteCrisis (Crisis c) throws CouchbaseLiteException {


        String id = getCrisisId(c);




        Document doc = database.getDocument(id);
        database.delete(doc);


    }


    public static void setUserProfile(String id){
        userProfile=id;
    }

    public static void addUserProfile(MutableDocument doc){
        doc.setString("user_profile", userProfile);
    }



    public static void addTreatment (Treatment t) throws CouchbaseLiteException {
        String id = getTreatmentId(t);
        Calendar cal = Calendar.getInstance();
        cal.setTime(t.getDate());
        MutableDocument doc = new MutableDocument(id);
        doc.setString("type", "treatment");
        doc.setInt("hour", t.getHour());
        doc.setInt("minute",t.getMinute());
        doc.setString("description", t.getDescription());
        doc.setDate("date", t.getDate());
        doc.setBoolean("sideEffects", t.isSideEffects());
        addUserProfile(doc);



        doc.setInt("day", cal.get(Calendar.DAY_OF_MONTH));
        doc.setInt("month", cal.get(Calendar.MONTH));
        doc.setInt("year", cal.get(Calendar.YEAR));

        database.save(doc);
    }

    public static void addRCS (int rcs, Date date) throws CouchbaseLiteException {

        //We add the current date to the database with the names of the images
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String id = getRcsId(date);

        MutableDocument doc = new MutableDocument(id);
        doc.setString("type", "rcs");
        doc.setInt("rcs", rcs);
        doc.setInt("day", cal.get(Calendar.DAY_OF_MONTH));
        doc.setInt("month", cal.get(Calendar.MONTH));
        doc.setInt("year", cal.get(Calendar.YEAR));
        addUserProfile(doc);



        database.save(doc);
    }


    public static void addReminder (Reminder r) throws CouchbaseLiteException {
        String id = getReminderId(r);

        MutableDocument doc = new MutableDocument(id);
        doc.setString("type", "reminder");
        doc.setInt("hour", r.getHour());
        doc.setInt("minute",r.getMinute());
        doc.setString("title", r.getTitle());
        doc.setBoolean("isChecked", r.isChecked());
        addUserProfile(doc);




        database.save(doc);


    }
    public static void addUserData (String numberPatient, String numberPhase) throws CouchbaseLiteException {

        String id = "userdata_";
        MutableDocument doc = new MutableDocument(id);
        doc.setString("type", "userData");
        doc.setString("patient", numberPatient);

        doc.setString("phase", numberPhase);
        addUserProfile(doc);

        database.save(doc);


    }



    public static void addCommentary (String text, Date date) throws CouchbaseLiteException {

        //We add the current date to the database with the names of the images
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String id = getCommentaryId(date);

        MutableDocument doc = new MutableDocument(id);
        doc.setString("type", "commentary");
        doc.setString("commentary", text);

        doc.setInt("day", cal.get(Calendar.DAY_OF_MONTH));
        doc.setInt("month", cal.get(Calendar.MONTH));
        doc.setInt("year", cal.get(Calendar.YEAR));
        addUserProfile(doc);
        database.save(doc);
    }

    public static void addCrisis (Crisis crisis) throws CouchbaseLiteException {

        //We add the current date to the database with the names of the images
        Calendar cal = Calendar.getInstance();
        cal.setTime(crisis.getDate());

        String id = getCrisisId(crisis);

        MutableDocument doc = new MutableDocument(id);
        doc.setString("type", "crisis");
        doc.setDate("date", crisis.getDate());
        doc.setString("startName", crisis.getStartFileName());
        doc.setString("endName", crisis.getEndFileName());

        doc.setBoolean("isNotes", crisis.isNotes());

        doc.setString("thermStartName", crisis.getThermStartName());
        doc.setString("thermEndName", crisis.getThermEndName());

        doc.setString("startText", crisis.getStartText());
        doc.setString("endText", crisis.getEndText());

        doc.setInt("hourEnd", crisis.getHourEnd());
        doc.setInt("minuteEnd", crisis.getMinuteEnd());

        doc.setInt("hourStart", crisis.getHourStart());
        doc.setInt("minuteStart", crisis.getMinuteStart());

        doc.setInt("day", cal.get(Calendar.DAY_OF_MONTH));
        doc.setInt("month", cal.get(Calendar.MONTH));
        doc.setInt("year", cal.get(Calendar.YEAR));
        doc.setInt("pain", crisis.getPain());
        addUserProfile(doc);
        database.save(doc);
    }

    public static void updateTreatment (Treatment t, String id) throws CouchbaseLiteException {
        deleteDocById(id);
    addTreatment(t);

    }
    public static void updateCrisis (Crisis c, String id) throws CouchbaseLiteException {
        deleteDocById(id);
        addCrisis(c);

    }
    public static void updateReminder (Reminder r, String id) throws CouchbaseLiteException {
        deleteDocById(id);
        addReminder(r);

    }

    private static void deleteDocById(String id) throws CouchbaseLiteException {

        Document doc = database.getDocument(id);
        database.delete(doc);

    }

    public static boolean checkLogin(String firstname, String lastname) throws CouchbaseLiteException {

        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("user_profile"))
                        .and(Expression.property("firstname").equalTo(Expression.string(firstname)))
                        .and(Expression.property("lastname").equalTo(Expression.string(lastname))));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");

            if(all.count()>=1) {
                log.log(Level.INFO, "Login validated");
                return true;
            }

        }


        log.log(Level.INFO, "Login refused");
        return false;





    }


    //initialise the database
    protected void initDB() throws IOException, CouchbaseLiteException {



        //Create a new document(i.e a record) in the database.
        MutableDocument mutableDoc = new MutableDocument()
                .setFloat("version", 2.0F)
                .setString("type", "SDK");
        //Save it to the database
        database.save(mutableDoc);

        // Update a document.
        mutableDoc = database.getDocument(mutableDoc.getId()).toMutable();
        mutableDoc.setString("language", "Java");
        database.save(mutableDoc);
        Document document = database.getDocument(mutableDoc.getId());
        // Log the document ID (generated by the database) and properties
       // Log.i(TAG, "Document ID :: " + document.getId());
        //Log.i(TAG, "Learning " + document.getString("language"));


        mutableDoc.setString("name2", "fdp2");
        try {
            database.save(mutableDoc);
        } catch (CouchbaseLiteException e) {

        }

        String test;
        test = database.getDocument(mutableDoc.getId()).getString("name2");



    }



    public static void deleteAll () throws CouchbaseLiteException {

    database.delete();


    }
    public static String getCrisisId(Crisis c){
        Calendar cal = Calendar.getInstance();


        //Create ID based on current Date
        String id = "crisis_"
                +c.getStartFileName()
                +c.getEndFileName()
                +c.getStartText()
                +c.getEndText();
        return id;
    }

    private static String getRcsId(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //Create ID based on current Date
        return "rcs_"
                +cal.get(Calendar.DAY_OF_MONTH)
                +cal.get(Calendar.MONTH)
                +cal.get(Calendar.YEAR);
    }


    private static String getCommentaryId(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //Create ID based on current Date
        return "commentary_"
                +cal.get(Calendar.DAY_OF_MONTH)
                +cal.get(Calendar.MONTH)
                +cal.get(Calendar.YEAR);
    }


    public static String getTreatmentId(Treatment t){
        Calendar cal = Calendar.getInstance();
        cal.setTime(t.getDate());

        //Create ID based on current Date and Treatment's properties
        return "treatment_"
                +cal.get(Calendar.SECOND)
                +cal.get(Calendar.MINUTE)
                +t.getDescription()
                +t.getMinute()
                +t.getHour()
                +cal.get(Calendar.HOUR)
                +cal.get(Calendar.DAY_OF_MONTH)
                +cal.get(Calendar.MONTH)
                +cal.get(Calendar.YEAR);
    }

    public static String getReminderId(Reminder r){

        String id = "reminder_"
                +r.getTitle()
                +r.getMinute()
                +r.getHour();

        return id;
    }


    public static void addUserRequest (UserRequest userRequest) throws CouchbaseLiteException {

        //We add the current date to the database with the names of the images
        String uniqueID = UUID.randomUUID().toString();
        String id = userRequest.getFirstname()+"_"+uniqueID;


        MutableDocument doc = new MutableDocument(id);
        doc.setString("firstname", userRequest.getFirstname());

        doc.setString("lastname", userRequest.getLastname());
        doc.setString("birthDate", userRequest.getBirthdate());
        doc.setBoolean("isActivated", userRequest.isActivated());
        doc.setString("type", USER_REQUEST_TYPE);
        addUserProfile(doc);
        database.save(doc);
    }


    public static void addBlobPicture(String fileName, String key){
        MutableDocument newTask = new MutableDocument(key);
        if(fileName!=null) {
            String path = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + fileName + ".jpg";
            File imgFile = new File(path);
            InputStream is = null;



            try {
                is = new FileInputStream(imgFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Blob blob = new Blob("image/png", is);
                newTask.setBlob("avatartest", blob);


                database.save(newTask);
            } catch (CouchbaseLiteException e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
        else{
            Toast.makeText(context, "unknow file to push", Toast.LENGTH_SHORT).show();
        }
    }

    public static  Context getContext(){
        return context;
    }
}

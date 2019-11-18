package fr.hes.raynaudmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;



public class LoginActivity extends Activity {

    public static final String MESSAGE_LOGIN_SUCCESS = "Connexion réussie !";
    public static final String MESSAGE_LOGIN_UNACTIVATED = "Ce compte est en cours de validation.";
    public static final String MESSAGE_LOGIN_ERROR = "Connexion impossible. : ";
    public static final String MESSAGE_LOGIN_404 = "Ce compte n'existe pas.";
    public static final String MESSAGE_LOGIN_500 = "Le serveur n'est pas disponible. Erreur : ";
    private Button registryButton;
    private Button loginButton;
    static Context ctx;

    private EditText firstnameEdit;
    private EditText lastnameEdit;

    private String firstname;
    private String lastname;
    final Logger logger = Logger.getLogger(String.valueOf(LoginActivity.class));
    public boolean isActivated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    ctx= this;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Singleton
        try {
            DatabaseManager database = new DatabaseManager(getApplicationContext());
            //DatabaseManager.deleteAll();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        /**
         * Si le patient s'est déjà login on skip la page de login
         */
        try {
            if  (DatabaseManager.isLogin()){
                Intent skipLogin = new Intent(ctx, MainActivity.class);
                startActivity(skipLogin);
                logger.log(Level.INFO, "wesh skip login ");
            }
            else{
                logger.log(Level.INFO, "wesh wtf ");
            }
        } catch (CouchbaseLiteException e) {
            logger.log(Level.WARNING, "wesh ",e );
        }

        firstnameEdit = findViewById(R.id.username_edittext);
        lastnameEdit = findViewById(R.id.password_edittext);


        firstnameEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                firstname = s.toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        lastnameEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                lastname = s.toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        registryButton = findViewById(R.id.registry_link_button);

        registryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginButton = findViewById(R.id.login_link_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Retrive Data from the sync gateway
                //DatabaseManager.pullData();
                final String[] status = {""};
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            isActivated = false;
                            status[0] =     checkIfUserIsActivated(logger);

                            logger.log(Level.INFO, "Data pulled from Sync Gateway");

                            //Check if firstname + lastname match any user_profile type in SG


                        } catch (Exception e) {
                            logger.log(Level.INFO, "Error ", e);
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if(isActivated) {
                    Intent intent = new Intent(ctx, MainActivity.class);
                    Toast.makeText(LoginActivity.this, status[0], Toast.LENGTH_SHORT).show();
                    try {
                        DatabaseManager.setUserProfile(lastname+firstname);
                    } catch (CouchbaseLiteException e) {
                        e.printStackTrace();
                    }
                    try {
                        DatabaseManager.login(true);
                    } catch (CouchbaseLiteException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, status[0], Toast.LENGTH_SHORT).show();
                }



            }
        });


    }



    private String checkIfUserIsActivated(Logger logger) {

        try {
            String url = "http://"+ DatabaseManager.IP_CIBLE + "/" + DatabaseManager.DB_NAME + "/" + lastname + firstname;
            URL publicApi = new URL(url);
            HttpURLConnection myConnection = (HttpURLConnection) publicApi.openConnection();

            logger.log(Level.INFO, "Rest api connection status "+myConnection.getResponseCode()+" on "+url);

            if (myConnection.getResponseCode() == 200) {
                // Success
                InputStream responseBody = myConnection.getInputStream();

                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");



            InputStream is = publicApi.openStream();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                    String jsonText = readAll(rd);
                    JSONObject json = new JSONObject(jsonText);
                  isActivated =  json.getBoolean("isActivated");
                  if(isActivated){
                      return MESSAGE_LOGIN_SUCCESS;
                  }
                  else{
                      return MESSAGE_LOGIN_UNACTIVATED;
                  }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return MESSAGE_LOGIN_ERROR+e;
                } finally {
                    is.close();
                    myConnection.disconnect();
                }

            } else if (myConnection.getResponseCode() == 404){
                // Error handling code goes here
                return MESSAGE_LOGIN_404;
            }
            else{
                return MESSAGE_LOGIN_500 +myConnection.getResponseCode();
            }

        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Rest api connection 400", e);
            return MESSAGE_LOGIN_500+e;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Rest api connection 400", e);
            return MESSAGE_LOGIN_500+e;
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}

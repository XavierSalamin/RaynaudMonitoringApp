package fr.hes.raynaudmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginActivity extends Activity {

    private Button registryButton;
    private Button loginButton;
    static Context ctx;

    private EditText firstnameEdit;
    private EditText lastnameEdit;

    private String firstname;
    private String lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Logger logger = Logger.getLogger(String.valueOf(LoginActivity.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Singleton
        try {
            DatabaseManager database = new DatabaseManager(getApplicationContext());
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
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
        ctx = this;
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
                DatabaseManager.pullData();
                logger.log(Level.INFO, "Data pulled from Sync Gateway");

                //Check if firstname + lastname match any user_profile type in SG
                try {
                    if(DatabaseManager.checkLogin(firstname, lastname)) {
                        Intent intent = new Intent(ctx, MainActivity.class);
                        Toast.makeText(LoginActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                        DatabaseManager.setUserProfile(firstname+lastname);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Ce compte n'existe pas. Veuillez vérifier vos identifiants !", Toast.LENGTH_SHORT).show();
                    }
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }

            }
        });
    }



}

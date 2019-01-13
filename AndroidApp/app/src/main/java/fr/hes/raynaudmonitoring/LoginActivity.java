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
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

public class LoginActivity extends Activity {

    private Button registryButton;
    private Button loginButton;
    static Context ctx;

    private EditText usernameEdit;
    private EditText passwordEdit;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Singleton
        try {
            DatabaseManager database = new DatabaseManager(getApplicationContext());
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        usernameEdit = findViewById(R.id.username_edittext);
        passwordEdit = findViewById(R.id.password_edittext);

        usernameEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                username = s.toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        passwordEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                password = s.toString();
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
                if("admin".equals(username)&&"raynaud2018".equals(password)) {
                    Intent intent = new Intent(ctx, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Ce compte n'existe pas. Veuillez vérifier vos identifiants !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}

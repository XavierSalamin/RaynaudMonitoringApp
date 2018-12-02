package fr.hes.raynaudmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.couchbase.lite.CouchbaseLiteException;

import java.util.Date;

import fr.hes.raynaudmonitoring.model.UserRequest;

public class RegisterActivity extends Activity {


    private Button termsButton;
    private EditText firstnameEdit;
    private EditText lastnameEdit;
    private EditText birthDateEdit;

    private String firstname;
    private String lastname;
    private String birthDate;


    static Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        termsButton = findViewById(R.id.terms_button);
        ctx = this;
        firstname = null;
        lastname = null;
        birthDate = null;
        termsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserRequest(new UserRequest(firstname, lastname, birthDate));

                Intent intent = new Intent(ctx, TermsActivity.class);
                startActivity(intent);

            }
        });

        firstnameEdit = findViewById(R.id.firstname_edittext);
        lastnameEdit = findViewById(R.id.lastname_edittext);
        birthDateEdit = findViewById(R.id.birthdate_edittext);

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
        birthDateEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                birthDate = s.toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


    }

    public static void addUserRequest(UserRequest Ur) {
        try {
            DatabaseManager.addUserRequest(Ur);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

}

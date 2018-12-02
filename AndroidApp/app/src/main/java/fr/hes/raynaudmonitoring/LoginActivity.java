package fr.hes.raynaudmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    static Context ctx;

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


        registryButton = findViewById(R.id.registry_link_button);
        ctx = this;
        registryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }



}

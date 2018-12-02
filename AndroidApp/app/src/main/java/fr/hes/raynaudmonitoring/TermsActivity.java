package fr.hes.raynaudmonitoring;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TermsActivity extends AppCompatActivity {


    private Button mainButton;
    static Context ctx;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        this.setTitle("Conditions d'utilisations");
        ctx = this;
        mainButton = findViewById(R.id.agree_button);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

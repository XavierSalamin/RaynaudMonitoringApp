package fr.hes.raynaudmonitoring;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class TermsActivity extends AppCompatActivity {


    private Button mainButton;
    private Button cancelButton;
    private CheckBox termsCheck;
    static Context ctx;
    boolean agreed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        this.setTitle("Conditions d'utilisations");
        ctx = this;
        agreed=false;

        termsCheck = findViewById(R.id.terms_checkbox);
        termsCheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(termsCheck.isChecked()){
                    agreed=true;
                }else{
                    agreed=false;
                }
            }
        });

        mainButton = findViewById(R.id.agree_button);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreed){
                    Intent intent = new Intent(ctx, LoginActivity.class);

                    //Intent intent = new Intent(ctx, LoginActivity.class);
                    startActivity(intent);
                   // Toast.makeText(ctx, "ERROR - Le serveur n'est pas disponible", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    DatabaseManager.startReplication();
                }
                else{
                    Toast.makeText(ctx, "Vous n'avez pas validé le formulaire", Toast.LENGTH_SHORT).show();
                }

            }
        });



        cancelButton = findViewById(R.id.decline_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

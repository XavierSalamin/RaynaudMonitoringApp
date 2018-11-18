package fr.hes.raynaudmonitoring;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * An Activity who add an instance of {@link Crisis} to the DataBase
 * This activity is also used to update a crisis
 * This activity contains 2 fragment
 */
public class AddCrisisActivity extends AppCompatActivity {

    private Fragment fragmentPictures;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_notes:
                    selectedFragment = NotesFragment.newInstance();
                    break;
                case R.id.navigation_pictures:
                    selectedFragment = PicturesFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.add_crisis_layout, selectedFragment);
            transaction.commit();


            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crisis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setTitle(R.string.title_activity_add_crisis);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //We use the same activity for the Create and Update operation
        //If startName is not null this a Pictures Activity, if it's null it's a Notes Activity
        if(getIntent().getStringExtra("startName")==null&&getIntent().getBooleanExtra("isModified", false)){
            transaction.replace(R.id.add_crisis_layout, NotesFragment.newInstance()); //Set the selected fragment to Note Fragment
            transaction.commit();
            navigation.getMenu().removeItem(R.id.navigation_pictures); //Remove the bottom navigation
        }
        else{
            transaction.replace(R.id.add_crisis_layout, PicturesFragment.newInstance());
            transaction.commit();
        }


    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check:
            return false;

            default:
                return false;
        }
    }
    public void setActionBarTitle(int title) {
        getSupportActionBar().setTitle(title);
    }
}

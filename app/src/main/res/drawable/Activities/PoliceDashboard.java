package com.ranafkd.myproject.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.ranafkd.myproject.R;

public class PoliceDashboard extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_dashboard);
        getSupportActionBar().setTitle("Missing Child");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new com.ranafkd.myproject.Activities.MissedFragment())
                .commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment frag = null;
                switch (item.getItemId()){

                    case R.id.missed_menu:
                        frag = new com.ranafkd.myproject.Activities.MissedFragment();
                        break;

                    case R.id.sighted_menu:
                        frag = new com.ranafkd.myproject.Activities.SightedFragment();
                        break;

                    case R.id.stats_menu:
                        frag = new com.ranafkd.myproject.Activities.StatsFragment();
                        break;

                    case R.id.profile_menu:
                        frag = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
                return true;
            }
        });
    }

    // Code twice back press
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}

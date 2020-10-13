package com.ranafkd.myproject.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ranafkd.myproject.R;

public class CitizenDashboard extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView missed, sighted, search, setting;
    NavigationView navigationView;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizendashboard);
        getSupportActionBar().setTitle("Missing Child");


        // Navigation bar code
        drawerLayout = findViewById(R.id.Drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(com.ranafkd.myproject.Activities.CitizenDashboard.this, drawerLayout, R.string.open, R.string.close );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Other functioning of navigation bar are done through onOptionItemListener outside onCreate

        missed = findViewById(R.id.text_missed);
        sighted = findViewById(R.id.text_sighted);
        search = findViewById(R.id.text_search);
        setting = findViewById(R.id.text_setting);
        navigationView = findViewById(R.id.nav_details);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.Contact_us:
                        break;
                    case R.id.Share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, "Share With");
                        startActivity(Intent.createChooser(intent, "Share with"));
                        break;
                    case R.id.About:
                        break;
                    case R.id.citizen_logout:
                        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent1 = new Intent(com.ranafkd.myproject.Activities.CitizenDashboard.this, LoginAcitivity.class);
                        startActivity(intent1);
                        finish();
                        break;
                }
                return true;
            }
        });


        // SLIDER CODE FOR PUTTING IMAGES INTO ARRAY ----STARTS-----
        viewFlipper = findViewById(R.id.home_slider);
        int images[] = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};

        for(int image : images){
            flipperImages(image);
        }
        // SLIDER CODE FOR PUTTING IMAGES INTO ARRAY ----Ends-----



            missed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.ranafkd.myproject.Activities.CitizenDashboard.this,MissedDetails.class);
                    startActivity(intent);
                }
            });


            sighted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.ranafkd.myproject.Activities.CitizenDashboard.this, com.ranafkd.myproject.Activities.SightedActivity.class);
                startActivity(intent);
                }
            });

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.ranafkd.myproject.Activities.CitizenDashboard.this, com.ranafkd.myproject.Activities.SearchActivity.class);
                    startActivity(intent);
                }
            });

            setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(com.ranafkd.myproject.Activities.CitizenDashboard.this,CitizenSettingsActivity.class);
                    startActivity(intent);
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

    // Fuctionality of side Navigation bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void flipperImages(int image) {
        ImageView img = new ImageView(this);
        img.setImageResource(image);
        viewFlipper.addView(img);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        //Animation
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
}

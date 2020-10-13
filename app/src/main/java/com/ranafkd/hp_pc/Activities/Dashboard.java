package com.ranafkd.hp_pc.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ranafkd.hp_pc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Dashboard extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView cat1, cat2, cat3, cat4;
    NavigationView navigationView;
    ViewFlipper viewFlipper;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView viewAll;
    ProgressDialog progressDialog;
    FirebaseAuth auth;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setTitle("App Name");

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        cat1 = findViewById(R.id.grocery_dash);
        cat2 = findViewById(R.id.beverage_dash);
        cat3 = findViewById(R.id.pc_dash);
        cat4 = findViewById(R.id.dc_dash);

        // example of some general store item are taken

        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ItemsActivity.class);
                intent.putExtra("cat1", "grocery");
                intent.putExtra("cat2", "a");
                intent.putExtra("cat3", "b");
                intent.putExtra("cat4", "b");
                startActivity(intent);
            }
        });

        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ItemsActivity.class);
                intent.putExtra("cat1", "a");
                intent.putExtra("cat2", "beverages");
                intent.putExtra("cat3", "b");
                intent.putExtra("cat4", "c");
                startActivity(intent);
            }
        });

        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ItemsActivity.class);
                intent.putExtra("cat1", "a");
                intent.putExtra("cat2", "b");
                intent.putExtra("cat3", "personal care");
                intent.putExtra("cat4", "c");
                startActivity(intent);
            }
        });

        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ItemsActivity.class);
                intent.putExtra("cat1", "a");
                intent.putExtra("cat2", "b");
                intent.putExtra("cat3", "c");
                intent.putExtra("cat4", "dry fruit");
                startActivity(intent);
            }
        });



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("OfferDBName");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        auth = FirebaseAuth.getInstance();
        viewAll = findViewById(R.id.text_view_all);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, CategoryActivity.class);
                startActivity(intent);
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final Boolean status = sharedPreferences.getBoolean("loginstatus", false);

        // Navigation bar code
        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(Dashboard.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_details);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.my_profile:
                        if (status) {
                            Intent intent = new Intent(Dashboard.this, UserProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Dashboard.this, UserLoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.my_order:
                        if(status) {
                            Intent intent = new Intent(Dashboard.this, my_order.class);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(Dashboard.this, UserLoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.my_cart:
                        if(status) {
                            Intent intent = new Intent(Dashboard.this, CartActivity.class);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(Dashboard.this, UserLoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, "Share With");
                        startActivity(Intent.createChooser(intent, "Share with"));
                        break;
                    case R.id.contact_us:
                        Intent intent1 = new Intent(Dashboard.this, contact_us.class);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });

        // SLIDER CODE FOR PUTTING IMAGES INTO ARRAY ----STARTS-----
        viewFlipper = findViewById(R.id.home_slider);
        int images[] = {R.drawable.s1, R.drawable.s2, R.drawable.s3};

        for(int image : images){
            flipperImages(image);
        }
        // SLIDER CODE FOR PUTTING IMAGES INTO ARRAY ----Ends-----

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


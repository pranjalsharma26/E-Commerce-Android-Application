package com.ranafkd.myproject.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ranafkd.myproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Welcome extends AppCompatActivity {

    Button citizen_signup, police_signup;
    TextView signin;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase1;
    DatabaseReference databaseReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        citizen_signup = findViewById(R.id.citizen_signup);
        police_signup = findViewById(R.id.police_signup);
        signin = findViewById(R.id.textView);

        // Redirecting user directly to dashboard if logged in (For Citizen)
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CitizenDB");
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("loginstatus", false)){

            Intent intent = new Intent(com.ranafkd.myproject.Activities.Welcome.this, com.ranafkd.myproject.Activities.CitizenDashboard.class);
            startActivity(intent);
            finish();
        }

        // Redirecting user directly to dashboard if logged in (For Citizen)
        firebaseDatabase1 = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase1.getReference("PoliceDB");
        SharedPreferences sharedPreferences1 = getSharedPreferences("policedata", Context.MODE_PRIVATE);
        if (sharedPreferences1.getBoolean("loginstatus", false)){

            Intent intent1 = new Intent(com.ranafkd.myproject.Activities.Welcome.this, PoliceDashboard.class);
            startActivity(intent1);
            finish();
        }


        citizen_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.ranafkd.myproject.Activities.Welcome.this, com.ranafkd.myproject.Activities.CitizenRegisterActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        police_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.ranafkd.myproject.Activities.Welcome.this, PoliceRegisterAcitivity.class);
                startActivity(intent);
//                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.ranafkd.myproject.Activities.Welcome.this, LoginAcitivity.class);
                startActivity(intent);
//                finish();
            }
        });
    }
}

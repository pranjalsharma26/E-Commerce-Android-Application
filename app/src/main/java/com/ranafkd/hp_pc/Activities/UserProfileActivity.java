package com.ranafkd.hp_pc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ranafkd.hp_pc.R;

public class UserProfileActivity extends AppCompatActivity {

    TextView name, number, email, pass;
    Button changePass, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //finding view by id
        name = findViewById(R.id.profile_name);
        number = findViewById(R.id.profile_number);
        email = findViewById(R.id.profile_email);
        pass = findViewById(R.id.profile_password);
        changePass = findViewById(R.id.change_pass_btn);
        logout = findViewById(R.id.logout);


        //getting data from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);
        String nameS = sharedPreferences.getString("name","");
        String numberS = sharedPreferences.getString("number", "");
        String emailS = sharedPreferences.getString("email", "");
        String passS = sharedPreferences.getString("password", "");


        //setting data on textview
        name.setText(nameS);
        number.setText(numberS);
        email.setText(emailS);
        pass.setText(passS);


        //change password
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UserChangePassActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //logout code
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent1 = new Intent(UserProfileActivity.this, Dashboard.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}

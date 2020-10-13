package com.ranafkd.myproject.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ranafkd.myproject.Pojo.CitizenUserPojo;
import com.ranafkd.myproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CitizenChangePassActivity extends AppCompatActivity {

    EditText old_pass, new_pass, new_pass_conf;
    Button changeButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Boolean error = false;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_change_pass);

        old_pass = findViewById(R.id.c_change_old_pass);
        new_pass = findViewById(R.id.c_change_new_pass);
        new_pass_conf = findViewById(R.id.c_change_new_conf_pass);
        changeButton = findViewById(R.id.cPass_change_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CitizenDB");

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String o_pass = old_pass.getText().toString();
                final String n_pass = new_pass.getText().toString();
                String n_cpass = new_pass_conf.getText().toString();

                if(!n_pass.equals(n_cpass)){
                    error = true;
                }

                if (!error){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                CitizenUserPojo pojo = dataSnapshot1.getValue(CitizenUserPojo.class);
                                String pass = pojo.getPassword();
                                String id = pojo.getId();
                                try {
                                    if (pass.equals(o_pass)) {
                                        databaseReference.child(id).child("password").setValue(n_pass);
                                        flag=1;
                                        //creating shared prefernce with the name data
                                        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        //storing the user data into shared preference data
                                        editor.putString("password",n_pass);
                                        editor.commit();
                                        break;
                                    }
                                }
                                catch(Exception e){
                                    e.getMessage();
                                }
                            }
                            if(flag==1){
                                Toast.makeText(com.ranafkd.myproject.Activities.CitizenChangePassActivity.this, "Password Change Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(com.ranafkd.myproject.Activities.CitizenChangePassActivity.this, com.ranafkd.myproject.Activities.CitizenDashboard.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(com.ranafkd.myproject.Activities.CitizenChangePassActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}

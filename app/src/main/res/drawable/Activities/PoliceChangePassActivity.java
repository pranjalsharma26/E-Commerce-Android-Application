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

import com.ranafkd.myproject.Pojo.PoliceUserPojo;
import com.ranafkd.myproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PoliceChangePassActivity extends AppCompatActivity {

    EditText old_pass, new_pass, new_pass_conf;
    Button changeButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Boolean error = false;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_change_pass);

        old_pass = findViewById(R.id.p_change_old_pass);
        new_pass = findViewById(R.id.p_change_new_pass);
        new_pass_conf = findViewById(R.id.p_change_new_conf_pass);
        changeButton = findViewById(R.id.pPass_change_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("PoliceDB");

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String o_pass = old_pass.getText().toString();
                final String n_pass = new_pass.getText().toString();
                String n_p_pass = new_pass_conf.getText().toString();

                if(!n_pass.equals(n_p_pass)){
                    error = true;
                }

                if (!error){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                PoliceUserPojo pojo = dataSnapshot1.getValue(PoliceUserPojo.class);
                                String pass = pojo.getPassword();
                                String id = pojo.getId();
                                try {
                                    if (pass.equals(o_pass)) {
                                        databaseReference.child(id).child("password").setValue(n_pass);
                                        flag=1;
                                        //creating shared prefernce with the name data
                                        SharedPreferences sharedPreferences = getSharedPreferences("policedata",MODE_PRIVATE);
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
                                Toast.makeText(com.ranafkd.myproject.Activities.PoliceChangePassActivity.this, "Password Change Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(com.ranafkd.myproject.Activities.PoliceChangePassActivity.this, com.ranafkd.myproject.Activities.PoliceDashboard.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(com.ranafkd.myproject.Activities.PoliceChangePassActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

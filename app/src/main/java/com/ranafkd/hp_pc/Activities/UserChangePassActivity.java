package com.ranafkd.hp_pc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ranafkd.hp_pc.Pojo.CustomerPojo;
import com.ranafkd.hp_pc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserChangePassActivity extends AppCompatActivity {

    EditText oldPass, newPass, conPass;
    Button change;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    Boolean error = false;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_pass);

        oldPass = findViewById(R.id.old_pass);
        newPass = findViewById(R.id.new_pass);
        conPass = findViewById(R.id.new_pass2);
        change = findViewById(R.id.change);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CustomerDB");


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String o_pass = oldPass.getText().toString();
                final String n_pass = newPass.getText().toString();
                String n_cpass = conPass.getText().toString();

                if(!n_pass.equals(n_cpass)){
                    error = true;
                }

                progressDialog = new ProgressDialog(UserChangePassActivity.this);
                progressDialog.setTitle("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if (!error){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                CustomerPojo pojo = dataSnapshot1.getValue(CustomerPojo.class);
                                String pass = pojo.getPass();
                                String id = pojo.getId();
                                try {
                                    if (pass.equals(o_pass)) {
                                        databaseReference.child(id).child("pass").setValue(n_pass);
                                        flag=1;
                                        //creating shared prefernce with the name data
                                        SharedPreferences sharedPreferences = getSharedPreferences("userdata",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        //storing the user data into shared preference data
                                        editor.putString("pass",n_pass);
                                        editor.commit();
                                        break;
                                    }
                                }
                                catch(Exception e){
                                    e.getMessage();
                                }
                            }
                            if(flag==1){
                                Toast.makeText(UserChangePassActivity.this, "Password Change Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserChangePassActivity.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                                progressDialog.cancel();
                            }
                            else{
                                progressDialog.cancel();
                                Toast.makeText(UserChangePassActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

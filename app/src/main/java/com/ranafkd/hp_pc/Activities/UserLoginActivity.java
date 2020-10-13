package com.ranafkd.hp_pc.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ranafkd.hp_pc.Pojo.CustomerPojo;
import com.ranafkd.hp_pc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserLoginActivity extends AppCompatActivity {

    EditText userLoginEmail, userLoginPass;
    TextView forgetPass, Register;
    Button loginButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //Finding view by id
        userLoginEmail = findViewById(R.id.user_login_email);
        userLoginPass = findViewById(R.id.user_login_pass);
        forgetPass = findViewById(R.id.user_forget_pass);
        Register = findViewById(R.id.register_link);
        loginButton = findViewById(R.id.user_login_btn);


        //firebase setup
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CustomerDB");


        //Checking login status
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("loginstatus", false)){

            Intent intent = new Intent(UserLoginActivity.this, Dashboard.class);
            startActivity(intent);
            finish();
        }


        //forgetpass
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, UserForgetPassActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //onclick on login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserDataBase();
                progressDialog = new ProgressDialog(UserLoginActivity.this);
                progressDialog.setTitle("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });


        //register onclick
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getUserDataBase() {

        final String email = userLoginEmail.getText().toString();
        String password = userLoginPass.getText().toString();
        Log.d("2456","email : "+email+"\n"+"Pass : "+password);

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("2456","check if 1\n");
                    if(auth.getCurrentUser().isEmailVerified()){
                        Log.d("2456","check if 2\n");
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d("2456","check if 2.5\n");
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Log.d("2456","check if 3\n");
                                        CustomerPojo pojo = dataSnapshot1.getValue(CustomerPojo.class);
                                        String idP = pojo.getId();
                                        String nameP = pojo.getName();
                                        String numberP = pojo.getNumber();
                                        String emailP = pojo.getEmail();
                                        String passP = pojo.getPass();

                                        if (emailP.equals(email)) {
                                            flag = 1;
                                            Log.d("2456","check if 4\n");
                                            SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("id", idP);
                                            editor.putString("name", nameP);
                                            editor.putString("number", numberP);
                                            editor.putString("email", emailP);
                                            editor.putString("password", passP);
                                            editor.putBoolean("loginstatus", true);
                                            editor.commit();
                                            break;
                                        }
                                    }
                                    if (flag == 1) {
                                        //      Log.d("1234","check if 2");
                                        Log.d("2456","check if 5\n");
                                        Toast.makeText(UserLoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UserLoginActivity.this, Dashboard.class);
                                        startActivity(intent);
                                        finish();
                                        progressDialog.cancel();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    progressDialog.cancel();
                                    Toast.makeText(UserLoginActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                    else{
                        progressDialog.cancel();
                        Toast.makeText(UserLoginActivity.this, "Not Verified", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    progressDialog.cancel();
                    Toast.makeText(UserLoginActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(UserLoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

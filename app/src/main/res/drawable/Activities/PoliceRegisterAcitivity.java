package com.ranafkd.myproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ranafkd.myproject.Pojo.PoliceUserPojo;
import com.ranafkd.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PoliceRegisterAcitivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText p_user_name, p_user_email, p_user_pass, p_user_confpass;
    EditText p_user_number;
    Button p_register_btn;
//    Button p_verify_btn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_register);
        getSupportActionBar().setTitle("Missing Child");

        // Creating instance of Database
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("PoliceDB");

        // Finding components by ID
        // p_user_number = findViewById(R.id.police_edit_number);
        p_user_name = findViewById(R.id.police_edit_name);
        p_user_email = findViewById(R.id.police_edit_email);
        p_user_pass = findViewById(R.id.police_edit_pass);
        p_user_confpass = findViewById(R.id.police_edit_confpass);
        p_register_btn = findViewById(R.id.police_register_btn);
       // p_verify_btn = findViewById(R.id.police_verify_btn);
//        p_register_btn.setEnabled(false);


//        p_verify_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                p_register_btn.setEnabled(true);
//                verification();
//            }
//        });

        p_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPoliceSignUpData();
            }
        });

    }

    // Getting data from edittext and setting them to database
    private void getPoliceSignUpData(){

        final String cid = databaseReference.push().getKey();
        final String name = p_user_name.getText().toString();
        final String email = p_user_email.getText().toString();
        final String password = p_user_pass.getText().toString();
        final String confirmPass = p_user_confpass.getText().toString();
        Boolean flag1 = false;
        Boolean flag2 = false;
        Boolean flag3 = false;
        Boolean flag4 = false;

       // Log.d("12345", "getCitizenSignUpData: "+cid+" "+name+" "+number+" "+password+" "+confirmPass);

        // Validation Code
        if ( name.isEmpty() || name.trim().length() < 4 ){
            flag1 = true;
        }

        if ( email.isEmpty()){
            flag2 = true;
        }

        if ( password.isEmpty() || password.trim().length()<8 ){
            flag3 = true;
        }

        if ( !password.equals(confirmPass) ){
            flag4 = true;
        }


        if (flag1){
            Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this,"Invalid name entered !!",Toast.LENGTH_SHORT).show();
        } else if (flag2){
            Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this,"Invalid email entered !!",Toast.LENGTH_SHORT).show();
        }else if (flag3){
            Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this,
                    "Your password is too weak it should contain atleast 8 characters",Toast.LENGTH_SHORT).show();
        }else if (flag4){
            Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this,"Password mismatch !!",Toast.LENGTH_SHORT).show();
        }else{

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("1234", "1");

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this, "Verification mail sent",Toast.LENGTH_SHORT).show();
                                    PoliceUserPojo pojo = new PoliceUserPojo();
                                    pojo.setId(cid);
                                    pojo.setName(name);
                                    pojo.setNumber(email);
                                    pojo.setPassword(password);

                                    // Inserting data in database
                                    databaseReference.child(cid).setValue(pojo);
                                    Intent intent = new Intent(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this, com.ranafkd.myproject.Activities.LoginAcitivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

//            PoliceUserPojo pojo = new PoliceUserPojo();
//            pojo.setId(cid);
//            pojo.setName(name);
//            pojo.setNumber(email);
//            pojo.setPassword(password);
//
//            // Inserting data in database
//            databaseReference.child(cid).setValue(pojo);
//            Intent intent = new Intent(PoliceRegisterAcitivity.this, LoginAcitivity.class);
//            startActivity(intent);
//            finish();

        }
    }


    // AUTHENTICATION USING EMAIL AND PASSWORD
    private void verification() {

        p_register_btn.setEnabled(true);
        final String name = p_user_name.getText().toString();
        final String email = p_user_email.getText().toString();
        //   final String number = c_user_number.getText().toString();
        final String password = p_user_pass.getText().toString();
        final String confirmPass = p_user_confpass.getText().toString();

        Boolean flag1 = false;
        Boolean flag2 = false;
        Boolean flag3 = false;
        Boolean flag4 = false;

        // Log.d("12345", "getCitizenSignUpData: "+cid+" "+name+" "+number+" "+password+" "+confirmPass);

        // Validation Code
        if ( name.isEmpty() || name.trim().length() < 4 ){
            flag1 = true;
        }

        if ( email.isEmpty()){
            flag2 = true;
        }

        if ( password.isEmpty() || password.trim().length()<8 ){
            flag3 = true;
        }

        if ( !password.equals(confirmPass) ){
            flag4 = true;
        }


        if (flag1){
            Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this,"Please enter name!!",Toast.LENGTH_SHORT).show();
        } else if (flag2){
            Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this,"Please enter name!!",Toast.LENGTH_SHORT).show();
        }else if (flag3){
            Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this,
                    "Your password is too weak it should contain atleast 8 characters",Toast.LENGTH_SHORT).show();
        }else if (flag4){
            Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this,"Password mismatch !!",Toast.LENGTH_SHORT).show();
        }else{

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("1234", "1");

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this, "Verification mail sent",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(com.ranafkd.myproject.Activities.PoliceRegisterAcitivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



        }

    }
}

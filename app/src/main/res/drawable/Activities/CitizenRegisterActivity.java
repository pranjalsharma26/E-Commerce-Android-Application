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

import com.ranafkd.myproject.Pojo.CitizenUserPojo;
import com.ranafkd.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CitizenRegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText c_user_name, c_user_email, c_user_pass, c_user_confpass;
    //EditText  c_user_number;
    Button c_register_btn;
  //  Button c_verify_btn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_register);
        getSupportActionBar().setTitle("Missing Child");

        // Creating instance of Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CitizenDB");

        // Finding components by ID
        c_user_name = findViewById(R.id.citizen_edit_name);
        auth = FirebaseAuth.getInstance();

        //
        //c_user_number = findViewById(R.id.citizen_edit_number);
        //

        c_user_email = findViewById(R.id.citizen_edit_email);
        c_user_pass = findViewById(R.id.citizen_edit_pass);
        c_user_confpass = findViewById(R.id.citizen_edit_confpass);
        c_register_btn = findViewById(R.id.citizen_register_btn);
        //c_verify_btn = findViewById(R.id.citizen_verify_btn);


//        c_verify_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                verification();
//            }
//        });


        c_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCitizenSignUpData();
            }
        });
    }

    // Getting data from edittext and setting them to database
    private void getCitizenSignUpData(){

        final String cid = databaseReference.push().getKey();
        final String name = c_user_name.getText().toString();
        final String email = c_user_email.getText().toString();
     //   final String number = c_user_number.getText().toString();
        final String password = c_user_pass.getText().toString();
        final String confirmPass = c_user_confpass.getText().toString();
        Boolean flag1 = false;
        Boolean flag2 = false;
        Boolean flag3 = false;
        Boolean flag4 = false;



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
            Toast.makeText(com.ranafkd.myproject.Activities.CitizenRegisterActivity.this,"Invalid name entered !!",Toast.LENGTH_SHORT).show();
        } else if (flag2){
            Toast.makeText(com.ranafkd.myproject.Activities.CitizenRegisterActivity.this,"Invalid email entered !!",Toast.LENGTH_SHORT).show();
        }else if (flag3){
            Toast.makeText(com.ranafkd.myproject.Activities.CitizenRegisterActivity.this,
                    "Your password is too weak it should contain atleast 8 characters",Toast.LENGTH_SHORT).show();
        }else if (flag4){
            Toast.makeText(com.ranafkd.myproject.Activities.CitizenRegisterActivity.this,"Password mismatch !!",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(com.ranafkd.myproject.Activities.CitizenRegisterActivity.this, "Verification mail sent",Toast.LENGTH_SHORT).show();
                                    CitizenUserPojo pojo = new CitizenUserPojo();
                                    pojo.setId(cid);
                                    pojo.setName(name);
                                    pojo.setNumber(email);
                                    pojo.setPassword(password);

                                    // Inserting data in database
                                    databaseReference.child(cid).setValue(pojo);
                                    Intent intent = new Intent(com.ranafkd.myproject.Activities.CitizenRegisterActivity.this, LoginAcitivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else{
                                    Toast.makeText(com.ranafkd.myproject.Activities.CitizenRegisterActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(com.ranafkd.myproject.Activities.CitizenRegisterActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(com.ranafkd.myproject.Activities.CitizenRegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });












//            CitizenUserPojo pojo = new CitizenUserPojo();
//            pojo.setId(cid);
//            pojo.setName(name);
//            pojo.setNumber(email);
//            pojo.setPassword(password);
//
//            // Inserting data in database
//            databaseReference.child(cid).setValue(pojo);
//            Intent intent = new Intent(CitizenRegisterActivity.this, LoginAcitivity.class);
//            startActivity(intent);
//            finish();

        }


    }

//    private void verification() {
//
//        c_register_btn.setEnabled(true);
//        final String name = c_user_name.getText().toString();
//        final String email = c_user_email.getText().toString();
//        //   final String number = c_user_number.getText().toString();
//        final String password = c_user_pass.getText().toString();
//        final String confirmPass = c_user_confpass.getText().toString();
//        Boolean flag1 = false;
//        Boolean flag2 = false;
//        Boolean flag3 = false;
//        Boolean flag4 = false;
//
//
//        // Validation Code
//        if ( name.isEmpty() || name.trim().length() < 4 ){
//            flag1 = true;
//        }
//
//        if ( email.isEmpty()){
//            flag2 = true;
//        }
//
//        if ( password.isEmpty() || password.trim().length()<8 ){
//            flag3 = true;
//        }
//
//        if ( !password.equals(confirmPass) ){
//            flag4 = true;
//        }
//
//
//        if (flag1){
//            Toast.makeText(CitizenRegisterActivity.this,"Please enter name!!",Toast.LENGTH_SHORT).show();
//        } else if (flag2){
//            Toast.makeText(CitizenRegisterActivity.this,"Please enter email!!",Toast.LENGTH_SHORT).show();
//        }else if (flag3){
//            Toast.makeText(CitizenRegisterActivity.this,
//                    "Your password is too weak it should contain atleast 8 characters",Toast.LENGTH_SHORT).show();
//        }else if (flag4){
//            Toast.makeText(CitizenRegisterActivity.this,"Password mismatch !!",Toast.LENGTH_SHORT).show();
//        }else{
//
//
//            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                    if (task.isSuccessful()){
//
//                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Log.d("1234", "1");
//
//                                if (task.isSuccessful())
//                                {
//                                    Toast.makeText(CitizenRegisterActivity.this, "Verification mail sent",Toast.LENGTH_SHORT).show();
//                                }
//                                else{
//                                    Toast.makeText(CitizenRegisterActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }else{
//                        Toast.makeText(CitizenRegisterActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(CitizenRegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        }

}

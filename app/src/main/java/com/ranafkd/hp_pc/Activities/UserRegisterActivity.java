package com.ranafkd.hp_pc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ranafkd.hp_pc.Pojo.CustomerPojo;
import com.ranafkd.hp_pc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegisterActivity extends AppCompatActivity {

    EditText userName, userEmail, userPass, userConfirmPass, userNumber;
    Button registerButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);


        //Creating instances of firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CustomerDB");
        auth = FirebaseAuth.getInstance();


        // Finding the edit texts and button
        userName = findViewById(R.id.user_edit_name);
        userNumber = findViewById(R.id.user_edit_number);
        userEmail = findViewById(R.id.user_edit_email);
        userPass = findViewById(R.id.user_edit_pass);
        userConfirmPass = findViewById(R.id.user_edit_confpass);
        registerButton = findViewById(R.id.user_register_btn);
        
        
        //setting onclick listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putUserDataIntoDB();
            }
        });
    }

    private void putUserDataIntoDB() {

        final String cid = databaseReference.push().getKey();
        final String name = userName.getText().toString();
        final String number = userNumber.getText().toString();
        final String email = userEmail.getText().toString();
        final String password = userPass.getText().toString();
        final String confirmPass = userConfirmPass.getText().toString();
        Boolean flag1 = false;
        Boolean flag2 = false;
        Boolean flag3 = false;
        Boolean flag4 = false;
        Boolean flag5 = false;


        // Validation Code
        if ( name.isEmpty() || name.trim().length() < 4 ){
            flag1 = true;
        }

        if(number.length()<10){
            flag2 = true;
        }

        if ( email.isEmpty()){
            flag3 = true;
        }

        if ( password.isEmpty() || password.trim().length()<8 ){
            flag4 = true;
        }

        if ( !password.equals(confirmPass) ){
            flag5 = true;
        }

        if (flag1) {
            Toast.makeText(UserRegisterActivity.this, "Invalid name entered !!", Toast.LENGTH_SHORT).show();
        }else if(flag2){
            Toast.makeText(UserRegisterActivity.this,"Invalid number entered !!",Toast.LENGTH_SHORT).show();
        } else if (flag3){
            Toast.makeText(UserRegisterActivity.this,"Invalid email entered !!",Toast.LENGTH_SHORT).show();
        }else if (flag4){
            Toast.makeText(UserRegisterActivity.this,
                    "Your password is too weak it should contain atleast 8 characters",Toast.LENGTH_SHORT).show();
        }else if (flag5){
            Toast.makeText(UserRegisterActivity.this,"Password mismatch !!",Toast.LENGTH_SHORT).show();
        }else{

            progressDialog = new ProgressDialog(UserRegisterActivity.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();


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
                                    Toast.makeText(UserRegisterActivity.this, "Verification mail sent verify your mail first to login !!",
                                            Toast.LENGTH_SHORT).show();
                                    CustomerPojo pojo = new CustomerPojo();
                                    pojo.setId(cid);
                                    pojo.setName(name);
                                    pojo.setNumber(number);
                                    pojo.setEmail(email);
                                    pojo.setPass(password);

                                    // Inserting data in database
                                    databaseReference.child(cid).setValue(pojo);
                                    Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.cancel();
                                }
                                else{
                                    progressDialog.cancel();
                                    Toast.makeText(UserRegisterActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        progressDialog.cancel();
                        Toast.makeText(UserRegisterActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.cancel();
                    Toast.makeText(UserRegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}

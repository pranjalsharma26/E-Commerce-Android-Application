package com.ranafkd.myproject.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ranafkd.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PoliceForgetPasswordActivity extends AppCompatActivity {

    EditText pemail;
    Button btn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_forget_password);

        pemail = findViewById(R.id.police_forget_pass_email);
        btn = findViewById(R.id.p_forget_btn);
        auth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(pemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(com.ranafkd.myproject.Activities.PoliceForgetPasswordActivity.this,"Link is sent to you mail",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(com.ranafkd.myproject.Activities.PoliceForgetPasswordActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    }


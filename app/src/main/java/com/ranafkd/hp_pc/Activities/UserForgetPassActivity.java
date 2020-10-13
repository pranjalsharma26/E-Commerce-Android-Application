package com.ranafkd.hp_pc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ranafkd.hp_pc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserForgetPassActivity extends AppCompatActivity {

    EditText text;
    Button forget;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forget_pass);

        text = findViewById(R.id.forget_pass_edittext);
        forget = findViewById(R.id.forget);
        auth = FirebaseAuth.getInstance();

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(UserForgetPassActivity.this);
                progressDialog.setTitle("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                auth.sendPasswordResetEmail(text.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.cancel();
                            Toast.makeText(UserForgetPassActivity.this,"Link is sent to you mail",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserForgetPassActivity.this, UserLoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            progressDialog.cancel();
                            Toast.makeText(UserForgetPassActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

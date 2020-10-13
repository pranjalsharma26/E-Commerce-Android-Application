package com.ranafkd.hp_pc.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ranafkd.hp_pc.R;

public class MyCartActivity extends AppCompatActivity {

    Button checkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        checkout = findViewById(R.id.checkout_button);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCartActivity.this, UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

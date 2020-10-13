package com.ranafkd.hp_pc.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ranafkd.hp_pc.R;

public class AddressActivity extends AppCompatActivity {

    EditText address;
    Button proceed;
    Boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        address = findViewById(R.id.orderAddress);
        proceed = findViewById(R.id.addProceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String add = address.getText().toString();
                String error = "";

                if(add.isEmpty()){
                    flag = true;
                    error = "Please enter a place to ship.";
                }
                if(add.length()<6){
                    flag = true;
                    error += " Address seems inapropriate. ";
                }

                if(!flag) {
                    Log.d("789456", "onClick: address of address = " + add);
                    String id = getIntent().getStringExtra("id");
                    String name = getIntent().getStringExtra("name");
                    String price = getIntent().getStringExtra("price");
                    String description = getIntent().getStringExtra("description");
                    String count = getIntent().getStringExtra("count");

                    Log.d("789456", "onClick: address all val id = " + id + " name= " + name + " price = " + price + " count = " + count);

                    Intent intent = new Intent(AddressActivity.this, TotalBuy.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("price", price);
                    intent.putExtra("description", description);
                    intent.putExtra("count", count + "");
                    intent.putExtra("address", add);
                    startActivity(intent);
                    finish();
                } else{
                    Log.d("789456", "onClick: address incorrect");
                    Toast.makeText(AddressActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

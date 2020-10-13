package com.ranafkd.hp_pc.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ranafkd.hp_pc.Adapter.CartAdapter;
import com.ranafkd.hp_pc.Pojo.CartPojo;
import com.ranafkd.hp_pc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    CartAdapter myOrderAdapter;
    List<CartPojo> orderList;
    FirebaseDatabase fdb;
    DatabaseReference db;
    ArrayList<CartPojo> arrayList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

//        progress bar
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

//        instancing all required classes
        orderList=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        get uid from shared preferences
        SharedPreferences sp = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String uid = sp.getString("id","");
        Boolean stat = sp.getBoolean("loginstatus",false);
        Log.d("123456", "onCreate: myorder uid = "+uid+" status "+stat);

//        get order details from db using uid
        fdb = FirebaseDatabase.getInstance();
        db = fdb.getReference("CartDBName");

        if(stat) {
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        CartPojo op = ds.getValue(CartPojo.class);
                        String oUid = op.getUid();
                        if (uid != null) {
                            if (oUid.equals(uid)) {
                                Log.d("123456", "onDataChange: my order db uid = " + oUid);
                                arrayList.add(op);
                                //set data on adapter

                            } else {
                                Toast.makeText(CartActivity.this, "No items available...Please buy something", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CartActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CartActivity.this, UserLoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    myOrderAdapter = new CartAdapter(arrayList, CartActivity.this);
                    recyclerView.setAdapter(myOrderAdapter);
                    myOrderAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.cancel();
                    Toast.makeText(CartActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            });

            //end progress bar
            progressDialog.cancel();
        } else{
            Toast.makeText(CartActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CartActivity.this, UserLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

//    prevent cart activity from destroying on pressing back button
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, Dashboard.class);
//        startActivity(intent);
//    }
}

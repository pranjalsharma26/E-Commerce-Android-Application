package com.ranafkd.hp_pc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ranafkd.hp_pc.Adapter.ItemsCustomAdapter;
import com.ranafkd.hp_pc.Pojo.ItemsPojo;
import com.ranafkd.hp_pc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemsActivity extends AppCompatActivity {

    int count=0;
    Toolbar toolbar;
    ImageButton imageButton;
    TextView textView;
    MenuItem menuItem;

    public String category, cat1, cat2, cat3, cat4;
    boolean doubleBackToExitPressedOnce = false;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<ItemsPojo> arrayList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        //getSupportActionBar().setTitle("All Items");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        toolbar = findViewById(R.id.toolbar_item_list_activity);
        setSupportActionBar(toolbar);

        category = getIntent().getStringExtra("category");
        cat1 = getIntent().getStringExtra("cat1");
        cat2 = getIntent().getStringExtra("cat2");
        cat3 = getIntent().getStringExtra("cat3");
        cat4 = getIntent().getStringExtra("cat4");
        Log.d("12345690",""+category+"~~~"+cat1+" "+cat2+" "+cat3+" "+cat4);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ItemsDbName");

        recyclerView = findViewById(R.id.items_list_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getItemsDataFromFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.cart2, menu);
        menuItem = menu.findItem(R.id.cart_menu_item1);

        View actionView = menuItem.getActionView();

        if(actionView != null){
            textView = actionView.findViewById(R.id.textView2);
            imageButton = actionView.findViewById(R.id.image_btn);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemsActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void getItemsDataFromFirebase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    ItemsPojo members = dataSnapshot1.getValue(ItemsPojo.class);
                    String cate = dataSnapshot1.child("categogy").getValue(String.class);
                    Log.d("1234561","cate : "+cate+"="+category);
                    if(category==null){
                        if (cat1.equals(cate.toLowerCase()) || cat2.equals(cate.toLowerCase()) || cat3.equals(cate.toLowerCase())
                                || cat4.equals(cate.toLowerCase())){
                            Log.d("123456", "onDataChange: if cat = "+cate);
                            arrayList.add(members);
                            Log.d("123456", "onDataChange: prod id 0 = "+members.getPid());
                            count++;
                        }
                        if(count==0){
                            Toast.makeText(ItemsActivity.this, "Sorry! There are no items in this category", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        if (category.equals(cate)){
                            Log.d("123456", "onDataChange: if cat = "+cate);
                            arrayList.add(members);
                            Log.d("123456", "onDataChange: prod id 0 = "+members.getPid());
                            count++;
                        }
                        if(count==0){
                            Toast.makeText(ItemsActivity.this, "Sorry! There are no items in this category", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                progressDialog.cancel();
                ItemsCustomAdapter customAdapter = new ItemsCustomAdapter(arrayList, ItemsActivity.this);
                recyclerView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(ItemsActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
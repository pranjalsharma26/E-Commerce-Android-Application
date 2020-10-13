package com.ranafkd.myproject.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ranafkd.myproject.Adapters.SearchCustomAdapter;
import com.ranafkd.myproject.Pojo.SearchPojo;
import com.ranafkd.myproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<SearchPojo> arrayList = new  ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    ImageView back_button, search_btn;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("MissedDB");

        recyclerView = findViewById(R.id.search_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //Toolbar code----------------Starts
        back_button = findViewById(R.id.back_image_btn);
        search = findViewById(R.id.search_bar);
        search_btn = findViewById(R.id.find);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromFirebase();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.ranafkd.myproject.Activities.SearchActivity.this,CitizenDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        //Toolbar code----------------Ends
    }

    private void getDataFromFirebase() {

        final String edit_name = search.getText().toString();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (edit_name != null || edit_name != ""){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        SearchPojo pojo = dataSnapshot1.getValue(SearchPojo.class);

                        if (edit_name.equals(pojo.getName())){
                            arrayList.add(pojo);
                        }
                        Log.d("2345",""+pojo.getDescription()+""+pojo.getName()+"");
                    }
                }else {
                    Toast.makeText(com.ranafkd.myproject.Activities.SearchActivity.this,"No Result Found",Toast.LENGTH_SHORT).show();
                }

               // progressDialog.cancel();
                SearchCustomAdapter adapter = new SearchCustomAdapter(arrayList, com.ranafkd.myproject.Activities.SearchActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(com.ranafkd.myproject.Activities.SearchActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

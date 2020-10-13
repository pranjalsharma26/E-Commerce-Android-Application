package com.ranafkd.hp_pc.Activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ranafkd.hp_pc.Adapter.CategoryCustomAdapter;
import com.ranafkd.hp_pc.Pojo.CategoryPojo;
import com.ranafkd.hp_pc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {


//    String count;
//    Toolbar toolbar;
//    ImageButton imageButton;
//    TextView textView;
//    MenuItem menuItem;

    boolean doubleBackToExitPressedOnce = false;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<CategoryPojo> arrayList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setTitle("Categories");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //toolbar
//        toolbar = findViewById(R.id.toolbar_category_list);
//        setSupportActionBar(toolbar);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CategoryList");

        recyclerView = findViewById(R.id.category_recycle_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getCategoryFromDatabase();
    }

    // Code twice back press
//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }

//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.cart2, menu);
//        menuItem = menu.findItem(R.id.cart_menu_item1);
//
//        View actionView = menuItem.getActionView();
//
//        if(actionView != null){
//            textView = actionView.findViewById(R.id.textView2);
//            imageButton = actionView.findViewById(R.id.image_btn);
//        }
//
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CategoryActivity.this,MyCartActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }

    private void getCategoryFromDatabase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    CategoryPojo members = dataSnapshot1.getValue(CategoryPojo.class);
                    arrayList.add(members);
                }
                progressDialog.cancel();
                CategoryCustomAdapter customAdapter = new CategoryCustomAdapter(arrayList, CategoryActivity.this);
                recyclerView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(CategoryActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

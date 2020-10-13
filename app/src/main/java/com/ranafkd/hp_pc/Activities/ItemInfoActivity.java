package com.ranafkd.hp_pc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ranafkd.hp_pc.Pojo.CartPojo;
import com.ranafkd.hp_pc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemInfoActivity extends AppCompatActivity {

    FirebaseDatabase fdb;
    DatabaseReference db;

    public int count=0;
    Toolbar toolbar;
    TextView textView_count;
    Button buttonPlus, buttonMinus, buy_now, addToCart;
    ImageButton imageButton;
    TextView textView;
    MenuItem menuItem;
    FirebaseAuth auth;
    //boolean doubleBackToExitPressedOnce = false;

    public String id, name, price, description,url, category;
    ImageView item_image;
    TextView item_name, item_description, item_price;

    ImageButton  cart;              //In toolbar.xml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);


        //getting intent
        id = getIntent().getStringExtra("pid");
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        category = getIntent().getStringExtra("category");
        description = getIntent().getStringExtra("description");
        url = getIntent().getStringExtra("url");

        toolbar = findViewById(R.id.toolbar_item_list);
        setSupportActionBar(toolbar);
        buy_now = findViewById(R.id.buy_now);
        buttonPlus = findViewById(R.id.plus_btn);
        buttonMinus = findViewById(R.id.minus_btn);
        textView_count = findViewById(R.id.text_count);
        addToCart = findViewById(R.id.addToCart);


        item_image = findViewById(R.id.product_image);
        item_name = findViewById(R.id.product_name);
        item_description = findViewById(R.id.product_description);
        item_price = findViewById(R.id.product_price);

        item_name.setText(name);
        item_price.setText(price);
        item_description.setText(description);
        Glide.with(ItemInfoActivity.this).load(url).into(item_image);

        //Buy Now Code
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
                Boolean status = sharedPreferences.getBoolean("loginstatus", false);
                if (status)
                {
                    if (count==0){
                        Toast.makeText(ItemInfoActivity.this,"Select item first !!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(ItemInfoActivity.this, AddressActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("name",name);
                        intent.putExtra("price",price);
                        intent.putExtra("description",description);
                        intent.putExtra("count",count+"");
                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(ItemInfoActivity.this, "You need to login first!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ItemInfoActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //add to cart core
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("userdata", Context.MODE_PRIVATE);
                Boolean stat = sp.getBoolean("loginstatus", false);
                String uid = sp.getString("id", "");
                if(stat){
                    if (count==0){
                        Toast.makeText(ItemInfoActivity.this,"Select item first !!",Toast.LENGTH_SHORT).show();
                    } else {
                        fdb = FirebaseDatabase.getInstance();
                        db = fdb.getReference("CartDBName");
                        CartPojo cp = new CartPojo();
                        cp.setUid(uid);
                        cp.setQuantity(count+"");
                        cp.setImageUrl(url);
                        cp.setDescription(description);
                        cp.setPrice(price);
                        cp.setName(name);
                        cp.setPid(id);
                        String cid = db.push().getKey();
                        cp.setCid(cid);
                        db.child(cid).setValue(cp);
                        Toast.makeText(ItemInfoActivity.this, "Item added to cart successfully!!", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(ItemInfoActivity.this, "Login to start adding items to cart!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ItemInfoActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.cart, menu);
        menuItem = menu.findItem(R.id.cart_menu_item);

        View actionView = menuItem.getActionView();

        if(actionView != null){
            textView = actionView.findViewById(R.id.textView2);
            imageButton = actionView.findViewById(R.id.image_btn);
        }

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Increament();
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Decrement();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
                Boolean status = sharedPreferences.getBoolean("loginstatus", false);
                if (status){
                    if (count==0)
                        Toast.makeText(ItemInfoActivity.this,"Select item first !!",Toast.LENGTH_SHORT).show();
                    else {
                        Intent intent = new Intent(ItemInfoActivity.this,CartActivity.class);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(ItemInfoActivity.this,"You need to login first!!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ItemInfoActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void Decrement() {
        if(count == 0){
            Toast.makeText(ItemInfoActivity.this,"Choose item first !!",Toast.LENGTH_SHORT).show();
        }else{
            count--;
            textView.setText(String.valueOf(count));
            textView_count.setText(String.valueOf(count));
        }
    }

    private void Increament() {

        if(count == 5){
            Toast.makeText(ItemInfoActivity.this,"You can't select more than 5 items at once.",Toast.LENGTH_SHORT).show();
        }else{
            ++count;
            textView.setText(String.valueOf(count));
            textView_count.setText(String.valueOf(count));
        }
    }
}


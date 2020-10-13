package com.ranafkd.hp_pc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ranafkd.hp_pc.Activities.ItemInfoActivity;
import com.ranafkd.hp_pc.Pojo.ItemsPojo;
import com.ranafkd.hp_pc.R;

import java.util.ArrayList;

public class ItemsCustomAdapter extends RecyclerView.Adapter<ItemsCustomAdapter.ViewHolder> {


    //    int count;
//    Toolbar toolbar;
//    Button add_to_cart;
//    ImageButton imageButton;
    TextView textView;
    //    MenuItem menuItem;
    private ArrayList<ItemsPojo> arrayList;
    private Context context;

    public ItemsCustomAdapter(ArrayList<ItemsPojo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        //Log.d("123456", "ItemsCustomAdapter: abc");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        //Log.d("123456", "ItemsCustomAdapter: def");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.items_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ItemsPojo myMembers = arrayList.get(position);
        holder.item_name.setText(myMembers.getName());
        holder.item_price.setText(myMembers.getPrice());
        Glide.with(context).load(myMembers.getImageUrl()).into(holder.item_img);
        //Log.d("123456", "ItemsCustomAdapter: ghi name = "+myMembers.getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("123456", "ItemsCustomAdapter: jkl ");
                Intent intent = new Intent(context, ItemInfoActivity.class);
                intent.putExtra("pid", myMembers.getPid());
                Log.d("123456", "onClick: prod id 1 = "+myMembers.getPid());
                intent.putExtra("name", myMembers.getName());
                intent.putExtra("price", myMembers.getPrice());
                intent.putExtra("description", myMembers.getDescription());
                intent.putExtra("url", myMembers.getImageUrl());

                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_name, item_price;
        public ImageView item_img;
        public Button add_to_cart;
        public LinearLayout linearLayout;
        public ImageButton imageButton;

        public ViewHolder(View v) {
            super(v);

            //getting the id's of all elements
            item_name = v.findViewById(R.id.item_name);
            item_price = v.findViewById(R.id.item_price);
            item_img = v.findViewById(R.id.item_img);
            imageButton = v.findViewById(R.id.image_btn);
            linearLayout = v.findViewById(R.id.linearlayout);
            //textView = v.findViewById(R.id.textView2);
            //add_to_cart = v.findViewById(R.id.add_to_cart_btn_list);
        }
    }
}
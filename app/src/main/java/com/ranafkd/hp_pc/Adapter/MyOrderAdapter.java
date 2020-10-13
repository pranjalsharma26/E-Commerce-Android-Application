package com.ranafkd.hp_pc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ranafkd.hp_pc.Activities.ItemInfoActivity;
import com.ranafkd.hp_pc.Pojo.ItemsPojo;
import com.ranafkd.hp_pc.Pojo.oderPojo;
import com.ranafkd.hp_pc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrderAdapter extends  RecyclerView.Adapter<MyOrderAdapter.ViewHolder>{

    ArrayList<oderPojo> arrayList;
    Context context;
    public String pname, pimg, pprice, pdesc;

    public MyOrderAdapter(ArrayList<oderPojo> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
        Log.d("123456", "MyOrderAdapter: absed");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.my_order_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        Log.d("123456", "MyOrderAdapter: holdererr");
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final oderPojo op = arrayList.get(position);

//        get the product name
        final String pid = op.getPid();
//        getProdtDetails(pid);

        FirebaseDatabase afdb = FirebaseDatabase.getInstance();
        DatabaseReference adb = afdb.getReference("DBNameofItems");
        adb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ItemsPojo ip = ds.getValue(ItemsPojo.class);
                    String ipId = ip.getPid();
                    Log.d("1234567", "onDataChange: my order adapter pid ondatachange = "+ipId);
                    if(pid!=null){
                        if(ipId.equals(pid)){
                            pname = ip.getName();
                            pimg = ip.getImageUrl();
                            pprice = ip.getPrice();
                            pdesc = ip.getDescription();
//                            Log.d("1234567", "onDataChange: my order adapter pname = "+pname);
//                            Log.d("1234567", "onDataChange: my order adapter img = "+pimg);
                        }
                    } else{
                        Toast.makeText(context, "Sorry System Error.....Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

                Log.d("1234567", "onDataChange: my order adapter pname before holder = "+pname);
                Log.d("1234567", "onDataChange: my order adapter img before holder = "+pimg);
                holder.iName.setText(op.getPname());
                holder.iPrice.setText(op.getTotal());
                holder.iQuant.setText(op.getCount());
                Glide.with(context).load(pimg).into(holder.iImg);
                holder.iBlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ItemInfoActivity.class);
                        intent.putExtra("pid", pid);
                        // Log.d("123456", "onClick: my order prod id 1 = "+pid);
                        intent.putExtra("name", pname);
                        intent.putExtra("price", pprice);
                        intent.putExtra("description", pdesc);
                        intent.putExtra("url", pimg);

                        context.startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        instantiate all views
        public TextView iName, iPrice, iQuant;
        public ImageView iImg;
        public RelativeLayout iBlock;

        public ViewHolder(View v) {
            super(v);

            Log.d("123456", "onDataChange: set ho gya ");

//            linking views with instances
            iName = v.findViewById(R.id.myOrderItemName);
            iPrice = v.findViewById(R.id.myOrderItemPrice);
            iQuant = v.findViewById(R.id.myOrderItemQuant);
            iImg = v.findViewById(R.id.myOrderItemImg);
            iBlock = v.findViewById(R.id.myOrderBlock);
        }
    }
}

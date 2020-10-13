package com.ranafkd.hp_pc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ranafkd.hp_pc.Activities.AddressActivity;
import com.ranafkd.hp_pc.Pojo.CartPojo;
import com.ranafkd.hp_pc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ArrayList<CartPojo> arrayList;
    Context context;
    public String pname, pimg, pprice, pdesc, pquant, ptotal;

    public CartAdapter(ArrayList<CartPojo> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.cart_detail, parent, false);
        ViewHolder vh = new ViewHolder(v);
        Log.d("123456", "MyOrderAdapter: holdererr");
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CartPojo op = arrayList.get(position);

//        get the product name
        final String pid = op.getPid();

//        get the prod details from db
        FirebaseDatabase afdb = FirebaseDatabase.getInstance();
        final DatabaseReference adb = afdb.getReference("CrtDBName");
        adb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    CartPojo ip = ds.getValue(CartPojo.class);
                    String ipId = ip.getPid();
                    Log.d("1234567", "onDataChange: my order adapter pid ondatachange = "+ipId);
                    if(pid!=null){
                        if(ipId.equals(pid)){
                            pname = ip.getName();
                            pimg = ip.getImageUrl();
                            pprice = ip.getPrice();
                            pdesc = ip.getDescription();
                            pquant = ip.getQuantity();
//                            calculate the total price of product
                            ptotal = (Integer.parseInt(pprice)*Integer.parseInt(pquant))+"";

                            Log.d("1234567", "onDataChange: my order adapter pname = "+pname);
                        }
                    } else{
                        Toast.makeText(context, "Sorry System Error.....Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

                Log.d("1234567", "onDataChange: my order adapter pname before holder = "+pname);
                Log.d("1234567", "onDataChange: my order adapter img before holder = "+pimg);
                holder.cartName.setText(pname);
                holder.cartPrice.setText(ptotal);
                holder.cartQuant.setText(pquant);
                Glide.with(context).load(pimg).into(holder.cartImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.cartBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddressActivity.class);
                // Log.d("123456", "onClick: my order prod id 1 = "+pid);
                intent.putExtra("id",op.getPid());
                intent.putExtra("name", op.getName());
                intent.putExtra("price", op.getPrice());
                intent.putExtra("description", op.getDescription());
//                CartPojo xy = arrayList.get(position);
//                String quant = xy.getQuantity();
                intent.putExtra("count",op.getQuantity());

                context.startActivity(intent);

            }
        });

//                delete product from cart and db
        holder.cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartPojo c = arrayList.get(position);
                String cartId = c.getCid();
                adb.child(cartId).removeValue();
                Toast.makeText(context, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                arrayList.remove(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        instantiate all views
        RelativeLayout cartBlock;
        TextView cartName, cartQuant, cartPrice;
        ImageView cartImg;
        Button cartDelete;

        public ViewHolder( View v) {
            super(v);

//            link instances with the elements
            cartBlock = v.findViewById(R.id.cartBlock);
            cartName = v.findViewById(R.id.cartName);
            cartQuant = v.findViewById(R.id.cartQuant);
            cartPrice = v.findViewById(R.id.cartPrice);
            cartImg = v.findViewById(R.id.cartImg);
            cartDelete = v.findViewById(R.id.cartDelete);

        }
    }
}

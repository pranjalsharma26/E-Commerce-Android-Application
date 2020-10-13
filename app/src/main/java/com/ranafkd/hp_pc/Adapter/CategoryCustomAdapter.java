package com.ranafkd.hp_pc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ranafkd.hp_pc.Activities.ItemsActivity;
import com.ranafkd.hp_pc.Pojo.CategoryPojo;
import com.ranafkd.hp_pc.R;

import java.util.ArrayList;

public class CategoryCustomAdapter extends RecyclerView.Adapter<CategoryCustomAdapter.ViewHolder> {

    private ArrayList<CategoryPojo> arrayList;
    private Context context;

    public CategoryCustomAdapter(ArrayList<CategoryPojo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.category_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final CategoryPojo myMembers = arrayList.get(position);
        holder.category.setText(myMembers.getCategory());
        Glide.with(context).load(myMembers.getImageUrl()).into(holder.image);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemsActivity.class);
                intent.putExtra("category", myMembers.getCategory());
                //Log.d("098",""+myMembers.getCategory());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public ImageView image;
        public LinearLayout linearLayout;
        public ViewHolder(View v){
            super(v);

            image = v.findViewById(R.id.category_image_recycler);
            category = v.findViewById(R.id.category_name);
            linearLayout = v.findViewById(R.id.category_layout);
        }
    }

}

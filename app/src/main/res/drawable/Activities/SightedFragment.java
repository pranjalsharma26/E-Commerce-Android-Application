package com.ranafkd.myproject.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ranafkd.myproject.Adapters.SightedCustomAdapter;
import com.ranafkd.myproject.Pojo.SightedPojo;
import com.ranafkd.myproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SightedFragment extends Fragment {

    public Long i = 0L;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<SightedPojo> arrayList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sighted, container, false);

        recyclerView = view.findViewById(R.id.Sighted_recycler);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("SightedDB");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getSightedDataFromFirebase();

        return view;
    }

    private void getSightedDataFromFirebase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    SightedPojo pojo = dataSnapshot1.getValue(SightedPojo.class);
                    arrayList.add(pojo);
                }
                /*
                i = dataSnapshot.getChildrenCount();
                Bundle bundle = new Bundle();
                bundle.putLong("sighted",i);
                StatsFragment statsFragment = new StatsFragment();
                statsFragment.setArguments(bundle);
                */
                SightedCustomAdapter adapter = new SightedCustomAdapter(arrayList, getActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

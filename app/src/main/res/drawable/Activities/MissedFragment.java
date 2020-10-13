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

import com.ranafkd.myproject.Adapters.MissedCustomAdapter;
import com.ranafkd.myproject.Pojo.MissedPojo;
import com.ranafkd.myproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MissedFragment extends Fragment {

    public Long i = 0L;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<MissedPojo> arrayList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_missed, container, false);

        recyclerView = view.findViewById(R.id.Missed_recycler);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("MissedDB");
        
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getMissedDataFromFirebase();

        return view;
    }

    private void getMissedDataFromFirebase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    MissedPojo members = dataSnapshot1.getValue(MissedPojo.class);
                    arrayList.add(members);
                }
                /*
                i = dataSnapshot.getChildrenCount();
                Log.d("0123",""+i);
                Bundle bundle = new Bundle();
                bundle.putLong("miss",i);
                StatsFragment statsFragment = new StatsFragment();
                statsFragment.setArguments(bundle);
                */
                MissedCustomAdapter adapter = new MissedCustomAdapter(arrayList, getActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
/*
       int i = arrayList.size();
       Bundle bundle = new Bundle();
       bundle.putInt("miss", i);
       StatsFragment statsFragment = new StatsFragment();
       statsFragment.setArguments(bundle);
*/
    }
}

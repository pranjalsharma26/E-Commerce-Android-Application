package com.ranafkd.myproject.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ranafkd.myproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class StatsFragment extends Fragment {

    public Long miss;
    public Long sighted;
    FirebaseDatabase database;
    DatabaseReference reference, reference1;
    long sightedCount, missedCount;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_stats, container, false);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("MissedDB");
        reference1 = database.getReference("SightedDB");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("1234", "onDataChange: "+dataSnapshot.getChildrenCount());
                missedCount = dataSnapshot.getChildrenCount();
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        sightedCount = dataSnapshot.getChildrenCount();

                        Long count[] = { missedCount , sightedCount};
                        String type[] = {"Missed", "Sighted"};

                        List<PieEntry> pieEntries = new ArrayList<>();
                        for (int i=0; i<count.length; i++){
                            pieEntries.add(new PieEntry(count[i], type[i]));
                        }

                        PieDataSet dataSet = new PieDataSet(pieEntries, "Analysis");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        PieData data = new PieData(dataSet);

                        PieChart chart = view.findViewById(R.id.pieChart);
                        chart.setData(data);
                        chart.animateY(2000);
                        chart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Record Not Found", Toast.LENGTH_SHORT).show();
            }
        });

        //Bundle bundle = getArguments();
       // miss = getArguments().getLong("miss");
       // sighted = getArguments().getLong("sighted");

      //  Log.d("01",""+miss+":"+sighted);

        // Code for pie chart




        return view;
    }
}

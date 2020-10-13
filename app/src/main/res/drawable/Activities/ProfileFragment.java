package com.ranafkd.myproject.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ranafkd.myproject.R;

public class ProfileFragment extends Fragment {

    TextView p_profile_name, p_profile_number, p_profile_pass;
    Button logout_police, police_change_pass_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        p_profile_name = view.findViewById(R.id.police_text_name);
        p_profile_number = view.findViewById(R.id.police_text_number);
        p_profile_pass = view.findViewById(R.id.police_text_password);
        logout_police = view.findViewById(R.id.police_logout);
        police_change_pass_btn = view.findViewById(R.id.police_change_pass);

        setViewProfile();

        logout_police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policeLogout();
            }
        });

        police_change_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PoliceChangePassActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    public void setViewProfile(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("policedata", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String number = sharedPreferences.getString("number", "");
        String pass = sharedPreferences.getString("password", "");

        p_profile_name.setText(name);
        p_profile_number.setText(number);
        p_profile_pass.setText(pass);

    }

    private void policeLogout() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("policedata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(getActivity(), com.ranafkd.myproject.Activities.LoginAcitivity.class);
        startActivity(intent);
        getActivity().finish();

    }
}

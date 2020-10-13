package com.ranafkd.myproject.Activities;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ranafkd.myproject.Pojo.PoliceUserPojo;
import com.ranafkd.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class PoliceLoginFragment extends Fragment {

    EditText p_login_email, p_login_password;
    Button p_login_btn;
    TextView forget;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    int flag = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.plogin_frag, container, false);
        p_login_email = view.findViewById(R.id.police_login_email);
        p_login_password = view.findViewById(R.id.police_login_pass);
        p_login_btn = view.findViewById(R.id.police_login);
        forget = view.findViewById(R.id.police_forget_txt);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PoliceForgetPasswordActivity.class);
                startActivity(intent);
            }
        });


        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("PoliceDB");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("policedata", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("loginstatus", false)){

            Intent intent = new Intent(getActivity(), com.ranafkd.myproject.Activities.PoliceDashboard.class);
            startActivity(intent);
            getActivity().finish();
        }

        p_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("1234","onclick");
                setPoliceLoginData();
            }
        });
        return view;
    }



    private void setPoliceLoginData() {
        //  Log.d("1234","function");
        String email = p_login_email.getText().toString();
        String password = p_login_password.getText().toString();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){


                    if (auth.getCurrentUser().isEmailVerified()) {

                        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot snapshot : dataSnapshot.getChildren() ){
         //           Log.d("1234","loop");
                    PoliceUserPojo pojo = snapshot.getValue(PoliceUserPojo.class);
                    String cid = pojo.getId();
                    String name = pojo.getName();
                    String number = pojo.getNumber();
                    String password = pojo.getPassword();

                    if ( number.equals(p_login_email.getText().toString())){
                     //   Log.d("1234","check if 1");
                        flag = 1;
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("policedata", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id",cid);
                        editor.putString("name",name);
                        editor.putString("number",number);
                        editor.putString("password",password);
                        editor.putBoolean("loginstatus", true);
                        editor.commit();
                        break;

                    }
                }
                if (flag == 1) {
                //    Log.d("1234","check if 2");
                    Intent intent = new Intent(getActivity(), com.ranafkd.myproject.Activities.PoliceDashboard.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
                    }
                    else{
                        Toast.makeText(getActivity(), "Not Verify", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





//    private void setPoliceLoginData() {
//      //  Log.d("1234","function");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for( DataSnapshot snapshot : dataSnapshot.getChildren() ){
//         //           Log.d("1234","loop");
//                    PoliceUserPojo pojo = snapshot.getValue(PoliceUserPojo.class);
//                    String cid = pojo.getId();
//                    String name = pojo.getName();
//                    String number = pojo.getNumber();
//                    String password = pojo.getPassword();
//
//                    if ( number.equals(p_login_number.getText().toString()) && password.equals(p_login_password.getText().toString()) ){
//                     //   Log.d("1234","check if 1");
//                        flag = 1;
//                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("policedata", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("id",cid);
//                        editor.putString("name",name);
//                        editor.putString("number",number);
//                        editor.putString("password",password);
//                        editor.putBoolean("loginstatus", true);
//                        editor.commit();
//                        break;
//
//                    }
//                }
//                if (flag == 1) {
//                //    Log.d("1234","check if 2");
//                    Intent intent = new Intent(getActivity(), PoliceDashboard.class);
//                    startActivity(intent);
//                    getActivity().finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}

package com.ranafkd.myproject.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ranafkd.myproject.Helper.SelectImageHelper;
import com.ranafkd.myproject.Pojo.SightedPojo;
import com.ranafkd.myproject.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SightedActivity extends AppCompatActivity {

    EditText sighted_name, sighted_age, sighted_description, sighted_number, sighted_location;
   // TextView sighted_location;
    Button sighted_btn;
    ImageView imageView, slocation_btn;
    RadioGroup radioGroup;
    RadioButton radioButton;
    FirebaseDatabase firebaseDatabase;
    SelectImageHelper selectImageHelper;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    //For getting location
    private static final int REQUEST_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighted);
        getSupportActionBar().setTitle("Missing Child");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("SightedDB");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        sighted_location = findViewById(R.id.sighted_location);
        sighted_age = findViewById(R.id.sighted_age);
        sighted_name = findViewById(R.id.sighted_name);
        sighted_description = findViewById(R.id.sighted_descrip);
        sighted_btn = findViewById(R.id.sighted_btn);
        radioGroup = findViewById(R.id.sighted_radio_group);
        int selectID = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectID);
        imageView = findViewById(R.id.add_simg_btn);
        sighted_number = findViewById(R.id.user_number_sighted);
        slocation_btn = findViewById(R.id.gets_location_btn);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Onclick for image selection
        selectImageHelper = new SelectImageHelper(this, imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("12345","check");
                selectImageHelper.selectImageOption();
            }
        });

        // Onclick for submit
        sighted_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSightedData();
            }
        });


        // Onclick for getting location
        slocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });
    }



    //Function to get location
    private void fetchLocation() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(com.ranafkd.myproject.Activities.SightedActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("12345", "fetchLocation: 1");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(com.ranafkd.myproject.Activities.SightedActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("12345", "fetchLocation: 3");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Requered Location Permission")
                        .setMessage("You need to allow for this permission to proceed")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(com.ranafkd.myproject.Activities.SightedActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_PERMISSION);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .create()
                        .show();

            } else {
                Log.d("12345", "fetchLocation: 4");
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(com.ranafkd.myproject.Activities.SightedActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            Log.d("12345", "fetchLocation: sdfsdf");
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if ( location != null ){
                                Log.d("12345", "fetchLocation: 5");
                                Double latitude = location.getLatitude();
                                Double longitude = location.getLongitude();

                                try{
                                    Geocoder geocoder = new Geocoder(com.ranafkd.myproject.Activities.SightedActivity.this, Locale.getDefault());
                                    List<Address>addresses =geocoder.getFromLocation(latitude, longitude, 1);
                                    String add = addresses.get(0).getSubLocality();
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();

                                    sighted_location.setText(add+", "+city+", "+state);
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

        }


    }



    // Override for getting location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{

            }
        }

        selectImageHelper.handleGrantedPermisson(requestCode, grantResults);
    }


    public void onChecked(View view){
        int selectID = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectID);
        if(selectID==-1){
            Toast.makeText(com.ranafkd.myproject.Activities.SightedActivity.this,"Please select gender", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
        selectImageHelper.handleResult(requestCode, resultCode, result);  // call this helper class method
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        selectImageHelper.handleGrantedPermisson(requestCode, grantResults);    // call this helper class method
//    }

    private void getSightedData() {

        final String id = databaseReference.push().getKey();
        final String name = sighted_name.getText().toString();
        final String location = sighted_location.getText().toString();
        final String age = sighted_age.getText().toString();
        final String description = sighted_description.getText().toString();
        final String gender = radioButton.getText().toString();
        final String user_no = sighted_number.getText().toString();
        //   Log.d("1234","gender: "+gender);
        Uri uri = selectImageHelper.getURI_FOR_SELECTED_IMAGE();
        Log.d("1234",""+uri);


        Boolean f1 = false;
        Boolean f2 = false;
        Boolean f3 = false;
        Boolean f4 = false;
        Boolean f5 = false;
        Boolean f6 = false;

        // Validation Code

        if (location.isEmpty()) {
            f2 = true;
        }
        if (name.isEmpty() || name.trim().length() < 4) {
            f1 = true;
        }
        if (age.isEmpty()) {
            f3 = true;
        }
        if (description.isEmpty()) {
            f4 = true;
        }
        if (user_no.isEmpty() || user_no.length() < 10) {
            f5 = true;
        }
        if (uri == null) {
            f6 = true;
        }


        if (f1) {
            Toast.makeText(com.ranafkd.myproject.Activities.SightedActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f2) {
            Toast.makeText(com.ranafkd.myproject.Activities.SightedActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f3) {
            Toast.makeText(com.ranafkd.myproject.Activities.SightedActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f4) {
            Toast.makeText(com.ranafkd.myproject.Activities.SightedActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f5) {
            Toast.makeText(com.ranafkd.myproject.Activities.SightedActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f6) {
            Toast.makeText(com.ranafkd.myproject.Activities.SightedActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
        } else {

            final StorageReference reference = storageReference.child("/image" +id);
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            SightedPojo pojo = new SightedPojo();
                            pojo.setId(id);
                            pojo.setName(name);
                            pojo.setLocation(location);
                            pojo.setGender(gender);
                            pojo.setAge(age);
                            pojo.setUser_no(user_no);
                            pojo.setDescription(description);
                            pojo.setImageUrl(uri.toString());

                            databaseReference.child(id).setValue(pojo);
                            Toast.makeText(com.ranafkd.myproject.Activities.SightedActivity.this,"Submitted Successfully!!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(com.ranafkd.myproject.Activities.SightedActivity.this, CitizenDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(com.ranafkd.myproject.Activities.SightedActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}

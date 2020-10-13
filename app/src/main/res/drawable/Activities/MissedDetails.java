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
import com.ranafkd.myproject.Pojo.MissedPojo;
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

public class MissedDetails extends AppCompatActivity {

    EditText miss_name, miss_age, miss_description, user_number, miss_location;
    //TextView miss_location;
    Button missed_btn;
    ImageView imageView, location_btn;
    RadioGroup radioGroup;
    RadioButton radioButton;
    FirebaseDatabase firebaseDatabase;
    SelectImageHelper selectImageHelper;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    //flag var for validation
    boolean flag = false;

    //For getting location
    private static final int REQUEST_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed_details);
        getSupportActionBar().setTitle("Missing Child");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("MissedDB");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        user_number = findViewById(R.id.user_number);
        miss_location = findViewById(R.id.missed_location);         //textview for location
        miss_name = findViewById(R.id.missed_name);
        miss_age = findViewById(R.id.missed_age);
        miss_description = findViewById(R.id.missed_descrip);
        imageView = findViewById(R.id.add_img_btn);
        missed_btn = findViewById(R.id.missed_submit_btn);
        location_btn = findViewById(R.id.get_location_btn);

        radioGroup = findViewById(R.id.missed_radio_group);
        int selectID = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectID);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Onclick for image selection
        selectImageHelper = new SelectImageHelper(this, imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("123", "image onclick");
                selectImageHelper.selectImageOption();
            }
        });

        // Onclick for submit
        missed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMissedData();
            }
        });

        // Onclick for getting location
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });

    }


    //Function to get location
    private void fetchLocation() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(com.ranafkd.myproject.Activities.MissedDetails.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(com.ranafkd.myproject.Activities.MissedDetails.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Requered Location Permission")
                        .setMessage("You need to allow for this permission to proceed")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(com.ranafkd.myproject.Activities.MissedDetails.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
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
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(com.ranafkd.myproject.Activities.MissedDetails.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Log.d("1234", "else already");
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                //Log.d("1234", "check");
                                Double latitude = location.getLatitude();
                                Double longitude = location.getLongitude();

                                //Log.d("1234", "check"+latitude+" : "+longitude);
                                //textView.setText("Latitude : "+latitude+"\n"+"Longitude"+longitude);
                                try {
                                    Geocoder geocoder = new Geocoder(com.ranafkd.myproject.Activities.MissedDetails.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    String address = addresses.get(0).getSubLocality();
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    //String country = addresses.get(0).getCountryName();
                                    // String postalCode = addresses.get(0).getPostalCode();
                                    //String knownName = addresses.get(0).getFeatureName();

                                    miss_location.setText(address + ", " + city + " " + state);
                                    Log.d("1234", "address");
                                } catch (IOException e) {
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
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }

        selectImageHelper.handleGrantedPermisson(requestCode, grantResults);
    }


    public void onChecked(View view) {
        int selectID = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectID);
        if (selectID == -1) {
            Toast.makeText(com.ranafkd.myproject.Activities.MissedDetails.this, "Please select gender", Toast.LENGTH_SHORT).show();
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

    private void getMissedData() {

        final String id = databaseReference.push().getKey();
        final String name = miss_name.getText().toString();
        final String location = miss_location.getText().toString();
        final String age = miss_age.getText().toString();
        final String description = miss_description.getText().toString();
        final String gender = radioButton.getText().toString();
        final String user_no = user_number.getText().toString();
        //   Log.d("1234","gender: "+gender);
        Uri uri = selectImageHelper.getURI_FOR_SELECTED_IMAGE();
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
            Toast.makeText(com.ranafkd.myproject.Activities.MissedDetails.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f2) {
            Toast.makeText(com.ranafkd.myproject.Activities.MissedDetails.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f3) {
            Toast.makeText(com.ranafkd.myproject.Activities.MissedDetails.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f4) {
            Toast.makeText(com.ranafkd.myproject.Activities.MissedDetails.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f5) {
            Toast.makeText(com.ranafkd.myproject.Activities.MissedDetails.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
        } else if (f6) {
            Toast.makeText(com.ranafkd.myproject.Activities.MissedDetails.this, "Please select image", Toast.LENGTH_SHORT).show();
        } else {

            final StorageReference reference = storageReference.child("/image" + id);
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            MissedPojo pojo = new MissedPojo();
                            pojo.setId(id);
                            pojo.setName(name);
                            pojo.setLocation(location);
                            pojo.setGender(gender);
                            pojo.setAge(age);
                            pojo.setUser_no(user_no);
                            pojo.setDescription(description);
                            pojo.setImageUrl(uri.toString());

                            databaseReference.child(id).setValue(pojo);
                            Toast.makeText(com.ranafkd.myproject.Activities.MissedDetails.this,"Submitted Successfully!!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(com.ranafkd.myproject.Activities.MissedDetails.this, com.ranafkd.myproject.Activities.CitizenDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(com.ranafkd.myproject.Activities.MissedDetails.this, "network error", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
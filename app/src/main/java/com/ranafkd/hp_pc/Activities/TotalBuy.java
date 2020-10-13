package com.ranafkd.hp_pc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ranafkd.hp_pc.Pojo.oderPojo;
import com.ranafkd.hp_pc.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TotalBuy extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    TextView pName, pTotal, pQuant, sAddress;
    Button pBuyProceed, pBuyCOD;
    int pCount;
    final int UPI_PAYMENT = 0;
    String pid, uid, pname, uname, address, uNumber, phoneNo, message;
    Integer tPPrice;

    FirebaseDatabase fdb;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_buy);

//        order db instances
        fdb = FirebaseDatabase.getInstance();
        db = fdb.getReference("OrderDBName");

        //link the views
        pName = findViewById(R.id.prodt_name);
        pTotal = findViewById(R.id.prodt_total);
        pQuant = findViewById(R.id.prodt_quantity);
        pBuyProceed = findViewById(R.id.prodt_pay);
        pBuyCOD = findViewById(R.id.prodt_cod);
        sAddress = findViewById(R.id.prodt_address);

        //get product details from shared preferences
//        SharedPreferences sp = getSharedPreferences("cart_data", Context.MODE_PRIVATE);
//        pCount = sp.getInt("count",0);
//        pid = sp.getString("id","");
//        pname = sp.getString("name","");
//        Log.d("123456", "onClick: prod id 3 = "+pid);
//        String tPName = sp.getString("name","");
//        tPPrice = Integer.parseInt(sp.getString("price",""));

        //get product details from intent
        String temp = getIntent().getStringExtra("count");
        Log.d("123123", "onCreate: total buy count * = "+temp);
        pCount = Integer.parseInt(temp);
        pid = getIntent().getStringExtra("id");
        pname = getIntent().getStringExtra("name");
        String tPName = getIntent().getStringExtra("name");
        tPPrice = Integer.parseInt(getIntent().getStringExtra("price"));
        address = getIntent().getStringExtra("address");
        Log.d(address, "onCreate: total buy address = "+address);

        //set values on display
        pName.setText(tPName);
        pQuant.setText(pCount+"");
        pTotal.setText((pCount*tPPrice)+"");
        sAddress.setText(address);

        //get user id
        SharedPreferences sp1 = getSharedPreferences("userdata",Context.MODE_PRIVATE);
        uid = sp1.getString("id","");
        uname = sp1.getString("name","");
        uNumber = sp1.getString("number","");
        //put total price, prodt id, quantity and user id
        SharedPreferences sharedPreferences = getSharedPreferences("oderDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ProdtName",tPName);
        editor.putString("ProdtQuant",pCount+"");
        editor.putString("UserId",uid);
        editor.putString("TotalPrice",(pCount*tPPrice)+"");

        //call the payment gateway function
        pBuyProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payUsingUpi((pCount*tPPrice)+"");
            }
        });

        //cash on delivery
        pBuyCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payUsingCOD((pCount*tPPrice)+"");
            }
        });

    }

    //cod code
    private void payUsingCOD(String amounttxt){
        //save oder data
        oderPojo op = new oderPojo();
        String sCount = pCount+"";
        String sTotal = amounttxt;
        String oid = db.push().getKey();

        Log.d("123456", "upi_payment_data_operation: "+sCount+" "+sTotal+" "+pid+" "+uid+" "+oid);

        op.setUname(uname);
        op.setPname(pname);
        op.setCount(sCount);
        op.setPid(pid);
        op.setTotal(sTotal);
        op.setUid(uid);
        op.setOid(oid);
        op.setPayMode("COD");
        op.setuAddress(address);
        db.child(oid).setValue(op);

//                send sms when order is placed
        String messageToSend = "Your order with order ID "+oid+" has been received. Please wait until we deliver it to you. \nFor any queries vist the contact us page in the app.\nThank you.";
        String number = uNumber;
        sendSms(messageToSend, number);

        Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(TotalBuy.this,Dashboard.class);
        startActivity(intent);

    }

    //    buy now code
    private void payUsingUpi(String amounttxt) {

        String notetxt = "Product Payment";
        String nametxt = "Your ID Name";
        String upitxt = "#######@upi";
        Uri uri = Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa", upitxt)
                .appendQueryParameter("pn", nametxt)
                .appendQueryParameter("tn", notetxt)
                .appendQueryParameter("am", amounttxt)
                .appendQueryParameter("cu", "INR").build();

        Intent upi_payment = new Intent(Intent.ACTION_VIEW);

        upi_payment.setData(uri);

        Intent chooser = Intent.createChooser(upi_payment, "Pay with");

        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(TotalBuy.this, "No UPI app found ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode)){
                    if(data!=null){
                        String txt=data.getStringExtra("response");
                        Log.d("UPI","onACTIVITYresult: "+ txt);
                        ArrayList<String> dataList=new ArrayList<>();
                        dataList.add(txt);
                        upi_payment_data_operation(dataList);
                    }
                    else{
                        Log.d("UPI","onACTIVITYresult: "+ "Return data is null");
                        ArrayList<String>dataList=new ArrayList<>();
                        dataList.add("Nothing");
                        upi_payment_data_operation(dataList);
                    }
                }
                else{
                    Log.d("UPI","onACTIVITYresult: "+ "Return data is null");
                    ArrayList<String>dataList=new ArrayList<>();
                    dataList.add("Nothing");
                    upi_payment_data_operation(dataList);
                }
                break;
        }
    }

    private void upi_payment_data_operation(ArrayList<String> data) {
        if (isConnectionAvailable(TotalBuy.this)) {
            String str = data.get(0);
            Log.d("UPI_PAY", "UPI payment operation: " + str);
            String paymentcancel = " ";

            if (str == null) str = "discard";
            String status = " ";
            String approvalref = " ";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                Log.d("123456", "upi_payment_data_operation: response = " + response[i]);
                String equalstr[] = response[i].split("=");
                if (equalstr.length >= 2) {
                    Log.d("123456", "upi_payment_data_operation: equalstr = " + equalstr[0] + " __ " + equalstr[1]);
                    if (equalstr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalstr[1].toLowerCase();
                    } else if (equalstr[0].toLowerCase().equals("approvalref".toLowerCase()) ||
                            equalstr[0].toLowerCase().equals("txnref".toLowerCase())) {
                        approvalref = equalstr[1];
                    }
                } else {
                    paymentcancel = "Payment cancel by user";
                }
            }
            Log.d("123456", "upi_payment_data_operation: status = "+status);
            if (status.equals("success"))
            {
                int flag = 1;
                Log.d("123456", "upi_payment_data_operation: flag "+flag);
                //save oder data
                oderPojo op = new oderPojo();
                String sCount = pCount+"";
                String sTotal = (pCount*tPPrice)+"";
                String oid = db.push().getKey();

                Log.d("123456", "upi_payment_data_operation: "+sCount+" "+sTotal+" "+pid+" "+uid+" "+oid);

                op.setUname(uname);
                op.setPname(pname);
                op.setCount(sCount);
                op.setPid(pid);
                op.setTotal(sTotal);
                op.setUid(uid);
                op.setOid(oid);
                op.setPayMode("Online Payment");
                op.setuAddress(address);

                Toast.makeText(this, "Transaction success", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responsestr: " + approvalref);

                db.child(oid).setValue(op);

//                send sms when order is placed
                String messageToSend = "Your order with order ID "+oid+" has been received. Please wait until we deliver it to you. \nFor any queries vist the contact us page in the app.\nThank you.";
                String number = uNumber;
                sendSms(messageToSend, number);

                Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();

                //take user to dashboard after saving order data
                Intent intent=new Intent(TotalBuy.this,Dashboard.class);
                startActivity(intent);
                finish();
            }
            else if ("Payment cancel by user".equals(paymentcancel))
            {
                Log.d("123456", "upi_payment_data_operation: else 1");
                Toast.makeText(this, "Payment cancel by user", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(TotalBuy.this,Dashboard.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Log.d("123456", "upi_payment_data_operation: else 2");
                Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(TotalBuy.this,Dashboard.class);
                startActivity(intent);
                finish();
            }
        } else{
            Toast.makeText(this, "Internet Connection is not available. Please check and try again.", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected() && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable())
            {
                return true;
            }
        }
        return  false;
    }

    //    sms code
    protected void sendSms( String mesg, String no) {
        phoneNo = no;
        message = mesg;
        Log.d("qwerty", "sendSms: no. = "+phoneNo+" mesg = "+message);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:"+phoneNo));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", message);
        intent.putExtra(Intent.EXTRA_STREAM, "");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        Log.d("qwerty", "sendSms: sms sent");
    }
}
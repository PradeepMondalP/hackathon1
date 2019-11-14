package com.example.hackathon10.ui.myui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackathon10.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RetailerRegisterActivity extends AppCompatActivity
        implements OnMapReadyCallback, LocationListener {

    private EditText email , pass , verify_pass , name , location ;
    private Button createAccountBtn;
    private TextView logIn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private String currentUserID;
    private Toolbar mToolbar;
    private GoogleMap mMap;
    LocationManager locationManager;
    LatLng cur_LatLng;

    private DatabaseReference rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_register);


        initialize();



        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });


        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();
                String mVerifyPass = verify_pass.getText().toString().trim();
                String mName = name.getText().toString();
                String mLocation = cur_LatLng.toString();
                //mLocation=mLocation.substring(1,mLocation.length()-2);
                System.out.println(mLocation);

                createAccountInDatabse(mEmail ,mPass ,mVerifyPass , mName , mLocation);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.id_map_fragment);
        mapFragment.getMapAsync(this);


    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initialize() {

        mToolbar = (Toolbar)findViewById(R.id.id_toolbar_retailer_register);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        email =(EditText) findViewById(R.id.id_email_signUp);
        pass = (EditText)findViewById(R.id.id_pass_signUp);
        verify_pass = (EditText)findViewById(R.id.id_confirm_pass_signUp);
        createAccountBtn = (Button)findViewById(R.id.id_register_signUp);
        logIn = (TextView)findViewById(R.id.id_login_signUp);
        name = (EditText)findViewById(R.id.id_name_signUp);
        //location = (EditText)findViewById(R.id.id_location_signUp);
//        radioConsumer = (RadioButton)findViewById(R.id.consumer);
//        radioRetailer  =(RadioButton)findViewById(R.id.retailer);
//        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);


        loadingBar = new ProgressDialog(this);

        rootRef = FirebaseDatabase.getInstance().getReference();

    }



    private void createAccountInDatabse(final String mEmail, final  String mPass,
                                        final String mVerifyPass  , final String mName ,
                                        final String mLocation)
    {
        if(TextUtils.isEmpty(mEmail))
        {
            email.setError("Enter a Email");
            return;
        }
        else if(TextUtils.isEmpty(mPass) || mPass.length()<7){
            pass.setError("Password req of min 7 char");
            return;
        }

        if(TextUtils.isEmpty(mName))
        {
            name.setError("Enter a name");
            return;
        }
        else if(TextUtils.isEmpty(mLocation)){
            location.setError("Enter a location");
            return;
        }
        else if(TextUtils.isEmpty(mVerifyPass) || mVerifyPass.length()<7)
        {
            verify_pass.setError("Enter the correct pass again ");
            return;
        }
        else if( ! mPass.equals(mVerifyPass)  && (mPass.length() <7 ))
        {
            Toast.makeText(getApplicationContext(), "Password Doesnt match or min req pass" +
                            "length 7 char..",
                    Toast.LENGTH_SHORT).show();
        }
        else {


            loadingBar.setTitle("Creating new Account");
            loadingBar.setMessage("Please wait while creating new Account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);

            mAuth.createUserWithEmailAndPassword(mEmail ,mPass).addOnCompleteListener
                    (new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Toast.makeText(RetailerRegisterActivity.this,
                                        "successfully authenticated", Toast.LENGTH_SHORT).show();

                                currentUserID = mAuth.getCurrentUser().getUid().toString();

                                saveDataToDatabase(mEmail , mLocation , mName , mPass , currentUserID );


                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(RetailerRegisterActivity.this,
                                        "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }

    private void saveDataToDatabase(String email, String location, String nam, String pass,
                                    String currentUserID) {

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        String  saveCurrentDate = currentDate.format(Calendar.getInstance().getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(Calendar.getInstance().getTime());
        String unikey = currentUserID +saveCurrentDate + saveCurrentTime;

        DatabaseReference temp =
                FirebaseDatabase.getInstance().getReference("Users").
                        child("Retailer").child(currentUserID).child(unikey);


        Map map = new HashMap();
        map.put("email" , email);
        map.put("location" , location);
        map.put("name" , nam);
        map.put("pass" , pass);

        temp.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RetailerRegisterActivity.this, "success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(),RetailerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(RetailerRegisterActivity.this, "failur", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }




    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        Location cur_Loc=null;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(RetailerRegisterActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, RetailerRegisterActivity.this);
            cur_Loc=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(cur_Loc==null)
            {
                Toast.makeText(this,"Could not obtain current location!",Toast.LENGTH_LONG);
                return;
            }
        }
        catch(SecurityException e) {
            Toast.makeText(this,cur_Loc.getLatitude()+","+cur_Loc.getLongitude(),Toast.LENGTH_LONG);
            e.printStackTrace();
        }

        Toast.makeText(this,cur_Loc.getLatitude()+","+cur_Loc.getLongitude(),Toast.LENGTH_LONG);
        //showLoc_TxtView.setText(cur_Loc.getLatitude()+","+cur_Loc.getLongitude());

        // Add a marker in Sydney and move the camera
        cur_LatLng = new LatLng(cur_Loc.getLatitude(), cur_Loc.getLongitude());
        final Marker store_Loc = mMap.addMarker(new MarkerOptions().position(cur_LatLng).title("Marker in Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cur_LatLng));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(cur_LatLng).zoom(17).build()));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point)
            {
                store_Loc.setPosition(point);
                cur_LatLng=point;
                //mMap.addMarker(new MarkerOptions().position(point));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getApplicationContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }


}

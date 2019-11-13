package com.example.hackathon10.ui.myui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackathon10.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConsumerActivity extends AppCompatActivity
        implements OnMapReadyCallback, LocationListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

    private DatabaseReference rootRef;
    //map
    private GoogleMap mMap;
    LocationManager locationManager;


    private Button searchBtn;
    private String []itemsArray;
    private TextView autoCompleteTextView;
    private ListView listView;

    private ArrayAdapter<String>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);


        initialize();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.  id_order_my:
                        toast("order");
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.  my_cart:
                        toast("cart");
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.  id_new_product:
                        toast("id_addedess");
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.  id_log_out_2:
                        mAuth.signOut();
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(getApplicationContext() , LoginActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });


        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.id_map_fragment);
        mapFragment.getMapAsync(this);

        getAllValues();


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = autoCompleteTextView.getText().toString();
                if(TextUtils.isEmpty(text))
                {
                    autoCompleteTextView.setError("enter something");
                    return;
                }
                else
                {
                    String []matchedString = findPattern(text , itemsArray);

                    ArrayList<String> tempList = new ArrayList();

                    for(int i = 0 ; i<matchedString.length ; i++)
                    {
                        if((matchedString[i]==null))
                        {
                           continue;
                        }
                        else
                        {
                            tempList.add( matchedString[i]);
                        }
                    }
                    String []temp = new String[tempList.size()];
                    for(int i = 0 ; i<tempList.size() ; i++)
                    {
                         temp[i] = tempList.get(i);
                    }

                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1 , temp);



                    for(String x : temp)
                    {
                        System.out.println("matched  STring is ...."+x +"\n");
                    }

                    listView.setAdapter(adapter);
                }


            }
        });


    }

    private String[] findPattern(String text, String[] itemsArray) {

            String array[] = new String[itemsArray.length];
           int start=0;
          for(int i= 0;i<itemsArray.length;i++)
               {
                    if(text.length()<=itemsArray[i].length())
                    {
                        String ss=itemsArray[i].substring(0,text.length());
                        if(ss.equalsIgnoreCase(text) )
                        {
                            array[start++]=itemsArray[i];
                        }
                    }
               }
          return array;
    }

    private void getAllValues() {
        DatabaseReference all_retailer_ref = rootRef.child("All_retailer_product");

        System.out.println("getting all value.....................................");

        all_retailer_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                     Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    Iterator it = iterable.iterator();

                    DataSnapshot obj;
                    ArrayList<String> list = new ArrayList();
                    while (it.hasNext())
                    {
                        obj =(DataSnapshot) it.next();
                        list.add(obj.getKey());
                    }
                    System.out.println("list is ...........\n"+ list);

                    itemsArray = new String[list.size()];

                    for(int i=0 ; i<itemsArray.length ; i++)
                    {
                        itemsArray[i] = list.get(i);
                        System.out.println("array elemst\t"+itemsArray[i]+"\n");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    private void toast(String order) {
        Toast.makeText(this, ""+ order, Toast.LENGTH_SHORT).show();
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.id_main_app_bar_consumer);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_consumer);
        navigationView = (NavigationView)findViewById(R.id.id_navigationView_consumer);

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        autoCompleteTextView = (TextView) findViewById(R.id.id_search_item_consumer);
        listView = (ListView)findViewById(R.id.id_my_list_view);
        searchBtn = (Button)findViewById(R.id.id_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.three_coloums , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.id_open_navig:
                drawerLayout.openDrawer(GravityCompat.END);
                break;

        }
        return true;
    }


    // implementaiom of maps

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location cur_Loc=null;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ConsumerActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, ConsumerActivity.this);
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
        LatLng cur_LatLng = new LatLng(cur_Loc.getLatitude(), cur_Loc.getLongitude());
        mMap.addMarker(new MarkerOptions().position(cur_LatLng).title("Marker in Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cur_LatLng));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().
                target(cur_LatLng).zoom(17).build()));


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

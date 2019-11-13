package com.example.hackathon10.ui.myui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hackathon10.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ConsumerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

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
}

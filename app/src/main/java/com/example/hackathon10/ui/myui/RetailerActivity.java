package com.example.hackathon10.ui.myui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.hackathon10.R;
import com.example.hackathon10.ui.myui.fragment.ViewMyAllProductsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import my_fragments.add_new_product_fragment;

public class RetailerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    FrameLayout frameLayout;
    private Bundle myBundle;


    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer);
        myBundle = savedInstanceState;

        initialization();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.  id_order:
                        toast("order");
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.  id_view_my_products:
                        toast("order");
                        openViewMyProducts();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.  id_existing:
                        toast("exiting");
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.  id_new_product:
                        toast("new pro");
                        drawerLayout.closeDrawers();
                        openMyaddnewItemFragment();
                        break;
                    case R.id.  id_log_out:
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

    private void openViewMyProducts() {

        if(findViewById(R.id.id_my_retailer_fram_lay)!=null)
        {
            if(myBundle!=null)
            {
                return;
            }
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            ViewMyAllProductsFragment obj = new ViewMyAllProductsFragment();

            transaction.add(R.id.id_my_retailer_fram_lay , obj , null);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void openMyaddnewItemFragment() {
        if(findViewById(R.id.id_my_retailer_fram_lay)!=null)
        {
            if(myBundle!=null)
            {
                return;
            }
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            add_new_product_fragment obj = new add_new_product_fragment();
            transaction.add(R.id.id_my_retailer_fram_lay , obj , null);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void initialization() {
        Toolbar toolbar = findViewById(R.id.id_main_app_bar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.id_navigationView);
        mAuth = FirebaseAuth.getInstance();

        frameLayout = (FrameLayout)findViewById(R.id.id_my_retailer_fram_lay);
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
    public void toast(String x)
    {
        Toast.makeText(this,
                ""+x, Toast.LENGTH_SHORT).show();
    }
}

package com.example.hackathon10.ui.myui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hackathon10.R;

public class HomeActivity extends AppCompatActivity {

    Button mBtn_Consumer,mBtn_Retailer;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();

        mBtn_Consumer=findViewById(R.id.consumer_button);
        mBtn_Retailer=findViewById(R.id.retailer_button);

        mBtn_Consumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), ConsumerRegisterActivity.class);
                startActivity(intent);
            }
        });
        mBtn_Retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), RetailerRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initialize() {

        mToolbar = (Toolbar)findViewById(R.id.id_home_act_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}

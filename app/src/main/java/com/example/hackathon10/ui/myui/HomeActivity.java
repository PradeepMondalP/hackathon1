package com.example.hackathon10.ui.myui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hackathon10.ConsumerRegisterActivity;
import com.example.hackathon10.R;

public class HomeActivity extends AppCompatActivity {

    Button mBtn_Consumer,mBtn_Retailer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
}

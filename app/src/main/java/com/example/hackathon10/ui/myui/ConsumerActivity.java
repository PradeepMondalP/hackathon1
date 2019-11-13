package com.example.hackathon10.ui.myui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hackathon10.R;
import com.google.firebase.auth.FirebaseAuth;

public class ConsumerActivity extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);
        mauth=FirebaseAuth.getInstance();

        String currentUserId = mauth.getCurrentUser().getUid();
        System.out.println("user Id" + currentUserId);

        logout = (Button)findViewById(R.id.id_log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }
}

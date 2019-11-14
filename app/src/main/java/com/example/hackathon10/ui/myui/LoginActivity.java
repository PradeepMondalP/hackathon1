package com.example.hackathon10.ui.myui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hackathon10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText email , pass;
    private Button login ;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef , rootRef;
    private Toolbar mToolbar;

    private RadioGroup radioGroup;
    private RadioButton radioConsumer , radioRetailer;
    int radioStatus=R.id.consumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize_id();

        radioConsumer = (RadioButton)findViewById(R.id.consumer);
        radioRetailer  =(RadioButton)findViewById(R.id.retailer);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i)
                {
                    case R.id.consumer:
                        radioStatus=R.id.consumer;
                        break;
                    case R.id.retailer:
                        radioStatus=R.id.retailer;
                        break;
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();

                loginToDatabase(mEmail , mPass) ;
            }
        });

    }


    private void loginToDatabase(String mEmail, String mPass) {
        if(TextUtils.isEmpty(mEmail)){
            email.setError("Enter a mail");
            return;
        }
        if(TextUtils.isEmpty(mPass)){
            pass.setError("Enter a password");
            return;
        }
        else {
            mDialog.setTitle("Logging in");
            mDialog.setMessage("Loading....");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

            mAuth.signInWithEmailAndPassword(mEmail , mPass).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                if(radioStatus==R.id.consumer)
                                {
                                    startActivity(new Intent(getApplicationContext(),
                                            ConsumerActivity.class));
                                }
                                else
                                {
                                    startActivity(new Intent(getApplicationContext(),
                                            RetailerActivity.class));
                                }

                            }else
                            {
                               mDialog.dismiss();
                                Toast.makeText(LoginActivity.this,
                                        "login failed..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }

        }

    private void initialize_id() {

        mToolbar = (Toolbar)findViewById(R.id.id_login_toolbar);
        setSupportActionBar(mToolbar);

        email =(EditText) findViewById(R.id.id_email);
        pass = (EditText) findViewById(R.id.id_pass);
        login = (Button) findViewById(R.id.id_login);
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Users");


    }

    public void sendUserToRegisterActivity()
    {
        Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_up_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.id_id_sign_up_menu:
                sendUserToRegisterActivity();
                break;
        }
        return true;
    }
}

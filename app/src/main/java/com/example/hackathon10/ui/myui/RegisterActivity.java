package com.example.hackathon10.ui.myui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackathon10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email , pass , verify_pass , name , location ;
    private Button createAccountBtn;
    private TextView logIn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference rootRef , userRef  , consumerRef , retailerRef;
    private String currentUserID;
    private RadioGroup radioGroup;
    private RadioButton radioConsumer , radioRetailer;

    private String radioStatus;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        
        initialize();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.consumer:
                        radioStatus  ="Consumer";
                        break;
                    case R.id.retailer:
                        radioStatus  ="Retailer";
                        break;
                }
            }
        });

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
                String mLocation = location.getText().toString();

                createAccountInDatabse(mEmail ,mPass ,mVerifyPass , mName , mLocation);
            }
        });


    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initialize() {

        mAuth = FirebaseAuth.getInstance();

        email =(EditText) findViewById(R.id.id_email_signUp);
        pass = (EditText)findViewById(R.id.id_pass_signUp);
        verify_pass = (EditText)findViewById(R.id.id_confirm_pass_signUp);
        createAccountBtn = (Button)findViewById(R.id.id_register_signUp);
        logIn = (TextView)findViewById(R.id.id_login_signUp);
        name = (EditText)findViewById(R.id.id_name_signUp);
        location = (EditText)findViewById(R.id.id_loation_signUp);
        radioConsumer = (RadioButton)findViewById(R.id.consumer);
        radioRetailer  =(RadioButton)findViewById(R.id.retailer);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);


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
                                Toast.makeText(RegisterActivity.this,
                                        "successfully authenticated", Toast.LENGTH_SHORT).show();

                               currentUserID = mAuth.getCurrentUser().getUid().toString();
                               saveDataToDatabase(mEmail , mLocation , mName , mPass , radioStatus);


                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this,
                                        "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }

    private void saveDataToDatabase(String mEmail, String mLocation, String mName,
                                    String mPass , String radioStatus) {

        userRef = rootRef.child("Users");
        Map map = new HashMap();
        map.put("name" , mName);
        map.put("email" , mEmail);
        map.put("location" , mLocation);
        map.put("password" , mPass);
        map.put("customer_type" , radioStatus);


        if(radioStatus.equals("Consumer"))
        {
            consumerRef = userRef.child(currentUserID);
            consumerRef.updateChildren(map);
            sendUserToConsumerActivity();
        }
        else
            if(radioStatus.equals("Retailer"))
            {
                retailerRef = userRef.child(currentUserID);
                retailerRef.updateChildren(map);
                sendUserToRetailerActivity();
            }

    }

    private void sendUserToRetailerActivity()
    {
        startActivity(new Intent(getApplicationContext(), RetailerActivity.class));
    }

    private void sendUserToConsumerActivity()
    {
        startActivity(new Intent(getApplicationContext(), ConsumerActivity.class));

    }


}

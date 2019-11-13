package com.example.hackathon10.ui.myui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hackathon10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
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
    private Button login , phoneLoginBtn;
    private TextView newAccount;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference userRef , rootRef;
    private String currentuserId , fetchingCustomerType="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize_id();

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
                Toast.makeText(LoginActivity.this, "hii", Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();

                loginToDatabase(mEmail , mPass);
            }
        });

        phoneLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToPhoneLoginActivity();
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
//                                mDialog.dismiss();
//                                Toast.makeText(LoginActivity.this,
//                                        "welcome ", Toast.LENGTH_SHORT).show();
//
//                                mAuth=FirebaseAuth.getInstance();
//                                currentuserId= mAuth.getCurrentUser().getUid();
//                                System.out.println("id.........."+ currentuserId);
//
//                                String userType = fetchData(currentuserId) ;
//                                System.out.println("id.....2="+ userType);
//
//                                if( userType.equals("Retailer") )
//                                {
//                                    sendUserToRetailerActivity();
//
//                                }
//                                else if(userType.equals("Consumer"))
//                                {
//                                    sendUserToConsumeractivity();
//                                }

                                startActivity(new Intent(getApplicationContext() ,RetailerActivity.class));

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



    private String fetchData(final String currentuserId) {


       DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");

       userRef.child(currentuserId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if(dataSnapshot.exists())
               {
                   System.out.println("data exits.....");
                   fetchingCustomerType = dataSnapshot.child("customer_type").getValue().toString();
                   System.out.println("customer type = "+ fetchingCustomerType);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

       return fetchingCustomerType;



    }




    private void initialize_id() {

        email =(EditText) findViewById(R.id.id_email);
        pass = (EditText) findViewById(R.id.id_pass);
        login = (Button) findViewById(R.id.id_login);
        newAccount = (TextView) findViewById(R.id.id_signup);
        phoneLoginBtn = (Button)findViewById(R.id.id_phone);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Users");




    }

    private void sendUserToPhoneLoginActivity() {
        Intent obj = new Intent(getApplicationContext() , PhoneLoginActivity.class);
        startActivity(obj);
    }

    public void sendUserToRegisterActivity()
    {
        Intent intent = new Intent(getApplicationContext() , RegisterActivity.class);
        startActivity(intent);

    }

    private void sendUserToRetailerActivity() {
        startActivity(new Intent(getApplicationContext(),RetailerActivity.class));

    }

    private void sendUserToConsumeractivity()
    {
        startActivity(new Intent(getApplicationContext(),ConsumerActivity.class));

    }

}

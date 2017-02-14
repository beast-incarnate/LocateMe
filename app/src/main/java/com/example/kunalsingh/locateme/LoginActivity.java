package com.example.kunalsingh.locateme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText fn,ln,phone,email,pass;
    Button signUp;
    FirebaseAuth mAuth;
    Firebase mRef;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final String TAG="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mRef = new Firebase("https://locateme-a9ef0.firebaseio.com/");

        //Log.d(TAG,"check Firebase : "+mRef);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Intent in = new Intent(LoginActivity.this,PermisssionActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                }
            }
        };

        fn = (EditText)findViewById(R.id.first_name);
        ln = (EditText)findViewById(R.id.last_name);
        phone = (EditText)findViewById(R.id.contact_number);
        email = (EditText)findViewById(R.id.user_email);
        pass = (EditText)findViewById(R.id.user_pass);



        signUp = (Button)findViewById(R.id.btn_sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = fn.getText().toString();
                final String lastName =  ln.getText().toString();
                final String phoneNumber = phone.getText().toString();
                final String emailUser = email.getText().toString();
                final String passUser = pass.getText().toString();

                if(firstName.equals("")||lastName.equals("")||phoneNumber.equals("")||emailUser.equals("")||passUser.equals("")){
                    Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                }else{
                    if(phoneNumber.length()!=10){
                        Toast.makeText(LoginActivity.this, "Wrong Phone Number", Toast.LENGTH_SHORT).show();
                    }
                    mAuth.createUserWithEmailAndPassword(emailUser,passUser)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                               // Toast.makeText(LoginActivity.this, "Sign Up failed", Toast.LENGTH_SHORT).show();
                            }else {

                                    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

                                    if(mUser!=null){

                                        Firebase mChild = mRef.child(mUser.getUid());
                                        Firebase mPhone = mRef.child(phoneNumber);
                                        mPhone.child("Latitude");
                                        mPhone.child("Longitude");
                                        Log.d(TAG,"checking existence of User");
                                    }

                                    Intent in = new Intent(LoginActivity.this,PermisssionActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);
                            }
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener!=null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    public void startSignInActivity(View view){
        Intent in = new Intent(this,SignInActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }

}

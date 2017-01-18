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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText fn,ln,phone,email,pass;
    Button signUp;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

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
                                    Intent in = new Intent(LoginActivity.this,MainActivity.class);
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
}

package com.example.kunalsingh.locateme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

public class PhoneNumberVerificationActivity extends AppCompatActivity {

    EditText et[] = new EditText[4];
    Button  btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verification);

        Intent intent = getIntent();

        final String phoneNumber = intent.getStringExtra("PhoneNumber");

        et[0] = (EditText)findViewById(R.id.et_otp_one);
        et[1] = (EditText)findViewById(R.id.et_otp_two);
        et[2] = (EditText)findViewById(R.id.et_otp_three);
        et[3] = (EditText)findViewById(R.id.et_otp_four);

        for(int i=0;i<4;i++){
            final int i1=i;
            et[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(i1!=3){
                        if(count==1){
                            et[i1+1].requestFocus();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        btn = (Button)findViewById(R.id.verify_otp);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s="";
                for(int i=0;i<4;i++){
                    String s1 = et[i].getText().toString();

                    if(s1.equals("")){
                        Toast.makeText(PhoneNumberVerificationActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    s=s+s1;
                }

                if(s.length()==4){
                    Verification mVerification = SendOtpVerification.createSmsVerification(PhoneNumberVerificationActivity.this, phoneNumber, new VerificationListener() {
                        @Override
                        public void onInitiated(String response) {

                        }

                        @Override
                        public void onInitiationFailed(Exception paramException) {

                        }

                        @Override
                        public void onVerified(String response) {
                            Intent intent = new Intent(PhoneNumberVerificationActivity.this,PermisssionActivity.class);

                            Firebase mRefs = new Firebase("https://locateme-a9ef0.firebaseio.com/");

                            Firebase mRef = mRefs.child(phoneNumber);

                            SharedPreferences sharedPreferences =getSharedPreferences("FILE",0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("PhoneNumber",phoneNumber);
                            editor.commit();

                            Firebase mLat = mRef.child("Lat");
                            mLat.setValue("0");
                            Firebase mLong = mRef.child("Long");
                            mLong.setValue("0");


                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("PhoneNumber",phoneNumber);
                            startActivity(intent);
                        }

                        @Override
                        public void onVerificationFailed(Exception paramException) {
                            Toast.makeText(PhoneNumberVerificationActivity.this, paramException.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },"91");
                    mVerification.verify(s);
                }
            }
        });

    }
}

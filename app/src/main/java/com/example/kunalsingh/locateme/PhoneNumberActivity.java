package com.example.kunalsingh.locateme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

public class PhoneNumberActivity extends AppCompatActivity {

    EditText et[] = new EditText[10];
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);



        et[0] = (EditText)findViewById(R.id.et_number_one);
        et[1] = (EditText)findViewById(R.id.et_number_two);
        et[2] = (EditText)findViewById(R.id.et_number_three);
        et[3] = (EditText)findViewById(R.id.et_number_four);
        et[4] = (EditText)findViewById(R.id.et_number_five);
        et[5] = (EditText)findViewById(R.id.et_number_six);
        et[6] = (EditText)findViewById(R.id.et_number_seven);
        et[7] = (EditText)findViewById(R.id.et_number_eight);
        et[8] = (EditText)findViewById(R.id.et_number_nine);
        et[9] = (EditText)findViewById(R.id.et_number_ten);

        for(int i=0;i<10;i++){
            final int i1=i;
            et[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(i1!=9){
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


        btn = (Button)findViewById(R.id.submit_phone_number);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s="";
                for(int i=0;i<10;i++){
                    String s1 = et[i].getText().toString();

                    if(s1.equals("")){
                        Toast.makeText(PhoneNumberActivity.this,"Wrong",Toast.LENGTH_SHORT).show();
                        break;
                    }//

                    s=s+s1;
                }

                if(s.length()==10){

                    final String s1 = s;
                    Verification mVerification = SendOtpVerification.createSmsVerification(PhoneNumberActivity.this, s, new VerificationListener() {
                        @Override
                        public void onInitiated(String response) {
                            Intent intent = new Intent(PhoneNumberActivity.this,PhoneNumberVerificationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("PhoneNumber",s1);
                            startActivity(intent);
                        }

                        @Override
                        public void onInitiationFailed(Exception paramException) {
                            Toast.makeText(PhoneNumberActivity.this, paramException.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVerified(String response) {

                        }

                        @Override
                        public void onVerificationFailed(Exception paramException) {

                        }
                    },"91");

                    mVerification.initiate();
                }
            }
        });


    }
}

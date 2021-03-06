package com.example.kunalsingh.locateme;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PermisssionActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==100){
            if(permissions[0].equals(Manifest.permission.READ_CONTACTS)&&grantResults[0]== PackageManager.PERMISSION_GRANTED){

             // new MainActivity().readContacts();

            }else{
                Toast.makeText(this, "Cannot read Contacts", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisssion);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser==null){

            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);
            finish();
        }

        String permission = Manifest.permission.READ_CONTACTS;
        int res = this.checkCallingOrSelfPermission(permission);
        String permission2 = Manifest.permission.ACCESS_FINE_LOCATION;
        int res2 = this.checkCallingOrSelfPermission(permission2);
        if (res == PackageManager.PERMISSION_GRANTED&&res2 == PackageManager.PERMISSION_GRANTED) {
           // new MainActivity().readContacts();
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS , Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE}, 100);
            }else{
               // new MainActivity().readContacts();
                Intent intent = new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
}

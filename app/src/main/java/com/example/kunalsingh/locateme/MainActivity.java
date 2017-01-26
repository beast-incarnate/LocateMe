package com.example.kunalsingh.locateme;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;
    ViewPager viewPager;
    TabLayout tabLayout;
    static boolean status = false;
   static ArrayList<String> contactsName = new ArrayList<>();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG,"onRequestPermissionsResult called");
        if (requestCode==100){
            Log.d(TAG,"came in 100");
            if(permissions[0].equals(Manifest.permission.READ_CONTACTS)&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                readContacts();
            }else{
                Toast.makeText(this, "Cannot read Contacts", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String permission = Manifest.permission.READ_CONTACTS;
        int res = this.checkCallingOrSelfPermission(permission);
        if (res== PackageManager.PERMISSION_GRANTED){
            readContacts();
        }else {

            Intent intent = new Intent(this,PermisssionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }



        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        viewPager.setAdapter(new ViewPagerAdapter(fragmentManager));

        tabLayout.setupWithViewPager(viewPager);



    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        private String[] tabs = {"Friends","Contacts","Unknown"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG,"Position Value = "+position);
            switch (position){
                case 0: return new FragmentOne();

                case 1 :
                    return  new FragmentTwo();

                case 2 : return new FragmentThree();

                default: return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

  public void readContacts(){

     final ProgressDialog progress =  ProgressDialog.show(this,"Reading Contacts","contacts");

      new Thread(new Runnable() {
          @Override
          public void run() {
              Log.d(TAG,"readContacts called");
              ContentResolver contentResolver = getContentResolver();

              Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null,null);

              cursor.moveToFirst();

              while (cursor.moveToNext()){
                  try{
                      // contacts.put(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                      contactsName.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                      // Log.d(TAG,cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                      //Log.d(TAG,"name : "+cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                      //  cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.Photo));
                  }catch (Exception e){
                      Log.d(TAG,"error : "+cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                  }
              }
              cursor.close();
              runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  progress.dismiss();

              }
          });

          }
      }).start();





  }

    public static ArrayList<String> getContacts(){
        return contactsName;
    }

}

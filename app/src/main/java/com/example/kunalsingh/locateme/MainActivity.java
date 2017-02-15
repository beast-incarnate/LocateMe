package com.example.kunalsingh.locateme;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity" ;
    static ViewPager viewPager;
    TabLayout tabLayout;
    static ViewPagerAdapter viewPagerAdapter;
    static boolean status = false;
    static SwipeRefreshLayout swipeRefreshLayout;
   static ArrayList<String> contactsName = new ArrayList<>();

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        Log.d(TAG,"onRequestPermissionsResult called");
//        if (requestCode==100){
//            Log.d(TAG,"came in 100");
//            if(permissions[0].equals(Manifest.permission.READ_CONTACTS)&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                readContacts();
//            }else{
//                Toast.makeText(this, "Cannot read Contacts", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_main);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewPagerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        String permission = Manifest.permission.READ_CONTACTS;
        int res = this.checkCallingOrSelfPermission(permission);
        if (res== PackageManager.PERMISSION_GRANTED){
            readContacts();
        }




        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(fragmentManager);

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter.notifyDataSetChanged();


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                Log.d(TAG,"checking"+event.getAction());
                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:swipeRefreshLayout.setEnabled(true);
                                                break;



                }

                return false;
            }
        });

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
                case 0:
                    return new FragmentOne();

                case 1 :
                    return  new FragmentTwo();

                case 2 :
                    return new FragmentThree();

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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

  public void readContacts(){

     final ProgressDialog progress =  ProgressDialog.show(this,"Reading Contacts","contacts");

      final boolean[] b = {false};
      new Thread(new Runnable() {
          @Override
          public void run() {
              Log.d(TAG,"readContacts called");
              ContentResolver contentResolver = getContentResolver();

              Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+ " ASC");
              cursor.moveToFirst();

              while (cursor.moveToNext()){
                  try{
                      b[0] = true;
                      String s = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                      String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                      int j=0;
                      for(int i=0;i<phone.length();i++){
                          char c = phone.charAt(i);
                          if((int)c>=48&&(int)c<=57){
                              j++;
                          }
                      }
                      if(j>=10&&j<=12) {
                          if (!contactsName.contains(s))
                              contactsName.add(s);
                      }
                  }catch (Exception e){
                      Log.d(TAG,"error : "+cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                  }
              }
              cursor.close();
              Collections.sort(contactsName);
              runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  progress.dismiss();

              }
          });

          }
      }).start();


//      if(!b[0]){
//          contactsName.add("Mine");
//      }



  }

    public static ArrayList<String> getContacts(){
        return contactsName;
    }

    public static ViewPagerAdapter getAdapter(){
        return viewPagerAdapter;
    }


    public static SwipeRefreshLayout getSwipeRefreshLayout(){

        return swipeRefreshLayout;
    }

}

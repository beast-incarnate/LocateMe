package com.example.kunalsingh.locateme;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;


/**
 * Created by kunalsingh on 19/01/17.
 */

public class FragmentOne extends Fragment implements LocationListener {


    public static final String TAG="FragmentOne";
    public FragmentOne() {
        Log.d(TAG,"constructor cslled");
        setHasOptionsMenu(true);

    }

    public static double Lat=0;
    public static double Long=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null ;
        Log.d(TAG,"onCreateView calld");
        final MainActivity.ViewPagerAdapter viewPagerAdapter = MainActivity.getAdapter();
        String permission2 = android.Manifest.permission.ACCESS_COARSE_LOCATION;
        String permission3 = android.Manifest.permission.ACCESS_FINE_LOCATION;
        int res2 = getContext().checkCallingOrSelfPermission(permission2);
        int res3 = getContext().checkCallingOrSelfPermission(permission3);
        if(res2== PackageManager.PERMISSION_GRANTED&&res3==PackageManager.PERMISSION_GRANTED){

            LocationManager locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);

            boolean b1 = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean b2 = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(!b1&&!b2){

                    Log.d(TAG,"if called");
                    view = inflater.inflate(R.layout.fragment_one_enable_location,container,false);
                Button btn = (Button)view.findViewById(R.id.btn_enable_location);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     getContext().startActivity(intent);

                    }
                });

            }else{
                Log.d(TAG,"else called");
              view  = inflater.inflate(R.layout.fragment_one,container,false);


                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3*1000,5, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,this);

            }


        }
        return view;

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();

        inflater.inflate(R.menu.main_menu,menu);

        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.open_map :

                Intent intent = new Intent(getContext(),MapsActivity.class);
                startActivity(intent);
                                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d(TAG,"onCreate called");

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Log.d(TAG,"onViewCreatedcalled");

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"OnPauseCalled");

    }


    @Override
    public void onLocationChanged(Location location) {
        Firebase mRefs = new Firebase("https://locateme-a9ef0.firebaseio.com/");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("FILE",0);
        String phone = sharedPreferences.getString("PhoneNumber","");

        Firebase mRef = mRefs.child("9711890684");
        Lat = location.getLatitude();
        Long = location.getLongitude();
        Firebase mLat = mRef.child("Lat");
        mLat.setValue(location.getLatitude());
        Firebase mLong = mRef.child("Long");
        mLong.setValue(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public static double getLat(){
        return Lat;
    }

    public static double getLong(){
        return Long;
    }
}

package com.example.kunalsingh.locateme;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleApiClient googleApiClient;
    public static final String TAG = "MapsActivity";
    TextView tvLat, tvLong;
    LocationRequest mLocationRequest;
    double a,b;
    ArrayList<String> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        contacts = MainActivity.getContacts();

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        tvLat = (TextView) findViewById(R.id.tv_lat);
        tvLong = (TextView) findViewById(R.id.tv_long);


        createLocationRequest();




    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this, "Fail1:" + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (mLocation != null) {
            tvLat.setText(String.valueOf(mLocation.getLatitude()));
            tvLong.setText(String.valueOf(mLocation.getLongitude()));
            a = mLocation.getLatitude();
            b = mLocation.getLongitude();
        }
        startLocationUpdate();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(a,b);
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Me"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f));

                ArrayList<Double> Lat = MainActivity.friendsLat;
                ArrayList<Double> Long = MainActivity.friendsLong;
                ArrayList<String> name = MainActivity.friendsName;
                Log.d(TAG,"size"+" "+Lat.size());
                Log.d(TAG,"size2 "+Long.size());
                for(int i=0;i<Lat.size();i++){
                    Log.d(TAG,"no of freinds"+i);
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(Lat.get(i),Long.get(i))).title(name.get(i)));
                }

            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    }

    protected void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(final Location location) {

        if (location != null) {
            tvLat.setText(String.valueOf(location.getLatitude()));
            tvLong.setText(String.valueOf(location.getLongitude()));
            final String[] phone = new String[1];
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            Firebase mRef = new Firebase("https://locateme-a9ef0.firebaseio.com/"+mUser.getUid()+"/PhoneNumber");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "checking class"+String.valueOf(dataSnapshot.getClass()));
                    phone[0] =  dataSnapshot.getValue(String.class);
                    Log.d(TAG,"phone : "+ phone[0]);
                    Firebase mRefs = new Firebase("https://locateme-a9ef0.firebaseio.com" + "/" + phone[0] + "/");
                    Firebase mLat = mRefs.child("Lat");
                    mLat.setValue(String.valueOf(location.getLatitude()));
                    Firebase mLong = mRefs.child("Long");
                    mLong.setValue(String.valueOf(location.getLongitude()));

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            if(phone[0]!=null) {
                Log.d(TAG,"if checking Phone" +phone[0]);

            }else{
                Log.d(TAG,"else checking Phone" +phone[0]);
            }

        }

    }


}

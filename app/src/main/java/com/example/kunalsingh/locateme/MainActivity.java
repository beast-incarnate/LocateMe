package com.example.kunalsingh.locateme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        viewPager.setAdapter(new ViewPagerAdapter(fragmentManager));

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG,"Position Value = "+position);
            switch (position){
                case 0: return new FragmentOne();

                case 1 : return  new FragmentTwo();

                case 2 : return new FragmentThree();

                default: return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

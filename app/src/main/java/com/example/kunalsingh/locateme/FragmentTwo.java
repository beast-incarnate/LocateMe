package com.example.kunalsingh.locateme;

import android.*;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kunalsingh on 19/01/17.
 */

public class FragmentTwo extends Fragment {


    ArrayList<String> contactsName = new ArrayList<>();
   static  ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    public static final String TAG = "FragmentTwo";


    public FragmentTwo() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view ;

        ArrayList<String> child = new ArrayList<>();
        child.add("a");
        child.add("b");
        Log.d(TAG,"asking for contacts");
        contactsName = MainActivity.getContacts();
        if(contactsName.size()==0){
            view = inflater.inflate(R.layout.cannot_read_contacts,container,false);
            Button btn = (Button)view.findViewById(R.id.btn_gp_contacts);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",getContext().getPackageName(),null));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getContext().startActivity(intent);

                }
            });
        }else {
            view = inflater.inflate(R.layout.fragment_two, container, false);

            Log.d(TAG, "size : : " + contactsName.size());
            expandableListAdapter = new ExpandableListAdapter(getActivity(), contactsName, child);
            expandableListView = (ExpandableListView) view.findViewById(R.id.exp_list_view);
            expandableListView.setAdapter(expandableListAdapter);

            expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if(expandableListView.getChildAt(0)!=null){
                        MainActivity.getSwipeRefreshLayout().setEnabled(expandableListView.getFirstVisiblePosition()==0&&expandableListView.getChildAt(0).getTop()==0);
                    }
                }
            });
        }
        return view;


    }


    public static boolean listAtop(){

        if(expandableListView.getChildCount()==0)
            return true;

        return expandableListView.getChildAt(0).getTop()==0;
    }

    public static ExpandableListView getList(){
        return expandableListView;
    }

}

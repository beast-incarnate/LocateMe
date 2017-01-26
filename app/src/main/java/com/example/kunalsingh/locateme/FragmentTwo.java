package com.example.kunalsingh.locateme;

import android.*;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kunalsingh on 19/01/17.
 */

public class FragmentTwo extends Fragment {


    ArrayList<String> contactsName = new ArrayList<>();
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    public static final String TAG = "FragmentTwo";


    public FragmentTwo() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_two,container,false);

        ArrayList<String> child = new ArrayList<>();
        child.add("a");
        child.add("b");
        Log.d(TAG,"asking for contacts");
        contactsName = MainActivity.getContacts();
        Log.d(TAG,"size : : "+contactsName.size());
        expandableListAdapter = new ExpandableListAdapter(getActivity(),contactsName,child);
        expandableListView = (ExpandableListView)view.findViewById(R.id.exp_list_view);
        expandableListView.setAdapter(expandableListAdapter);

        return view;


    }


}

package com.example.kunalsingh.locateme;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kunalsingh on 26/01/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    Context ctx;
    ArrayList<String> contactsName;
    ArrayList<String> child;

    public static final String TAG = "ExpandableListAdapter";

    public ExpandableListAdapter(Context ctx, ArrayList<String> contactsName,ArrayList<String> child) {
        this.ctx = ctx;
        this.contactsName = contactsName;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        return contactsName.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return child.size();
    }

    @Override
    public Object getGroup(int i) {
        return contactsName.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return child.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        Log.d(TAG,"adapter : "+i);
        String name = contactsName.get(i);

        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item,viewGroup,false);
        }
        TextView textView = (TextView)view.findViewById(R.id.contact_name);
        textView.setHint(name);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        Log.d(TAG,"adapter : "+i+" "+i1);
        if(view==null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_list_item, viewGroup, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.tv_expanded_list);
        textView.setText(child.get(i1));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

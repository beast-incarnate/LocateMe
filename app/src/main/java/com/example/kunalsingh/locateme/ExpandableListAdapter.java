package com.example.kunalsingh.locateme;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
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
        return contactsName.size()-1;
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
        String s = ""+name.charAt(0);
        for(int i2=0;i2<name.length();i2++){
            if(name.charAt(i2)==' '){
                for(int j=i2+1;j<name.length();j++){
                    if(name.charAt(j)!=' '){
                        s=s+name.charAt(j);
                        break;
                    }
                }
                break;
            }
        }

        ImageView imageView = (ImageView)view.findViewById(R.id.contacts_image);
        Bitmap bitmap = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50.0f);
        c.drawText(s.toUpperCase(),50.0f,65.0f,paint);
        imageView.setImageBitmap(bitmap);

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

package com.example.shruti.midterm;

/**
 * Created by shruti on 3/5/18.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity Context;
    private final String[] ListItemsName;

    private final String [] ItemDescription;
    //private final String[] Description;
    public CustomListAdapter(Activity context, String[] content,
                             String[] ItemDescription) {
        super(context, R.layout.myclasses, content);
        this.Context = context;
        this.ListItemsName = content;
        this.ItemDescription = ItemDescription;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = Context.getLayoutInflater();
        View ListViewSingle = inflater.inflate(R.layout.myclasses, null, true);
        TextView ListViewItems = (TextView)
                ListViewSingle.findViewById(R.id.courseNameTextview);
        TextView ListViewDesc = (TextView)
                ListViewSingle.findViewById(R.id.courseNoTextview);

        ListViewItems.setText(ListItemsName[position]);

        ListViewDesc.setText(ItemDescription[position]);

        return ListViewSingle;
    };

}
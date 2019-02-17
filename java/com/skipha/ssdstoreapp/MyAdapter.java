/**
 * Author: Tania López Martín
 * Date: 25/01/2019
 * Version: 1.0
 *
 */

package com.skipha.ssdstoreapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.ArrayList;

/**
 * Class MyAdapter
 */
public class MyAdapter extends ArrayAdapter {
    /**
     * Attributes
     */
    private Activity context;
    private String[] data;
    private ArrayList<Items> currentItems;

    /**
     * Constructor
     * @param context
     * @param data
     * @param currentItems
     */
    public MyAdapter(Activity context, String[] data, ArrayList<Items> currentItems) {
        super(context, R.layout.listview, data);
        this.currentItems = currentItems;
        this.context = context;
        this.data = data;
    }

    /**
     * View
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.listview, null);

        TextView type = item.findViewById(R.id.textView3);
        TextView price = item.findViewById(R.id.textView4);
        TextView amountText = item.findViewById(R.id.textView5);
        type.setText(currentItems.get(position).getName());
        price.setText("" + currentItems.get(position).getPrice());
        amountText.setText("" + currentItems.get(position).getNumber());

        return(item);
    }
}


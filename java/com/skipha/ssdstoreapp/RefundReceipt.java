/**
 * Author: Tania López Martín
 * Date: 25/01/2019
 * Version: 1.0
 *
 */

package com.skipha.ssdstoreapp;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * RefundReceipt Class
 */
public class RefundReceipt extends AppCompatActivity {
    /**
     * Attributes
     */
    private String TABLE_ITEMS;
    private int code;
    private SQLiteDatabase db;
    private SQLiteHelper helper;
    private ListView listView;
    private String toastMsg;
    private ArrayList<TextView> textViews;
    private ArrayList<Items> items;
    private ArrayList<String> types;
    private ArrayList<String> prices;
    private MyAdapter adapter;
    private Button button;

    /**
     * onCreate
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt);
        Bundle bundle = getIntent().getExtras();
        code = (int)bundle.get("code");
        items = new ArrayList<>();
        textViews = new ArrayList<>();
        types = new ArrayList<>();
        prices = new ArrayList<>();
        toastMsg = getResources().getString(R.string.toast_update);
        button = findViewById(R.id.button3);
        TABLE_ITEMS = getResources().getString(R.string.table_items);
        listView = findViewById(R.id.receiptListView);
        textViews.add((TextView)findViewById(R.id.textView26));
        textViews.add((TextView)findViewById(R.id.textView20));
        textViews.get(1).setText("" + code);
        helper = new  SQLiteHelper(this, "SSDStore.db", null, 1);
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE  codfactura = " + code, null);

        if(cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                items.add(new Items(cursor.getString(cursor.getColumnIndex("nombre")),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex("precio"))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex("cantidad")))));
            }
        }
        calculatetotal();
        String [] data = new String[items.size()];
        adapter = new MyAdapter(this, data, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items.get(position).setNumber(items.get(position).getNumber() - 1);
                types = new ArrayList<String> (Arrays.asList(getResources().getStringArray(R.array.ssdtypes)));
                prices = new ArrayList<String> (Arrays.asList(getResources().getStringArray(R.array.ssdprices)));

                for (int i = 0; i < types.size(); i++) {
                    if(items.get(position).getName().equals(types.get(i))) {
                        items.get(position).setPrice(items.get(position).getPrice() - Integer.parseInt(prices.get(i)));
                    }
                }
                if(items.get(position).getNumber() == 0) {
                    items.remove(position);
                    String []data = new String[items.size()];
                    adapter = new MyAdapter((Activity)view.getContext(), data, items);
                    listView.setAdapter(adapter);
                }
                calculatetotal();
                adapter.notifyDataSetChanged();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        toastMsg + "",
                        Toast.LENGTH_SHORT);
                toast.show();
                helper = new  SQLiteHelper(getBaseContext(), "SSDStore.db", null, 1);
                db = helper.getWritableDatabase();
                db.execSQL("DELETE FROM " + TABLE_ITEMS + " WHERE codfactura = " + code);
                for (int i = 0; i < items.size(); i++) {
                    db.execSQL("INSERT INTO " + TABLE_ITEMS + " VALUES ('" + items.get(i).getName() + "', "
                            + items.get(i).getPrice() + ", " + items.get(i).getNumber() + ", " + code + ")");
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * int
     * @return
     */
    private int calculatetotal() {
        int total = 0;

        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice();
        }
        textViews.get(0).setText("" + total + " €");
        return total;
    }
}

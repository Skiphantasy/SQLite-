/**
 * Author: Tania López Martín
 * Date: 25/01/2019
 * Version: 1.0
 *
 */

package com.skipha.ssdstoreapp;


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
public class QueryReceipt extends AppCompatActivity {
    /**
     * Attributes
     */
    private static final String TABLE_ITEMS = "items";
    private int code;
    private SQLiteDatabase db;
    private SQLiteHelper helper;
    private ListView listView;
    private String toastMsg;
    private ArrayList<Items> items;
    private ArrayList<String> types;
    private ArrayList<TextView> textViews;
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
        types = new ArrayList<>();
        prices = new ArrayList<>();
        toastMsg = getResources().getString(R.string.toast_update);
        button = findViewById(R.id.button3);
        textViews = new ArrayList<>();
        textViews.add((TextView)findViewById(R.id.textView26));
        textViews.add((TextView)findViewById(R.id.textView20));
        textViews.get(1).setText("" + code);
        listView = findViewById(R.id.receiptListView);
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

        String [] data = new String[items.size()];
        adapter = new MyAdapter(this, data, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        calculatetotal();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
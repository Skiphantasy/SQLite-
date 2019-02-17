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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Class Receipt
 */
public class Receipt extends AppCompatActivity {
    private static final String TABLE_RECEIPTS = "receipts";
    private static final String TABLE_ITEMS = "items";
    private ArrayList<TextView> textViews;
    private SQLiteDatabase db;
    private SQLiteHelper helper;
    private ListView listView;
    private ArrayList<Items> items;
    private MyAdapter adapter;
    private Button button;

    /**
     * Constructor
     */
    public Receipt() {
    }

    /**
     * onCreate
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt);
        textViews = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        items = (ArrayList<Items>)bundle.get("parcel_data");
        button = findViewById(R.id.button3);
        String [] data = new String[items.size()];
        helper = new  SQLiteHelper(this, "SSDStore.db", null, 1);
        db = helper.getWritableDatabase();
        textViews.add((TextView)findViewById(R.id.textView20));
        textViews.add((TextView)findViewById(R.id.textView26));
        listView = findViewById(R.id.receiptListView);
        insertReceipt();
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
     * void
     */
    private void insertReceipt() {
        if(db != null) {
            db.execSQL("INSERT INTO " + TABLE_RECEIPTS + " (empty) VALUES (' ')");
            helper = new  SQLiteHelper(this, "SSDStore.db", null, 1);
            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT codfactura FROM " + TABLE_RECEIPTS + "", null);
            int factura = cursor.getCount();
            textViews.get(0).setText("" + factura);

            helper = new  SQLiteHelper(this, "SSDStore.db", null, 1);
            db = helper.getWritableDatabase();
            for (int i = 0; i < items.size(); i++) {
                db.execSQL("INSERT INTO " + TABLE_ITEMS + " VALUES ('" + items.get(i).getName() + "', "
                        + items.get(i).getPrice() +", " + items.get(i).getNumber() + ", " + factura + ")");
            }
        }
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
        textViews.get(1).setText("" + total + " €");
        return total;
    }

}

/**
 * Author: Tania López Martín
 * Date: 25/01/2019
 * Version: 1.0
 *
 */

package com.skipha.ssdstoreapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Buy extends AppCompatActivity {
    /**
     * Attributes
     */
    private static final String TABLE_SSD = "ssd";
    private static final String TABLE_RECEIPTS = "receipts";
    private static final String TABLE_ITEMS = "items";
    private static int amount;
    private MyAdapter myadapter;
    private String toastMsg;
    private  ArrayList<Items> items;
    private  ArrayList<Items> currentItems;
    private ArrayList<String> types;
    private ArrayList<String> prices;
    private ArrayList<Button> buttons;
    private ArrayList<Spinner> spinners;
    private int spinnersCount;
    private ArrayList <String> numbers;
    private ArrayAdapter adapter;
    private SQLiteDatabase db;
    private SQLiteHelper helper;
    private RelativeLayout relativeLayout;
    private ListView listView;
    private TextView textView;

    /**
     * onCreate
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new  SQLiteHelper(this, "SSDStore.db", null, 1);
        db = helper.getWritableDatabase();
        relativeLayout = findViewById(R.id.rlay);
        textView = findViewById(R.id.textView24);
        items = new ArrayList<>();
        currentItems = new ArrayList<>();
        buttons = new ArrayList<>();
        spinners = new ArrayList<>();
        amount = Integer.parseInt(getResources().getString(R.string.amount));
        types = new ArrayList<>();
        prices = new ArrayList<>();
        numbers = new ArrayList<>();
        listView = findViewById(R.id.listview);
        fillButtons();
        spinnersCount = Integer.parseInt(getResources().getString(R.string.menu_buttons));
        insertSSD();
        toastMsg = getResources().getString(R.string.toast);

        for (int i = 0; i < spinnersCount; i++) {
            String spner = "spinner" + (i + 1);
            spinners.add((Spinner) findViewById(getResources().getIdentifier(spner,"id", getPackageName())));
        }

        spinners.get(0).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateAmountLeft();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * int
     * @return
     */
    private int updateAmountLeft() {
        numbers.clear();
        helper = new  SQLiteHelper(getBaseContext(), "SSDStore.db", null, 1);
        db = helper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_SSD + " WHERE nombre LIKE '"
                + spinners.get(0).getSelectedItem().toString() + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();

        for (int i = 0; i < Integer.parseInt(cursor.getString(cursor.getColumnIndex("cantidad"))); i++) {
            numbers.add("" + (i + 1));
        }

        adapter = new ArrayAdapter(getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item,
                numbers);
        spinners.get(1).setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return Integer.parseInt(cursor.getString(cursor.getColumnIndex("cantidad")));
    }

    private void fillButtons() {
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            if (relativeLayout.getChildAt(i) instanceof Button) {
                buttons.add((Button) relativeLayout.getChildAt(i));
            }
        }

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId() == buttons.get(0).getId()) {
                        if(currentItems.size() > 0) {
                            Intent intent = new Intent(v.getContext(),Receipt.class);
                            intent.putExtra("parcel_data", currentItems);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        if(currentItems.size() > 0) {
                            int counter = 0;
                            for (int j = 0; j < currentItems.size(); j++) {
                                if (spinners.get(0).getSelectedItem().toString().equals(currentItems.get(j).getName())) {
                                    counter++;
                                    currentItems.get(j).setNumber(currentItems.get(j).getNumber() + Integer.parseInt(spinners.get(1).getSelectedItem().toString()));
                                    currentItems.get(j).setPrice(Integer.parseInt(prices.get(spinners.get(0).getSelectedItemPosition()))
                                            * currentItems.get(j).getNumber());
                                }
                            }

                            if(counter == 0) {

                                for (int j = 0; j < items.size(); j++) {
                                    if(spinners.get(0).getSelectedItem().toString().equals(items.get(j).getName())) {
                                        currentItems.add( new Items(spinners.get(0).getSelectedItem().toString(),
                                                items.get(j).getPrice() * Integer.parseInt(spinners.get(1).getSelectedItem().toString()),
                                                Integer.parseInt(spinners.get(1).getSelectedItem().toString())));
                                    }
                                }
                            }
                        } else {
                            for (int j = 0; j < items.size(); j++) {
                                if(spinners.get(0).getSelectedItem().toString().equals(items.get(j).getName())) {
                                    currentItems.add( new Items(spinners.get(0).getSelectedItem().toString(),
                                            items.get(j).getPrice() * Integer.parseInt(spinners.get(1).getSelectedItem().toString()),
                                            Integer.parseInt(spinners.get(1).getSelectedItem().toString())));
                                }
                            }
                        }

                        String[] data = new String[currentItems.size()];
                        myadapter = new MyAdapter((Activity)v.getContext(), data, currentItems);
                        listView.setAdapter(myadapter);
                        myadapter.notifyDataSetChanged();

                        helper = new  SQLiteHelper(getBaseContext(), "SSDStore.db", null, 1);
                        db = helper.getReadableDatabase();
                        String query = "SELECT * FROM " + TABLE_SSD + " WHERE nombre LIKE '"
                                + spinners.get(0).getSelectedItem().toString() + "'";
                        Cursor cursor = db.rawQuery(query, null);
                        cursor.moveToNext();
                        int totalLeft = Integer.parseInt(cursor.getString(cursor.getColumnIndex("cantidad")));
                        db.close();

                        helper = new  SQLiteHelper(getBaseContext(), "SSDStore.db", null, 1);
                        db = helper.getWritableDatabase();
                        db.execSQL("UPDATE " + TABLE_SSD + " SET cantidad = " + (totalLeft - Integer.parseInt(spinners.get(1).getSelectedItem().toString()))
                                +"  WHERE nombre LIKE '"
                                + spinners.get(0).getSelectedItem().toString() + "'");
                    }
                    calculatetotal();

                    if (updateAmountLeft() <= 2) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                toastMsg,
                                Toast.LENGTH_SHORT);
                        toast.show();
                        starThread();
                        updateAmountLeft();
                    }
                }
            });
        }
    }

    /**
     * void
     */
    private void starThread() {
        new Thread(new Runnable() {
            public void run() {
                helper = new  SQLiteHelper(getBaseContext(), "SSDStore.db", null, 1);
                db = helper.getWritableDatabase();
                db.execSQL("UPDATE " + TABLE_SSD + " SET cantidad = " + 10
                        +"  WHERE nombre LIKE '"
                        + spinners.get(0).getSelectedItem().toString() + "'");
            }
        }).start();
    }

    /**
     * void
     */
    private void insertSSD() {
        if(db != null)
        {
            types = new ArrayList<String> (Arrays.asList(getResources().getStringArray(R.array.ssdtypes)));
            prices = new ArrayList<String> (Arrays.asList(getResources().getStringArray(R.array.ssdprices)));

            for(int i = 0; i < types.size(); i++)
            {
                db.execSQL("INSERT INTO " + TABLE_SSD +
                        " VALUES ('" + types.get(i) + "', " + Integer.parseInt(prices.get(i)) + ", " + amount + ")");
                items.add(new Items(types.get(i), Integer.parseInt(prices.get(i)), amount));
            }

            db.close();
        }
    }

    /**
     * int
     * @return
     */
    private int calculatetotal() {
        int total = 0;

        for (int i = 0; i < currentItems.size(); i++) {
            total += currentItems.get(i).getPrice();
        }
        textView.setText("" + total + " €");
        return total;
    }
}

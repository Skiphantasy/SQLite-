/**
 * Author: Tania López Martín
 * Date: 25/01/2019
 * Version: 1.0
 *
 */

package com.skipha.ssdstoreapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Class MainActivity
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Attributes
     */
    private int numbersCount;
    private ArrayList<Button> buttons;
    private EditText editText;
    private String toastMsg;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        buttons = new ArrayList<>();
        numbersCount = Integer.parseInt(getResources().getString(R.string.menu_buttons));
        editText = findViewById(R.id.editText2);
        toastMsg = getResources().getString(R.string.toast_rr);
        fillButtons();
    }

    /**
     * void
     */
    private void fillButtons() {
        for (int i = 0; i < numbersCount; i++) {
            String buttonid = "button" + (i + numbersCount + 1);
            buttons.add((Button)findViewById(getResources().getIdentifier(buttonid,"id", getPackageName())));
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId() == buttons.get(0).getId()) {
                        Intent intent = new Intent(v.getContext(), QueryReceipt.class);

                        if(tryParseInt(editText.getText().toString())) {
                            intent.putExtra("code", Integer.parseInt(editText.getText().toString()));
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    toastMsg + "",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else if (v.getId() == buttons.get(1).getId()){
                        Intent intent = new Intent(v.getContext(), Buy.class);
                        startActivity(intent);
                    } else if (v.getId() == buttons.get(2).getId()){
                        Intent intent = new Intent(v.getContext(), RefundReceipt.class);

                        if(tryParseInt(editText.getText().toString())) {
                            intent.putExtra("code", Integer.parseInt(editText.getText().toString()));
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    toastMsg + "",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            });
        }
    }

    /**
     * boolean
     * @param value
     * @return
     */
    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

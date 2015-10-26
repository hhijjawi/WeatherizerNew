package com.weatherizer.hisham.weatherizer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        NumberPicker numDays=(NumberPicker)findViewById(R.id.numPicker);
        numDays.setMinValue(1);
        numDays.setMaxValue(9);
        numDays.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                MainActivity.numDays = newVal;
            }
        });
        RadioGroup unitSelection=(RadioGroup)findViewById(R.id.unitsGroup);
        unitSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton){
                    MainActivity.units="celsius";
                }
                else{
                    MainActivity.units="fahrenheit";
                }
            }
        });
        final EditText zipCode= (EditText)findViewById(R.id.editText);
        zipCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    MainActivity.mZipCode = Integer.parseInt(zipCode.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        //From Stack OVerFlow how to delay since i had no clue about handlers

final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Wait to update settings");
        progressDialog.show();
        final Handler h = new Handler();

        Runnable r = new Runnable() {
            @Override
            public void run(){
                progressDialog.dismiss();
                 final Runnable finish = new Runnable() {
                    @Override
                    public void run(){
                        finish();
                    };


                };
                h.postDelayed(finish, 1000);
        };
            };
        h.postDelayed(r, 10000);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.example.android.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ViewAnimator;

import com.example.android.addClothing;
import com.example.android.common.activities.SampleActivityBase;
import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends SampleActivityBase {

    public static final String TAG = "MainActivity";
    HashSet<String> clothSet = new HashSet();
    HashMap<Integer, ArrayList<String>> clothdata = new HashMap<>();
    String[] option = new String[5];
    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        HashSet<String> clothSet = new HashSet();
//        HashMap<Integer, ArrayList<String>> clothdata = new HashMap<Integer, ArrayList<String>>();
        ArrayList<String> clothchoice1 = new ArrayList<String>();
        ArrayList<String> clothchoice2 = new ArrayList<String>();
        ArrayList<String> clothchoice3 = new ArrayList<String>();
        ArrayList<String> clothchoice4 = new ArrayList<String>();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

        clothdata.put(2, clothchoice2);
        clothdata.put(3, clothchoice3);
        clothdata.put(1, clothchoice1);
        clothdata.put(4, clothchoice4);

    }

    public boolean sendMessage(View view)
    {
        if (view.getId() == R.id.tshirtID) {
            Log.d("vivian", "tshirt");
            Addcloth(1, "tshirt");
            return true;
        } else if (view.getId() == R.id.hoodieID) {
            Log.d("vivian", "hoodie");
            Addcloth(2, "hoodie");
            return true;
        } else if (view.getId() == R.id.sweaterID) {
            Log.d("vivian", "sweater");
            Addcloth(3, "sweater");
            return true;
        } else if (view.getId() == R.id.lightjacketID) {
            Log.d("vivian", "lightjacket");
            Addcloth(4, "lightjacket");
            return true;
        } else if (view.getId() == R.id.thickjacketID) {
            Log.d("vivian", "thickjacket");
            Addcloth(5, "thickjacket");
            return true;
        } else if (view.getId() == R.id.puffyjacketID) {
            Log.d("vivian", "puffyjacket");
            Addcloth(6, "puffyjacket");
            return true;
        }
        return false;
    }

    public void Addcloth(int m, String clothes){
        if (clothdata.containsKey(m)) {
            clothdata.get(m).add(clothes);
        } else {
            ArrayList<String> newList = new ArrayList<String>();
            newList.add(clothes);
            clothdata.put(m, newList);
        }
    }

    public String[] choice(int warmlevel, int temp){
        int need = warmlevel - temp;
        Object[] key = clothdata.keySet().toArray();
        int i = 0;
        while (need != 0) {
            Object randomkey = key[new Random().nextInt(key.length)];
            Random rand = new Random();
            int randomIndex = rand.nextInt(clothdata.get(randomkey).size());
            String randomcloth = clothdata.get(randomkey).get(randomIndex);
            need = need - (int) randomkey;
            option[i] = randomcloth;
            i++;
        }
        return option;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
            case R.id.new_item:
                Intent myIntent = new Intent(MainActivity.this, addClothing.class);
                startActivityForResult(myIntent, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}

package com.example.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.common.activities.SampleActivityBase;
import com.example.android.recyclerview.MainActivity;
import com.example.android.recyclerview.R;

public class addClothing extends SampleActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_new_clothing);
    }

}

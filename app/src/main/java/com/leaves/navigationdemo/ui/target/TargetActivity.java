package com.leaves.navigationdemo.ui.target;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.leaves.navigationannotation.ActivityDestination;
import com.leaves.navigationdemo.R;

@ActivityDestination(pageUrl = "activity/center/target",asStart = false)
public class TargetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
    }
}
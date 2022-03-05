package com.ideaworks3d.airplay;


import android.os.Bundle;

import com.hostapp.R;

public class AirplayActivity extends PluginBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airplay);
        initPlugin();
    }
}

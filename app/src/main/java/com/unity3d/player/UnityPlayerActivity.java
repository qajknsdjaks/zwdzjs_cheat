package com.unity3d.player;


import android.os.Bundle;

import com.ideaworks3d.airplay.PluginBaseActivity;

public class UnityPlayerActivity extends PluginBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPlugin();
    }
}

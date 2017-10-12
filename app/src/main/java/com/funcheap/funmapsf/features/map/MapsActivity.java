package com.funcheap.funmapsf.features.map;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.funcheap.funmapsf.R;

public class MapsActivity extends AppCompatActivity {

    private static final String MAP_FRAGMENT_TAG = "map_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initMapFragment();
        initListFragment();
    }

    private void initMapFragment() {
        // Load the MapFragment
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.map_container, MapFragment.newInstance(), MAP_FRAGMENT_TAG)
                .commit();
    }

    private void initListFragment() {
        // TODO Load ListFragment
    }
}

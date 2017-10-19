package com.funcheap.funmapsf.features.list.cluster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.funcheap.funmapsf.R;

/**
 * Created by Jayson on 10/19/2017.
 *
 * This is an empty activity that simply loads the ListClusterFragment.
 */

public class ListClusterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster_list);

        loadListClusterActivity();

    }

    private void loadListClusterActivity() {
        Fragment fragment = ListClusterFragment.newInstance();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame_cluster, fragment, null);
        ft.commit();
    }
}

package com.funcheap.funmapsf.features.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.features.list.bookmarks.ListBookmarksFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/12/2017.
 * <p>
 * This is the Main activity which holds the bottom navigation view. The bottom navigation
 * loads different fragments within the content container.
 */

public class HomeActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.bottom_navigation)
    public BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initBottomNav();
    }

    private void initBottomNav() {
        // TODO Figure out how to stop loaded fragments from being reselected
        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    loadFragment(HomeFragment.newInstance());
                    return true;
                case R.id.action_filters:
                    Toast.makeText(this, "Open Filters fragment!", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.action_bookmarks:
                    loadFragment(ListBookmarksFragment.newInstance());
                    return true;
                default:
                    Log.d(TAG, "initBottomNav: Unrecognized menu selection!");
                    return false;
            }
        });

        // Select the search button by default
        mBottomNav.setSelectedItemId(R.id.action_search);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.disallowAddToBackStack();
        ft.replace(R.id.content_frame_home, fragment, null);
        ft.commit();
    }

}

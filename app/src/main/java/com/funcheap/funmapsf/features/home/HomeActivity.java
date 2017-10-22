package com.funcheap.funmapsf.features.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.features.detail.DetailActivity;
import com.funcheap.funmapsf.features.filter.list.ListFiltersFragment;
import com.funcheap.funmapsf.features.filter.edit.EditFilterFragment;
import com.funcheap.funmapsf.features.list.bookmarks.ListBookmarksFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/12/2017.
 * <p>
 * This is the Main activity which holds the bottom navigation view. The bottom navigation
 * loads different fragments within the content container.
 */

public class HomeActivity extends AppCompatActivity implements 
		HomeFragment.FilterClickListener,
        EditFilterFragment.FilterSavedListener{

    private String TAG = this.getClass().getSimpleName();
    private EditFilterFragment selectedFragment;

    @BindView(R.id.bottom_navigation)
    public BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        checkNotification();

        initBottomNav();
    }

    /**
     * Checks if we've been opened from a notification. If so, launch
     * the DetailActivity to the corresponding event.
     */
    private void checkNotification() {
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.hasExtra(DetailActivity.EVENT_EXTRA_ID)) {
            Log.d(TAG, "checkNotification: Launching directly to detail activity with EventID = "
                    + intent.getStringExtra(DetailActivity.EVENT_EXTRA_ID));
            intent.setClass(this, DetailActivity.class);
            startActivity(intent);
        }

    }

    private void initBottomNav() {
        // TODO Figure out how to stop loaded fragments from being reselected
        mBottomNav.setVisibility(View.VISIBLE);
        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    loadFragment(HomeFragment.newInstance());
                    return true;
                case R.id.action_filters:
                    loadFragment(ListFiltersFragment.newInstance());
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

    @Override
    public void onFilterClicked() {
        EditFilterFragment frag = EditFilterFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("backStack");
        mBottomNav.setVisibility(View.GONE);
        ft.replace(R.id.content_frame_home, frag, null);
        ft.commit();
    }

    @Override
    public void onFilterSaved() {
        mBottomNav.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSelectedFragment(EditFilterFragment editFilterFragment) {
        this.selectedFragment = editFilterFragment;
    }


    @Override
    public void onBackPressed() {
        if(selectedFragment == null) {
            // Selected fragment did not consume the back press event.
            super.onBackPressed();
        }
        else{

            // For EditFilterFragment if back key is pressed
            getSupportFragmentManager().popBackStack();
            mBottomNav.setVisibility(View.VISIBLE);
        }

    }
}

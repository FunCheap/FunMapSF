package com.funcheap.funmapsf.features.home;

import android.arch.lifecycle.ViewModelProviders;
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
import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.features.detail.DetailActivity;
import com.funcheap.funmapsf.features.filter.SaveFilterDialogFragment;
import com.funcheap.funmapsf.features.filter.list.ListFiltersFragment;
import com.funcheap.funmapsf.features.filter.edit.EditFilterFragment;
import com.funcheap.funmapsf.features.list.bookmarks.ListBookmarksFragment;
import com.funcheap.funmapsf.features.map.MapsViewModel;

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
        EditFilterFragment.FilterSavedListener,
        SaveFilterDialogFragment.SaveFilterListener {

    private String TAG = this.getClass().getSimpleName();
    private EditFilterFragment mSelectedFragment;

    @BindView(R.id.bottom_navigation)
    public BottomNavigationView mBottomNav;

    MapsViewModel mMapsModel;
    Filter mFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        checkNotification();
        mMapsModel = ViewModelProviders.of(this).get(MapsViewModel.class);

        initBottomNav();
        mFilter = Filter.setDefaultFilter();
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


    // Callback from HomeFragment to launch EditFilterFragment
    @Override
    public void onFilterClicked() {
        EditFilterFragment frag = EditFilterFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("backStack");
        mBottomNav.setVisibility(View.GONE);
        ft.replace(R.id.content_frame_home, frag, null);
        ft.commit();
    }


    // Callback from EditFilterFragment to apply filter,
    // Get the events list from the db based on the filter and update the Map View
    @Override
    public void onFilterSaved(Filter filter) {
        mBottomNav.setVisibility(View.VISIBLE);
        // update the events in Map View  based on filter
        if(!filter.getQuery().isEmpty()){
            mMapsModel.setEvents(Events.getEvents(filter.getQuery()));
            mFilter = filter;
        }
        mSelectedFragment=null;
    }

    @Override
    public void setSelectedFragment(EditFilterFragment editFilterFragment) {
        this.mSelectedFragment = editFilterFragment;
    }


    @Override
    public void onBackPressed() {
        if(mSelectedFragment == null) {
            // Selected fragment did not consume the back press event.
            super.onBackPressed();
        }
        else{
            // For EditFilterFragment if back key is pressed
            getSupportFragmentManager().popBackStack();
            mBottomNav.setVisibility(View.VISIBLE);
            mSelectedFragment = null;
        }

    }

    // On FAB Click the filter is saved with filter name
    @Override
    public void saveFilter(String filterName) {
        mFilter.setFilterName(filterName);
        mFilter.save();
    }
}

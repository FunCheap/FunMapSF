package com.funcheap.funmapsf.features.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.database.MyDatabase;
import com.funcheap.funmapsf.commons.interfaces.OnBackClickCallback;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.features.detail.DetailActivity;
import com.funcheap.funmapsf.features.filter.SaveFilterDialogFragment;
import com.funcheap.funmapsf.features.filter.edit.EditFilterDiaglogFragment;
import com.funcheap.funmapsf.features.filter.list.ListFilterViewModel;
import com.funcheap.funmapsf.features.filter.list.ListFiltersFragment;
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
        SaveFilterDialogFragment.SaveFilterListener {

    private final String TAG = this.getClass().getSimpleName();

    private final String TAG_MAP_FRAGMENT = "map_fragment";
    private final String TAG_FILTERS_FRAGMENT = "filters_fragment";
    private final String TAG_BOOKMARKS_FRAGMENT = "bookmarks_fragment";

    private EditFilterDiaglogFragment mSelectedFragment;

    @BindView(R.id.bottom_navigation)
    public BottomNavigationView mBottomNav;

    public MapsViewModel mMapsModel;
    public ListFilterViewModel mListFiltersViewModel;
    private MyDatabase db;

    private HomeFragment mHomeFragment = (HomeFragment) HomeFragment.newInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mMapsModel = ViewModelProviders.of(this).get(MapsViewModel.class);
        mListFiltersViewModel = ViewModelProviders.of(this).get(ListFilterViewModel.class);

        checkNotification();
        initBottomNav();
        loadFragments();

        db = new MyDatabase(this);
        db.getDB();
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

    /**
     * Load Home fragment initially
     */
    private void loadFragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.disallowAddToBackStack()
                .add(R.id.content_frame_home, mHomeFragment, mHomeFragment.getClass().getSimpleName())
                .commit();
    }

    private void initBottomNav() {
        /*
         * Programmatically show and hide fragments based on the selection
         */
        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.action_search:
                    ft.disallowAddToBackStack();
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    if (fm.findFragmentByTag(TAG_FILTERS_FRAGMENT) != null) {
                        ft.remove(getSupportFragmentManager().findFragmentByTag(TAG_FILTERS_FRAGMENT));
                    }
                    if (fm.findFragmentByTag(TAG_BOOKMARKS_FRAGMENT) != null) {
                        ft.remove(getSupportFragmentManager().findFragmentByTag(TAG_BOOKMARKS_FRAGMENT));
                    }
                    ft.show(mHomeFragment)
                            .commit();
                    return true;
                case R.id.action_filters:
                    ft.disallowAddToBackStack()
                            .hide(mHomeFragment);
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    if (fm.findFragmentByTag(TAG_BOOKMARKS_FRAGMENT) != null) {
                        ft.remove(getSupportFragmentManager().findFragmentByTag(TAG_BOOKMARKS_FRAGMENT));
                    }
                    ft.add(R.id.content_frame_home, ListFiltersFragment.newInstance(), TAG_FILTERS_FRAGMENT)
                            .commit();
                    return true;
                case R.id.action_bookmarks:
                    ft.disallowAddToBackStack()
                            .hide(mHomeFragment);
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    if (fm.findFragmentByTag(TAG_FILTERS_FRAGMENT) != null) {
                        ft.remove(getSupportFragmentManager().findFragmentByTag(TAG_FILTERS_FRAGMENT));
                    }
                    ft.add(R.id.content_frame_home, ListBookmarksFragment.newInstance(), TAG_BOOKMARKS_FRAGMENT)
                            .commit();
                    return true;
                default:
                    Log.d(TAG, "initBottomNav: Unrecognized menu selection!");
                    ft.commit();
                    return false;
            }
        });

        // Select the mEditSearch button by default
        mBottomNav.setSelectedItemId(R.id.action_search);
    }

    @Override
    public void onBackPressed() {
        OnBackClickCallback homeFragment = (HomeFragment) getSupportFragmentManager()
                .findFragmentByTag(HomeFragment.class.getSimpleName());
        if (homeFragment != null) {
            if (!homeFragment.onBackClick()) {
                // Selected fragment did not consume the back press event.
                super.onBackPressed();
            }
        }
    }

    // On FAB Click the filter is saved with filter name
    @Override
    public void saveFilter(String filterName) {
        Filter filter = mMapsModel.getFilter().getValue();
        if (filter != null) {
            filter.setFilterName(filterName);
            filter.save();
        }
    }

    /**
     * Set the current filter using our MapViewModel and load the MapFragment
     *
     * @param filter new filter to use
     */
    public void setFilter(Filter filter) {
        mMapsModel.setFilter(filter);
        mBottomNav.setSelectedItemId(R.id.action_search);
    }
}

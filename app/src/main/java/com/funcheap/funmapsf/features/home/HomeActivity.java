package com.funcheap.funmapsf.features.home;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.database.MyDatabase;
import com.funcheap.funmapsf.commons.interfaces.OnBackClickCallback;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.features.detail.DetailActivity;
import com.funcheap.funmapsf.features.filter.list.ListFilterViewModel;
import com.funcheap.funmapsf.features.filter.list.ListFiltersActivity;
import com.funcheap.funmapsf.features.list.bookmarks.ListBookmarksFragment;
import com.funcheap.funmapsf.features.map.MapsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

/**
 * Created by Jayson on 10/12/2017.
 * <p>
 * This is the Main activity which holds the bottom navigation view. The bottom navigation
 * loads different fragments within the content container.
 */

public class HomeActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    public final int REQUEST_CODE_SAVED_FILTERS = 0;
    private final String TAG_MAP_FRAGMENT = "map_fragment";
    private final String TAG_FILTERS_FRAGMENT = "filters_fragment";
    private final String TAG_BOOKMARKS_FRAGMENT = "bookmarks_fragment";

    @BindView(R.id.bottom_navigation)
    public BottomNavigation mBottomNav;
    @BindView(R.id.toolbar_main)
    public Toolbar mToolbar;

    public MapsViewModel mMapViewModel;
    public ListFilterViewModel mListFiltersViewModel;
    private MyDatabase db;

    private HomeFragment mHomeFragment = (HomeFragment) HomeFragment.newInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mMapViewModel = ViewModelProviders.of(this).get(MapsViewModel.class);
        mListFiltersViewModel = ViewModelProviders.of(this).get(ListFilterViewModel.class);

        initToolbar();
        checkNotification();
        initBottomNav();
        loadFragments();

        db = new MyDatabase(this);
        db.getDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_filters:
                Intent intent = new Intent(this, ListFiltersActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SAVED_FILTERS);
                return true;
            case R.id.action_switch_view:
                mMapViewModel.toggleListMode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        this.setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        // TODO Tint toolbar icons
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
                .add(R.id.content_frame_home, mHomeFragment, TAG_MAP_FRAGMENT)
                .commit();
    }

    private void initBottomNav() {
        /*
         * Programmatically show and hide fragments based on the selection
         */
        mBottomNav.setOnMenuItemClickListener(
                new BottomNavigation.OnMenuItemSelectionListener() {
                    @Override
                    public void onMenuItemSelect(@IdRes int item, int i1, boolean b) {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        switch (item) {
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
                                break;
                            case R.id.action_bookmarks:
                                ft.disallowAddToBackStack()
                                        .hide(mHomeFragment);
                                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                                if (fm.findFragmentByTag(TAG_FILTERS_FRAGMENT) != null) {
                                    ft.remove(getSupportFragmentManager().findFragmentByTag(TAG_FILTERS_FRAGMENT));
                                }
                                ft.add(R.id.content_frame_home, ListBookmarksFragment.newInstance(), TAG_BOOKMARKS_FRAGMENT)
                                        .commit();
                                break;
                            default:
                                Log.d(TAG, "initBottomNav: Unrecognized menu selection!");
                                ft.commit();
                        }
                    }

                    @Override
                    public void onMenuItemReselect(@IdRes int id, int i1, boolean b) {
                        // Do nothing
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SAVED_FILTERS:
                // Set the current filter to the returned saved filter
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "onActivityResult: User applied a saved filter.");
                    mMapViewModel.setFilter(
                            data.getParcelableExtra(ListFiltersActivity.EXTRA_FILTER_RESULT));
                } else {
                    Log.d(TAG, "onActivityResult: User canceled aplying a saved filter.");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        OnBackClickCallback homeFragment = (HomeFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_MAP_FRAGMENT);
        if (homeFragment != null) {
            if (!homeFragment.onBackClick()) {
                // Selected fragment did not consume the back press event.
                super.onBackPressed();
            }
        }
    }

    /**
     * Set the current filter using our MapViewModel and load the MapFragment
     *
     * @param filter new filter to use
     */
    public void setFilter(Filter filter) {
        // TODO This is showing the bottomsheet for some reason.
        getSupportFragmentManager().popBackStack();
        mMapViewModel.setFilter(filter);
    }
}

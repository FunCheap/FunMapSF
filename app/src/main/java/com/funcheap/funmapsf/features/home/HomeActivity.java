package com.funcheap.funmapsf.features.home;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.interfaces.OnBackClickCallback;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.features.detail.DetailActivity;
import com.funcheap.funmapsf.features.filter.list.ListFiltersActivity;
import com.funcheap.funmapsf.features.login.LoginActivity;
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
    private final String TAG_HOME_FRAGMENT = "home_fragment";
    private final String TAG_FILTERS_FRAGMENT = "filters_fragment";
    private final String TAG_BOOKMARKS_FRAGMENT = "bookmarks_fragment";

    @BindView(R.id.bottom_navigation)
    public BottomNavigation mBottomNav;
    @BindView(R.id.toolbar_main)
    public Toolbar mToolbar;

    public MapsViewModel mMapViewModel;

    private HomeFragment mHomeFragment = (HomeFragment) HomeFragment.newInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mMapViewModel = ViewModelProviders.of(this).get(MapsViewModel.class);

        initToolbar();
        checkNotification();
        initBottomNav();
        loadFragments();
        initSearchMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.home_toolbar, menu);

        if (mMapViewModel.getDisplayMode().getValue() != null) {
            int displayMode = mMapViewModel.getDisplayMode().getValue();
            MenuItem item = mToolbar.getMenu().findItem(R.id.action_filters);

            if (displayMode == MapsViewModel.SEARCH_MODE) {
                item.setVisible(true);
            } else if (displayMode == MapsViewModel.BOOKMARKS_MODE) {
                item.setVisible(false);
            }
        }

        if (mMapViewModel.getListMode().getValue() != null) {
            MenuItem item = mToolbar.getMenu().findItem(R.id.action_switch_view);

            if (mMapViewModel.getListMode().getValue()) { // List Mode
                item.setIcon(R.drawable.ic_map);
            } else { // Map Mode
                item.setIcon(R.drawable.ic_format_list_bulleted);
            }
        }

        // Color icons
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }
        }

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
                invalidateOptionsMenu();
                return true;
            case R.id.action_logout:
                AlertDialog dialog = new AlertDialog.Builder(this).
                        setMessage(this.getResources().getString(R.string.logout_msg)).
                        setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LoginManager.getInstance().logOut();
                                Intent intent = new Intent();
                                intent.setClass(HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).
                        setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                dialog.show();
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

    private void initSearchMode() {
        /*
         * Hide the filter button in the options menu when
         * the display mode is Bookmark.
         */
        mMapViewModel.getDisplayMode().observe(this, displayMode -> this.invalidateOptionsMenu());
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
                .add(R.id.content_frame_home, mHomeFragment, TAG_HOME_FRAGMENT)
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
                        switch (item) {
                            case R.id.action_search:
                                // Handle 'Search' button click
                                mMapViewModel.setDisplayMode(MapsViewModel.SEARCH_MODE);
                                break;
                            case R.id.action_bookmarks:
                                // Handle 'Bookmark' button click
                                mMapViewModel.setDisplayMode(MapsViewModel.BOOKMARKS_MODE);
                                break;
                            default:
                                Log.d(TAG, "initBottomNav: Unrecognized menu selection!");
                        }
                    }

                    @Override
                    public void onMenuItemReselect(@IdRes int item, int i1, boolean b) {
                        // Re-apply filter if bookmarks is re-selected
                        switch (item) {
                            case R.id.action_bookmarks:
                                mMapViewModel.setDisplayMode(MapsViewModel.BOOKMARKS_MODE);
                                break;
                        }
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
                .findFragmentByTag(TAG_HOME_FRAGMENT);
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

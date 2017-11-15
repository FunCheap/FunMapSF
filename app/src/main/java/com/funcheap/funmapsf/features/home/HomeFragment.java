package com.funcheap.funmapsf.features.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.interfaces.OnBackClickCallback;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.commons.utils.ChipUtils;
import com.funcheap.funmapsf.features.filter.SaveFilterDialogFragment;
import com.funcheap.funmapsf.features.list.home.ListHomeFragment;
import com.funcheap.funmapsf.features.map.MapFragment;
import com.funcheap.funmapsf.features.map.MapsViewModel;
import com.vpaliy.chips_lover.ChipView;

import org.apmem.tools.layouts.FlowLayout;
import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/12/2017.
 * <p>
 * This fragment holds the view pager which switches between the Map and List fragments as
 * well as the filter settings and SaveFiler FAB
 */

public class HomeFragment extends Fragment
        implements OnBackClickCallback {

    public static final int SAVE_REQUEST_CODE = 1;

    private static final String[] PLACES = new String[] {
            "San Francisco", "Oakland", "Berkeley", "Mill Valley", "Mountain View", "Brisbane", "Alameda",
            "Palo Alto", "Fremont", "San Jose", "Santa Clara", "Livermore", "Dublin", "Burlingame", "San Carlos"
    };

    private static final String TAG_MAP_FRAGMENT = "map_fragment";
    private static final String TAG_LIST_FRAGMENT = "list_fragment";

    private MapsViewModel mMapsViewModel;

    // Fragment Container
    @BindView(R.id.content_frame_home_fragment)
    public FrameLayout mHomeContainer;

    // Bottom Sheet
    @BindView(R.id.filter_bottom_sheet)
    public ConstraintLayout mFilterSheet;
    @BindView(R.id.filter_chips)
    public LinearLayout mChipsFilterLayout;
    @BindView(R.id.when_spin)
    public Spinner mSpinWhen;
    @BindView(R.id.edit_where)
    public AutoCompleteTextView mEditWhere;
    @BindView(R.id.price_mstb)
    public MultiStateToggleButton mTogglePrice;
    @BindView(R.id.category_spin)
    Spinner mSpinCategory;
    @BindView(R.id.category_chips)
    public FlowLayout mChipsCategoryLayout;
    @BindView(R.id.query)
    public EditText mEditQuery;

    @BindView(R.id.fab_search)
    public FloatingActionButton mFabSearch;
    @BindView(R.id.fab_save)
    public FloatingActionButton mFabSave;
    @BindView(R.id.fab_apply)
    public FloatingActionButton mFabApply;

    MapFragment mMapFragment;
    ListHomeFragment mListHomeFragment;

    private BottomSheetBehavior mBottomSheetBehavior;

    ArrayList<String> mWhenList;
    ArrayList<String> mCategoryList;
    ArrayAdapter<String> mCategoryAdp;
    ArrayList<String> mCategoriesSelected;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        // Set any input args here
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);

        mMapsViewModel = ViewModelProviders.of(getActivity()).get(MapsViewModel.class);

        initHomeContainer();
        initBottomSheet();
        initFabs();
        initSearchMode();
        prepareWhenList();
        preparePlace();
        prepareCategories();
        initFilterChips();
        initFilterListener();
        mTogglePrice.setValue(0);

        return root;
    }

    private void initHomeContainer() {

        mMapFragment = MapFragment.newInstance();
        mListHomeFragment = ListHomeFragment.newInstance();

        // Load initial fragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.disallowAddToBackStack()
                .add(R.id.content_frame_home_fragment, mMapFragment, TAG_MAP_FRAGMENT)
                .add(R.id.content_frame_home_fragment, mListHomeFragment, TAG_LIST_FRAGMENT)
                .hide(mListHomeFragment)
                .commit();

        mMapsViewModel.getListMode().observe(this, isListMode -> {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.animator.fragment_fade_in, R.animator.fragment_fade_out);

            if (!isListMode) { // Show Map
                if (mMapFragment.isAdded()) {
                    transaction.show(mMapFragment);
                } else {
                    transaction.add(R.id.content_frame_home_fragment, mMapFragment, TAG_MAP_FRAGMENT);
                }

                if (mListHomeFragment.isAdded())
                    transaction.hide(mListHomeFragment);
            } else { // Show List
                if (mListHomeFragment.isAdded()) {
                    transaction.show(mListHomeFragment);
                } else {
                    transaction.add(R.id.content_frame_home_fragment, mListHomeFragment, TAG_LIST_FRAGMENT);
                }

                if (mMapFragment.isAdded()) {
                    transaction.hide(mMapFragment);
                }
            }

            transaction.commit();
        });
    }

    /**
     * Handle UI toggles between Search mode and Bookmarks mode
     */
    private void initSearchMode() {
        mMapsViewModel.getDisplayMode().observe( this, displayMode -> {
            if (displayMode == MapsViewModel.SEARCH_MODE) {
                mBottomSheetBehavior.setHideable(false);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mFabSearch.setVisibility(View.VISIBLE);
            } else if (displayMode == MapsViewModel.BOOKMARKS_MODE) {
                mBottomSheetBehavior.setHideable(true);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                mFabSearch.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Hook to consume the back click provided to the parent activity.
     * @return true if we consumed the back click, false otherwise.
     */
    @Override
    public boolean onBackClick() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        } else {
            return false;
        }
    }

    // On FAB Click the filter is saved with filter name

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SaveFilterDialogFragment.RESULT_SAVE) {
            String filterName = data.getStringExtra(SaveFilterDialogFragment.EXTRA_FILTER_NAME);

            Filter filter = mMapsViewModel.getFilter().getValue();
            if (filter != null) {
                filter.setFilterName(filterName);
                filter.save();
            }

            searchDBandSendEvents();

            // Delay bottom sheet collapse so keyboard has time to collapse first.
            Handler handler = new Handler(Looper.getMainLooper());
            final Runnable r = () -> mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            handler.postDelayed(r, 300);
        }
    }

    private void initFabs() {
        mFabSearch.setOnClickListener(
                view -> mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        );

        mFabSave.setOnClickListener(
                view -> {
                    SaveFilterDialogFragment saveFilter = SaveFilterDialogFragment.newInstance();
                    saveFilter.setTargetFragment(this, SAVE_REQUEST_CODE);

                    FragmentManager fm;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fm = getFragmentManager();
                    } else {
                        fm = getChildFragmentManager();
                    }
                    saveFilter.show(fm, "save_flter");
                });

        mFabApply.setOnClickListener(view -> {
            searchDBandSendEvents();
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
    }

    private void initBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mFilterSheet);
        mBottomSheetBehavior.setHideable(false);

        mChipsFilterLayout.setOnClickListener(
                view -> mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d("BottomSheetCallback", "onStateChanged: " + newState);

                /**
                 * Show category chips only when drawer is expanded
                 */
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // Edit category chips
                    Filter filter = mMapsViewModel.getFilter().getValue();
                    mCategoriesSelected.clear();
                    mCategoriesSelected.addAll(filter.getCategoriesList());
                    mChipsCategoryLayout.removeAllViews();
                    for (String s : filter.getCategoriesList()) {
                        // Create category chip
                        ChipView chip = ChipUtils.createRemovableChip(s);
                        // Set up to remove chip when clicked
                        chip.setEndIconEventClick(endView -> {
                            ChipView clickedChip = (ChipView) endView.getParent();
                            mChipsCategoryLayout.removeView(clickedChip);
                            mCategoriesSelected.remove(clickedChip.getChipText());
                        });
                        mChipsCategoryLayout.addView(chip);

                        ((ViewGroup.MarginLayoutParams) chip.getLayoutParams())
                                .setMargins(0, 0, 20, 20);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("BottomSheetCallback", "onSlide: " + slideOffset);
            }
        });
    }

    private void prepareWhenList() {
        List<String> temp = Arrays.asList(getResources().getStringArray(R.array.when_array));
        mWhenList = new ArrayList<>(temp);
        ArrayAdapter<String> spinAdp =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mWhenList);
        mSpinWhen.setAdapter(spinAdp);
    }

    private void preparePlace() {
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, PLACES);
        mEditWhere.setAdapter(placeAdapter);
    }

    private void prepareCategories() {
        List<String> temp = Arrays.asList(
                getResources().getStringArray(R.array.categories_array));
        mCategoryList = new ArrayList<>(temp);
        mCategoryAdp = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mCategoryList);
        mCategoriesSelected = new ArrayList<>();
        mSpinCategory.setAdapter(mCategoryAdp);
        mSpinCategory.setSelection(0, false); // Set this so initial selection does not trigger listener

        mSpinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String category = mCategoryList.get(i);
                if (!mCategoriesSelected.contains(category) && i != 0) {
                    mCategoriesSelected.add(category);

                    // Create category chip
                    ChipView chip = ChipUtils.createRemovableChip(category);
                    // Set up to remove chip when clicked
                    chip.setEndIconEventClick(endView -> {
                        ChipView clickedChip = (ChipView) endView.getParent();
                        mChipsCategoryLayout.removeView(clickedChip);
                        mCategoriesSelected.remove(clickedChip.getChipText());
                    });
                    mChipsCategoryLayout.addView(chip);
                    ((ViewGroup.MarginLayoutParams) chip.getLayoutParams())
                            .setMargins(0, 0, 20, 20);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void searchDBandSendEvents() {
        Filter filter = new Filter();
        filter.setQuery(mEditQuery.getText().toString());
        filter.setWhenDate((String) (mSpinWhen.getSelectedItem()));
        filter.setFree(mTogglePrice.getValue() == 1); // 1 == true == FREE, 0 == false == Any
        filter.setVenueQuery(mEditWhere.getText().toString());
        if (mCategoriesSelected.size() != 0)
            filter.setCategories(mCategoriesSelected.toString());
        else
            filter.setCategories("");

        // Complete filter
        mMapsViewModel.setFilter(filter);
    }

    /**
     * Create chips whenever filter is updated and display them in the mChipsFilterLayout
     */
    private void initFilterChips() {
        mMapsViewModel.getFilter().observe(this, filter -> {
            mChipsFilterLayout.removeAllViews();
            filter = mMapsViewModel.getFilter().getValue();
            List<ChipView> chipList = ChipUtils.chipsFromFilter(filter);

            for (ChipView chipView : chipList) {
                mChipsFilterLayout.addView(chipView);
                ((ViewGroup.MarginLayoutParams) chipView.getLayoutParams()).setMarginEnd(20);
            }
        });
    }

    /**
     * Update filter settings whenever a filter is selected from the saved filters screen.
     */
    private void initFilterListener() {
        mMapsViewModel.getFilter().observe(this, filter -> {
            if (filter != null) {
                mEditQuery.setText(filter.getQuery());
                mSpinWhen.setSelection(mWhenList.indexOf(filter.getWhenDate()));
                mEditWhere.setText(filter.getVenueQuery());
                mTogglePrice.setValue((filter.isFree()) ? 1 : 0);
            }
        });
    }
}

package com.funcheap.funmapsf.features.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.interfaces.OnBackClickCallback;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.commons.utils.ChipUtils;
import com.funcheap.funmapsf.features.filter.SaveFilterDialogFragment;
import com.funcheap.funmapsf.features.map.MapsViewModel;
import com.vpaliy.chips_lover.ChipView;
import com.vpaliy.chips_lover.ChipsLayout;

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

public class HomeFragment extends Fragment implements OnBackClickCallback {

    private static final String[] PLACES = new String[] {
            "San Francisco", "EastBay", "NorthBay", "Peninsula", "SouthBay"
    };

    private static final String[] CATEGORIES = new String[] {  // This can be fetched from database and dynamically filled
            "Top Pick", "Annual Event", "Art & Museums", "Charity & Volunteering", "Club / DJ", "Comedy",
            "Eating & Drinking","Fairs & Festivals","Free Stuff","Fun & Games","Live Music","Movies","Shopping & Fashion"
    };

    private MapsViewModel mMapsViewModel;

    // Home Fragment
    @BindView(R.id.pager_container_home)
    public ViewPager mHomePager;
    @BindView(R.id.tabs_home)
    public TabLayout mTabLayout;
    @BindView(R.id.fab_save_filter)
    public FloatingActionButton mFabSaveFilter;

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
    public ChipsLayout mChipsCategoryLayout;
    @BindView(R.id.done)
    public Button mButtonDone;
    @BindView(R.id.search)
    public EditText mEditSearch;

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

        initHomePager();
        initBottomSheet();
        prepareWhenList();
        preparePlace();
        prepareCategories();
        prepareDoneClick();
        initChips();
        initFilterListener();
        mTogglePrice.setValue(0);

        return root;
    }

    @Override
    public boolean onBackClick() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        } else {
            return false;
        }
    }

    private void initHomePager() {
        mHomePager.setAdapter(new HomePagerAdapter(getChildFragmentManager(), getContext()));
        mTabLayout.setupWithViewPager(mHomePager);
        mFabSaveFilter.setOnClickListener(
                view -> {
                    FragmentManager fm = getChildFragmentManager();
                    SaveFilterDialogFragment saveFilter = SaveFilterDialogFragment.newInstance();
                    saveFilter.show(fm, "save_flter");
                });
    }

    private void initBottomSheet() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mFilterSheet);
        mBottomSheetBehavior.setHideable(false);

        mChipsFilterLayout.setOnClickListener( view -> {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    private void prepareWhenList(){
        List<String> temp = Arrays.asList(getResources().getStringArray(R.array.when_array));
        mWhenList = new ArrayList<>(temp);
        ArrayAdapter<String> spinAdp =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mWhenList);
        mSpinWhen.setAdapter(spinAdp);
    }

    private void preparePlace(){
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, PLACES);
        mEditWhere.setAdapter(placeAdapter);
    }

    private void prepareCategories(){
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
                    chip.setOnClickListener( chipView -> {
                        ChipView clickedChip = (ChipView) chipView;
                        mChipsCategoryLayout.removeView(clickedChip);
                        mCategoriesSelected.remove(clickedChip.getChipText());
                    });
                    mChipsCategoryLayout.addView(chip);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void prepareDoneClick(){
        mButtonDone.setOnClickListener(view -> {
            searchDBandSendEvents();
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
    }

    private void searchDBandSendEvents(){
        Filter filter = new Filter();
        filter.setQuery(mEditSearch.getText().toString());
        filter.setWhenDate((String)(mSpinWhen.getSelectedItem()));
        filter.setFree(mTogglePrice.getValue() == 1); // 1 == true == FREE, 0 == false == Any
        filter.setVenueQuery(mEditWhere.getText().toString());
        if(mCategoriesSelected.size()!=0)
            filter.setCategories(mCategoriesSelected.toString());
        else
            filter.setCategories("default");

        // Complete filter
        mMapsViewModel.setFilter(filter);
    }

    /**
     * Create chips whenever filter is updated and display them in the mChipsFilterLayout
     */
    private void initChips() {
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
                mEditSearch.setText(filter.getQuery());
                mSpinWhen.setSelection(mWhenList.indexOf(filter.getWhenDate()));
                mEditWhere.setText(filter.getVenueQuery());
                mTogglePrice.setValue( (filter.isFree()) ? 1 : 0 );
                // Edit category chips
            }
        });
    }
}

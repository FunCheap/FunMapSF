package com.funcheap.funmapsf.features.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.interfaces.OnBackClickCallback;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.features.filter.SaveFilterDialogFragment;
import com.funcheap.funmapsf.features.filter.edit.GridButtonAdapter;
import com.funcheap.funmapsf.features.map.MapsViewModel;
import com.vpaliy.chips_lover.ChipBuilder;
import com.vpaliy.chips_lover.ChipView;

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

    @BindView(R.id.pager_container_home)
    public ViewPager mHomePager;
    @BindView(R.id.tabs_home)
    public TabLayout mTabLayout;
    @BindView(R.id.fab_save_filter)
    public FloatingActionButton mFabSaveFilter;
    @BindView(R.id.filter_bottom_sheet)
    public RelativeLayout mFilterSheet;
    @BindView(R.id.filter_chips)
    public LinearLayout mChipsFilterLayout;
    @BindView(R.id.when_spin)
    public Spinner when_spin;
    @BindView(R.id.edit_where)
    public AutoCompleteTextView edit_where;
    @BindView(R.id.price_mstb)
    public MultiStateToggleButton price_mstb;
    @BindView(R.id.category_spin)
    Spinner category_spin;
    @BindView(R.id.button_list)
    public GridView grid_button_list;
    @BindView(R.id.done)
    public Button done;
    @BindView(R.id.search)
    public EditText search;

    private BottomSheetBehavior mBottomSheetBehavior;

    ArrayList<String> whenList;
    ArrayList<String> categoryList;
    ArrayAdapter<String> categoryAdp;
    ArrayList<String> categoriesSelected;
    GridButtonAdapter gridButtonAdp;

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
        price_mstb.setValue(0);

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

        applyChips();
    }

    private void prepareWhenList(){
        whenList = new ArrayList<>();
        whenList.add("Today");
        whenList.add("Tomorrow");
        whenList.add("This Week");
        whenList.add("This Weekend");
        whenList.add("Next Week");
        whenList.add("This Month");
        whenList.add("Next Month");
        ArrayAdapter<String> spinAdp =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, whenList);
        when_spin.setAdapter(spinAdp);
    }

    private void preparePlace(){
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, PLACES);
        edit_where.setAdapter(placeAdapter);
    }

    private void prepareCategories(){
        List<String> temp = Arrays.asList(CATEGORIES);
        categoryList = new ArrayList<>();
        categoryList.addAll(temp);
        categoryAdp = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, categoryList);
        category_spin.setAdapter(categoryAdp);
        categoriesSelected = new ArrayList<>();
        gridButtonAdp = new GridButtonAdapter(getContext(), categoriesSelected);
        grid_button_list.setAdapter(gridButtonAdp);

        category_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoriesSelected.add(categoriesSelected.size(),categoryList.get(i));
                categoryList.remove(i);
                categoryAdp.notifyDataSetChanged();
                gridButtonAdp.notifyDataSetChanged();
                grid_button_list.setOnItemClickListener((adapterView1, view1, i1, l1) -> {
                    categoryList.add(categoryList.size(),categoriesSelected.get(i1));
                    categoryAdp.notifyDataSetChanged();
                    categoriesSelected.remove(i1);
                    gridButtonAdp.notifyDataSetChanged();

                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void prepareDoneClick(){
        done.setOnClickListener(view -> {
            searchDBandSendEvents();
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
    }

    private void searchDBandSendEvents(){

        applyChips();

        Filter filter = new Filter();
        filter.setQuery(search.getText().toString());
        filter.setWhenDate((String)(when_spin.getSelectedItem()));
        filter.setFree(price_mstb.getValue() == 1); // 1 == true == FREE, 0 == false == Any
        filter.setVenueQuery(edit_where.getText().toString());
        filter.setCategories(categoriesSelected.toString());

        // Complete filter
        mMapsViewModel.setFilter(filter);

        //Todo: search the db with all the chosen parameters
    }

    /**
     * Create chips for each filter and display them in the mChipsFilterLayout
     *
     * TODO Break this out into a reusable component for lists
     */
    private void applyChips() {
        mChipsFilterLayout.removeAllViews();

        for (int i = 0; i < 5; i++) {
            ChipBuilder cb = ChipBuilder.create(getContext());
            cb.setText("TestChip " + i);

            ChipView chipView = cb.build();
            chipView.setClickable(false);
            chipView.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_text_inverse));
            chipView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));

            mChipsFilterLayout.addView(chipView);

            ((ViewGroup.MarginLayoutParams) chipView.getLayoutParams()).setMarginEnd(20);
        }
    }
}

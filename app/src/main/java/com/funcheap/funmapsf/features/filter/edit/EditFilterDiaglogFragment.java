package com.funcheap.funmapsf.features.filter.edit;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.features.map.MapsViewModel;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/11/2017.
 *
 * Fragment to expose filter settings. This can be a separate fragment or a
 * {@link android.support.design.widget.BottomSheetDialogFragment}
 */

public class EditFilterDiaglogFragment extends BottomSheetDialogFragment {
    private static final String TAG = "EditFilterDiaglogFragment";
    private static final String[] PLACES = new String[] {
            "San Francisco", "EastBay", "NorthBay", "Peninsula", "SouthBay"
    };

    private static final String[] CATEGORIES = new String[] {  // This can be fetched from database and dynamically filled
            "Top Pick", "Annual Event", "Art & Museums", "Charity & Volunteering", "Club / DJ", "Comedy",
            "Eating & Drinking","Fairs & Festivals","Free Stuff","Fun & Games","Live Music","Movies","Shopping & Fashion"
    };
	public interface FilterSavedListener{
        public void onFilterSaved(Filter filter);
        //for handling back key pressed
        public void setSelectedFragment(EditFilterDiaglogFragment editFilterFragment);
    }

    private MapsViewModel mMapsViewModel;

    @BindView(R.id.when_spin)
    Spinner when_spin;
    @BindView(R.id.edit_where)
    AutoCompleteTextView edit_where;
    @BindView(R.id.price_mstb)
    MultiStateToggleButton price_mstb;
    @BindView(R.id.category_spin) Spinner category_spin;
    @BindView(R.id.category_chips) GridView grid_button_list;
    @BindView(R.id.fab_save) Button done;
    @BindView(R.id.query) EditText search;
    ArrayList<String> whenList;
    Context mCtx;

    ArrayList<String> categoryList;
    ArrayAdapter<String> categoryAdp;
    ArrayList<String> categoriesSelected;
    GridButtonAdapter gridButtonAdp;

    public static EditFilterDiaglogFragment newInstance() {
        Bundle args = new Bundle();
        // Set any input args here
        EditFilterDiaglogFragment fragment = new EditFilterDiaglogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_edit_filter, null);
        dialog.setContentView(contentView);

        ButterKnife.bind(this, contentView);

        mMapsViewModel = ViewModelProviders.of(getActivity()).get(MapsViewModel.class);

        prepareSelectedFragment();
        prepareWhenList();
        preparePlace();
        prepareCategories();
        prepareDoneClick();
        price_mstb.setValue(0);
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
        ArrayAdapter<String> spinAdp = new ArrayAdapter<String>(mCtx,android.R.layout.simple_spinner_dropdown_item,whenList);
        when_spin.setAdapter(spinAdp);
    }

    private void preparePlace(){
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(mCtx,
                android.R.layout.simple_dropdown_item_1line, PLACES);
        edit_where.setAdapter(placeAdapter);
    }

    private void prepareCategories(){
        List<String> temp = Arrays.asList(CATEGORIES);
        categoryList = new ArrayList<>();
        categoryList.addAll(temp);
        categoryAdp = new ArrayAdapter<String>(mCtx,
                android.R.layout.simple_dropdown_item_1line, categoryList);
        category_spin.setAdapter(categoryAdp);
        categoriesSelected = new ArrayList<>();
        gridButtonAdp = new GridButtonAdapter(mCtx, categoriesSelected);
        grid_button_list.setAdapter(gridButtonAdp);

        category_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoriesSelected.add(categoriesSelected.size(),categoryList.get(i));
                categoryList.remove(i);
                categoryAdp.notifyDataSetChanged();
                gridButtonAdp.notifyDataSetChanged();
                grid_button_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        categoryList.add(categoryList.size(),categoriesSelected.get(i));
                        categoryAdp.notifyDataSetChanged();
                        categoriesSelected.remove(i);
                        gridButtonAdp.notifyDataSetChanged();

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void prepareDoneClick(){
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDBandSendEvents();
                getActivity().getSupportFragmentManager().popBackStack();
                
            }
        });
    }


    private void searchDBandSendEvents(){

        FilterSavedListener listener = (FilterSavedListener) getActivity();

        Filter filter = new Filter();
        filter.setQuery(search.getText().toString());
        filter.setWhenDate((String)(when_spin.getSelectedItem()));
        filter.setFree(price_mstb.getValue() == 1); // 1 == true == FREE, 0 == false == Any
        filter.setVenueQuery(edit_where.getText().toString());
        filter.setCategories(categoriesSelected.toString());

        // Complete filter
        mMapsViewModel.setFilter(filter);

        //Todo: mEditQuery the db with all the chosen parameters
    }

    private void prepareSelectedFragment(){
        FilterSavedListener listener = (FilterSavedListener) getActivity();
        listener.setSelectedFragment(this);
    }

}

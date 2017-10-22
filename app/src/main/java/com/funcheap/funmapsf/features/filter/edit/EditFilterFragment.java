package com.funcheap.funmapsf.features.filter.edit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.funcheap.funmapsf.R;

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

public class EditFilterFragment extends Fragment {
    private static final String TAG = "EditFilterFragment";
    private static final String[] PLACES = new String[] {
            "San Francisco", "EastBay", "NorthBay", "Peninsula", "SouthBay"
    };

    private static final String[] CATEGORIES = new String[] {  // This can be fetched from database and dynamically filled
            "Top Pick", "Annual Event", "Art & Museums", "Charity & Volunteering", "Club / DJ", "Comedy",
            "Eating & Drinking","Fairs & Festivals","Free Stuff","Fun & Games","Live Music","Movies","Shopping & Fashion"
    };
	public interface FilterSavedListener{
        public void onFilterSaved();
        //for handling back key pressed
        public void setSelectedFragment(EditFilterFragment editFilterFragment);
    }

    @BindView(R.id.when_spin)
    Spinner when_spin;
    @BindView(R.id.edit_where)
    AutoCompleteTextView edit_where;
    @BindView(R.id.price_mstb)
    MultiStateToggleButton price_mstb;
    @BindView(R.id.category_spin) Spinner category_spin;
    @BindView(R.id.button_list) GridView grid_button_list;
    @BindView(R.id.done)
    Button done;
    ArrayList<String> whenList;
    Context mCtx;

    public static EditFilterFragment newInstance() {
        Bundle args = new Bundle();
        // Set any input args here
        EditFilterFragment fragment = new EditFilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_filter, container, false);
        ButterKnife.bind(this, view);
        prepareSelectedFragment();
        prepareWhenList();
        preparePlace();
        prepareCategories();
        prepareDoneClick();
        price_mstb.setValue(0);
        return view;
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
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.addAll(temp);
        ArrayAdapter<String> categoryAdp = new ArrayAdapter<String>(mCtx,
                android.R.layout.simple_dropdown_item_1line, categoryList);
        category_spin.setAdapter(categoryAdp);
        ArrayList<String> categoriesSelected = new ArrayList<>();
        GridButtonAdapter gridButtonAdp = new GridButtonAdapter(mCtx, categoriesSelected);
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
        listener.onFilterSaved();
        //Todo: search the db with all the chosen parameters
    }

    private void prepareSelectedFragment(){
        FilterSavedListener listener = (FilterSavedListener) getActivity();
        listener.setSelectedFragment(this);
    }

}

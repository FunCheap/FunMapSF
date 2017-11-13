package com.funcheap.funmapsf.features.filter.list;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.features.filter.list.adapters.FilterAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/11/2017.
 *
 * This fragment displays a list of user saved filters. It should allow users to
 * open, edit, and delete filters.
 *
 * Clicking filters should apply the filters to the current
 * {@link com.funcheap.funmapsf.features.map.MapFragment}
 */

public class ListFiltersActivity extends FragmentActivity {

    public static final String EXTRA_FILTER_RESULT = "filter_result";

    private ListFilterViewModel mListFilterViewModel;

    @BindView(R.id.recycler_filter_list)
    public RecyclerView mFilterRecycler;

    // Our incoming intent
    Intent mIntent;
    int mRequestCode;

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mRequestCode = requestCode;
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_filters_list);
        ButterKnife.bind(this);

        mListFilterViewModel = ViewModelProviders.of(this).get(ListFilterViewModel.class);

        mIntent = this.getIntent();

        initRecyclerView();
        initSelectListener();
    }

    private void initSelectListener() {


    }

    /**
     * Sets the activity result true with the specified filter.
     * @param filter the filter selected by the user
     */
    public void setFilter(Filter filter) {
        mIntent.putExtra(EXTRA_FILTER_RESULT, filter);
        setResult(Activity.RESULT_OK, mIntent);
        finish();
    }

    private void initRecyclerView() {
        mFilterRecycler.setLayoutManager(new LinearLayoutManager(this));
        mFilterRecycler.setAdapter(new FilterAdapter(this, new ArrayList<>()));
        mListFilterViewModel.getSavedFilters().observe(this, filters -> {
            // Assign filters to adapter
            ((FilterAdapter) mFilterRecycler.getAdapter()).replaceEvents(filters);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }
}

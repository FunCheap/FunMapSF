package com.funcheap.funmapsf.features.filter.list;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.funcheap.funmapsf.R;

import butterknife.BindView;

/**
 * Created by Jayson on 10/11/2017.
 *
 * This activity displays a list of user saved filters. It should allow users to
 * open, edit, and delete filters.
 *
 * Clicking filters should apply the filters to the current
 * {@link com.funcheap.funmapsf.features.map.MapFragment}
 */

public class ListFiltersActivity extends AppCompatActivity {

    private ListFilterViewModel mListFilterViewModel;

    @BindView(R.id.recycler_filter_list)
    public RecyclerView mFilterRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters_list);

        mListFilterViewModel = ViewModelProviders.of(this).get(ListFilterViewModel.class);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mFilterRecycler.setLayoutManager(new LinearLayoutManager(this));

        mListFilterViewModel.getSavedFilters().observe(this, filters -> {
            // Assign filters to adapter
        });
    }
}

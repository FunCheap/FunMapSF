package com.funcheap.funmapsf.features.filter.list;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcheap.funmapsf.R;
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

public class ListFiltersFragment extends Fragment {

    private ListFilterViewModel mListFilterViewModel;

    @BindView(R.id.recycler_filter_list)
    public RecyclerView mFilterRecycler;

    public static ListFiltersFragment newInstance() {
        Bundle args = new Bundle();
        // Set any input args here
        ListFiltersFragment fragment = new ListFiltersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_filters_list, container, false);
        ButterKnife.bind(this, view);
        mListFilterViewModel = ViewModelProviders.of(getActivity()).get(ListFilterViewModel.class);
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        mFilterRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mFilterRecycler.setAdapter(new FilterAdapter(getActivity(), new ArrayList<>()));
        mListFilterViewModel.getSavedFilters().observe(this, filters -> {
            // Assign filters to adapter
            ((FilterAdapter) mFilterRecycler.getAdapter()).replaceEvents(filters);
        });
    }
}

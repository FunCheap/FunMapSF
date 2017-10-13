package com.funcheap.funmapsf.features.list.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcheap.funmapsf.features.list.ListBaseFragment;
import com.funcheap.funmapsf.features.map.MapsViewModel;

/**
 * Created by Jayson on 10/11/2017.
 *
 * Fragment for displaying a list of all events on the home screen. It should
 * display a list of events that correspond to the currently visible events in the Home/MapFragment
 */

public class ListHomeFragment extends ListBaseFragment {

    MapsViewModel mMapsViewModel;

    public static ListHomeFragment newInstance(){
        Bundle args = new Bundle();
        // Set any input args here
        ListHomeFragment fragment = new ListHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Get a reference to our ViewModel
        mMapsViewModel = ViewModelProviders.of(getActivity()).get(MapsViewModel.class);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void bindData() {
        // Bind data from ListBookmarksViewModel to our adapter via the onBindData method
        mMapsViewModel.getEventsData().observe(this, this::onBindData);
    }
}

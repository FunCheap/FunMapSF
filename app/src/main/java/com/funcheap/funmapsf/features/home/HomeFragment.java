package com.funcheap.funmapsf.features.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.funcheap.funmapsf.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/12/2017.
 *
 * This fragment holds the view pager which switches between the Map and List fragments as
 * well as the filter settings and SaveFiler FAB
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.pager_container_home)
    public ViewPager mHomePager;
    @BindView(R.id.tabs_home)
    public TabLayout mTabLayout;
    @BindView(R.id.fab_save_filter)
    public FloatingActionButton mFabSaveFilter;

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

        mHomePager.setAdapter(new HomePagerAdapter(getChildFragmentManager(), getContext()));
        mTabLayout.setupWithViewPager(mHomePager);
        mFabSaveFilter.setOnClickListener(
                view -> Toast.makeText(getContext(), "Save filter!", Toast.LENGTH_LONG).show());

        return root;
    }
}

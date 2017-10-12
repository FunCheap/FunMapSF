package com.funcheap.funmapsf.features.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.funcheap.funmapsf.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    
    @BindView(R.id.home_pager_container)
    ViewPager mHomePager;
    @BindView(R.id.home_tabs)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        mHomePager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(), this));
        mTabLayout.setupWithViewPager(mHomePager);

    }
}

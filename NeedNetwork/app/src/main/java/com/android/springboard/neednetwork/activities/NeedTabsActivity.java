package com.android.springboard.neednetwork.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.fragments.CurrentNeedsListFragment;
import com.android.springboard.neednetwork.fragments.MyNeedsListFragment;
import com.android.springboard.neednetwork.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import static com.android.springboard.neednetwork.constants.Constants.TAB_CURRENT_NEEDS;
import static com.android.springboard.neednetwork.constants.Constants.TAB_MY_NEEDS;

public class NeedTabsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mAddNewNeedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_tabs);

        initViews();
    }

    private void initViews() {
        mAddNewNeedButton = (FloatingActionButton) findViewById(R.id.add_need_btn);
        mAddNewNeedButton.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MyNeedsListFragment(), TAB_MY_NEEDS);
        adapter.addFragment(new CurrentNeedsListFragment(), TAB_CURRENT_NEEDS);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabTitle = tab.getText().toString();

        if(tabTitle.equals(TAB_MY_NEEDS)) {
            mAddNewNeedButton.setVisibility(View.VISIBLE);
        } else {
            mAddNewNeedButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.add_need_btn) {
            ActivityUtil.startActivity(this, NeedActivity.class);
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

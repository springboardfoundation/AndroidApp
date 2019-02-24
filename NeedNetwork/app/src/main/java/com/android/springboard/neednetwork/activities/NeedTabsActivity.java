package com.android.springboard.neednetwork.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.springboard.neednetwork.R;
import com.android.springboard.neednetwork.constants.ActivityConstants;
import com.android.springboard.neednetwork.constants.Constants;
import com.android.springboard.neednetwork.databinding.ActivityNeedTabsBinding;
import com.android.springboard.neednetwork.fragments.CurrentNeedsListFragment;
import com.android.springboard.neednetwork.fragments.MyNeedsListFragment;
import com.android.springboard.neednetwork.listeners.OnActivityInteractionListener;
import com.android.springboard.neednetwork.managers.NeedManager;
import com.android.springboard.neednetwork.managers.UserManager;
import com.android.springboard.neednetwork.models.Need;
import com.android.springboard.neednetwork.models.User;
import com.android.springboard.neednetwork.utils.ActivityUtil;
import com.android.springboard.neednetwork.utils.SharedPrefsUtils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.springboard.neednetwork.constants.ActivityConstants.INTENT_EXTRA_NEED_TYPE;
import static com.android.springboard.neednetwork.constants.Constants.HEADER_AUTHORIZATION;
import static com.android.springboard.neednetwork.constants.Constants.TAB_CURRENT_NEEDS;
import static com.android.springboard.neednetwork.constants.Constants.TAB_MY_NEEDS;
import static com.android.springboard.neednetwork.constants.NeedConstants.FINANCIAL_NEED;
import static com.android.springboard.neednetwork.constants.NeedConstants.NON_FINANCIAL_NEED;

public class NeedTabsActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, OnActivityInteractionListener {


    private String[] mTitles;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private UserManager mUserManager;
    private NeedManager mNeedManager;
    private MyNeedsListFragment mMyNeedsListFragment;
    private CurrentNeedsListFragment mCurrentNeedsListFragment;
    private ActivityNeedTabsBinding mBinding;
    private boolean mIsNeedOptionShown;
    private boolean mShouldAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_need_tabs);

        mUserManager = new UserManager(this);
        mNeedManager = new NeedManager(this);
        initViews();
    }

    @Override
    protected void
    onStart() {
        super.onStart();

        mMyNeedsListFragment.addNeed();
    }

    private void initViews() {
        mBinding.addNeedBtn.setOnClickListener(this);
        mBinding.financialNeed.setOnClickListener(this);
        mBinding.nonFinancialNeed.setOnClickListener(this);

        setSupportActionBar(mBinding.toolbar);

        setupViewPager(mBinding.viewpager);

        mBinding.tabs.addOnTabSelectedListener(this);
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);

        mTitles = getResources().getStringArray(R.array.left_menu_titles);

        // Set the adapter for the list view
        mBinding.leftDrawer.setAdapter(new ArrayAdapter<String>(this,

                R.layout.drawer_list_item, mTitles));
        // Set the list's click listener
        mBinding.leftDrawer.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, mBinding.toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        // Set the drawer toggle as the DrawerListener
        mBinding.drawerLayout.addDrawerListener(mDrawerToggle);
        animateAddNeedsButton(true);
    }

    private void loadNeeds(String mobileNumber) {
        User user = new User();
        user.setMobileNumber(mobileNumber);
        Log.i("shoeb", "test before making rest call" );
        showProgressDialog(getString(R.string.please_wait));
        mUserManager.login(user, mLoginResponseListener, mLoginErrorListener);
    }

    private Response.Listener mLoginResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            dismissProgressDialog();
            Log.i("shoeb", "" + response);
            try {
                JSONObject headers = response.getJSONObject(Constants.RESPONSE_HEADERS);
                String token = headers.getString(HEADER_AUTHORIZATION);
                SharedPrefsUtils.setStringPreference(NeedTabsActivity.this, ActivityConstants.PREF_TOKEN, Base64.encodeToString(token.getBytes(),
                        Base64.DEFAULT));
                JSONArray responseData = response.getJSONArray(Constants.RESPONSE_DATA);
                handleResponseData(responseData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void handleResponseData(JSONArray responseData) {
        Gson gson = new Gson();
        TypeToken<List<Need>> token = new TypeToken<List<Need>>() {};
        List<Need> needList = gson.fromJson(responseData.toString(), token.getType());
        Log.i("shoeb", responseData.toString());
        mMyNeedsListFragment.loadAdapter(needList);
    }

    private Response.ErrorListener mLoginErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("shoeb", "" + error);
            dismissProgressDialog();
            Toast.makeText(NeedTabsActivity.this, R.string.text_network_error, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
       /* boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);*/
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRecyclerViewScroll(int dx, int dy) {
        if (dy > 0 && mBinding.addNeedBtn.getVisibility() == View.VISIBLE) {
            mBinding.addNeedBtn.hide();
        } else if (dy < 0 && mBinding.addNeedBtn.getVisibility() != View.VISIBLE) {
            mBinding.addNeedBtn.show();
        }
    }

/*    @Override
    public void onListFragmentInteraction(Need item) {
        Intent intent = new Intent();
        intent.setClass(this, NeedActivity.class);
        intent.putExtra(ActivityConstants.INTENT_EXTRA_NEED, item);
        startActivity(intent);
    }*/

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
/*        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();*/

        // Highlight the selected item, update the title, and close the drawer
        mBinding.leftDrawer.setItemChecked(position, true);
        setTitle(mTitles[position]);
        mBinding.drawerLayout.closeDrawer(mBinding.leftDrawer);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mMyNeedsListFragment = new MyNeedsListFragment();
        mCurrentNeedsListFragment = new CurrentNeedsListFragment();
        adapter.addFragment(mMyNeedsListFragment, TAB_MY_NEEDS);
        adapter.addFragment(mCurrentNeedsListFragment, TAB_CURRENT_NEEDS);
        viewPager.setAdapter(adapter);
    }

    private void RequestOthersNeeds() {
        if (!mCurrentNeedsListFragment.isAdapterInitialized()) {
            showProgressDialog(getString(R.string.please_wait));
            mNeedManager.getCurrentNeeds(mOthersNeedsResponseListener, mOthersNeedsErrorListener);
        }
    }

    private Response.Listener mOthersNeedsResponseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i("shoeb", "" + response);
            dismissProgressDialog();
            handleOthersNeedsResponse(response);

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleOthersNeedsResponse(String responseData) {
        Gson gson = new Gson();
        TypeToken<List<Need>> token = new TypeToken<List<Need>>() {};
        List<Need> needList = gson.fromJson(responseData, token.getType());
        Log.i("shoeb", responseData);
        mCurrentNeedsListFragment.loadAdapter(needList);
    }

    private Response.ErrorListener mOthersNeedsErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("shoeb", "" + error);
            dismissProgressDialog();
            Toast.makeText(NeedTabsActivity.this, R.string.text_network_error, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabTitle = tab.getText().toString();

        if(tabTitle.equals(TAB_MY_NEEDS)) {
            mBinding.addNeedBtn.show();
            animateAddNeedsButton(true);
            String mobileNumber = SharedPrefsUtils.getStringPreference(this, ActivityConstants.PREF_MOBILE_NUMBER);
            loadNeeds(mobileNumber);
        } else {
            mBinding.addNeedBtn.setVisibility(View.GONE);
            animateAddNeedsButton(false);
            hideNeedOptions();
            RequestOthersNeeds();
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
        switch (v.getId()) {

            case  R.id.add_need_btn:
                if (mIsNeedOptionShown) {
                    hideNeedOptions();
                    animateAddNeedsButton(true);
                } else {
                    showNeedOptions();
                    animateAddNeedsButton(false);
                }
                break;
            case R.id.financial_need:
                addNeed(FINANCIAL_NEED);
                break;
            case R.id.non_financial_need:
                addNeed(NON_FINANCIAL_NEED);
                break;
        }
    }

    private void animateAddNeedsButton(boolean animate) {

        mShouldAnimate = animate;

        if (!mShouldAnimate) {
            return;
        }

        mBinding.addNeedBtn.setScaleX(1f);
        mBinding.addNeedBtn.setScaleY(1f);
        mBinding.addAnimator.setScaleX(0.5f);
        mBinding.addAnimator.setScaleY(0.5f);
        mBinding.addAnimator.setAlpha(1f);
        mBinding.addAnimator.animate().alpha(0).scaleX(1.7f).scaleY(1.7f).setDuration(1000);
        mBinding.addNeedBtn.animate().scaleX(1.2f).scaleY(1.2f).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                mBinding.addNeedBtn.animate().scaleX(1f).scaleY(1f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        animateAddNeedsButton(mShouldAnimate);
                    }
                });
            }
        });

    }

    private void showNeedOptions() {
        animateAddNeedsButton(false);
        mIsNeedOptionShown = true;
        mBinding.financialNeed.setVisibility(View.VISIBLE);
        mBinding.nonFinancialNeed.setVisibility(View.VISIBLE);
    }

    private void hideNeedOptions() {
        mIsNeedOptionShown = false;
        mBinding.nonFinancialNeed.setVisibility(View.GONE);
        mBinding.financialNeed.setVisibility(View.GONE);
    }

    private void addNeed(int needType) {
        ActivityUtil.startActivity(this, NeedActivity.class, INTENT_EXTRA_NEED_TYPE, needType);
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

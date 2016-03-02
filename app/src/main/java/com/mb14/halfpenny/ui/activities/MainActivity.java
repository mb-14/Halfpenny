package com.mb14.halfpenny.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.mb14.halfpenny.R;
import com.mb14.halfpenny.api.jsonblob.Transaction;
import com.mb14.halfpenny.ui.fragments.TransactionListFragment;

public class MainActivity extends AppCompatActivity {
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new SlidingPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }



}
class SlidingPagerAdapter extends FragmentPagerAdapter {
    String[] titles = new String[]{"ALL","TAXI","RECHARGE"};
    public SlidingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return TransactionListFragment.newInstance(Transaction.CATEGORY_ALL);
            case 1:
                return TransactionListFragment.newInstance(Transaction.CATEGORY_TAXI);
            case 2:
                return TransactionListFragment.newInstance(Transaction.CATEGORY_RECHARGE);

        }
        return TransactionListFragment.newInstance(Transaction.CATEGORY_ALL);
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

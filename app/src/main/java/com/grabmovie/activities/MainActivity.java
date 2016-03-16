package com.grabmovie.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.grabmovie.R;
import com.grabmovie.fragments.AllMoviesFragment;
import com.grabmovie.fragments.FavouriteMoviesFragment;

/**
 * Main screen showing all and favourite movies
 *
 */
public class MainActivity extends FragmentActivity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private View mNavigationIndicator;
    private View mTabAll, mTabFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // navigation tab
        mNavigationIndicator = findViewById(R.id.navigation_indicator);
        mTabAll = findViewById(R.id.tab_all);
        mTabFav = findViewById(R.id.tab_favourite);

        // Instantiate a ViewPager and a PagerAdapter.
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mNavigationIndicator.setTranslationX(
                        (position + positionOffset) * mNavigationIndicator.getLayoutParams().width);
            }

            @Override
            public void onPageSelected(int position) {
                boolean isDefault = (position == 0);
                mTabAll.setSelected(isDefault);
                mTabFav.setSelected(!isDefault);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * A simple pager adapter for sliding between all and favourite list
     */
    private class SlidePagerAdapter extends FragmentPagerAdapter {

        static final int NUM_PAGES = 2;

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new FavouriteMoviesFragment();
                default:
                    return new AllMoviesFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

package com.geniteam.lifetabs.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.dao.DBAdapter;
import com.geniteam.lifetabs.fragments.HomeFragment;
import com.geniteam.lifetabs.fragments.LifeTabCustomFragment;
import com.geniteam.lifetabs.fragments.PlaceHolderFragment2;
import com.geniteam.lifetabs.fragments.SettingsFragment;
import com.geniteam.lifetabs.fragments.TutorialFragment;
import com.instabug.library.IBGInvocationEvent;
import com.instabug.library.Instabug;

;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public LifeTabCustomFragment currFragment = null;
    public int mLastPostitionOfScreen;
    private String currentFragmentName="";
    FragmentManager fragmentManager ;
    public static boolean isShowTutorials=false;
    public DrawerLayout mDrawerManager;
    public boolean isShowSplash = false;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public  Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        LifeTabCustomFragment fg = HomeFragment.newInstance(fragmentManager);
        currFragment = fg;
        tx.replace(R.id.container,fg);
        tx.commit();

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        mDrawerManager = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),(FrameLayout)findViewById(R.id.container));

        try {
            DBAdapter.getDBAdapterInstance(this).createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //*************** instabug integration

        /*new Instabug.Builder(getApplication(), "b60e6271de8d0429486285641a75f30f")
                .setInvocationEvent(IBGInvocationEvent.IBGInvocationEventShake)
                .build();*/
        new Instabug.Builder(getApplication(), "7e4fa9be24ca35eb8705500ed79c8921")
                .setInvocationEvent(IBGInvocationEvent.IBGInvocationEventShake)
                .build();

        //***************


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        if (position == 0) {
           // do nothing
        } else if (position == 1) {

            LifeTabCustomFragment homeFragment = HomeFragment.newInstance(fragmentManager);
            currFragment = homeFragment;
            mLastPostitionOfScreen = position;
            currentFragmentName = com.geniteam.lifetabs.constants.LifeTabsConstents.HOME;
            fragmentManager.beginTransaction()
                    .replace(R.id.container,homeFragment)
                    .commit();
        }
        else if (position == 2) {
            LifeTabCustomFragment tutorialFragment = TutorialFragment.newInstance(fragmentManager);
            currFragment = tutorialFragment;
            mLastPostitionOfScreen = position;
            currentFragmentName = com.geniteam.lifetabs.constants.LifeTabsConstents.TUTORIAL;
            fragmentManager.beginTransaction()
                    .replace(R.id.container,tutorialFragment)
                    .commit();
        }
        else if (position == 3) {

            LifeTabCustomFragment placeHolder = PlaceHolderFragment2.newInstance("Help");
            currFragment  = placeHolder;
            mLastPostitionOfScreen = position;
            fragmentManager.beginTransaction()
                    .replace(R.id.container,placeHolder)
                    .commit();
        }
        else if (position == 4) {
            LifeTabCustomFragment placeHolder = PlaceHolderFragment2.newInstance("Privacy Policy");
            currFragment  = placeHolder;
            mLastPostitionOfScreen = position;
            fragmentManager.beginTransaction()
                    .replace(R.id.container,placeHolder)
                    .commit();
        }
        else if (position == 5) {
            LifeTabCustomFragment placeHolder = PlaceHolderFragment2.newInstance("Feedback");
            currFragment  = placeHolder;
            mLastPostitionOfScreen = position;
            fragmentManager.beginTransaction()
                    .replace(R.id.container,placeHolder)
                    .commit();
        }
        else if (position == 6) {
            LifeTabCustomFragment settingsFragment =SettingsFragment.newInstance(fragmentManager);
            currFragment = settingsFragment ;
            mLastPostitionOfScreen = position;
            currentFragmentName = com.geniteam.lifetabs.constants.LifeTabsConstents.SETTINGS;
            fragmentManager.beginTransaction()
                    .replace(R.id.container,settingsFragment )
                    .commit();
        }
        else
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment.newInstance(fragmentManager))
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(currFragment == null){
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    return currFragment.onKeyDown();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        MainActivity mContext;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mContext=(MainActivity)activity;
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}

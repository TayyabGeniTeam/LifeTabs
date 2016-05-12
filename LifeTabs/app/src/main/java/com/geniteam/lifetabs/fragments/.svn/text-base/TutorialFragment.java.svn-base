package com.geniteam.lifetabs.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.activities.MainActivity;
import com.geniteam.lifetabs.constants.LifeTabsConstents;
import com.geniteam.lifetabs.managers.LifeTabPrefManager;

/**
 * Created by Tayyab on 3/18/2016.
 */
public class TutorialFragment extends LifeTabCustomFragment implements View.OnClickListener {
    MainActivity mContext;
    private View view;
    private ImageView tutorialCheckbox;
    LifeTabPrefManager mPrefManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;

    }

    public TutorialFragment() {
        // Required empty public constructor
    }

    public static LifeTabCustomFragment newInstance(FragmentManager fragmentManager) {
        LifeTabCustomFragment tutorialFragment = (LifeTabCustomFragment) fragmentManager.findFragmentByTag(LifeTabsConstents.TUTORIAL);
        if(tutorialFragment == null){
            tutorialFragment = new TutorialFragment();
        }
        return tutorialFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefManager = new LifeTabPrefManager(mContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.tutorials, container, false);
        view.findViewById(R.id.tutorial_cont_button).setVisibility(View.GONE);
        view.findViewById(R.id.checkbox_show).setOnClickListener(this);
        view.findViewById(R.id.checkbox_show).setVisibility(View.GONE);
        view.findViewById(R.id.textView5).setVisibility(View.GONE);
        ((TextView) mContext.toolbar.findViewById(R.id.toolbar_title)).setText("Tutorial");
        mContext.toolbar.setNavigationIcon(null);
        mContext.toolbar.findViewById(R.id.toolbar_img1).setVisibility(View.VISIBLE);
        ((ImageView) mContext.toolbar.findViewById(R.id.toolbar_img1)).setImageResource(R.drawable.home);
        mContext.toolbar.findViewById(R.id.toolbar_img1).setOnClickListener(this);
        return view;
    }

    /*
    go to home screen
     */
    public void goToHome(){
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment homeFragment= HomeFragment.newInstance(fragmentManager);
        mContext.currFragment = homeFragment;
        mContext.mLastPostitionOfScreen = LifeTabsConstents.HOME_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, homeFragment, LifeTabsConstents.HOME).commit();
    }
    @Override
    public boolean onKeyDown() {
        goToHome();
        return true;
    }

    @Override
    public void onClick(View click) {
        switch (click.getId()) {
            case R.id.toolbar_img1:
                goToHome();
                break;
        }
    }
}

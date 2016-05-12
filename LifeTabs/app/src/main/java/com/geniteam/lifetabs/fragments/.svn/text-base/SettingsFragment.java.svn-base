package com.geniteam.lifetabs.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geniteam.lifetabs.constants.LifeTabsConstents;
import com.geniteam.lifetabs.activities.MainActivity;
import com.geniteam.lifetabs.managers.LifeTabPrefManager;
import com.geniteam.lifetabs.R;

/**
 * Created by Tayyab on 3/14/2016.
 */
public class SettingsFragment extends LifeTabCustomFragment  implements View.OnClickListener{


    MainActivity mContext;
    private View view;
    private ImageView tutorialCheckbox;
    LifeTabPrefManager mPrefManager;
    ImageView mol;
    ImageView mg;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;

    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LifeTabCustomFragment newInstance(FragmentManager fragmentManager) {
        LifeTabCustomFragment settingsFragment = (LifeTabCustomFragment) fragmentManager.findFragmentByTag(LifeTabsConstents.SETTINGS);
        if(settingsFragment == null){
            settingsFragment = new SettingsFragment();
        }
        return settingsFragment;
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
        view = inflater.inflate(R.layout.settings_screen, container, false);
        setBasicContents(view);
        return view;
    }

    public void setBasicContents(View v){

        mContext.toolbar.setVisibility(View.VISIBLE);
        mContext.toolbar.setNavigationIcon(null);
        mContext.toolbar.findViewById(R.id.toolbar_img1).setVisibility(View.VISIBLE);
        mContext.toolbar.findViewById(R.id.toolbar_img2).setVisibility(View.GONE);
        mContext.toolbar.findViewById(R.id.toolbar_title).setVisibility(View.VISIBLE);
        ((TextView)mContext.toolbar.findViewById(R.id.toolbar_title)).setText("Settings");

        mContext.toolbar.findViewById(R.id.toolbar_img1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.toolbar.setNavigationIcon(R.drawable.ic_drawer);
                view.setVisibility(View.GONE);
                goBackToHome();
            }
        });

        tutorialCheckbox = (ImageView)v.findViewById(R.id.tutorial_img);

        if(mPrefManager.getSharedPreferenceBoolean(LifeTabPrefManager.SharedPreferencesNames.IS_SHOW_TUTORIALS)){
            /*tutorialCheckbox.setImageResource(R.drawable.arrow_unselected);
            tutorialCheckbox.setTag(R.drawable.arrow_unselected);*/
            tutorialCheckbox.setEnabled(false);
        }else{
            /*tutorialCheckbox.setImageResource(R.drawable.checkbox_purple);
            tutorialCheckbox.setTag(R.drawable.checkbox_purple);*/
        }

        mg = (ImageView)v.findViewById(R.id.mgunit_img);
        mol = (ImageView)v.findViewById(R.id.mol_img);
        v.findViewById(R.id.mgunit_img).setOnClickListener(this);
        mg.setTag(R.drawable.radio_button_check_circle);

        mol.setTag(R.drawable.radio_button_uncheck_circle);
        v.findViewById(R.id.mol_img).setOnClickListener(this);
    }

    public void goBackToHome(){
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment homeFragment= HomeFragment.newInstance(fragmentManager);
        mContext.currFragment = homeFragment;
        mContext.mLastPostitionOfScreen = LifeTabsConstents.HOME_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, homeFragment, LifeTabsConstents.HOME).commit();
    }

    @Override
    public boolean onKeyDown() {
        goBackToHome();
        return true;
    }

    @Override
    public void onClick(View click) {
        switch (click.getId()) {
            case R.id.tutorial_img:
                if((Integer)tutorialCheckbox.getTag() == R.drawable.checkbox_purple){
                   // tutorialCheckbox.setTag(R.drawable.arrow_unselected);
                   // mPrefManager.setSharedPreference(LifeTabPrefManager.SharedPreferencesNames.IS_SHOW_TUTORIALS,true);
                }/*else{
                    tutorialCheckbox.setTag(R.drawable.checkbox_purple);
                }*/
                break;
            case R.id.mgunit_img:
               if((Integer)mg.getTag() == R.drawable.radio_button_check_circle){
                   mg.setImageResource(R.drawable.radio_button_uncheck_circle);
                   mg.setTag(R.drawable.radio_button_uncheck_circle);

                   mol.setImageResource(R.drawable.radio_button_check_circle);
                   mol.setTag(R.drawable.radio_button_check_circle);

               }else{
                   mg.setImageResource(R.drawable.radio_button_check_circle);
                   mg.setTag(R.drawable.radio_button_check_circle);

                   mol.setImageResource(R.drawable.radio_button_uncheck_circle);
                   mol.setTag(R.drawable.radio_button_uncheck_circle);
               }
                break;
            case R.id.mol_img:
                if((Integer)mol.getTag() == R.drawable.radio_button_check_circle){
                    mol.setImageResource(R.drawable.radio_button_uncheck_circle);
                    mol.setTag(R.drawable.radio_button_uncheck_circle);

                    mg.setImageResource(R.drawable.radio_button_check_circle);
                    mg.setTag(R.drawable.radio_button_check_circle);


                }else{
                    mol.setImageResource(R.drawable.radio_button_check_circle);
                    mol.setTag(R.drawable.radio_button_check_circle);

                    mg.setImageResource(R.drawable.radio_button_uncheck_circle);
                    mg.setTag(R.drawable.radio_button_uncheck_circle);
                }
                break;
        }
    }
}

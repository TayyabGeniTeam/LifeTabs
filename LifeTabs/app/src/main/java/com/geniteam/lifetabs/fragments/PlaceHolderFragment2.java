package com.geniteam.lifetabs.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.activities.MainActivity;

public class PlaceHolderFragment2 extends LifeTabCustomFragment {
    MainActivity mContext;
    static String textToShow;
    public PlaceHolderFragment2() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PlaceHolderFragment2 newInstance(String str) {
        textToShow = str;
        PlaceHolderFragment2 fragment = new PlaceHolderFragment2();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // mContext.toolbar.findViewById(R.id.action_search).setVisibility(View.GONE);
        //mContext.toolbar.getMenu().clear(); // I believe this is what you are looking for
        // Set title
        mContext.toolbar.setTitle("");
        //Set SubTitle
        mContext.toolbar.setSubtitle("");
        mContext.toolbar.setNavigationIcon(null);

        ((TextView)mContext.toolbar.findViewById(R.id.toolbar_title)).setText(textToShow);

        mContext.toolbar.findViewById(R.id.toolbar_img1).setVisibility(View.VISIBLE);

        mContext.toolbar.findViewById(R.id.toolbar_img1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.toolbar.setNavigationIcon(R.drawable.ic_drawer);
                view.setVisibility(View.GONE);

                FragmentManager fragmentManager = mContext.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance(fragmentManager))
                        .commit();
            }
        });


      //  LifeTabUtils.sortReadingsList();

       /* mContext.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.toolbar.setNavigationContentDescription(s);
                mContext.toolbar.setTitle("clicked");
                FragmentManager fragmentManager = mContext.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance())
                        .commit();


            }
        });*/
        return inflater.inflate(R.layout.fragment_place_holder_fragment2, container, false);
    }
    @Override
    public boolean onKeyDown() {
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance(fragmentManager))
                .commit();
        return true;
    }

}

package com.geniteam.lifetabs.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.activities.MainActivity;
import com.geniteam.lifetabs.adapters.LastReadingsAdapter;
import com.geniteam.lifetabs.bo.ReadingBO;
import com.geniteam.lifetabs.constants.LifeTabsConstents;
import com.geniteam.lifetabs.dao.DBAdapter;
import com.geniteam.lifetabs.dao.ReadingDAO;
import com.geniteam.lifetabs.managers.LifeTabPrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tayyab on 3/8/2016.
 */
public class HomeFragment extends LifeTabCustomFragment implements View.OnClickListener {
    MainActivity mContext;
    private View view;
    private ListView mListView;
    private LastReadingsAdapter mAdapter;
    private RelativeLayout waitPopup;
    private LinearLayout main;
    private RelativeLayout tutorials;
    LifeTabPrefManager mPrefManager;
    private ImageView checkBoxShow;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    private TextView timerTxt;
    int timersec = 30;
    LinearLayout splashView;
    DBAdapter dbAdapter;
    TextView no_readings;
    CountDownTimer mTimer;
    RelativeLayout gotButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;
        mContext.toolbar.setNavigationIcon(R.drawable.ic_drawer);
        mPrefManager = new LifeTabPrefManager(mContext);

    }

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LifeTabCustomFragment newInstance(FragmentManager fragmentManager) {

        LifeTabCustomFragment homeFragment = (LifeTabCustomFragment) fragmentManager.findFragmentByTag(LifeTabsConstents.HOME);

        if(homeFragment == null){
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dbAdapter = DBAdapter.getDBAdapterInstance(mContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home_screen, container, false);
        if(mContext.toolbar.getVisibility()==View.GONE){
            mContext.toolbar.setVisibility(View.VISIBLE);
        }
        ((TextView) mContext.toolbar.findViewById(R.id.toolbar_title)).setText("LifeTabs");
        mContext.toolbar.findViewById(R.id.toolbar_img1).setVisibility(View.GONE);
        mContext.toolbar.findViewById(R.id.toolbar_img2).setVisibility(View.GONE);

        mContext.mDrawerManager.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setDefaultRanges();
        setBasicContents(view);
        addHeaderToListView();
        showLastReadings();

        return view;
    }

    /**
     * setting basic contents
     * @param v
     */
    public void setBasicContents(View v){
        mListView =  (ListView)       v.findViewById(R.id.last_reading_lv);
        waitPopup =  (RelativeLayout) v.findViewById(R.id.rl_wait);
        main =       (LinearLayout)   v.findViewById(R.id.ll_main_content);
        tutorials =  (RelativeLayout)   v.findViewById(R.id.ll_tutorials);
        v.findViewById(R.id.imageView1).setOnClickListener(this);
        v.findViewById(R.id.imageView2).setOnClickListener(this);
        v.findViewById(R.id.gotit_txt).setOnClickListener(this);
        checkBoxShow = (ImageView) v.findViewById(R.id.checkbox_show);
        checkBoxShow.setTag(R.drawable.checkbox_unchecked);
        v.findViewById(R.id.tutorial_cont_button).setOnClickListener(this);
        checkBoxShow.setOnClickListener(this);
        timerTxt = (TextView) v.findViewById(R.id.main_txt);
        splashView =(LinearLayout) v.findViewById(R.id.ll_splash);
        no_readings = (TextView) v.findViewById(R.id.no_readings_tv);
        gotButton = (RelativeLayout) v.findViewById(R.id.rl_btn);
        gotButton.setOnClickListener(this);

        if(mContext.isShowSplash ==  false){
            showSplash();
        }
    }

    public void showSplash(){
        splashView.setVisibility(View.VISIBLE);
        mContext.toolbar.setVisibility(View.GONE);
        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                splashView.setVisibility(View.GONE);
                mContext.toolbar.setVisibility(View.VISIBLE);
                mContext.isShowSplash=true;

                if(!mPrefManager.getSharedPreferenceBoolean(LifeTabPrefManager.SharedPreferencesNames.IS_SHOW_TUTORIALS))
                {
                    if(!mContext.isShowTutorials){
                        mContext.toolbar.setVisibility(View.GONE);
                        main.setVisibility(View.GONE);
                        tutorials.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    mContext.toolbar.setVisibility(View.VISIBLE);
                }
            }

        }.start();
    }

    public void addHeaderToListView(){
        LayoutInflater inflater = mContext.getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.lastreading_listview_header, mListView, false);
        mListView.addHeaderView(header, null, false);
    }

    /**
     * show last reading panel
     */
    public void showLastReadings() {

        List<ReadingBO> lastFiveReadings =  new ArrayList<ReadingBO>();

        List<ReadingBO> readings = ReadingDAO.getAllReadings(dbAdapter);

        if(readings!=null) {

            no_readings.setVisibility(View.GONE);

            if (readings.size() > 5) {

                for (int i = readings.size() - 5; i < readings.size(); i++) {
                    lastFiveReadings.add(readings.get(i));
                }

            } else {
                lastFiveReadings = readings;
            }
        }else{
            no_readings.setVisibility(View.VISIBLE);
        }

        if(lastFiveReadings!=null){
            mAdapter = new LastReadingsAdapter(mContext,R.layout.history_item,lastFiveReadings,mPrefManager);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    goToHistoryViewList();
                }
            });
        }


    }

    //goto camera screen
    public void goToCameraView(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FragmentManager fragmentManager = getFragmentManager();
            //LifeTabCustomFragment cameraFragment= CameraFragment.newInstance(fragmentManager);
            LifeTabCustomFragment cameraFragment= Camera2FragmentD.newInstance(fragmentManager);
            mContext.currFragment = cameraFragment;
            mContext.mLastPostitionOfScreen = LifeTabsConstents.HOME_FRAGMENT_POSITION;
        /*fragmentManager.beginTransaction().replace(R.id.container, cameraFragment, LifeTabsConstents.CAMERA).commit();*/
            fragmentManager.beginTransaction().replace(R.id.container, cameraFragment, LifeTabsConstents.CAMERA).commitAllowingStateLoss();
        }else{
            FragmentManager fragmentManager = getFragmentManager();
            //LifeTabCustomFragment cameraFragment= CameraFragment.newInstance(fragmentManager);
            LifeTabCustomFragment cameraFragment= CameraFragmentD.newInstance(fragmentManager);
            mContext.currFragment = cameraFragment;
            mContext.mLastPostitionOfScreen = LifeTabsConstents.HOME_FRAGMENT_POSITION;
        /*fragmentManager.beginTransaction().replace(R.id.container, cameraFragment, LifeTabsConstents.CAMERA).commit();*/
            fragmentManager.beginTransaction().replace(R.id.container, cameraFragment, LifeTabsConstents.CAMERA).commitAllowingStateLoss();
        }



    }
//goto history screen
    public void goToHistoryView(){
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment historyFragment= HistoryFragment.newInstance(fragmentManager);
        mContext.currFragment = historyFragment;
        mContext.mLastPostitionOfScreen = LifeTabsConstents.HOME_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, historyFragment, LifeTabsConstents.HISTORY).commit();
    }

    public void goToHistoryViewList(){
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment historyFragment= HistoryFragment.newInstance(fragmentManager);
        mContext.currFragment = historyFragment;
        mContext.mLastPostitionOfScreen = LifeTabsConstents.HISTORY_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, historyFragment, LifeTabsConstents.HISTORY).commit();
    }

    public void setDefaultRanges(){

        if(! mPrefManager.getSharedPreferenceBoolean(LifeTabPrefManager.SharedPreferencesNames.IS_DEFAULT_RANGES_SET)){
            mPrefManager.setSharedPreference(LifeTabPrefManager.SharedPreferencesNames.MIN_VALUE_SET,75);
            mPrefManager.setSharedPreference(LifeTabPrefManager.SharedPreferencesNames.MAX_VALUE_SET,180);
            mPrefManager.setSharedPreference(LifeTabPrefManager.SharedPreferencesNames.IS_DEFAULT_RANGES_SET,true);
        }
    }

    public void checkPermissionAndGOTOCamera(){
        if(checkPermissionForCamera()){
            if(checkPermissionForWriteExternalStorage()){
                try {
                    goToCameraView();
                }catch(IllegalStateException e){
                    e.printStackTrace();
                }
            }else{
                requestPermissionForExternalStorage();
            }
        }else{
            requestPermissionForCamera();
        }
    }

    private boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;

        } else {
            return false;

        }
    }

    private boolean checkPermissionForWriteExternalStorage(){
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;

        } else {
            return false;

        }
    }

    private void requestPermissionForCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.CAMERA)){

            Toast.makeText(mContext, "Camera permission is necessary for capturing images.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(mContext,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void requestPermissionForExternalStorage(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.CAMERA)){

            Toast.makeText(mContext, "Camera permission is necessary for capturing images.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(mContext,new String[]{Manifest.permission.CAMERA},STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(checkPermissionForWriteExternalStorage()){
                        goToCameraView();
                    }else{
                        requestPermissionForExternalStorage();
                    }

                } else {
                }
                break;
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToCameraView();

                } else {
                    // do nothing
                }
                break;
        }
    }


    @Override
    public void onClick(View click) {
        switch (click.getId()){
            case R.id.imageView1:
                waitPopup.setVisibility(View.VISIBLE);
                view.findViewById(R.id.imageView1).setEnabled(false);
                view.findViewById(R.id.imageView2).setEnabled(false);
                view.findViewById(R.id.last_reading_lv).setEnabled(false);

                mTimer =  new CountDownTimer(31000, 1000) {

                    public void onTick(long millisUntilFinished) {

                            String timestr = "please wait " + ((millisUntilFinished / 1000)-1) + " seconds for the";
                            SpannableStringBuilder builder = new SpannableStringBuilder();
                            StyleSpan bss = new StyleSpan(Typeface.BOLD);

                            String NOTICE = "Notice: ";
                            SpannableString redSpannable = new SpannableString(NOTICE);
                            redSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#455A64")), 0, NOTICE.length(), 0);
                            builder.append(redSpannable);
                            SpannableString whiteSpannable = new SpannableString(timestr);
                            whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#66d8ff")), 12, 22, 0);
                            builder.append(whiteSpannable);
                            builder.setSpan(bss, 0, NOTICE.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            timerTxt.setText(builder);
                    }

                    public void onFinish() {
                        if(waitPopup.getVisibility() != View.GONE){
                            waitPopup.setVisibility(View.GONE);
                            view.findViewById(R.id.imageView1).setEnabled(true);
                            view.findViewById(R.id.imageView2).setEnabled(true);
                            view.findViewById(R.id.last_reading_lv).setEnabled(true);
                            checkPermissionAndGOTOCamera();
                        }
                    }
                };
                mTimer.start();
                break;
            case R.id.imageView2:
                goToHistoryView();
                break;
            case R.id.gotit_txt:
                waitPopup.setVisibility(View.GONE);
                view.findViewById(R.id.imageView1).setEnabled(true);
                view.findViewById(R.id.imageView2).setEnabled(true);
                view.findViewById(R.id.last_reading_lv).setEnabled(true);
                checkPermissionAndGOTOCamera();
                break;
            case R.id.rl_btn:
                waitPopup.setVisibility(View.GONE);
                view.findViewById(R.id.imageView1).setEnabled(true);
                view.findViewById(R.id.imageView2).setEnabled(true);
                view.findViewById(R.id.last_reading_lv).setEnabled(true);
                checkPermissionAndGOTOCamera();
                break;
            case R.id.tutorial_cont_button:
                if((Integer)checkBoxShow.getTag() == R.drawable.checkbox_checked){
                    mPrefManager.setSharedPreference(LifeTabPrefManager.SharedPreferencesNames.IS_SHOW_TUTORIALS,true);
                }else{
                    mContext.isShowTutorials = true;
                }
                mContext.toolbar.setVisibility(View.VISIBLE);
                main.setVisibility(View.VISIBLE);
                tutorials.setVisibility(View.GONE);
                break;
            case R.id.checkbox_show:
                if((Integer)checkBoxShow.getTag() == R.drawable.checkbox_unchecked){
                    checkBoxShow.setImageResource(R.drawable.checkbox_checked);
                    checkBoxShow.setTag(R.drawable.checkbox_checked);
                }else{
                    checkBoxShow.setImageResource(R.drawable.checkbox_unchecked);
                    checkBoxShow.setTag(R.drawable.checkbox_unchecked);
                }
                    break;
            default:
                break;

        }
    }

    @Override
    public boolean onKeyDown() {
        if(mTimer!=null){
            mTimer.cancel();
            waitPopup.setVisibility(View.GONE);
            view.findViewById(R.id.imageView1).setEnabled(true);
            view.findViewById(R.id.imageView2).setEnabled(true);
            view.findViewById(R.id.last_reading_lv).setEnabled(true);
        }
        return true;
    }
}


package com.geniteam.lifetabs.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.activities.MainActivity;
import com.geniteam.lifetabs.bo.ReadingBO;
import com.geniteam.lifetabs.constants.DBConstents;
import com.geniteam.lifetabs.constants.LifeTabsConstents;
import com.geniteam.lifetabs.dao.DBAdapter;
import com.geniteam.lifetabs.dao.ReadingDAO;
import com.geniteam.lifetabs.interfaces.RangeSettingCallback;
import com.geniteam.lifetabs.managers.LifeTabPrefManager;
import com.geniteam.lifetabs.utils.LifeTabUtils;
import com.geniteam.lifetabs.utils.MyCustomView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tayyab on 3/14/2016.
 */

public class ResultFragment extends LifeTabCustomFragment implements View.OnClickListener{
    MainActivity mContext;
    private View view;
    DBAdapter mAdapter;
    String [] timeList=null;
    ReadingBO item;
    GraphView graph;
    List<com.geniteam.lifetabs.bo.CordinatesBO> mList = new ArrayList<com.geniteam.lifetabs.bo.CordinatesBO>();
    int dataList[] = new int[10];
    int dSize= 0;
    int repeatCount =0;
    int repeatValue;
    Date f1=null,f2=null,f3=null,f4=null,f5=null,f6=null,f7=null,f8=null,f9=null,f10=null;
    ImageView gearIcon;
    RelativeLayout settingsView;
    RelativeLayout infoView;
    RelativeLayout rescanView;
    LifeTabPrefManager mPrefManager;
    float minSetValue =0;
    float maxSetValue =0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;

    }

    public ResultFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LifeTabCustomFragment newInstance(FragmentManager fragmentManager) {
        LifeTabCustomFragment resultFragment = (LifeTabCustomFragment) fragmentManager.findFragmentByTag(LifeTabsConstents.RESULT);
        if(resultFragment == null){
            resultFragment = new ResultFragment();
        }
        return resultFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter     = DBAdapter.getDBAdapterInstance(mContext);
        mPrefManager = new LifeTabPrefManager(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.results_screen, container, false);
        settingsView = (RelativeLayout) view.findViewById(R.id.setting_layout);
        setBasicContents(view);
        return view;
    }

    /**
     * setting basic contents
     * @param v
     */

    public void setBasicContents(View v){
        mContext.toolbar.setVisibility(View.VISIBLE);
        mContext.toolbar.setNavigationIcon(null);
        ((TextView) mContext.toolbar.findViewById(R.id.toolbar_title)).setText("Results");
        mContext.toolbar.findViewById(R.id.toolbar_img1).setVisibility(View.VISIBLE);
        mContext.toolbar.findViewById(R.id.toolbar_img2).setVisibility(View.VISIBLE);
        ((ImageView)mContext.toolbar.findViewById(R.id.toolbar_img2)).setImageResource(R.drawable.scan_icon);

        mContext.toolbar.findViewById(R.id.toolbar_img1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.toolbar.setNavigationIcon(R.drawable.ic_drawer);
                view.setVisibility(View.GONE);
                goBackToHome();
            }
        });
        mContext.toolbar.findViewById(R.id.toolbar_img2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                goToCamera();
            }
        });
        gearIcon = (ImageView) v.findViewById(R.id.imageView5);
        gearIcon.setOnClickListener(this);
        v.findViewById(R.id.rl_activity_history).setOnClickListener(this);

        infoView = (RelativeLayout) v.findViewById(R.id.info_layout);
        v.findViewById(R.id.info_gotit_txt).setOnClickListener(this);
        v.findViewById(R.id.imageView1).setOnClickListener(this);
        rescanView = (RelativeLayout)v.findViewById(R.id.rescan_layout);
        v.findViewById(R.id.scan_again).setOnClickListener(this);


        Bundle b = getArguments();
        item =(ReadingBO) b.getSerializable("sugar_value");

       if(Integer.parseInt(item.getValue()) == 0){
           rescanView.setVisibility(View.VISIBLE);
       }
        else {
           try {
               if (mContext.mLastPostitionOfScreen == LifeTabsConstents.CAMERA_FRAGMENT_POSITION) {
                   ReadingDAO.saveReadingItem(mAdapter, item);
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

        if(Integer.parseInt(item.getValue()) != 0) {

            ((TextView) v.findViewById(R.id.textView6)).setText(item.getValue() + "");
            ((TextView) v.findViewById(R.id.textView1)).setText(item.getDateString() + "");
            ((TextView) v.findViewById(R.id.textView2)).setText(item.getTimeString() + "");

            if (Integer.parseInt(item.getValue()) >= 75 && Integer.parseInt(item.getValue()) <= 180) {
                v.findViewById(R.id.LinearLayout1).setBackgroundResource(R.drawable.normal_result_banner);
                ((TextView) v.findViewById(R.id.textView4)).setTextColor(Color.parseColor("#01a2a6"));

                ((TextView) v.findViewById(R.id.textView7)).setText("Lorem ipsum dolor sit amet consectetur.");
                ((TextView) v.findViewById(R.id.info_txt)).setText("Lorem ipsum dolor sit amet consectetur.");
            } else if (Integer.parseInt(item.getValue()) > 180) {
                v.findViewById(R.id.LinearLayout1).setBackgroundResource(R.drawable.high_result_banner);
                ((TextView) v.findViewById(R.id.textView5)).setTextColor(Color.parseColor("#b40111"));

                ((TextView) v.findViewById(R.id.textView7)).setText("You should exercise");
                ((TextView) v.findViewById(R.id.info_txt)).setText("You should exercise");
            } else if (Integer.parseInt(item.getValue()) < 75) {
                v.findViewById(R.id.LinearLayout1).setBackgroundResource(R.drawable.low_result_banner);
                ((TextView) v.findViewById(R.id.textView3)).setTextColor(Color.parseColor("#faa539"));

                ((TextView) v.findViewById(R.id.textView7)).setText("You should eat something sweet.");
                ((TextView) v.findViewById(R.id.info_txt)).setText("You should eat something sweet.");
            }
        }
        displayGraph(v);

    }

    /**
     * graph basic settings
     * @param pView
     */
    public void graphBasicSettings(View pView){

        graph.setVisibility(View.GONE);

        final LinearLayout ll = (LinearLayout) pView.findViewById(R.id.ll_range);

        ll.removeAllViews();

        final LinearLayout ll_title = (LinearLayout) pView.findViewById(R.id.ll_title);

        final MyCustomView mView = new MyCustomView(mContext,mPrefManager);

        mView.getRangeValues(new RangeSettingCallback() {
            @Override
            public void onSuccess(float minValue, float maxValue) {
                minSetValue = minValue;
                maxSetValue = maxValue;
            }

            @Override
            public void onFailure(String result) {

            }
        });

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View v = inflater.inflate(R.layout.settings_bottom, null);
        final View v1 = inflater.inflate(R.layout.layout_setting_header, null);



        ViewTreeObserver vto = ll.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width  = ll.getMeasuredWidth();
                int height = ll.getMeasuredHeight();

                int w1 = v.getMeasuredWidth();
                int h1 = v.getMeasuredHeight();

                int h2 = v1.getMeasuredHeight();

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width,(int) Math.round ( height-h1-(height*0.2)));
               /* LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width,height-h1-h2);*/
                //mView.setLayoutParams(new ViewGroup.LayoutParams(width,(int) Math.round ( height-h1-(height*0.2))  ));
                mView.setLayoutParams(p);
                ll.addView(v1);
                ll.addView(mView);
                ll.addView(v);

                v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        graph.setVisibility(View.VISIBLE);
                        settingsView.setVisibility(View.GONE);
                    }
                });
                v.findViewById(R.id.tv_reset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mView.setRanges(LifeTabsConstents.GRAPH_DEFAULT_MIN_VALUE,LifeTabsConstents.GRAPH_DEFAULT_MAX_VALUE);
                    }
                });
                v.findViewById(R.id.tv_apply).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mPrefManager.setSharedPreference(LifeTabPrefManager.SharedPreferencesNames.MIN_VALUE_SET , (int)minSetValue);
                        mPrefManager.setSharedPreference(LifeTabPrefManager.SharedPreferencesNames.MAX_VALUE_SET , (int)maxSetValue);

                        graph.resetXY();
                        displayGraph(view);

                        graph.setVisibility(View.VISIBLE);
                        settingsView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * display graph
     * @param mView
     */
    public void displayGraph(View mView) {

        graph = (GraphView) mView.findViewById(R.id.graph);
        graph.removeAllSeries();
        graph.removeList();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

        Date date1 = c.getTime();
        String hour_24_filter = df.format(date1);

        List<ReadingBO> mList = ReadingDAO.getFilteredData(mAdapter, DBConstents.READING_ITEM.TABLE_NAME, DBConstents.READING_ITEM.K_FORMATED_DATE, hour_24_filter);

        if (mList != null && mList.size() != 0) {

            dataList = new int[mList.size()];

            for (int i = 0; i < mList.size(); i++) {
                dataList[i] = Integer.parseInt(mList.get(i).getValue());
            }

            if (dataList.length == 1) {
                timeList = new String[2];
            } else {
                timeList = new String[dataList.length];
            }

            SimpleDateFormat f1 = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat f2 = new SimpleDateFormat("MM/yy");

            for (int i = 0; i < mList.size(); i++) {
                timeList[i] = mList.get(i).getTimeString();
            }
            if (timeList.length == 1) {
                timeList[1] = " ";
            }

            DataPoint[] dp = new DataPoint[dataList.length];
            DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy h:mm a");
            List<Date> dateList = new ArrayList<Date>();

            for (int i = 0; i < dataList.length; i++) {

                String inputStr1 = mList.get(i).getDateString()+" "+mList.get(i).getTimeString();
                try {
                    Date inputDate1 = dateFormat1.parse(inputStr1);
                    dateList.add(inputDate1);
                    dp[i] = new DataPoint(inputDate1, dataList[i]);

                }catch (ParseException e){
                    e.printStackTrace();
                }
            }

            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>(dp);

            LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(dp);



            series.setCustomShape(new PointsGraphSeries.CustomShape() {
                @Override
                public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {

                    Bitmap red    = BitmapFactory.decodeResource(getResources(), com.jjoe64.graphview.R.drawable.red);
                    Bitmap yellow = BitmapFactory.decodeResource(getResources(), com.jjoe64.graphview.R.drawable.yellow);
                    Bitmap blue   = BitmapFactory.decodeResource(getResources(), com.jjoe64.graphview.R.drawable.blue);
                    Bitmap transparent = BitmapFactory.decodeResource(getResources(), com.jjoe64.graphview.R.drawable.info);
                    com.jjoe64.graphview.CordinatesBO bo = new com.jjoe64.graphview.CordinatesBO();

                    if (LifeTabUtils.getColorNumber(dataPoint.getY(),mPrefManager) == LifeTabsConstents.ORANGE) {
                        canvas.drawBitmap(yellow, x - yellow.getWidth() / 2, y - yellow.getHeight() / 2, paint);
                        bo.setColorType(1);
                    } else if (LifeTabUtils.getColorNumber(dataPoint.getY(),mPrefManager) == LifeTabsConstents.BLUE) {
                        canvas.drawBitmap(blue, x - blue.getWidth() / 2, y - blue.getHeight() / 2, paint);
                        bo.setColorType(2);
                    } else if (LifeTabUtils.getColorNumber(dataPoint.getY(),mPrefManager) == LifeTabsConstents.RED) {
                        canvas.drawBitmap(red, x - red.getWidth() / 2, y - red.getHeight() / 2, paint);
                        bo.setColorType(3);
                    }

                    com.geniteam.lifetabs.bo.CordinatesBO bo2 = new com.geniteam.lifetabs.bo.CordinatesBO();

                    bo.setX(x);
                    bo.setY(y);

                    if (dSize < dataList.length) {
                        bo.setValue(dataList[dSize]);
                        dSize++;
                        if (dSize == dataList.length) {
                            dSize = 0;
                        }
                    }

                    bo2.setX(x);
                    bo2.setY(y);

                    graph.addItem(bo);
                }
            });

            series1.setColor(Color.parseColor("#73767D"));

            graph.addSeries(series1);
            graph.addSeries(series);

            graph.getGridLabelRenderer().setNumHorizontalLabels(6); // only 4 because of the space

            graph.getGridLabelRenderer().setTextSize((float) (getScreenWidth() * 0.025));

            graph.getGridLabelRenderer().reloadStyles();

// set manual x bounds to have nice steps
            graph.getViewport().setMinX(1);
            if (dataList.length == 1) {
                graph.getViewport().setMaxX(dataList.length + 1);
            } else {
                graph.getViewport().setMaxX(dataList.length);
            }

            graph.getViewport().setXAxisBoundsManual(true);

            graph.getViewport().setMinY(45);
            graph.getViewport().setMaxY(350);

            graph.getViewport().setMinX(dateList.get(0).getTime()-450000);
            graph.getViewport().setMaxX(dateList.get(dateList.size() - 1).getTime()+450000); //today

            graph.getViewport().setYAxisBoundsManual(true);

            final DateFormat time =  new SimpleDateFormat("h:mm a");
            final DateFormat date=  new SimpleDateFormat("MM/yyyy");

            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(mContext) {

                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        Date d = new Date((long) (value));
                            return (time.format(d));

                    } else {
                        return " ";
                    }

                }
            });
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        }
    }

    /**
     * getting screen width
     * @return
     */
    public int getScreenWidth(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        return displaymetrics.widthPixels;
    }

    public int getIndex(){
        float x = mList.get(0).getX();
        int index = 0;

        for(int i=1;i<mList.size();i++){
        if(mList.get(i).getX() == x){
        index = i;
        break;
        }
        }
        return index;
        }

    // goto home screen
    public void goBackToHome(){
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment homeFragment= HomeFragment.newInstance(fragmentManager);
        mContext.currFragment = homeFragment;
        mContext.mLastPostitionOfScreen = LifeTabsConstents.HOME_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, homeFragment, LifeTabsConstents.HOME).commit();
    }
// goto camera screen
    public void goToCamera(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FragmentManager fragmentManager = getFragmentManager();
            //LifeTabCustomFragment cameraFragment= CameraFragment.newInstance(fragmentManager);
            LifeTabCustomFragment cameraFragment= Camera2FragmentD.newInstance(fragmentManager);
            mContext.currFragment = cameraFragment;
            mContext.mLastPostitionOfScreen = LifeTabsConstents.CAMERA_FRAGMENT_POSITION;
            fragmentManager.beginTransaction().replace(R.id.container, cameraFragment, LifeTabsConstents.CAMERA).commit();
        }else{
            FragmentManager fragmentManager = getFragmentManager();
            //LifeTabCustomFragment cameraFragment= CameraFragment.newInstance(fragmentManager);
            LifeTabCustomFragment cameraFragment= CameraFragmentD.newInstance(fragmentManager);
            mContext.currFragment = cameraFragment;
            mContext.mLastPostitionOfScreen = LifeTabsConstents.CAMERA_FRAGMENT_POSITION;
            fragmentManager.beginTransaction().replace(R.id.container, cameraFragment, LifeTabsConstents.CAMERA).commit();
        }


    }

    // goto history screen
    public void goToHistory(){

        Bundle args = new Bundle();
        args.putSerializable("sugar_value",item);

        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment historyFragment= HistoryFragment.newInstance(fragmentManager);
        mContext.currFragment = historyFragment;
        historyFragment.setArguments(args);
        mContext.mLastPostitionOfScreen = LifeTabsConstents.RESULT_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, historyFragment, LifeTabsConstents.HISTORY).commit();
    }

    @Override
    public boolean onKeyDown() {

        if(settingsView.getVisibility() == View.VISIBLE){
            graph.setVisibility(View.VISIBLE);
            settingsView.setVisibility(View.GONE);
        }else {
            goToCamera();

        }
        return true;
    }

    @Override
    public void onClick(View click) {
        switch (click.getId()){
            case R.id.rl_activity_history:
                goToHistory();
                break;
            case R.id.imageView5:
                settingsView.setVisibility(View.VISIBLE);
                graphBasicSettings(view);
                break;
            case R.id.tv_cancel:
                settingsView.setVisibility(View.GONE);
                break;
            case R.id.info_gotit_txt:
                infoView.setVisibility(View.GONE);
                break;
            case R.id.imageView1:
                infoView.setVisibility(View.VISIBLE);
                break;
            case R.id.scan_again:
                goToCamera();
                break;
        }
    }
}


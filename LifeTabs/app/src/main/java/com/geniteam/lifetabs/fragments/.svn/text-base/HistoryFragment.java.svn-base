package com.geniteam.lifetabs.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geniteam.lifetabs.R;
import com.geniteam.lifetabs.activities.MainActivity;
import com.geniteam.lifetabs.adapters.HistoryCatagoriesAdapter;
import com.geniteam.lifetabs.adapters.LastReadingsAdapter;
import com.geniteam.lifetabs.bo.HistoryBO;
import com.geniteam.lifetabs.bo.ReadingBO;
import com.geniteam.lifetabs.constants.DBConstents;
import com.geniteam.lifetabs.constants.LifeTabsConstents;
import com.geniteam.lifetabs.dao.DBAdapter;
import com.geniteam.lifetabs.dao.ReadingDAO;
import com.geniteam.lifetabs.interfaces.RangeSettingCallback;
import com.geniteam.lifetabs.managers.LifeTabPrefManager;
import com.geniteam.lifetabs.utils.LifeTabUtils;
import com.geniteam.lifetabs.utils.MyCustomView;
import com.jjoe64.graphview.CordinatesBO;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Interfaces.ItemClickCallback;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
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
 * Created by Tayyab on 3/15/2016.
 */
public class HistoryFragment extends LifeTabCustomFragment implements View.OnClickListener {
    MainActivity mContext;
    private View view;
    private ListView historyListView,graphSpecificlv;
    int dSize= 0;
    View mPreviousReference;
    //range buttons
    TextView hours48;
    TextView week1;
    TextView week2;
    TextView month3;
    GraphView graph;
    int dataList[];
    List<ReadingBO> hours48List ;
    List<ReadingBO> week1List ;
    List<ReadingBO> week2List ;
    List<ReadingBO> month3List ;
    List<ReadingBO> mList=null;
    private boolean refreshGraph=false;
    private LinearLayout mainView;
    private View headerView;
    private TextView mRangeTitle;
    RelativeLayout settingsView;
    DBAdapter dbAdapter;
    String [] timeList=null;
    TextView no_readings;
    ReadingBO itemReceived;
    long diff;
    Long []miliList;
    LifeTabPrefManager mPrefManager;
    float minSetValue =0;
    float maxSetValue =0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;

    }

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static LifeTabCustomFragment newInstance(FragmentManager fragmentManager) {
        LifeTabCustomFragment historyFragment = (LifeTabCustomFragment) fragmentManager.findFragmentByTag(LifeTabsConstents.HISTORY);
        if(historyFragment == null){
            historyFragment = new HistoryFragment();
        }
        return historyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hours48List = new ArrayList<ReadingBO>();
        week1List = new ArrayList<ReadingBO>();
        week2List = new ArrayList<ReadingBO>();
        month3List = new ArrayList<ReadingBO>();
        dbAdapter = DBAdapter.getDBAdapterInstance(mContext);
        mPrefManager = new LifeTabPrefManager(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.history_screen, container, false);
        settingsView = (RelativeLayout) view.findViewById(R.id.setting_layout);
        headerView = inflater.inflate(R.layout.history_listvie_header, null);
        setBasicContents(view);
        populateRanges();

        if(mContext.mLastPostitionOfScreen == LifeTabsConstents.HOME_FRAGMENT_POSITION){

            ((ImageView) mContext.toolbar.findViewById(R.id.toolbar_img2)).setImageResource(R.drawable.switch_graph);
            mContext.toolbar.findViewById(R.id.toolbar_img2).setTag(R.drawable.switch_graph);
            mainView.setVisibility(View.VISIBLE);
            historyListView.setVisibility(View.GONE);
            showGraph(LifeTabsConstents.HOURS48);
            showGraph(LifeTabsConstents.HOURS48);
        }else{
            showHistoryCategorizedList();
        }

        return view;
    }

    /*
    setting basics
     */

    public void setBasicContents(View v){

        mContext.toolbar.findViewById(R.id.toolbar_img1).setVisibility(View.VISIBLE);
        mContext.toolbar.findViewById(R.id.toolbar_img2).setVisibility(View.VISIBLE);
        v.findViewById(R.id.imageView1).setOnClickListener(this);
        ((TextView)mContext.toolbar.findViewById(R.id.toolbar_title)).setText("History");
        ((ImageView)mContext.toolbar.findViewById(R.id.toolbar_img2)).setImageResource(R.drawable.switch_listview);
        ((ImageView)mContext.toolbar.findViewById(R.id.toolbar_img2)).setTag(R.drawable.switch_listview);
        mContext.toolbar.findViewById(R.id.toolbar_img2).setOnClickListener(this);

        mContext.toolbar.setNavigationIcon(null);
        ((ImageView)mContext.toolbar.findViewById(R.id.toolbar_img1)).setImageResource(R.drawable.home);
        mContext.toolbar.findViewById(R.id.toolbar_img1).setOnClickListener(this);
        historyListView = (ListView) v.findViewById(R.id.listView_history);

        graphSpecificlv = (ListView) v.findViewById(R.id.graph_specific_lv);
        graphSpecificlv.addHeaderView(headerView);
        graph = (GraphView) v.findViewById(R.id.graph);
        mainView = (LinearLayout) v.findViewById(R.id.history_main_container);

        hours48 = (TextView) v.findViewById(R.id.hours48);
        week1 = (TextView) v.findViewById(R.id.week1);
        week2 = (TextView) v.findViewById(R.id.week2);
        month3 = (TextView) v.findViewById(R.id.month3);
        mRangeTitle = (TextView) v.findViewById(R.id.textView1);

        graph = (GraphView) v.findViewById(R.id.graph);
        no_readings = (TextView) v.findViewById(R.id.tv_no_readings);

        hours48.setOnClickListener(this);
        week1.setOnClickListener(this);
        week2.setOnClickListener(this);
        month3.setOnClickListener(this);

        Bundle b = getArguments();
        if(b!=null){
            itemReceived =(ReadingBO) b.getSerializable("sugar_value");
        }
    }

    /*
    getting data for different ranges like 48-hours , 1/2 week and 3 month
     */

    public void populateRanges (){

       DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
       Calendar c = Calendar.getInstance();
       c.setTimeInMillis(System.currentTimeMillis() - 48 * 60 * 60 * 1000);

       Date date1 = c.getTime();
       String hour_48_filter  = df.format(date1);

       c.setTimeInMillis(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
       Date date2 = c.getTime();
       String week1_filter  = df.format(date2);

       c.setTimeInMillis(System.currentTimeMillis() - 14*24*60*60*1000);
       Date date3 = c.getTime();
       String week2_filter  = df.format(date3);

       c.setTimeInMillis(System.currentTimeMillis());
       c.add(Calendar.MONTH, -3);
       Date date4 = c.getTime();
       String month3_filter  = df.format(date4);

       hours48List = ReadingDAO.getFilteredData(dbAdapter , DBConstents.READING_ITEM.TABLE_NAME , DBConstents.READING_ITEM.K_FORMATED_DATE ,hour_48_filter);
       week1List   = ReadingDAO.getFilteredData(dbAdapter , DBConstents.READING_ITEM.TABLE_NAME , DBConstents.READING_ITEM.K_FORMATED_DATE ,week1_filter);
       week2List   = ReadingDAO.getFilteredData(dbAdapter , DBConstents.READING_ITEM.TABLE_NAME , DBConstents.READING_ITEM.K_FORMATED_DATE ,week2_filter);
       month3List  = ReadingDAO.getFilteredData(dbAdapter , DBConstents.READING_ITEM.TABLE_NAME , DBConstents.READING_ITEM.K_FORMATED_DATE ,month3_filter);

    }

    /*
     method to show graph on screen
     */

    public void showGraph(final int rangeType){

        graph.removeAllSeries();
        graph.removeList();
        mPreviousReference = null;
        dSize = 0;

        if(rangeType == LifeTabsConstents.HOURS48){
            mList = hours48List;
        }else if(rangeType == LifeTabsConstents.WEEK1){
            mList = week1List;
        }else if(rangeType == LifeTabsConstents.WEEK2){
            mList = week2List;
        }else if(rangeType == LifeTabsConstents.MONTH3){
            mList = month3List;
        }

        if(mList!=null && mList.size()!=0) {

            dataList = new int[mList.size()];

            for (int i = 0; i < mList.size(); i++) {
                dataList[i] = Integer.parseInt(mList.get(i).getValue());
            }

            if(dataList.length == 1){
                timeList = new String[2];
            }else{
                timeList = new String[dataList.length];
            }

            SimpleDateFormat f1 = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat f2 = new SimpleDateFormat("MM/yy");

            for (int i = 0; i < mList.size(); i++) {
                if (rangeType == LifeTabsConstents.WEEK1 || rangeType == LifeTabsConstents.WEEK2 || rangeType == LifeTabsConstents.MONTH3) {
                    try {
                        Date d = f1.parse(mList.get(i).getDateString());
                        timeList[i] = f2.format(d);
                    } catch (ParseException e) {
                    }

                } else {
                    timeList[i] = mList.get(i).getTimeString();
                }

            }
            if(timeList.length ==1){
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
              }catch (Exception e){
                e.printStackTrace();
              }
            }

            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>(dp);
            LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(dp);

            series.setCustomShape(new PointsGraphSeries.CustomShape() {
                @Override
                public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {

                    Bitmap red     = BitmapFactory.decodeResource(getResources(), com.jjoe64.graphview.R.drawable.red);
                    Bitmap yellow  = BitmapFactory.decodeResource(getResources(), com.jjoe64.graphview.R.drawable.yellow);
                    Bitmap blue    = BitmapFactory.decodeResource(getResources(), com.jjoe64.graphview.R.drawable.blue);
                    CordinatesBO bo = new CordinatesBO();


                    if (LifeTabUtils.getColorNumber(dataPoint.getY(),mPrefManager) == LifeTabsConstents.ORANGE){
                        canvas.drawBitmap(yellow, x - yellow.getWidth() / 2, y - yellow.getHeight() / 2, paint);
                        bo.setColorType(1);
                    } else if (LifeTabUtils.getColorNumber(dataPoint.getY(),mPrefManager) == LifeTabsConstents.BLUE){
                        canvas.drawBitmap(blue, x - blue.getWidth() / 2, y - blue.getHeight() / 2, paint);
                        bo.setColorType(2);
                    } else if (LifeTabUtils.getColorNumber(dataPoint.getY(),mPrefManager) == LifeTabsConstents.RED){
                        canvas.drawBitmap(red, x - red.getWidth() / 2, y - red.getHeight() / 2, paint);
                        bo.setColorType(3);
                    }

                    com.geniteam.lifetabs.bo.CordinatesBO bo2 = new com.geniteam.lifetabs.bo.CordinatesBO();

                    bo.setX(x);
                    bo.setY(y);
                    bo.setTime(mList.get(dSize).getTimeString());
                    bo.setDate(mList.get(dSize).getDateString());

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

            graph.getGridLabelRenderer().setNumHorizontalLabels(6);

            graph.getGridLabelRenderer().setTextSize((float) (getScreenWidth() * 0.025));
            graph.getGridLabelRenderer().reloadStyles();

// set manual x bounds to have nice steps
            graph.getViewport().setMinX(1);

            if(dataList.length==1){
                graph.getViewport().setMaxX(dataList.length+1);
            }else{
                graph.getViewport().setMaxX(dataList.length);
            }

            graph.getViewport().setXAxisBoundsManual(true);

            graph.getViewport().setMinY(45);
            graph.getViewport().setMaxY(350);

            graph.getViewport().setMinX(dateList.get(0).getTime()-450000);
            graph.getViewport().setMaxX(dateList.get(dateList.size() - 1).getTime()+450000);

            graph.getViewport().setYAxisBoundsManual(true);

            final DateFormat time =  new SimpleDateFormat("h:mm a");
            final DateFormat date=  new SimpleDateFormat("MM/yyyy");

            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(mContext) {

              @Override
              public String formatLabel(double value, boolean isValueX) {
                  if (isValueX) {
                      Date d = new Date((long) (value));
                      if(rangeType == LifeTabsConstents.HOURS48){
                          return (time.format(d));
                      }else{
                          return (date.format(d));
                      }
                  } else {
                      return " ";
                  }
              }
            });

            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
            //show listview

            graphSpecificlv.setAdapter(new LastReadingsAdapter(mContext, R.layout.history_item_withbg, mList,mPrefManager));
            graphSpecificlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    if (position != 0) {
                        arg1.setBackgroundResource(R.drawable.graph_specific_lv_item_selected);
                        if (mPreviousReference != null) {
                            mPreviousReference.setBackgroundResource(R.drawable.whitebar);
                        }
                        mPreviousReference = arg1;
                        graph.initilizeXY(Integer.parseInt(mList.get(position - 1).getValue()),mList.get(position - 1).getTimeString(),mList.get(position - 1).getDateString(), mList.size());
                    }
                }
            });

            graph.getSugarValue(new ItemClickCallback() {
                @Override
                public void onSuccess(String value) {

                    String [] parse = value.split(" ");
                    int calValue = Integer.parseInt(parse[0]);
                    String time = parse[1]+" "+parse[2];
                    String date = parse[3];

                    if (calValue > 0) {
                        for (int i = 1; i < graphSpecificlv.getChildCount(); i++) {
                            View v = graphSpecificlv.getChildAt(i);
                            if (Integer.parseInt(((TextView) v.findViewById(R.id.textView3)).getText().toString().trim()) == calValue &&
                                    ((TextView) v.findViewById(R.id.textView2)).getText().toString().equals(time) &&
                                    ((TextView) v.findViewById(R.id.textView1)).getText().toString().equals(date)) {
                                if (mPreviousReference != null) {
                                    mPreviousReference.setBackgroundResource(R.drawable.whitebar);
                                }
                                v.setBackgroundResource(R.drawable.graph_specific_lv_item_selected);
                                mPreviousReference = v;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(String result) {
                    // nothing to do
                }
            });
        }else{
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"", "", "", "", "", "", "", "", "", ""});
            staticLabelsFormatter.setVerticalLabels(new String[]{"", "", "", "", "", "", "", "", "", ""});
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
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

    /*
    showing history categorized listview
     */
    public void showHistoryCategorizedList(){

        List<HistoryBO> historyList = new ArrayList<HistoryBO>();

        List<ReadingBO> readingsList  =  ReadingDAO.getAllReadings(dbAdapter);

        if(readingsList!=null && readingsList.size()!=0) {

            no_readings.setVisibility(View.GONE);
            historyList = LifeTabUtils.sortReadingsList(readingsList);
            historyListView.setAdapter(new HistoryCatagoriesAdapter(mContext, R.layout.history_group_item, historyList,mPrefManager));
        }else{
            no_readings.setVisibility(View.VISIBLE);
        }
    }

    /*
    goto Home screen
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

        if(settingsView.getVisibility() == View.VISIBLE){
            graph.setVisibility(View.VISIBLE);
            settingsView.setVisibility(View.GONE);

        }else if(mContext.mLastPostitionOfScreen == LifeTabsConstents.RESULT_FRAGMENT_POSITION){
            goToResultsSection();
        }else{
            goToHome();
        }
        return true;
    }

/*
 go to results screen back
 */
    public void goToResultsSection(){
        Bundle args = new Bundle();
        args.putSerializable("sugar_value",itemReceived);
        FragmentManager fragmentManager = getFragmentManager();
        LifeTabCustomFragment resultFragment= ResultFragment.newInstance(fragmentManager);
        mContext.currFragment = resultFragment;
        resultFragment.setArguments(args);
        mContext.mLastPostitionOfScreen = LifeTabsConstents.RESULT_FRAGMENT_POSITION;
        fragmentManager.beginTransaction().replace(R.id.container, resultFragment, LifeTabsConstents.RESULT).commit();
    }

    /*
    graph range settings functionality
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

                int w2 = v1.getMeasuredWidth();
                int h2 = v1.getMeasuredHeight();

               //mView.setLayoutParams(new ViewGroup.LayoutParams(width, height-h1-h2));
                mView.setLayoutParams(new ViewGroup.LayoutParams(width,(int) Math.round ( height-h1-(height*0.2))  ));
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
                        showGraph(LifeTabsConstents.HOURS48);

                        graph.setVisibility(View.VISIBLE);
                        settingsView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View click) {
        switch (click.getId()){
            case R.id.hours48:

                mRangeTitle.setText("Last 48 hours results");

                hours48.setBackgroundResource(R.drawable.button_active);
                week1.setBackgroundResource(R.drawable.button_inactive);
                week2.setBackgroundResource(R.drawable.button_inactive);
                month3.setBackgroundResource(R.drawable.button_inactive);

                hours48.setTextColor(Color.WHITE);
                week1.setTextColor(Color.BLACK);
                week2.setTextColor(Color.BLACK);
                month3.setTextColor(Color.BLACK);

                graph.resetXY();
                showGraph(LifeTabsConstents.HOURS48);
                showGraph(LifeTabsConstents.HOURS48);
                break;
            case R.id.week1:

                mRangeTitle.setText("Last 1 week results");

                week1.setBackgroundResource(R.drawable.button_active);
                hours48.setBackgroundResource(R.drawable.button_inactive);
                week2.setBackgroundResource(R.drawable.button_inactive);
                month3.setBackgroundResource(R.drawable.button_inactive);

                week1.setTextColor(Color.WHITE);
                hours48.setTextColor(Color.BLACK);
                week2.setTextColor(Color.BLACK);
                month3.setTextColor(Color.BLACK);

                graph.resetXY();
                showGraph(LifeTabsConstents.WEEK1);
                showGraph(LifeTabsConstents.WEEK1);
                break;
            case R.id.week2:

                mRangeTitle.setText("Last 2 weeks results");

                week2.setBackgroundResource(R.drawable.button_active);
                week1.setBackgroundResource(R.drawable.button_inactive);
                hours48.setBackgroundResource(R.drawable.button_inactive);
                month3.setBackgroundResource(R.drawable.button_inactive);

                week2.setTextColor(Color.WHITE);
                week1.setTextColor(Color.BLACK);
                hours48.setTextColor(Color.BLACK);
                month3.setTextColor(Color.BLACK);

                graph.resetXY();
                showGraph(LifeTabsConstents.WEEK2);
                showGraph(LifeTabsConstents.WEEK2);
                break;
            case R.id.month3:

                mRangeTitle.setText("Last 3 months results");

                month3.setBackgroundResource(R.drawable.button_active);
                week1.setBackgroundResource(R.drawable.button_inactive);
                week2.setBackgroundResource(R.drawable.button_inactive);
                hours48.setBackgroundResource(R.drawable.button_inactive);

                month3.setTextColor(Color.WHITE);
                week1.setTextColor(Color.BLACK);
                week2.setTextColor(Color.BLACK);
                hours48.setTextColor(Color.BLACK);

                graph.resetXY();
                showGraph(LifeTabsConstents.MONTH3);
                showGraph(LifeTabsConstents.MONTH3);
                break;
            case R.id.toolbar_img1:
                goToHome();
                break;
            case R.id.toolbar_img2:
                if( (Integer)((ImageView)mContext.toolbar.findViewById(R.id.toolbar_img2)).getTag() == R.drawable.switch_listview){
                    ((ImageView)mContext.toolbar.findViewById(R.id.toolbar_img2)).setImageResource(R.drawable.switch_graph);
                    ((ImageView)mContext.toolbar.findViewById(R.id.toolbar_img2)).setTag(R.drawable.switch_graph);

                    if(no_readings.getVisibility() == View.VISIBLE){
                        no_readings.setVisibility(View.GONE);
                    }

                    month3.setBackgroundResource(R.drawable.button_inactive);
                    week1.setBackgroundResource(R.drawable.button_inactive);
                    week2.setBackgroundResource(R.drawable.button_inactive);
                    hours48.setBackgroundResource(R.drawable.button_active);

                    month3.setTextColor(Color.BLACK);
                    week1.setTextColor(Color.BLACK);
                    week2.setTextColor(Color.BLACK);
                    hours48.setTextColor(Color.WHITE);

                    mRangeTitle.setText("Last 48 hours results");

                    mainView.setVisibility(View.VISIBLE);
                    historyListView.setVisibility(View.GONE);

                    graph.resetXY();
                    showGraph(LifeTabsConstents.HOURS48);
                    showGraph(LifeTabsConstents.HOURS48);
                }
                else{
                    ((ImageView)mContext.toolbar.findViewById(R.id.toolbar_img2)).setImageResource(R.drawable.switch_listview);
                    ((ImageView)mContext.toolbar.findViewById(R.id.toolbar_img2)).setTag(R.drawable.switch_listview);

                    mainView.setVisibility(View.GONE);
                    historyListView.setVisibility(View.VISIBLE);
                    showHistoryCategorizedList();
                }
                break;

            case R.id.imageView1:
                settingsView.setVisibility(View.VISIBLE);
                graphBasicSettings(view);
                break;
            default:
                break;
        }
    }
}
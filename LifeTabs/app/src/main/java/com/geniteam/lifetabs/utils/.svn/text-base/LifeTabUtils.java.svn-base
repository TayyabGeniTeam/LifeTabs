package com.geniteam.lifetabs.utils;

import android.content.Context;

import com.geniteam.lifetabs.bo.HistoryBO;
import com.geniteam.lifetabs.bo.ReadingBO;
import com.geniteam.lifetabs.managers.LifeTabPrefManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Tayyab on 3/16/2016.
 */
public class LifeTabUtils {

    static int low_value   = 75;
    static int  high_value = 180;
    static int min_limit   =45;
    static int max_limit   =350;

    public static int getColorNumber(double num, LifeTabPrefManager mPrefManager)
    {
        low_value  = mPrefManager.getSharedPreferenceInt(LifeTabPrefManager.SharedPreferencesNames.MIN_VALUE_SET);
        high_value = mPrefManager.getSharedPreferenceInt(LifeTabPrefManager.SharedPreferencesNames.MAX_VALUE_SET);

        if(num >= low_value && num<=high_value){
            return 2;
        }
        else if(num > high_value){

            if(num >= high_value+1 && num <= high_value + high_value * 0.25){
                return 1;
            }
            else if(num >= high_value + high_value * 0.25 + 1   && num <= max_limit){
                return 3;
            }

        }else if(num < low_value){
            if(num > low_value - (low_value*0.25) && num < low_value){
                return 1;
            }
            else if(num >= min_limit && num < low_value - (low_value*0.25)-1){
                return 3;
            }
        }
        return 0;
    }

    public static  List<HistoryBO> sortReadingsList(List<ReadingBO> rList){

        List<HistoryBO> resultList = new ArrayList<HistoryBO>();
        List<String> headerList = new ArrayList<String>();
        /*List<ReadingBO> rList = new ArrayList<ReadingBO>();
        rList.add(new ReadingBO("1/6/2016","2:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("1/5/2016","2:35 AM","145","MG/DL"));
        rList.add(new ReadingBO("1/7/2016","4:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("1/2/2016","2:45 AM","145","MG/DL"));
        rList.add(new ReadingBO("1/4/2016","2:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("1/6/2015","2:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("1/16/2016","2:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("2/26/2015","2:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("1/3/2015","2:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("11/5/2013","2:35 AM","145","MG/DL"));
        rList.add(new ReadingBO("4/5/2016","4:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("7/6/2013","2:45 AM","145","MG/DL"));
        rList.add(new ReadingBO("8/9/2015","2:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("1/6/2014","2:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("3/26/2012","2:15 AM","145","MG/DL"));
        rList.add(new ReadingBO("2/26/2015","2:15 AM","145","MG/DL"));*/

        Collections.sort(rList, new Comparator<ReadingBO>() {
            public int compare(ReadingBO o1, ReadingBO o2) {

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = sdf.parse(o1.getDateString());
                    d2 = sdf.parse(o2.getDateString());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                if (d1 == null || d2 == null)
                    return 0;
                return (d1.getTime() > d2.getTime() ? -1 : 1);
                //return d1.compareTo(d2);
            }
        });


        for(int i=0 ; i<rList.size() ;i++){

            HistoryBO historyBO =  new HistoryBO();
            List<ReadingBO> readings = new ArrayList<ReadingBO>();

            String header = getHeader(rList.get(i).getDateString());

            if(! headerList.contains(header)){
                headerList.add(header);
            }
            else{
                continue;
            }

            for (int j = 0; j < rList.size(); j++) {

              if (getHeader(rList.get(j).getDateString()).equals(header)) {
                readings.add(rList.get(j));
              }

            }
                historyBO.setSection_header(header);
                historyBO.setSectionItemList(readings);
                resultList.add(historyBO);
             // else
        }// for loop outer

        return resultList;


    }// method end

    public static double [] printTwoMaxNumbers(double[] nums){
        double maxOne = 0;
        double maxTwo = 0;
        for(double n:nums){
            if(maxOne < n){
                maxTwo = maxOne;
                maxOne =n;
            } else if(maxTwo < n){
                maxTwo = n;
            }
        }
        double []result = {maxOne,maxTwo};
        return result;
    }

   public  static double [] TwoSmallestValues(double[] a)
    {

        double min1=0;
        double min2=0;

        double min1Index =0;
        double min2Index =0;

        min1 = a[0];
        min2 = a[1];
        min1Index =0;
        min2Index =1;
        if (min2 < min1)
        {
            min1 = a[1];
            min2 = a[0];

            min1Index =1;
            min2Index =0;
        }

        for (int i = 2; i < a.length; i++) {
            if (a[i] < min1) {
                min2 = min1;
                min1 = a[i];
                min1Index = i;
            } else if (a[i] < min2) {
                min2 = a[i];
                min2Index = i;
            }
        }

        double []res = {min1,min2,min1Index,min2Index};
        return res;
    }

    public static String getHeader (String datestr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date d = sdf.parse(datestr);

            sdf = new SimpleDateFormat("MMMM yyyy");

            return  sdf.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDataToSave(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        return formattedDate;
    }

    public static void writeToFile(String date_time,String value , String percentage,Context context,String filename) {
        try {
            File myFile = new File("/sdcard/"+filename+".txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append("Time & date: "+date_time+"\n");
            myOutWriter.append("Calculated value is : "+value+"\n");
            myOutWriter.append("Confidence percentage is: "+percentage);
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

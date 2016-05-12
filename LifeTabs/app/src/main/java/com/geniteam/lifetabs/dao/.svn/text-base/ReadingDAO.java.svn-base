package com.geniteam.lifetabs.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.geniteam.lifetabs.bo.ReadingBO;
import com.geniteam.lifetabs.constants.DBConstents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tayyab on 3/28/2016.
 */
public class ReadingDAO {
    public static long saveReadingItem(DBAdapter dbAdapter, ReadingBO itemBO) throws Exception {
        long returnId = -1;
        dbAdapter.openDataBase();
        try {

            ContentValues values = new ContentValues();

            values.put(DBConstents.READING_ITEM.K_READING_DATE,  itemBO.getDateString());
            values.put(DBConstents.READING_ITEM.K_READING_TIME,  itemBO.getTimeString());
            values.put(DBConstents.READING_ITEM.K_READING_VALUE, itemBO.getValue());
            values.put(DBConstents.READING_ITEM.K_READING_UNIT,  itemBO.getUnit());
            values.put(DBConstents.READING_ITEM.K_FORMATED_DATE, formatDate(itemBO.getDateString(),itemBO.getTimeString()));

            returnId = dbAdapter.insertRecordsInDB(DBConstents.READING_ITEM.TABLE_NAME, null, values);

        } catch(Exception ex){
            ex.printStackTrace();
        }finally {
            dbAdapter.close();
        }
        return returnId;
    }

    public static  ArrayList<ReadingBO> getFilteredData(DBAdapter dbAdapter, String tableName, String columnName,String formatedDate){
        String query =null;
        ArrayList<ReadingBO> readingList =new ArrayList<ReadingBO>();
        dbAdapter.openDataBase();
        try {

            query = "Select * from " + tableName + " WHERE " + columnName + "> ?";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query, new String[]{formatedDate});
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {

                    String pDate = cursor.getString(DBConstents.READING_ITEM.READING_DATE);
                    String pTime = cursor.getString(DBConstents.READING_ITEM.READING_TIME);
                    String pValue = cursor.getString(DBConstents.READING_ITEM.READING_VALUE);
                    String pUnit = cursor.getString(DBConstents.READING_ITEM.READING_UNIT);
                    String fd = cursor.getString(DBConstents.READING_ITEM.FORMATED_DATE);

                    readingList.add(new ReadingBO(pDate, pTime, pValue, pUnit));

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        dbAdapter.close();

        return  readingList;

    }

    public static String formatDate (String startDateString,String time){
        //String startDateString = "06/27/2007";
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date startDate=null;
        try{
            startDate = df.parse(startDateString+" "+time);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df1.format(startDate);
    }

    public static List<ReadingBO> getAllReadings(DBAdapter dbAdapter){

        String query =null;

        ArrayList<ReadingBO> readingList =new ArrayList<ReadingBO>();
        dbAdapter.openDataBase();
        try {

            query = "Select * from "+ DBConstents.READING_ITEM.TABLE_NAME;
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,null);

            if(cursor.getCount()!=0){
                while(cursor.moveToNext()){

                    String pDate = cursor.getString(DBConstents.READING_ITEM.READING_DATE);
                    String pTime = cursor.getString(DBConstents.READING_ITEM.READING_TIME);
                    String pValue = cursor.getString(DBConstents.READING_ITEM.READING_VALUE);
                    String pUnit = cursor.getString(DBConstents.READING_ITEM.READING_UNIT);

                    readingList.add(new ReadingBO(pDate,pTime,pValue,pUnit));

                }
                dbAdapter.close();

                return readingList;
            }

        }catch(Exception e){e.printStackTrace();}
        dbAdapter.close();
        return null;
    }
}

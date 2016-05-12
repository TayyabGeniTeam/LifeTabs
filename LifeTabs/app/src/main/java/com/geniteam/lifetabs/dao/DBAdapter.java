package com.geniteam.lifetabs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBAdapter extends SQLiteOpenHelper {

	// The Android's default system path of your application database.  /data/data/mypackagename/databases/ 
	private static String DB_PATH = "";

	private static final String DB_NAME = "lifetab_db.sqlite";

	private SQLiteDatabase myDataBase;

	private Context myContext;
	
	private static DBAdapter mDBConnection;
	
	/**
	 * 
	 * @param context
	 * @return 
	 */
	public static synchronized DBAdapter getDBAdapterInstance(Context context){
		if(mDBConnection==null){
			mDBConnection=new DBAdapter(context);
		}		
		return mDBConnection;
	}

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	private DBAdapter(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
		
		// store db in sdcard		
//		DB_PATH =  SDCardManager.getSDCardManagerInstance().getBaseDir() +"/database/";		
//		SDCardManager.getSDCardManagerInstance().makeDirectories(DB_PATH);

// ||		
		
		// store db in phone memory
		DB_PATH = "/data/data/"
	              + context.getApplicationContext().getPackageName()
	              + "/databases/";
	
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException, FileNotFoundException, Exception {

		boolean dbExist = checkDataBase();
		SQLiteDatabase db_Read = null;

		if (dbExist) {
			// do nothing - database already exist
			
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			db_Read = this.getReadableDatabase(); 
			db_Read.close();

			copyDataBase();
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	public void copyDataBase() throws IOException, FileNotFoundException, Exception{

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * This function used to select the records from DB.
	 * @param tableName
	 * @param tableColumns
	 * @param whereClase
	 * @param whereArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return A Cursor object, which is positioned before the first entry.
	 */
	public Cursor selectRecordsFromDB(String tableName, String[] tableColumns, String whereClase,String whereArgs[],String groupBy,String having,String orderBy) {		
		return myDataBase.query(tableName, tableColumns, whereClase, whereArgs, groupBy,having, orderBy);
	}
	
	/**
	 * This function used to insert the Record in DB.
	 * @param tableName
	 * @param nullColumnHack
	 * @param initialValues
	 * @return the row ID of the newly inserted row, or -1 if an error occurred 
	 */
	public long insertRecordsInDB(String tableName, String nullColumnHack, ContentValues initialValues) throws Exception{		
		return myDataBase.insert(tableName, nullColumnHack, initialValues);
	}
	
	/**
	 * Convenience method for replacing a row in the database.
	 * @param tableName
	 * @param nullColumnHack
	 * @param initialValues
	 * @return the row ID of the newly inserted row, or -1 if an error occurred 
	 */
	public long replace(String tableName, String nullColumnHack, ContentValues initialValues) throws Exception{		
		return myDataBase.replaceOrThrow(tableName, nullColumnHack, initialValues);
	}
	
	/**
	 * This function used to update the Record in DB.
	 * @param tableName
	 * @param initialValues
	 * @param whereClause
	 * @param whereArgs
	 * @return if no of rows are greater than 0 then it means records are updated.
	 */
	public boolean updateRecordsInDB(String tableName, ContentValues initialValues,String whereClause,String whereArgs[]) throws Exception{		
		return myDataBase.update(tableName,initialValues,whereClause,whereArgs)> 0;
	}
	
	/**
	 * This function used to delete the Record in DB.
	 * @param tableName
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are deleted.
	 */
	public int deleteRecordInDB(String tableName,String whereClause,String[] whereArgs){
		return myDataBase.delete(tableName, whereClause, whereArgs);
	}
	
	/**
	 * 
	 * @param query 
	 * @param selectionArgs 
	 * @return
	 */
	public Cursor selectRecordsFromDB(String query, String []selectionArgs) {		
		return myDataBase.rawQuery(query, selectionArgs);		
	}
	
	public long getRowCount(String sql){
		SQLiteStatement statement = myDataBase.compileStatement(sql);
		long count = statement.simpleQueryForLong();
		return count;
	}
	
	/**
	 * execute SQL query
	 * @param query
	 */
	public void execSQL(String query, String []selectionArgs) throws SQLException{
		 myDataBase.rawQuery(query,null);
	}
	public void beginTransaction() {
		myDataBase.beginTransaction();
	}

	public void setTransactionSuccessful() throws IllegalStateException {
		myDataBase.setTransactionSuccessful();
	}

	public void endTransaction() {
		myDataBase.endTransaction();
	}

}

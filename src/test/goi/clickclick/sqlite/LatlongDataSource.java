package test.goi.clickclick.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LatlongDataSource {
	
	private SQLiteDatabase db;
	private MySQLiteHelper dbHelper;
	private String[] column_latlong = {MySQLiteHelper.ID, MySQLiteHelper.COLUMN_TIMESTAMP, 
			MySQLiteHelper.COLUMN_LAT, MySQLiteHelper.COLUMN_LONG};
	
	public LatlongDataSource(Context context){
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		db = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void addLatLong(String timestamp, double lat, double lang){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TIMESTAMP, timestamp);
		values.put(MySQLiteHelper.COLUMN_LAT, lat);
		values.put(MySQLiteHelper.COLUMN_LONG, lang);
		
		long id = db.insert(MySQLiteHelper.TABLE_LATLONG, null, values);
		Log.d("Inserted", String.valueOf(id));
	}
	
	public int getAllEntries()
	{
		Cursor cursor = db.query(MySQLiteHelper.TABLE_LATLONG, column_latlong, 
				null, null, null, null, null);
		return cursor.getCount();
	}
}

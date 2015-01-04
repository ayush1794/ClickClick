package test.goi.clickclick.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_LATLONG = "latlong";
	public static final String ID = "_id";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LONG = "long";
	public static final String COLUMN_TIMESTAMP = "timestamp";

	private static final String DATABASE_NAME = "latlong.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table " + TABLE_LATLONG + "(" +
			ID + " integer primary key autoincrement, " + COLUMN_TIMESTAMP + " text not null, " +
			COLUMN_LAT + " real not null, " + COLUMN_LONG + " real not null );";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("Creating Db", DATABASE_CREATE);
		db.execSQL(DATABASE_CREATE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_LATLONG);
		    onCreate(db);	
	}

}
